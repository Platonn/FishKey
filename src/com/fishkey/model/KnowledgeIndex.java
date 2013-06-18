package com.fishkey.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * wspolczynnik znajomosci fiszki wraz z data ostatniego przepytania z tej fiszki
 * @author Platon
 *
 */
public class KnowledgeIndex {
	
	/** Format daty zapisanej jako tekst */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/** wartosc wspolczynnika znajomosci fiszki */
	private int value;
	
	/** data ostatniego przepytania z tej fiszki */
	private Date date;
	
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
			date = dateFormat.parse(dateText);
	}
	
	/** setter wspolczynnika znajomosci slowka */
	public void setValue(int v){
		value = v;
	}
	
	/** ustawia date ostatniego przepytania slowka na biezaca */
	public void setCurrentDate() {
		date = new Date();
	}
	
	/** getter wspolczynnika znajomosci slowka */
	public int getValue() {
		return value;
	}
	
	/** zwraca date (String) w odpowiedniej postaci */
	public String getDateAsString() {
		return dateFormat.format(date);
	}
	
	/** zwraca wartosc wspolczynnika i date ostatniego przepytania z fiszki oddzielone myslnikiem */
	@Override
	public String toString(){
		return getValue() + " - " + getDateAsString(); 
	}
}
