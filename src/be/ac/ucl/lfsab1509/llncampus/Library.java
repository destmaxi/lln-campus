package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

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
  * Implementation of a library object.
 */
public final class Library {
	private int id;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private LibrarySchedule schedule;
	private String acronym;
	private String scheduleUrl;
	private static ArrayList<Library> libraries = null;

	/**
	 * Constructor (private).
	 * 
	 * @param id
	 *            Library ID.
	 * @param name
	 *            Library name.
	 * @param latitude
	 *            Library latitude.
	 * @param longitude
	 *            Library longitude.
	 * @param address
	 *            Library address.
	 * @param schedule
	 *            Library opening schedule.
	 * @param acronym
	 *            Library acronym.
	 * @param scheduleUrl
	 *            URL to detailed schedule for the library ("blocus", holidays,...).
	 */
	private Library(int id, String name, double latitude,
			double longitude, String address, LibrarySchedule schedule,
			String acronym, String scheduleUrl) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.schedule = schedule;
		this.acronym = acronym;
		this.scheduleUrl = scheduleUrl;
	}

	/**
	 * Get the library ID.
	 * @return Library ID.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Get the library name.
	 * @return Library name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the library latitude.
	 * @return Library latitude.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Get the library longitude.
	 * @return Longitude library.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Get the library address.
	 * @return Library address.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Get the library picture ID.
	 * 
	 * @return Library picture ID.
	 */
	public int getPicture() {
		switch (this.id) {
		case 28:
			return R.drawable.bst;
		case 29:
			return R.drawable.bdrt;
		case 30:
			return R.drawable.bpsp;
		case 31:
			return R.drawable.bflt;
		case 32:
			return R.drawable.pasencore;
		case 33:
			return R.drawable.bisp;
		case 34:
			return R.drawable.bsm;
		case 35:
			return R.drawable.bspo;
		case 36:
			return R.drawable.btec;
		default:
			Log.e("Bibliotheque.java",
					"Didn't find the library picture");
			return 0;
		}
	}

	/**
	 * Get the library opening schedule.
	 * 
	 * @return Library opening schedule.
	 */
	public LibrarySchedule getSchedule() {
		return this.schedule;
	}

	/**
	 * Get the library acronym.
	 * 
	 * @return Library acronym.
	 */
	public String getSigle() {
		return this.acronym;
	}

	/**
	 * Get the URL to fetch detailed information about the library opening schedule.
	 * 
	 * @return URL to fetch detailed information about the library opening schedule.
	 */
	public String getScheduleUrl() {
		return this.scheduleUrl;
	}

	/**
	 * Indicate if the library is currently open.
	 * 
	 * @return true if the library is open, else false.
	 */
	public boolean isOpen() {
		return getSchedule().isOpen();
	}

	/**
	 * Get the list of all libraries in the database.
	 * 
	 * @return List of all libraries in the database.
	 */
	public static ArrayList<Library> getLibraryList() {
		if (libraries != null) {
			return libraries;
		}
		libraries = new ArrayList<Library>();
		Cursor c = LLNCampus.getDatabase().sqlRawQuery(
				"SELECT " + "Poi.ID, " + "Poi.NAME, " + "Poi.LATITUDE, "
						+ "Poi.LONGITUDE, " + "Poi.ADDRESS, "
						+ "Bibliotheque.SIGLE, " + "Bibliotheque.URL_SCHEDULE"
						+ " FROM " + "Poi, Bibliotheque " + " WHERE "
						+ "Poi.ID = Bibliotheque.BUILDING_ID " + "ORDER BY "
						+ "Poi.NAME ASC");
		Library bib;
		while (c.moveToNext()) {
			bib = new Library(c.getInt(0), c.getString(1), c.getDouble(2),
					c.getDouble(3), c.getString(4), new LibrarySchedule(
							c.getInt(0)), c.getString(5), c.getString(6));
			libraries.add(bib);
		}
		c.close();
		return libraries;
	}

}
