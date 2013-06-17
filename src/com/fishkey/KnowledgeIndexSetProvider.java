package com.fishkey;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.FlashcardSet;
import com.fishkey.model.KnowledgeIndexSet;
import com.fishkey.utils.AssetsUtil;
import com.fishkey.utils.ExternalStorageUtil;

/**
 * Dostarcza zestaw fiszek do przeprowadzenia quizu
 * @author Platon
 *
 */
public class KnowledgeIndexSetProvider extends AssetsUtil {
	
	/** tag to oznaczania logow */
	private static final String LOG_TAG = KnowledgeIndexSetProvider.class.getName();
	
	/** domyslna nazwa pliku JSON */
	private static final String DEFAULT_FILE_NAME_JSON = "slowka-knowledge-index.json.txt";

	// Nazwy pol JSON:
	static final String TAG_ID				= "id";
	static final String TAG_FLASHCARDS		= "flashcards";
	static final String TAG_F_ID			= "id";
	static final String TAG_F_VALUE		= "value";
	static final String TAG_F_DATE			= "date";
	
	/** 
	 * zwraca wczytany z pliku JSON zestaw fiszek 
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu fiszek
	 * @return	wczytany z pliku zestaw fiszek
	 * @throws QuizInitException gdy nie uda sie wczytac zestawu fiszek
	 */
	private static KnowledgeIndexSet parseJSON(String JSONText) throws QuizInitException {
		// Wczytywanie JSON:
		KnowledgeIndexSet knowledgeIndexSet;
		try {
			JSONObject jsonObject = new JSONObject(JSONText);
			knowledgeIndexSet = new KnowledgeIndexSet(jsonObject.getLong(TAG_ID));
			JSONArray flashcardsArray = jsonObject.getJSONArray(TAG_FLASHCARDS);
			for(int i=0; i < flashcardsArray.length(); i++){
				try{
					JSONObject c = flashcardsArray.getJSONObject(i);
				    long id 	= c.getLong(TAG_F_ID);
				    int value	= c.getInt(TAG_F_VALUE);
				    String date	= c.getString(TAG_F_DATE).trim();
				    knowledgeIndexSet.put(id, knowledgeIndexSet.new KnowledgeIndex(value,date));
				} catch(ParseException e) {
					Log.w(LOG_TAG, "Nie mozna wczytac fiszki. " + e.getMessage()); 
				}
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Blad czytania JSON. " + e.getMessage()); 
			throw new QuizInitException();
		}
		return knowledgeIndexSet;
	}
	
	/**
	 * zwraca wczytany z zewnetrznego zrodla zestaw fiszek
	 * @param context	obiekt Context
	 * @return			zestaw fiszek
	 * @throws QuizInitException	gdy wczytanie zestawu fiszek z zwenetrznego zrodla sie nie powiodlo sie
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
	
	private static String makeJSONString(KnowledgeIndexSet kis) throws JSONException{
        JSONObject json = new JSONObject();
		json.put(TAG_ID, kis.getId());
		for (Map.Entry<Long, KnowledgeIndexSet.KnowledgeIndex> entry : kis.entrySet()) {
			JSONObject jsonFlashcardKi = new JSONObject();
			Long id = entry.getKey();
			KnowledgeIndexSet.KnowledgeIndex ki = entry.getValue(); 	
			jsonFlashcardKi.put(TAG_F_ID, id);
			jsonFlashcardKi.put(TAG_F_VALUE, ki.getValue());
			jsonFlashcardKi.put(TAG_F_DATE, ki.getDateAsString());
			json.accumulate(TAG_FLASHCARDS, jsonFlashcardKi);
		}
		return json.toString();
	}

	
	public static boolean archiveKnowledgeIndexSet(Context context, KnowledgeIndexSet knowledgeIndexSet) {
		boolean result = false;
		try {
			String fileText = makeJSONString(knowledgeIndexSet);
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
