package com.fishkey;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;

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
	
	/** 
	 * zwraca wczytany z pliku JSON zestaw fiszek 
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu fiszek
	 * @return	wczytany z pliku zestaw fiszek
	 * @throws QuizInitException gdy nie uda sie wczytac zestawu fiszek
	 */
	private static KnowledgeIndexSet parseJSON(String JSONText) throws QuizInitException {
		// Nazwy pol JSON:
		final String TAG_ID				= "id";
		final String TAG_FLASHCARDS		= "flashcards";
		final String TAG_F_ID			= "id";
		final String TAG_F_VALUE		= "value";
		final String TAG_F_DATE			= "date";
		// Wczytywanie JSON:
		KnowledgeIndexSet knowledgeIndexSet = new KnowledgeIndexSet();
		try {
			JSONObject jsonObject = new JSONObject(JSONText);
			JSONArray flashcardsArray = jsonObject.getJSONArray(TAG_FLASHCARDS);
			for(int i=0; i < flashcardsArray.length(); i++){
				try{
					JSONObject c = flashcardsArray.getJSONObject(i);
				    long id 	= Long.parseLong(c.getString(TAG_F_ID).trim());
				    int value	= Integer.parseInt(c.getString(TAG_F_VALUE).trim());
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
}
