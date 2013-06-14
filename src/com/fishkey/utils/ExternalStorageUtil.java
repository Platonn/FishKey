package com.fishkey.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.widget.Toast;

/**
 * zapisuje i odczytuje zawartosc plikow z pamieci zewnetrznej
 * @author Platon
 *
 */
public class ExternalStorageUtil {
	
	/** tag to oznaczania logow */
	private static final String LOG_TAG = ExternalStorageUtil.class.getName();
	
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
		boolean result = false;
		if (FileUtil.isExternalStorageWritable()) {
		    File dir = FileUtil.getExternalFilesDirAllApiLevels(context.getPackageName());
		    File file = new File(dir, fileName);
		    FileUtil.writeStringAsFile(text, file);
		    result = true;
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
		String text = null;
		if (FileUtil.isExternalStorageReadable()) {
			File dir = FileUtil.getExternalFilesDirAllApiLevels(context.getPackageName());
	        File file = new File(dir, fileName);
	        if (file.exists() && file.canRead()) {
	        	text = FileUtil.readFileAsString(file);
	        }
		}
		return text; 
	}
}
