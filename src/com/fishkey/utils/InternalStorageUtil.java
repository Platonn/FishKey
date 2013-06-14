package com.fishkey.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;

/**
 * zapisuje i odczytuje zawartosc plikow z pamieci wewnetrznej
 * @author Platon
 *
 */
public class InternalStorageUtil {
	
	/** tag to oznaczania logow */
	private static final String LOG_TAG = InternalStorageUtil.class.getName();
	
	/**
     * tworzy/nadpisuje plik o podanej nazwie, wypelniajac go podana zawartoscia (tekstem) 
     * i zwraca, czy cala operacja sie powiodla 
     * 
     * @param context		obiekt Context
     * @param text			tekst do wpisania do pliku
     * @param fileName		nazwa pliku do wypelnienia tekstem
     * @return				czy operacja wypelnienia pliku podana zawartoscia sie powiodla
     */
	public static boolean writeStringAsFile(Context context, String text, String fileName) {
	    FileOutputStream fos = null;
	    boolean result = false;
	    synchronized (FileUtil.DATA_LOCK) {
		    try {
		        fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		        fos.write(text.getBytes());
		    } catch (FileNotFoundException e) {
				Log.e(LOG_TAG, "Nie odnaleziono pliku: '"+fileName+"'");
			} catch (IOException e) {
				Log.e(LOG_TAG, "Blad odczytu pliku: '"+fileName+"'");
			} finally {
		        try {
		        	fos.close();
		        } catch (IOException e) { /* zignoruj */ }
		    }
	    }
	    return result;
	}
	
	/**
	 * zwraca zawartosc (tekst) pliku albo null jesli plik nie istnieje
	 * badz nie mozna z niego czytac
	 * 
	 * @param context	obiekt Context
	 * @param fileName	nazwa pliku, ktorego zawartosc (tekst) chcemy odczytac
	 * @return			zawartosc (tekst) pliku albo null jesli plik nie istnieje,
	 */
	public static String readFileAsString(Context context, String fileName) {
		FileInputStream fis = null;
		Scanner scanner = null;
		StringBuilder sb = new StringBuilder();
		boolean result = false;
	    synchronized (FileUtil.DATA_LOCK) {
	    	try {
				fis = context.openFileInput(fileName);
				scanner = new Scanner(fis);
				while (scanner.hasNextLine()) {
					sb.append(scanner.nextLine() + FileUtil.LINE_SEP);
				}
				result = true;
			} catch (FileNotFoundException e) {
				Log.e(LOG_TAG, "Nie odnaleziono pliku '"+fileName+"'");
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) { /* zignoruj */ }
				}
				if (scanner != null) {
					scanner.close();
				}
			}
	    }
		if (result) return sb.toString();
		return null;
	}
}
