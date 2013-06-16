package com.fishkey.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * wspolczynnik znajomosci fiszki (o podanym id) wraz z data ostatniego przepytania z tej fiszki
 * @author Platon
 *
 */
public class KnowledgeIndex {
	
	/** id fiszki */
	private long id;
	
	/** wartosc wspolczynnika znajomosci fiszki */
	private int value;
	
	/** data ostatniego przepytania z tej fiszki */
	private Date date;
	
	/** format daty jako string */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public KnowledgeIndex(long i, int v, Date d) {
		id		= i;
		value	= v;
		date	= d;
	}
	
	/** setter wspolczynnika znajomosci slowka */
	public void setValue(int v){
		value = v;
	}
	
	/** ustawia date ostatniego przepytania slowka na biezaca */
	public void setCurrentDate() {
		date = new Date();
	}
	
	/** getter id */
	public long getID() {
		return id;
	}
	
	/** getter wspolczynnika znajomosci slowka */
	public int getValue() {
		return value;
	}
	
	public String getDateAsString() {
		return dateFormat.format(date);
	}
}
