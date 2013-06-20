package com.fishkey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.FlashcardSet;
import com.fishkey.utils.AssetsUtil;
import com.fishkey.utils.ExternalStorageUtil;

/**
 * dostarcza zestaw fiszek do przeprowadzenia quizu
 * @author Platon
 *
 */
public class FlashcardSetProvider {
	
	/** tag to oznaczania logow */
	private static final String LOG_TAG = FlashcardSetProvider.class.getName();
	
	/** domyslna nazwa pliku JSON */
	private static final String DEFAULT_FILE_NAME_JSON = "slowka.json.txt";
	
	/** 
	 * zwraca wczytany z pliku JSON zestaw fiszek 
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu fiszek
	 * @return	wczytany z pliku zestaw fiszek
	 * @throws QuizInitException gdy nie uda sie wczytac zestawu fiszek
	 */
	private static FlashcardSet parseJSON(String JSONText) throws QuizInitException {
		// Nazwy pol JSON:
		final String TAG_ID				= "id";
		final String TAG_NAME			= "name";
		final String TAG_FLASHCARDS		= "flashcards";
		final String TAG_F_ID			= "id";
		final String TAG_F_ANG			= "ang";
		final String TAG_F_POL			= "pol";
		// Wczytywanie JSON:
		FlashcardSet flashcardSet;
		try {
			JSONObject jsonObject = new JSONObject(JSONText);
			Long FSid = jsonObject.getLong(TAG_ID);
			String FSname = jsonObject.getString(TAG_NAME).trim();
			flashcardSet = new FlashcardSet(FSid, FSname);
			JSONArray flashcardsArray = jsonObject.getJSONArray(TAG_FLASHCARDS);
			for(int i=0; i < flashcardsArray.length(); i++){
			    JSONObject c = flashcardsArray.getJSONObject(i);
			    long Fid 		= Long.parseLong(c.getString(TAG_F_ID).trim());
			    String Fpol 	= c.getString(TAG_F_POL).trim();
			    String Fang 	= c.getString(TAG_F_ANG).trim();
			    Flashcard flashcardToPut = new Flashcard(Fpol, Fang);
			    Flashcard flashcardMaybeWasThere;
				if((flashcardMaybeWasThere = flashcardSet.put(Fid, flashcardToPut)) != null) {
			    	Log.w(LOG_TAG, "Dwie fiszki o tym samym id: (" + flashcardMaybeWasThere.toString() + ") oraz (" + flashcardToPut.toString() + ")");
			    }
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Blad czytania JSON. " + e.getMessage()); 
			throw new QuizInitException();
		}
		return flashcardSet;
	}
	
	/**
	 * zwraca wczytany z zewnetrznego zrodla zestaw fiszek
	 * @param context	obiekt Context
	 * @return			zestaw fiszek
	 * @throws QuizInitException	gdy wczytanie zestawu fiszek z zwenetrznego zrodla sie nie powiodlo sie
	 */
	public static FlashcardSet getFlashcardSet(Context context) throws QuizInitException {
		String fileText;
		fileText = ExternalStorageUtil.readFileAsString(context, DEFAULT_FILE_NAME_JSON);
		if (fileText == null){	// jesli nie udalo sie wczytac danych z pliku, sproboj go utworzyc na podstawie pliku przykladowego z katalogu assets. Jesli cos pojdzie nie tak, rzuc wyjatkiem 
			String fromAssetsText = AssetsUtil.readFileAsString(context, DEFAULT_FILE_NAME_JSON);
			if (fromAssetsText == null){
				Log.e(LOG_TAG, "Nie mozna wczytac zestawu fiszek z pliku assets");
				throw new QuizInitException();
			}
			if (!ExternalStorageUtil.writeStringAsFile(context, fromAssetsText, DEFAULT_FILE_NAME_JSON)) {
				Log.e(LOG_TAG, "Nie mozna zapisac zestawu fiszek do pliku");
				throw new QuizInitException();
			}
			fileText = ExternalStorageUtil.readFileAsString(context, DEFAULT_FILE_NAME_JSON );
			if (fileText == null) {
				Log.e(LOG_TAG, "Nie wczytac zestawu fiszek z pliku");
				throw new QuizInitException();
			}
		}
		return parseJSON(fileText);
	}
}
