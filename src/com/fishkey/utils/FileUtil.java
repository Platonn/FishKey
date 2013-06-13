package com.fishkey.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.FileChannel;

/**
 * posiada metody do operowania na plikach
 * 
 * @author Platonn 
 * @author ...na podstawie przykladu z ksiazki "Android in practice"
 */
public final class FileUtil {
	
	/** Tag do oznaczania logow */
	private static final String LOG_TAG = FileUtil.class.getName();
	
	/** Zalecana sciezka na dane */ 
	private static final String EXT_STORAGE_PATH_PREFIX = "/Android/data/";
   
   	/** Zalecany katalog na pliki */
   	private static final String EXT_STORAGE_FILES_PATH_SUFFIX = "/files/";
   
   	/** Zalecany katalog na pliki tymczasowe */
   	private static final String EXT_STORAGE_CACHE_PATH_SUFFIX = "/cache/";

   	/**
   	 * obiekt statyczny do blokowania ciagow wrazliwych operacji na plikach
   	 * za pomoca <code>synchronized</code> .
   	 * <p>
   	 * (<code>new Object[0]</code> zajmuje mniej pamieci niz <code>new Object</code>)
   	 */
   	public static final Object[] DATA_LOCK = new Object[0];

   	/**
   	 * zwraca, czy mozna pisac w zewnetrznej pamieci
   	 * 
   	 * @return	czy mozna pisac w zewnetrznej pamieci
   	 */
   	public static boolean isExternalStorageWritable() {
   		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
   	}

   	/**
   	 * zwraca, czy mozna czytac z zewnetrznej pamieci
   	 * 
   	 * @return	czy mozna czytac z zewnetrznej pamieci
   	 */
   	public static boolean isExternalStorageReadable() {
   		if (isExternalStorageWritable()) return true;
   		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
   	}

   	/**
   	 * zwraca zalecana pelna sciezke do katalogu na pliki aplikacji
   	 * (takze dla API level 8 lub nizszego). Tworzac sciezke, uwzglednia
   	 * podana nazwe paczki aplikacji.
   	 * 
   	 * @param packageName	nazwa paczki aplikacji
   	 * @return 				obiekt File zalecana pelna sciezka do katalogu na pliki aplikacji
   	 */
   	public static File getExternalFilesDirAllApiLevels(final String packageName) {
   		return FileUtil.getExternalDirAllApiLevels(packageName, EXT_STORAGE_FILES_PATH_SUFFIX);
   	}
   
   	/**
   	 * zwraca obiekt File - zalecana pelna sciezke do katalogu na pliki tymczasowe aplikacji
   	 * (takze dla API level 8 lub nizszego). Tworzac sciezke, uwzglednia
   	 * podana nazwe paczki aplikacji.
   	 * 
   	 * @param packageName	nazwa paczki aplikacji
   	 * @return 				obiekt File - zalecana pelna sciezka do katalogu na pliki tymczasowe aplikacji
   	 */
   	public static File getExternalCacheDirAllApiLevels(final String packageName) {
   		return FileUtil.getExternalDirAllApiLevels(packageName, EXT_STORAGE_CACHE_PATH_SUFFIX);
   	}
   
   	/**
     * zwraca obiekt File - zalecana sciezke do katalogu na dane aplikacji, uwzgledniajac nazwe paczki aplikacji 
     * oraz podana nazwe katalogu. Jesli nie ma jeszcze tego katalogu, to (jesli to mozliwe) go wraz z drzewem
     * katalogow (ojcow).
     * @param packageName	nazwa paczki aplikacji
     * @param suffixType		nazwa katalogu
     * @return				obiekt File - zalecana pelna sciezka do katalogu na dane aplikacji
     */
   	private static File getExternalDirAllApiLevels(final String packageName, final String suffixType) {
   		File dir = new File(Environment.getExternalStorageDirectory() + EXT_STORAGE_PATH_PREFIX + packageName + suffixType);
   		// wykonaj jako jeden nieprzerwany blok czynnosci na plikach - zaloz blokade na statyczny obiekt DATA_LOCK
   		synchronized (FileUtil.DATA_LOCK) {
   			try {
   				dir.mkdirs();
   				dir.createNewFile();
   			} catch (IOException e) {
   				Log.e(LOG_TAG, "Blad tworzenia pliku", e);
   			}
   		}
   		return dir;
   	}

    /**
     * kopiuje zawartosc podanego plik do podanego docelowego pliku i zwraca, czy operacja
     * kopiowania sie powiodla
     * 
     * @param src	plik zrodlowy
     * @param dst	plik docelowy
     * @return		czy operacja kopiowania sie 
     */
   	public static boolean copyFile(final File src, final File dst) {
   		boolean result = false;
   		FileChannel inChannel = null;
   		FileChannel outChannel = null;
   		// wykonaj jako jeden nieprzerwany blok czynnosci na plikach - zaloz blokade na statyczny obiekt DATA_LOCK
   		synchronized (FileUtil.DATA_LOCK) {
   			try {
   				inChannel = new FileInputStream(src).getChannel();
   				outChannel = new FileOutputStream(dst).getChannel();
   				inChannel.transferTo(0, inChannel.size(), outChannel);	// kopiowanie bitowe (szybkie)
   				result = true;
   			} catch (IOException e) {
   				Log.e(LOG_TAG, "Blad kopiowania pliku", e);
   			} finally {
   				// Zwolnij przydzielone zasoby dla inChannel
   				if (inChannel != null && inChannel.isOpen()) {
   					try {
   						inChannel.close();
   					} catch (IOException e) { /* zignorowac */ }
   				}
   				// Zwolnij przydzielone zasoby dla inChannel
   				if (outChannel != null && outChannel.isOpen()) {
   					try {
   						outChannel.close();
   					} catch (IOException e) { /* zignorowac */ }
   				}
   			}
   		}
   		return result;
   	}

   	/**
     * tworzy/nadpisuje podany plik, wypelniajac go podana zawartoscia (tekstem) 
     * i zwraca, czy cala operacja sie powiodla 
     * 
     * @param fileContents	tekst do wpisania do pliku
     * @param file			plik do wypelnienia
     * @return				czy operacja wypelnienia pliku podana zawartoscia sie powiodla
     */
   	public static boolean writeStringAsFile(final String fileContents, final File file) {
   		boolean result = false;
		// wykonaj jako jeden nieprzerwany blok czynnosci na plikach - zaloz blokade na statyczny obiekt DATA_LOCK
		synchronized (FileUtil.DATA_LOCK) {
			try{
				if (file != null) {
					file.createNewFile(); // jesli plik nie istnial, tworzy go, w przeciwnym przypadku nic sie nie stanie (tylko zwroci false)
					Writer toFileWriter = new BufferedWriter(new FileWriter(file), 1024);
					toFileWriter.write(fileContents);
					toFileWriter.close();
					result = true;
				}
			} catch (IOException e) {
				Log.e(LOG_TAG, "Blad przy wypelnianiu pliku podana zawartoscia " + e.getMessage(), e);
			}
		}
      	return result;
   	}

   	/**
   	 * Dopisuje podany String na koncu pliku i zwraca, czy operacja sie powiodla
   	 * 
   	 * @param appendContents	tekst do dopisania
   	 * @param file			plik, do ktorego ma byc dopisany tekst
   	 * @return				czy operacja dopisywania Stringa na koncu pliku sie powiodla
   	 */
   	public static boolean appendStringToFile(final String appendContents, final File file) {
   		boolean result = false;
		// wykonaj jako jeden nieprzerwany blok czynnosci na plikach - zaloz blokade na statyczny obiekt DATA_LOCK
   		synchronized (FileUtil.DATA_LOCK) {
   			try {
	            if ((file != null) && file.canWrite()) {
	            	file.createNewFile(); // jesli plik nie istnial, tworzy go, w przeciwnym przypadku nic sie nie stanie (tylko zwroci false)
	            	Writer out = new BufferedWriter(new FileWriter(file, true), 1024);
	            	out.write(appendContents);
	            	out.close();
	            	result = true;
	            	}
   			} catch (IOException e) {
   				Log.e(LOG_TAG, "Blad przy dopisywaniu tekstu na koncu pliku " + e.getMessage(), e);
   			}
   		}
   		return result;
   	}

   	/**
    * zwraca zawartosc (tekst) pliku albo null jesli plik nie istnieje,
    * badz nie mozna z niego czytac
    * 
    * @param file	plik, ktorego zawartosc (tekst) chcemy odczytac
    * @return		zawartosc (tekst) pliku albo null jesli plik nie istnieje,
    */
   	public static String readFileAsString(final File file) {
   		StringBuilder sb = null;
   		boolean result = false;
   		// wykonaj jako jeden nieprzerwany blok czynnosci na plikach - zaloz blokade na statyczny obiekt DATA_LOCK
   		synchronized (FileUtil.DATA_LOCK) {
   			try {
	   			if ((file != null) && file.canRead()) {
	   				sb = new StringBuilder();
	   				String line = null;
	   				BufferedReader in = new BufferedReader(new FileReader(file), 1024);
	   				while ((line = in.readLine()) != null) {
	   					sb.append(line + System.getProperty("line.separator"));
	   				}
	   			}
	   			result = true;
   			} catch (IOException e) {
   				Log.e(LOG_TAG, "Blad przy czytaniu zawartosci pliku " + e.getMessage(), e);
   			}
   		}
   		if (result) return sb.toString();
   		return null;
   	}
}
