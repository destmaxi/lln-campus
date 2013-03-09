package be.ac.ucl.lfsab1509.llncampus;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.ContentValues;

/**
 * Decrit un evenement (de ADE, ou ...)
 * @author damien
 *
 */
public class Event {
	private int day, month, year;
	private int beginHour, beginMin;
	private int endHour, endMin;
	private HashMap<String,String> details;
	/**
	 * Constructeur
	 * @param date
	 * @param beginTime
	 * @param duration
	 */
	public Event(final String date, final String beginTime, final String duration) {
		try {
			setDate(date);
			setBeginHour(beginTime);
			setDuration(duration);
		} catch(NumberFormatException e) {
			e.printStackTrace();//TODO
		}
		
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
	
	private void setDate(final String date) {
		this.day = Integer.valueOf(date.substring(3, 5));
		this.month = Integer.valueOf(date.substring(0, 2));
		this.year = Integer.valueOf(date.substring(6, 10));
	}
	private void setBeginHour(final String hour) {
		this.beginHour = Integer.valueOf(hour.substring(0, 2));
		this.beginMin = Integer.valueOf(hour.substring(3, 5));
	}
	private void setDuration(final String duration) {
		int i;
		String d = duration;
		this.endHour = this.beginHour;
		this.endMin = this.beginMin;
		i = d.indexOf('h');
		if (i != -1) {
			this.endHour += Integer.valueOf(d.substring(0,i));
			d = d.substring(i+1);
		}
		i = d.indexOf("min");
		if (i != -1) {
			this.endMin += Integer.valueOf(d.substring(0, i));
		}
		if (this.endMin >= 60) {
			this.endHour++;
			this.endMin-=60;
		}
	}
	/**
	 * Retourne le detail demande ou null si le detail n'existe pas 
	 * @param key
	 * @return le detail demande ou null si le detail n'existe pas
	 */
	public String getDetail(final String key) { return details.get(key); }
	
	/**
	 * Retourne l'annee de l'event
	 * @return l'annee
	 */
	public int getYear() { return this.year; }
	/**
	 * Retourne le mois de l'event
	 * @return le mois
	 */
	public int getMonth() { return this.month; }
	/**
	 * Retourne le jour de l'event
	 * @return le jour de l'event
	 */
	public int getDay() { return this.day; }
	/**
	 * Retourne l'heure de debut
	 * @return l'heure de debut ([0;23])
	 */
	public int getBeginHour() { return this.beginHour; }
	/**
	 * Retourne la minute de debut
	 * @return la minute de debut ([0;59])
	 */
	public int getBeginMin() { return this.beginMin; }
	/**
	 * Retourne l'heure de fin
	 * @return l'heure de fin ([0;23])
	 */
	public int getEndHour() { return this.endHour; }
	/**
	 * Retourne la minute de fin
	 * @return la minute de fin ([0;59])
	 */
	public int getEndMin() { return this.endMin; }

	
	/**
	 * 
	 */
	public String toString() {
		return "Date : "+day+"/"+month+"/"+year+"\n"
				+"Heure de début : "+beginHour+"h"+beginMin+"\n"
				+"Heure de fin : "+endHour+"h"+endMin+"\n"
				+"Détails :"+ details+"\n";
	}

	/**
	 * Pour l'insertion dans la table Horaire. 
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("DATE_BEGIN", this.year + "/" + this.month + "/" + this.day + " " + this.beginHour + ":" + this.beginMin );
		cv.put("DATE_END", this.year + "/" + this.month + "/" + this.day + " " + this.endHour + ":" + this.endMin );
		
		for ( String key : this.details.keySet()) {
			cv.put(key.toUpperCase(), this.details.get(key));
		}
		return cv;
	}
}