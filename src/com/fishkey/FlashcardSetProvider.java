package com.fishkey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

import com.fishkey.model.Flashcard;
import com.fishkey.model.FlashcardSet;

/**
 * Dostarcza zestaw fiszek do przeprowadzenia quizu
 * @author Platon
 *
 */
public class FlashcardSetProvider {
	
	/** 
	 * wczytuje z pliku assets zestaw fiszek 
	 * <p>
	 * TODO: Robic to w tle - watku/usludze. A moze nawet wczytywac z inernetu badz lokalnej bazy danych.  
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu fiszek
	 * @param	flashcarSet	zestaw fiszek do wypelnienia wczytanymi fiszkami
	 */
	public static void importDataFromAssetsFile(Context context, String fileName, FlashcardSet flashcardSet) {
		try{
			InputStream in = context.getAssets().open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));	// Plik musi posiadac poprawne kodowanie UTF-8!
			String line;																// Tu beda wczytywane kolejne linie
			String[] text;																// Tablica bedzie przechowywac dwa stringi: odpowiedz oraz pytanie fiszki (w podanej kolejnosci)
			do {
				line = reader.readLine();												// Wczytanie nastepnej linii
				if(line==null)															// Trzeba bylo tak zrobic, zeby mozna bylo uzyc potem funkcji split na nie-null'owym stringu
					break;
				text = line.split(" - ");												// Dwie spacje obok umozliwiaja podanie slowka zawierajacego "gêst¹ spacjê", np. semi-detached								// Rozdzielenie pytania od odpowiedzi przez separator (myslnik)
				flashcardSet.add(new Flashcard(text[1].trim(),text[0].trim()));		// Wczytuje wg schematu: "PYTANIE - ODPOWIEDZ" (musza byc spacje wokol myslnika)
			} while(true);
			in.close();
		} catch (IOException e) {
			Log.i("Quiz", "QuizState - cannot open a file");
		}
	}
}
