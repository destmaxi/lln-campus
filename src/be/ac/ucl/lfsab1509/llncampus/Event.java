package be.ac.ucl.lfsab1509.llncampus;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.text.format.Time;

/**
 * Decrit un evenement (de ADE, ou ...)
 * @author damien
 *
 */
public class Event {
	private Time begin;
	private Time end;
	private HashMap<String,String> details;
	/**
	 * Constructeur
	 * @param date
	 * @param beginTime
	 * @param duration
	 */
	public Event(final String date, final String beginTime, final String duration) {
		this.begin = new Time();
		this.end = new Time();
		try {
			setBegin(date, beginTime);
			setDuration(duration);
		} catch(NumberFormatException e) {
			e.printStackTrace();//TODO
		}
		details = new HashMap<String,String>();
	}
	/**
	 * Constructeur
	 * @param beginTime (en miliseconde depuis epoch [1 jan 1970])
	 * @param endTime (en miliseconde depuis epoch [1 jan 1970])
	 */
	public Event(final int beginTime, final int endTime){
		this.begin = new Time();
		this.begin.set(beginTime);
		this.end = new Time();
		this.end.set(endTime);
		details = new HashMap<String,String>();
	}
	
	/**
	 * Ajouter des details a l'evenement
	 * @param key
	 * @param value
	 */
	public void addDetail(final String key, final String value) {
		details.put(key, value);
	}
	private void setBegin(final String date, final String beginT) {
		int day = Integer.valueOf(date.substring(3, 5));
		int month = Integer.valueOf(date.substring(0, 2));
		int year = Integer.valueOf(date.substring(6, 10));
		
		int beginHour = Integer.valueOf(beginT.substring(0, 2));
		int beginMin = Integer.valueOf(beginT.substring(3, 5));
		
		begin.set(0, beginMin, beginHour, day, month, year);
	}
	private void setDuration(final String duration) {
		int i;
		String d = duration;
		int endHour = this.begin.hour;
		int endMin = this.begin.minute;
		i = d.indexOf('h');
		if (i != -1) {
			endHour += Integer.valueOf(d.substring(0,i));
			d = d.substring(i+1);
		}
		i = d.indexOf("min");
		if (i != -1) {
			endMin += Integer.valueOf(d.substring(0, i));
		}
		if (endMin >= 60) {
			endHour++;
			endMin-=60;
		}
		
		end.set(0, endMin, endHour, this.begin.monthDay, this.begin.month, this.begin.year);
	}
	/**
	 * Retourne le detail demande ou null si le detail n'existe pas 
	 * @param key
	 * @return le detail demande ou null si le detail n'existe pas
	 */
	public String getDetail(final String key) { return details.get(key); }
	
	/**
	 * Retourne la date/heure de début
	 */
	public Time getBeginTime() {
		return begin;
	}
	
	/**
	 * Retourne la date/heure de fin
	 */
	public Time getEndTime() {
		return end;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return "Début : "+begin.format("%d/%m/%Y %H:%M")+"\n"
				+"Fin : "+end.format("%d/%m/%Y %H:%M")+"\n"
				+"Détails :"+ details+"\n";
	}

	/**
	 * Pour l'insertion dans la table Horaire. 
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("TIME_BEGIN", begin.toMillis(false));
		cv.put("TIME_END", end.toMillis(false));
		
		for ( String key : this.details.keySet()) {
			cv.put(key.toUpperCase(), this.details.get(key));
		}
		return cv;
	}
}