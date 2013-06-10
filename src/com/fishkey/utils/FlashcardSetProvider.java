package com.fishkey.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.fishkey.exceptions.LoadFlashcardSetException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.FlashcardSet;

/**
 * Dostarcza zestaw fiszek do przeprowadzenia quizu
 * @author Platon
 *
 */
public class FlashcardSetProvider extends FileReader {
	
	/** tag to oznaczania logow */
	private static final String LOG_TAG = FlashcardSetProvider.class.getName();
	
	/** domyslna nazwa pliku plain */
	private static final String DEFAULT_FILE_NAME_TXT = "slowka.txt";
	
	/** domyslna nazwa pliku JSON */
	private static final String DEFAULT_FILE_NAME_JSON = "slowka.json";
	
	/** 
	 * zwraca wczytany z pliku zestaw fiszek. 
	 * <p>
	 * W pliku powinny znajdowac sie w kolejnych liniach slowka w formacie:
	 * pol - ang (oddzielone myslnikiem)
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu fiszek
	 * @return	wczytany z pliku zestaw fiszek
	 * @throws LoadFlashcardSetException gdy nie uda sie wczytac zestawu fiszek
	 * @deprecated Zaleca sie uzycie <code>readJSONfile()</code>
	 * @see #parseJSON()
	 */
	protected static FlashcardSet parsePlain(String textPlain) throws LoadFlashcardSetException {
		FlashcardSet flashcardSet = new FlashcardSet();
		try {
			BufferedReader reader = new BufferedReader(new StringReader(textPlain));
		    String line = null;
		    String text[];																// Tablica, do ktorej beda wczytywane tresci oddzielone potem myslnikiem
			while ((line = reader.readLine()) != null) {
				text = line.split(" - ");												// Dwie spacje obok umozliwiaja podanie slowka zawierajacego "gêst¹ spacjê", np. semi-detached								// Rozdzielenie pytania od odpowiedzi przez separator (myslnik)
				flashcardSet.add(new Flashcard(text[1].trim(),text[0].trim()));			// Wczytuje wg schematu: "PYTANIE - ODPOWIEDZ" (musza byc spacje wokol myslnika)
			}
		} catch (IOException e) {
			throw new LoadFlashcardSetException();
		}
		return flashcardSet;
	}
	
	/** 
	 * zwraca wczytany z pliku JSON zestaw fiszek 
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu fiszek
	 * @return	wczytany z pliku zestaw fiszek
	 * @throws LoadFlashcardSetException gdy nie uda sie wczytac zestawu fiszek
	 */
	protected static FlashcardSet parseJSON(String JSONText) throws LoadFlashcardSetException {
		// Nazwy pol JSON:
		final String TAG_ID				= "id";
		final String TAG_NAME			= "name";
		final String TAG_FLASHCARDS		= "flashcards";
		final String TAG_ANG			= "ang";
		final String TAG_POL			= "pol";
		// Wczytywanie JSON:
		FlashcardSet flashcardSet = new FlashcardSet();
		try {
			JSONObject jsonObject = new JSONObject(JSONText);
			JSONArray flashcardsArray = jsonObject.getJSONArray(TAG_FLASHCARDS);
			for(int i=0; i < flashcardsArray.length(); i++){
			    JSONObject c = flashcardsArray.getJSONObject(i);
			    flashcardSet.add(new Flashcard( c.getString(TAG_POL).trim(),
			    								c.getString(TAG_ANG).trim() ));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Blad czytania JSON. Przyczyna: " + e.getMessage()); 
			throw new LoadFlashcardSetException();
		}
		return flashcardSet;
	}
	
	/**
	 * zwraca wczytany z zewnetrznego zrodla zestaw fiszek
	 * @param context	obiekt Context
	 * @return			zestaw fiszek
	 * @throws LoadFlashcardSetException	gdy wczytanie zestawu fiszek z zwenetrznego zrodla sie nie powiodlo sie
	 */
	public static FlashcardSet getFlashcardSet(Context context) throws LoadFlashcardSetException {
		// Implementacja ta wykorzystuje parsowanie JSON
		try {
			String fileText = readFromAssetsFile(context, DEFAULT_FILE_NAME_JSON, null);
			return parseJSON(fileText);
		} catch (IOException e) {
			throw new LoadFlashcardSetException();
		}

	}
}
