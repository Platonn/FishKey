package com.fishkey.model;

import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * metody do operowania na dacie MidnightDate przechowywanej przez obiekt
 * @author Platon
 *
 */
public interface IMidnightDateHolder {
	
	/** Format daty zapisanej jako tekst: yyyy-MM-dd */
	public static final DateTimeFormatter dateFormat = ISODateTimeFormat.date();
	
	/** zwraca date */
	public DateMidnight getDate();
	
	/** zwraca date jako odpowiednio sformatowany String */
	public String getDateAsString();
	
	/** ustawia date ostatniego przepytania slowka na biezaca */
	public void setCurrentDate();
	
	/**
	 * zwraca liczbe dni od daty przechowywanej przez ten obiekt do daty podanej jako parametr 
	 * @param date	data do obliczenia roznicy dni
	 * @return		liczba dni od daty przechowywanej przez ten obiekt do daty
	 * 				podanej jako parametr
	 */
	public int dayDifferenceTo(DateMidnight date);
}
