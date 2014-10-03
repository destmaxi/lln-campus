package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;


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
 * Represent a library schedule.
 */
public class LibrarySchedule {
	private final int libID;
	private ArrayList<TimeSlot> timeSlots = null;

	/**
	 * Constructor.
	 * 
	 * @param libID
	 *            Library ID.
	 */
	public LibrarySchedule(int libID) {
		this.libID = libID;
	}

	/**
	 * Get the day of the week as a String (Monday, Tuesday,...) from its id.
	 * 
	 * @param i
	 *            Day of week in numerical form (0 = Monday, 1 = Tuesday,...).
	 * @return Day of the week.
	 * @todo Mettre les string dans strings.xml
	 */
	private String getDay(int i) {
		Resources r = LLNCampus.getContext().getResources();
		switch (i) {
		case 0:
			return r.getString(R.string.monday);
		case 1:
			return r.getString(R.string.tuesday);
		case 2:
			return r.getString(R.string.wednesday);
		case 3:
			return r.getString(R.string.thursday);
		case 4:
			return r.getString(R.string.friday);
		case 5:
			return r.getString(R.string.saturday);
		case 6:
			return r.getString(R.string.sunday);
		default:
			Log.e("BibliothequeHoraire.java", "Unknown day number : " + i);
			return "Unknown day";
		}
	}

	/**
	 * Check if we are currently in a opening hour.
	 * 
	 * @return true if the library is open, else false.
	 */
	public final boolean isOpen() {
		if (timeSlots == null) {
			loadTimeSlots();
		}

		Calendar now = Calendar.getInstance();
		int actualWeekday = (now.get(Calendar.DAY_OF_WEEK) - 1 + 6) % 7;
		int actualHourMin = (now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE));
		for (TimeSlot ts : timeSlots) {
			if (ts.getDay() == actualWeekday
					&& ts.getBeginTime() <= actualHourMin
					&& ts.getEndTime() > actualHourMin) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the full library schedule in textual form.
	 * 
	 * @return Full library schedule.
	 */
	public final String toString() {
		if (timeSlots == null) {
			loadTimeSlots();
		}
		String str = "";
		int precDay = -1;
		for (TimeSlot ts : timeSlots) {
			if (ts.getDay() != precDay) {
				str += getDay(ts.getDay()) + " : ";
			} else {
				str += "\t\t";
			}
			str += ts.getBeginTimeStr() + " - " + ts.getEndTimeStr() + "\n";
			precDay = ts.getDay();
		}

		return str;
	}

	/**
	 * Load time slots of the related library.
	 */
	private void loadTimeSlots() {
		timeSlots = new ArrayList<TimeSlot>();
		Cursor c = LLNCampus.getDatabase().select("Bibliotheque_Horaire",
				new String[] {"DAY", "BEGIN_TIME", "END_TIME"},
				"BUILDING_ID = ?", new String[] {String.valueOf(libID)},
				null, null, "DAY ASC", null);
		while (c.moveToNext()) {
			timeSlots.add(new TimeSlot(c.getInt(0), c.getInt(1), c
					.getInt(2)));
		}

	}

	private static class TimeSlot {
		private int day;
		private int beginTime;
		private int endTime;

		/**
		 * Constructor.
		 * 
		 * @param day
		 * 			Day number.
		 * @param beginTime
		 * 			Begin time (in minutes, starting from midnight).
		 * @param endTime
		 * 			End time (in minutes, starting from midnight).
		 */
		public TimeSlot(int day, int beginTime, int endTime) {
			this.day = day;
			this.beginTime = beginTime;
			this.endTime = endTime;
		}

		/**
		 * Get the day number of the week (0 = Monday,...).
		 * 
		 * @return Day number of the week.
		 */
		public int getDay() {
			return day;
		}

		/**
		 * Get the begin time.
		 * 
		 * @return Begin time under the form (hour * 60) + minutes.
		 */
		public int getBeginTime() {
			return beginTime;
		}

		/**
		 * Get the end time.
		 * 
		 * @return End time under the form (hour * 60) + minutes.
		 */
		public int getEndTime() {
			return endTime;
		}

		/**
		 * Get the begin time in String form.
		 * 
		 * @return Begin time under the form HHhMM or HHh.
		 */
		public String getBeginTimeStr() {
			return getStringTime(beginTime);
		}

		/**
		 * Get the end time in String form.
		 * 
		 * @return End time under the form HHhMM or HHh.
		 */
		public String getEndTimeStr() {
			return getStringTime(endTime);
		}

		/**
		 * Convert a time under the form (hour * 60) + minutes into String under
		 * the form HHhMM or HHh.
		 * 
		 * @param time
		 *            Time under the form (hour * 60) + minutes
		 * @return Time under the form HHhMM or HHh.
		 */
		private static String getStringTime(int time) {
			int hour = (int) Math.floor(time / 60);
			int min = time - hour * 60;
			if (min == 0) {
				return hour + "h";
			}
			if (min < 10) {
				return hour + "h0" + min;
			}
			return hour + "h" + min;
		}

	}
}
