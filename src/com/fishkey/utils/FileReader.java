package com.fishkey.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import android.content.Context;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.widget.Toast;

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
	
	private static final String LINE_SEP = System.getProperty("line.separator");

	public static void writeInternal(Context context, String fileName, String text) throws IOException {
	    FileOutputStream fos = null;
	    try {
	
	    	// note that there are many modes you can use
	        fos = context.openFileOutput(fileName, Context.MODE_WORLD_WRITEABLE); // TODO: uwaga - plik o publicznym dostepie, kiedys to zmienic na prywatny, gdy bede korzystal z lacza http do synchronizacji
	        fos.write(text.getBytes());
	        Toast.makeText(context, "Zapisano do pliku.", Toast.LENGTH_SHORT).show(); 
	    } catch (FileNotFoundException e) {
	        Log.e(Constants.DATA, "File not found", e);
	        throw new IOException();
	    } catch (IOException e) {
	        Log.e(Constants.DATA, "IO problem", e);
	        throw new IOException();
	    } finally {
	        try {
	        	fos.close();
	        } catch (IOException e) {
	        	// ignore, and take the verbosity punch from Java ;)
	        }
	    }
	}

   public static String readInternal(Context context, String fileName) throws IOException {
	   FileInputStream fis = null;
	   Scanner scanner = null;
	   StringBuilder sb = new StringBuilder();
	   try {
		   fis = context.openFileInput(fileName);
		   // scanner does mean one more object, but it's easier to work with
		   scanner = new Scanner(fis);
		   while (scanner.hasNextLine()) {
			   sb.append(scanner.nextLine() + LINE_SEP);
		   }
		   Toast.makeText(context, "Wczytano z pliku", Toast.LENGTH_SHORT).show();
	   } catch (FileNotFoundException e) {
		   Log.e(Constants.DATA, "File not found", e);
		   throw new IOException();
	   } finally {
		   if (fis != null) {
			   try {
				   fis.close();
			   } catch (IOException e) {
				   // ignore, and take the verbosity punch from Java ;)
			   }
		   }
		   if (scanner != null) {
			   scanner.close();
		   }
	   }      
      return sb.toString();
   }
   
   protected static void writeExternal(Context context, String fileName, String text) throws IOException {
	      if (FileUtil.isExternalStorageWritable()) {
	         File dir = FileUtil.getExternalFilesDirAllApiLevels(context.getPackageName());
	         File file = new File(dir, fileName);
	         FileUtil.writeStringAsFile(text, file);
	         Toast.makeText(context, "File written", Toast.LENGTH_SHORT).show();
	      } else {
	         Toast.makeText(context, "External storage not writable", Toast.LENGTH_SHORT).show();
	         throw new IOException();
	      }
	   }

	   protected static String readExternal(Context context, String fileName) throws IOException {
	      if (FileUtil.isExternalStorageReadable()) {
	         File dir = FileUtil.getExternalFilesDirAllApiLevels(context.getPackageName());
	         File file = new File(dir, fileName);
	         String text;
	         if (file.exists() && file.canRead()) {
	        	text = FileUtil.readFileAsString(file);
	            Toast.makeText(context, "File read", Toast.LENGTH_SHORT).show();
	         } else {
	            Toast.makeText(context, "Unable to read file: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();  
		         throw new IOException();
	         }
	         return text; 
	      } else {
	         Toast.makeText(context, "External storage not readable", Toast.LENGTH_SHORT).show();
	         throw new IOException();
	      }
	   }
}
