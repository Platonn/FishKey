package com.fishkey.model;

import java.text.ParseException;

import org.joda.time.DateMidnight;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import android.util.Log;

/**
 * wspolczynnik znajomosci fiszki wraz z data ostatniego przepytania z tej fiszki
 * @author Platon
 *
 */
public class KnowledgeIndex implements IMidnightDateHolder {
	
	/** tag do oznaczania logow */
	private static final String LOG_TAG = KnowledgeIndex.class.getName();
	
	/** Format daty zapisanej jako tekst: yyyy-MM-dd */
	public static final DateTimeFormatter dateFormat = ISODateTimeFormat.date();
	
	/** wartosc wspolczynnika znajomosci fiszki */
	private int value;
	
	/** data ostatniego przepytania z tej fiszki */
	private DateMidnight date;
	
	/**
	 * @param v			wartosc wspolczynnika znajomosci fiszki
	 * @param dateText	data ostatniego przepytania z fiszki
	 * @throws ParseException gdy data zostala podana w formacie innym niz
	 * 							<code>KnowledgeIndexSet#dateFormat</code>
	 * @see KnowledgeIndexSet#dateFormat
	 */
	public KnowledgeIndex(int v, String dateText) throws ParseException {
		value	= v;
		if (dateText == null || dateText.equals(""))
			setCurrentDate();
		else
			date = new DateMidnight(dateText);
		if (date==null)
			throw new ParseException(dateText, -1);
	}
	
	/** setter wspolczynnika znajomosci slowka */
	public void setValue(int v){
		value = v;
	}
	
	/** ustawia date ostatniego przepytania slowka na biezaca */
	public void setCurrentDate() {
		date = new DateMidnight();
	}
	
	/** getter wspolczynnika znajomosci slowka */
	public int getValue() {
		return value;
	}
	
	/** zwraca - jako String - date ostatniego przepytania z fiszki */
	public String getDateAsString() {
		return date.toString(dateFormat);
	}
	
	/** zwraca - jako obiekt DateTime - date ostatniego przepytania z fiszki */
	public DateMidnight getDate() {
		return date;
	}
	
	public int dayDifferenceTo(DateMidnight currentDate){
		return Days.daysBetween(new DateMidnight(this.getDate()), new DateMidnight(currentDate)).getDays();
	}
	
	/** zwraca wartosc wspolczynnika i date ostatniego przepytania z fiszki oddzielone myslnikiem */
	@Override
	public String toString(){
		return getValue() + " - " + getDateAsString(); 
	}
}
