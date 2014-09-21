package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;


/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Decrit un evenement (de ADE par exemple).
 */
public class Event {
	private Time begin;
	private Time end;
	private HashMap<String, String> details;
	private HashMap<String, String> keyName;
	private Coordinates coordinates;
	private boolean coordinates_loaded = false;
	
	/**
	 * Constructeur.
	 * 
	 * @param date
	 *            Date of begin
	 * @param beginTime
	 *            Hour of begin
	 * @param duration
	 *            Duration
	 * @param dayName
	 * 			  Lundi, Mardi,...
	 */
	public Event(final String date, final String beginTime,
			final String duration) {
		this.begin = new Time();
		this.end = new Time();
		try {
			setBegin(date, beginTime);
			setDuration(duration);
		} catch (NumberFormatException e) {
			e.printStackTrace(); // TODO
		}
		keyName = new HashMap<String, String>();
		details = new HashMap<String, String>();
		
	}

	/**
	 * Constructeur.
	 * 
	 * @param begin
	 *            Datetime de début (en miliseconde depuis epoch [1 jan 1970])
	 * @param end
	 *            Datetime de fin (en miliseconde depuis epoch [1 jan 1970])
	 */
	public Event(final long begin, final long end) {
		this.begin = new Time();
		this.begin.set(begin);
		this.end = new Time();
		this.end.set(end);
		keyName = new HashMap<String, String>();
		details = new HashMap<String, String>();
	}

	/**
	 * Ajouter des details a l'evenement.
	 * 
	 * @param key
	 *            Clé du détail
	 * @param value
	 *            Valeur du détail
	 */
	public final void addDetail(final String key, final String value) {
		details.put(key, value);
	}
	
	/**
	 * Permet de définir un nom pour la cle
	 * @param key
	 *            Clé de la clé
	 * @param value
	 *            Nom de la clé
	 */
	public final void addNameKey(final String key, final String name) {
		keyName.put(key, name);
	}

	/**
	 * Indique le début de l'évenement.
	 * 
	 * @param date
	 *            Date de l'évènement
	 * @param beginT
	 *            Heure de l'évènement
	 * @param dayName
	 * 			  Lundi, Mardi,...
	 */
	private void setBegin(final String date, final String beginT) {
		int day = Integer.valueOf(date.substring(0, 2));
		int month = Integer.valueOf(date.substring(3, 5)) - 1;
		int year = Integer.valueOf(date.substring(6, 10));

		int beginHour = Integer.valueOf(beginT.substring(0, 2));
		int beginMin = Integer.valueOf(beginT.substring(3, 5));

		begin.set(0, beginMin, beginHour, day, month, year);
	}

	/**
	 * Indique la durée de l'évènement.
	 * 
	 * @param duration
	 *            Durée de l'évènement
	 */
	private void setDuration(final String duration) {
		int i;
		String d = duration;
		int endHour = this.begin.hour;
		int endMin = this.begin.minute;
		i = d.indexOf('h');
		if (i != -1) {
			endHour += Integer.valueOf(d.substring(0, i));
			d = d.substring(i + 1);
		}
		i = d.indexOf("min");
		if (i != -1) {
			endMin += Integer.valueOf(d.substring(0, i));
		}
		if (endMin >= 60) {
			endHour++;
			endMin -= 60;
		}

		end.set(0, endMin, endHour, this.begin.monthDay, this.begin.month,
				this.begin.year);
	}

	/**
	 * Fournit le detail demandé ou null si le detail n'existe pas .
	 * 
	 * @param key
	 *            Clé du detail
	 * @return Valeur du détail demande ou null si le detail n'existe pas
	 */
	public final String getDetail(final String key) {
		return details.get(key);
	}
	
	/**
	 * Fournit le nom demandé ou null si la clé n'existe pas .
	 * 
	 * @param key
	 *            Clé de la clé
	 * @return Nom de la cle demande ou null si la cle n'existe pas
	 */
	public final String getKeyName(final String key) {
		return keyName.get(key);
	}

	/**
	 * Fournit la date/heure de début.
	 * 
	 * @return Le datetime de début
	 */
	public final Time getBeginTime() {
		return begin;
	}

	/**
	 * Fournit la date/heure de fin.
	 * 
	 * @return Le datetime de fin
	 */
	public final Time getEndTime() {
		return end;
	}

	@Override
	public final String toString() {
		String detailsTxt = "";
		for (String key : details.keySet()) {
			detailsTxt += keyName.get(key) + " : " + details.get(key) + "\n";
		}
		return "Date : " + begin.monthDay + "/" + begin.month + "/"
				+ begin.year + " " + getTime() + "\n" + detailsTxt;
	}

	/**
	 * Fournit les heures de début et de fin.
	 * 
	 * @return Heures de début et de fin.
	 */
	public final String getTime() {
		return begin.format("%H:%M") + " - " + end.format("%H:%M");
	}

	/**
	 * Fournit les clé-valeurs pour l'insertion dans la table Horaire de la base
	 * de donnée.
	 * 
	 * @return ContentValues pour l'insertion dans la base de donnée
	 */
	public final ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("TIME_BEGIN", begin.toMillis(false));
		cv.put("TIME_END", end.toMillis(false));

		for (String key : this.details.keySet()) {
			cv.put(key.toUpperCase(Locale.FRENCH), this.details.get(key));
		}
		return cv;
	}

	public Coordinates getCoordinates() {
		if(!this.coordinates_loaded){
			this.coordinates = Coordinates.getCoordinatesFromAuditorium(getDetail("room"));
			this.coordinates_loaded = true;
		}
		return this.coordinates;
	}
	
	/**
	 * Fournit la liste des évènements présents dans la base de donnée.
	 */
	public static ArrayList<Event> getList() {
		ArrayList<Event> events = new ArrayList<Event>();
		Cursor c = LLNCampus.getDatabase().sqlRawQuery(
				"SELECT " +
						"h.COURSE, " +
						"h.TIME_BEGIN, " +
						"h.TIME_END, " +
						"h.TRAINEES, " +
						"h.TRAINERS, " +
						"h.ROOM, " +
						"h.ACTIVITY_NAME, " +
						"c.NAME " +
				"FROM " +
					"Horaire as h, Courses as c " +
				"WHERE " +
					"h.COURSE = c.CODE " +
				"ORDER BY " +
					"TIME_BEGIN ASC");
		while (c.moveToNext()) {
			Event e = new Event(c.getLong(1), c.getLong(2));
			e.addDetail("course", c.getString(0));
			e.addNameKey("course", LLNCampus.getContext().getString(R.string.course));
			e.addDetail("trainees", c.getString(3));
			e.addNameKey("trainees", LLNCampus.getContext().getString(R.string.trainees));
			e.addDetail("trainers", c.getString(4));
			e.addNameKey("trainers", LLNCampus.getContext().getString(R.string.trainers));
			e.addDetail("room", c.getString(5));
			e.addNameKey("room", LLNCampus.getContext().getString(R.string.room));
			e.addDetail("activity_name", c.getString(6));
			e.addNameKey("activity_name", LLNCampus.getContext().getString(R.string.activity_name));
			e.addDetail("title", c.getString(7));
			e.addNameKey("title", LLNCampus.getContext().getString(R.string.title));
			events.add(e);
		}
		c.close();
		return events;
	}
}