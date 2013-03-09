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
	
	public Event(String date, String beginHour, String duration){
		try{
			setDate(date);
			setBeginHour(beginHour);
			setDuration(duration);
		} catch(NumberFormatException e){
			e.printStackTrace();//TODO
		}
		
		details = new HashMap<String,String>();
	}
	
	public void addDetail(String key, String value){
		details.put(key, value);
	}
	private void setDate(String d){
		this.day = Integer.valueOf(d.substring(3,5));
		this.month = Integer.valueOf(d.substring(0,2));
		this.year = Integer.valueOf(d.substring(6,10));
	}
	private void setBeginHour(String hour){
		this.beginHour = Integer.valueOf(hour.substring(0,2));
		this.beginMin = Integer.valueOf(hour.substring(3, 5));
	}
	private void setDuration(String duration){
		int i;
		this.endHour = this.beginHour;
		this.endMin = this.beginMin;				
		if((i = duration.indexOf('h')) != -1){
			this.endHour += Integer.valueOf(duration.substring(0,i));
			duration = duration.substring(i+1);
		}
		if((i = duration.indexOf("min")) != -1){
			this.endMin += Integer.valueOf(duration.substring(0, i));
		}
		if(this.endMin >= 60){
			this.endHour++;
			this.endMin-=60;
		}
	}
	
	public String getDetail(String key){ return details.get(key); }
	public int getYear(){ return this.year; }
	public int getMonth(){ return this.month; }
	public int getDay(){ return this.day; }
	public int getBeginHour(){ return this.beginHour; }
	public int getBeginMin(){ return this.beginMin; }
	public int getEndHour(){ return this.endHour; }
	public int getEndMin(){ return this.endMin; }

	
	public String toString(){
		
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