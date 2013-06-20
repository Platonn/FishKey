package com.fishkey.model;

import java.util.LinkedHashMap;

import org.joda.time.DateMidnight;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * zestaw wspolczynnikow znajomosci fiszek - indeksowanych po id fiszek
 * @author Platon
 *
 */
public class KnowledgeIndexSet extends LinkedHashMap<Long, KnowledgeIndex> implements IMidnightDateHolder {
	private static final long serialVersionUID = 3726907896583249801L;
	
	/** id zestawu fiszek, ktorego dotyczy ten Set ze wspolczynnikami znajomosci fiszek */
	Long id;
	
	/** data ostatniego przeprowadzenia quizu z tego zestawu fiszek */
	DateMidnight date;
	
	public KnowledgeIndexSet(Long id, String dateText) {
		this.id = id;
		if (dateText == null || dateText.equals(""))
			date = new DateMidnight();
		else {
			date = DateMidnight.parse(dateText,dateFormat);
		}
	}
	
	/** getter id zestawu fiszek, ktorego dotyczy ten Set ze wspolczynnikami znajomosci fiszek */
	public Long getId() {
		return id;
	}
	
	/** getter daty ostatniego przeprowadzenia quizu z zestawu fiszek, ktorego dotyczy ten Set ze wspolczynnikami znajomosci fiszek  */
	public DateMidnight getDate() {
		return date;
	}
	
	/** getter daty (jako String) ostatniego przeprowadzenia quizu z zestawu fiszek, ktorego dotyczy ten Set ze wspolczynnikami znajomosci fiszek  */
	public String getDateAsString() {
		return date.toString(dateFormat);
	}
	
	/** ustawia date ostatniego quizu na biezaca */
	public void setCurrentDate() {
		date = new DateMidnight();
	}
	
	/**
	 * zwraca liczbe dni od daty przechowywanej przez ten obiekt do daty podanej jako parametr 
	 * @param datedata do obliczenia roznicy dni
	 * @return				liczba dni od daty przechowywanej przez ten obiekt do daty
	 * 						podanej jako parametr
	 */
	public int dayDifferenceTo(DateMidnight date){
		return Days.daysBetween(this.getDate(), date).getDays();
	}
}
