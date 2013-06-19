package com.fishkey;

import java.text.ParseException;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.fishkey.exceptions.QuizInitException;

import com.fishkey.model.KnowledgeIndexSet;
import com.fishkey.model.KnowledgeIndex;
import com.fishkey.utils.AssetsUtil;
import com.fishkey.utils.ExternalStorageUtil;

/**
 * Dostarcza zestaw fiszek do przeprowadzenia quizu
 * @author Platon
 *
 */
public class KnowledgeIndexSetProvider {
	
	/** tag to oznaczania logow */
	private static final String LOG_TAG = KnowledgeIndexSetProvider.class.getName();
	
	/** domyslna nazwa pliku JSON */
	private static final String DEFAULT_FILE_NAME_JSON = "slowka-knowledge-index.json.txt";

	// Nazwy pol JSON:
	static final String TAG_ID				= "id";
	static final String TAG_DATE			= "date";
	static final String TAG_FLASHCARDS		= "flashcards";
	static final String TAG_F_ID			= "id";
	static final String TAG_F_VALUE			= "value";
	static final String TAG_F_DATE			= "date";
	
	/** 
	 * zwraca wczytany z pliku JSON zestaw wspolczynnikow znajomosci fiszek 
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu wspolczynnikow znajomosci fiszek 
	 * @return	wczytany z pliku zestaw wspolczynnikow znajomosci fiszek 
	 * @throws QuizInitException gdy nie uda sie wczytac zestawu wspolczynnikow znajomosci fiszek 
	 */
	private static KnowledgeIndexSet parseJSON(String JSONText) throws QuizInitException {
		// Wczytywanie JSON:
		KnowledgeIndexSet knowledgeIndexSet;
		try {
			JSONObject jsonObject = new JSONObject(JSONText);
			Long idSet 			= jsonObject.getLong(TAG_ID);
			String dateSet 		= jsonObject.getString(TAG_DATE).trim();
			knowledgeIndexSet 	= new KnowledgeIndexSet(idSet,dateSet);
			JSONArray flashcardsArray = jsonObject.getJSONArray(TAG_FLASHCARDS);
			for(int i=0; i < flashcardsArray.length(); i++){
				try{
					JSONObject c = flashcardsArray.getJSONObject(i);
				    long id 	= c.getLong(TAG_F_ID);
				    int value	= c.getInt(TAG_F_VALUE);
				    String date	= c.getString(TAG_F_DATE).trim();
				    KnowledgeIndex kiToPut = new KnowledgeIndex(value, date);
				    KnowledgeIndex kiMaybeWasThere;
					if((kiMaybeWasThere = knowledgeIndexSet.put(id, kiToPut)) != null) {
				    	Log.w(LOG_TAG, "Dwie wartosci wspolczynnika znajomosci fiszki o tym samym id: (" + kiMaybeWasThere.toString() + ") oraz (" + kiToPut.toString() + ")");
				    }
				} catch(ParseException e) {
					Log.w(LOG_TAG, "Nie mozna wczytac wspolczynnika znajomosci fiszki. " + e.getMessage()); 
				}
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Blad czytania JSON. " + e.getMessage()); 
			throw new QuizInitException();
		}
		return knowledgeIndexSet;
	}
	
	/**
	 * zwraca wczytany z zewnetrznego zrodla zestaw wspolczynnikow znajomosci fiszek 
	 * @param context	obiekt Context
	 * @return			zestaw wspolczynnikow znajomosci fiszek 
	 * @throws QuizInitException	gdy wczytanie zestawu wspolczynnikow znajomosci fiszek z zwenetrznego zrodla sie nie powiodlo sie
	 */
	public static KnowledgeIndexSet getKnowledgeIndexSet(Context context) throws QuizInitException {
		String fileText;
		fileText = ExternalStorageUtil.readFileAsString(context, DEFAULT_FILE_NAME_JSON);
		if (fileText == null){	// jesli nie udalo sie wczytac danych z pliku, sproboj go utworzyc na podstawie pliku przykladowego z katalogu assets. Jesli cos pojdzie nie tak, rzuc wyjatkiem 
			String fromAssetsText = AssetsUtil.readFileAsString(context, DEFAULT_FILE_NAME_JSON);
			if (fromAssetsText == null){
				Log.e(LOG_TAG, "Nie mozna wczytac zestawu wspolczynnikow znajomosci fiszek z pliku assets");
				throw new QuizInitException();
			}
			if (!ExternalStorageUtil.writeStringAsFile(context, fromAssetsText, DEFAULT_FILE_NAME_JSON)) {
				Log.e(LOG_TAG, "Nie mozna zapisac zestawu wspolczynnikow znajomosci fiszek do pliku");
				throw new QuizInitException();
			}
			fileText = ExternalStorageUtil.readFileAsString(context, DEFAULT_FILE_NAME_JSON );
			if (fileText == null) {
				Log.e(LOG_TAG, "Nie wczytac zestawu wspolczynnikow znajomosci fiszek z pliku");
				throw new QuizInitException();
			}
		}
		return parseJSON(fileText);
	}
	
	/**
	 * zwraca utworzony tekst JSON na podstawie podanego zestawu wspolczynnikow znajomosci
	 * fiszek
	 * @param kis				zestaw wspolczynnikow znajomosci fiszek
	 * @return					tekst JSON utworzony na podstawie podanego zestawu
	 * 							wspolczynnikow znajomosci fiszek
	 * @throws JSONException	gdy nie uda stowrzyc poprawnego tekstu JSON na podstawie
	 * 							zestawu wspolczynnikow znajomosci
	 */
	private static String makeJSONString(KnowledgeIndexSet kis, String currentDateText) throws JSONException{
        JSONObject json = new JSONObject();
		json.put(TAG_ID, kis.getId());
		json.put(TAG_DATE, currentDateText);
		for (Map.Entry<Long, KnowledgeIndex> entry : kis.entrySet()) {
			JSONObject jsonFlashcardKi = new JSONObject();
			Long id = entry.getKey();
			KnowledgeIndex ki = entry.getValue(); 	
			jsonFlashcardKi.put(TAG_F_ID, id);
			jsonFlashcardKi.put(TAG_F_VALUE, ki.getValue());
			jsonFlashcardKi.put(TAG_F_DATE, ki.getDateAsString());
			json.accumulate(TAG_FLASHCARDS, jsonFlashcardKi);
		}
		return json.toString();
	}

	/**
	 * archiwizuje podany zestaw wspolczynnikow znajomosci fiszek i zwraca czy cala operacja sie powiodla
	 * @param context				obiekt Context
	 * @param knowledgeIndexSet		zestaw wspolczynnikow znajomosci fiszek
	 * @return						czy operacja zarchiwizowania podanego zestawu wspolczynnikow
	 * 								znajomosci fiszek sie powiodla
	 */
	public static boolean archiveKnowledgeIndexSet(Context context, KnowledgeIndexSet knowledgeIndexSet, String currentDateText) {
		boolean result = false;
		try {
			String fileText = makeJSONString(knowledgeIndexSet,currentDateText);
			if (!ExternalStorageUtil.writeStringAsFile(context, fileText, DEFAULT_FILE_NAME_JSON))
				Log.e(LOG_TAG, "Nie mozna zapisac wspolczynnikow znajomosci fiszek z pliku");
			else
				result = true;
		}
		catch (JSONException e){
			Log.e(LOG_TAG,"Blad przy generowaniu napisu JSON.");
		}
		return result;
	}
	
}
