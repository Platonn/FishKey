package com.fishkey.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.util.Log;

/**
 * sluzy do wczytywania danych z pliku
 * @author Platon
 *
 */
public class AssetsUtil {
	
	/** tag to oznaczania logow */
	private static final String LOG_TAG = AssetsUtil.class.getName();

	/**
	 * zwraca zawartosc (tekst) pliku o podanej nazwie (z katalogu assets)
	 * albo null, jesli plik nie istnieje badz nie mozna z niego czytac
	 * @param context	obiekt context
	 * @param fileName	nazwa pliku
	 * @return			zawartosc pliku
	 */
	public static String readFileAsString(Context context, String fileName) {
		InputStream in;
		StringBuilder sb = new StringBuilder();
   		boolean result = false;
		// wykonaj jako jeden nieprzerwany blok czynnosci na plikach - zaloz blokade na statyczny obiekt DATA_LOCK
   		synchronized (FileUtil.DATA_LOCK) {
   			try{
				in = context.getAssets().open(fileName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in), 8*1024);
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			      sb.append(line).append(FileUtil.LINE_SEP);
			    }
			    in.close();
			    result = true;
			} catch (FileNotFoundException e) {
				Log.e(LOG_TAG, "Nie odnaleziono pliku: '"+fileName+"'");
			} catch (IOException e) {
				Log.e(LOG_TAG, "Blad odczytu pliku: '"+fileName+"'");
			}
   		}
   		if(result) return sb.toString();
   		return null;
	}
}
