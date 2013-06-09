package com.fishkey.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.util.Log;

/**
 * sluzy do wczytywania danych z pliku
 * @author Platon
 *
 */
public class FileReader {
	/**
	 * tag to oznaczania logow
	 */
	private static final String TAG = FileReader.class.getName();
	
	/**
	 * domyslne kodowanie znakow w pliku
	 */
	protected static final String DEFAULT_CHARSET_NAME = "UTF-8";
	
	/**
	 * zwraca zawartosc podanego pliku (z katalogu assets) jako string
	 * @param context	obiekt context
	 * @param fileName	nazwa pliku
	 * @return			zawartosc pliku
	 * @throws IOException gdy wczytywanie sie nie powiedzie
	 */
	protected static String readFromAssetsFile(Context context, String fileName, String charsetName) throws IOException {
		if (charsetName == null)
			charsetName = DEFAULT_CHARSET_NAME;
		InputStream in;
		try{
			in = context.getAssets().open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		      sb.append(line).append("\n");
		    }
		    in.close();
		    return sb.toString();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Nie odnaleziono pliku: '"+fileName+"'");
			throw new IOException();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Niepoprawna nazwa kodowania pliku.");
			throw new IOException();
		} catch (IOException e) {
			Log.e(TAG, "Blad odczytu pliku: '"+fileName+"'");
			throw new IOException();
		}
	}
	
	private String readFromInputStream(InputStream inputStream) throws IOException{
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
		    total.append(line);
		}
		return total.toString();
	}
}
