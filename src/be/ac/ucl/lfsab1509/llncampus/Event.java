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
    Copyright (C) 2014 Quentin De Coninck

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
 */

/**
 * Represent an Event (e.g. from ADE).
 */
public class Event {
	// Key values for the HashMaps
	/** The course key value. */
	public static final String COURSE = "course";
	/** The trainees key value. */
	public static final String TRAINEES = "trainees";
	/** The trainers key value. */
	public static final String TRAINERS = "trainers";
	/** The room key value. */
	public static final String ROOM = "room";
	/** The activity name key value. */
	public static final String ACTIVITY_NAME = "activity_name";
	/** The title (course name) key value. */
	public static final String TITLE = "title";
	
	private Time begin;
	private Time end;
	private HashMap<String, String> details;
	private HashMap<String, String> keyName;
	private Coordinates coordinates;
	private boolean coordinates_loaded = false;
	
	/**
	 * Constructor.
	 * 
	 * @param date
	 *            Begin date.
	 * @param beginTime
	 *            Begin hour.
	 * @param duration
	 *            Event duration.
	 */
	public Event(final String date, final String beginTime,
			final String duration) {
		this.begin = new Time();
		this.end = new Time();
		try {
			setBegin(date, beginTime);
			setDuration(duration);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		keyName = new HashMap<String, String>();
		details = new HashMap<String, String>();	
	}

	/**
	 * Constructor.
	 * 
	 * @param begin
	 *            Begin date time (in millis from epoch [1st January, 1970]).
	 * @param end
	 *            End date time (in millis from epoch [1st January, 1970]).
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
	 * Add details to Event.
	 * 
	 * @param key
	 *            Detail key.
	 * @param value
	 *            Detail value.
	 */
	public final void addDetail(final String key, final String value) {
		details.put(key, value);
	}
	
	/**
	 * Add a name for the key (of an Event) given as argument.
	 * 
	 * @param key
	 *            Event Key key.
	 * @param name
	 *            Event key name.
	 */
	public final void addNameKey(final String key, final String name) {
		keyName.put(key, name);
	}

	/**
	 * Set the beginning of an Event.
	 * 
	 * @param date
	 *            Event date.
	 * @param beginTime
	 *            Event time.
	 */
	private void setBegin(final String date, final String beginTime) {
		int day = Integer.valueOf(date.substring(0, 2));
		int month = Integer.valueOf(date.substring(3, 5)) - 1;
		int year = Integer.valueOf(date.substring(6, 10));

		int beginHour = Integer.valueOf(beginTime.substring(0, 2));
		int beginMin = Integer.valueOf(beginTime.substring(3, 5));

		begin.set(0, beginMin, beginHour, day, month, year);
	}

	/**
	 * Set the duration of the Event.
	 * 
	 * @param duration
	 *            Event duration.
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
	 * Get the detail with the key given as argument.
	 * 
	 * @param key
	 *            Detail key.
	 * @return The value of the detail key if it exists, else null.
	 */
	public final String getDetail(final String key) {
		return details.get(key);
	}
	
	/**
	 * Get the name of the key given as argument.
	 * 
	 * @param key
	 *            Key of the requested key.
	 * @return The value of the key name if the key exists, else null.
	 */
	public final String getKeyName(final String key) {
		return keyName.get(key);
	}

	/**
	 * Get the begin time.
	 * 
	 * @return Begin time.
	 */
	public final Time getBeginTime() {
		return begin;
	}

	/**
	 * Get the end time.
	 * 
	 * @return End time.
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
	 * Get a string that contains the begin and end hours in format "BH:BM - EH=EM"
	 * where BH is the begin hour, BM is the begin minute, EH is the end hour and 
	 * EM is the end minute.
	 * 
	 * @return A string that contains the begin and end hours of the event.
	 */
	public final String getTime() {
		return begin.format("%H:%M") + " - " + end.format("%H:%M");
	}

	/**
	 * Get the key-value for the insertion in the "Horaire" table of the database.
	 * 
	 * @return ContentValues for insertion in database.
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

	/**
	 * Get the coordinates for the Event (based on the location of the Auditorium referenced
	 * in the database, uses the "room" key).
	 * 
	 * @return The coordinates of the Event.
	 */
	public Coordinates getCoordinates() {
		if(!this.coordinates_loaded){
			this.coordinates = Coordinates.getCoordinatesFromAuditorium(getDetail("room"));
			this.coordinates_loaded = true;
		}
		return this.coordinates;
	}
	
	/**
	 * Get the list of Events in the database.
	 * 
	 * @returns The list of Events in the database.
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
			e.addDetail(COURSE, c.getString(0));
			e.addNameKey(COURSE, LLNCampus.getContext().getString(R.string.course));
			e.addDetail(TRAINEES, c.getString(3));
			e.addNameKey(TRAINEES, LLNCampus.getContext().getString(R.string.trainees));
			e.addDetail(TRAINERS, c.getString(4));
			e.addNameKey(TRAINERS, LLNCampus.getContext().getString(R.string.trainers));
			e.addDetail(ROOM, c.getString(5));
			e.addNameKey(ROOM, LLNCampus.getContext().getString(R.string.room));
			e.addDetail(ACTIVITY_NAME, c.getString(6));
			e.addNameKey(ACTIVITY_NAME, LLNCampus.getContext().getString(R.string.activity_name));
			e.addDetail(TITLE, c.getString(7));
			e.addNameKey(TITLE, LLNCampus.getContext().getString(R.string.title));
			events.add(e);
		}
		c.close();
		return events;
	}
}