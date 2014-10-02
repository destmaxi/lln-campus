package be.ac.ucl.lfsab1509.llncampus;

import android.database.Cursor;
import android.location.Location;


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
 * Representation of simple GPS coordinates.
 */
public class Coordinates {
	private double lat;
	private double lon;

	/**
	 * Constructor.
	 * @param lat
	 * 			Latitude of the coordinate.
	 * @param lon
	 * 			Longitude of the coordinate.
	 */
	public Coordinates(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Get the latitude of the coordinate.
	 * @return Coordinate latitude.
	 */
	public double getLatitude() {
		return this.lat;
	}

	/** 
	 * Get the longitude of the coordinate.
	 * @return Coordinate longitude.
	 */
	public double getLongitude() {
		return this.lon;
	}

	/**
	 * Get a String representation of the coordinate in the form "latitude,longitude".
	 * @return String representation of the coordinate.
	 */
	public String toString(){
		return this.lat+","+this.lon;
	}
	
	/**
	 * Get the distance (in meters) between the current coordinate and the coordinate passed
	 * in argument.
	 * @param away
	 * 			Away coordinates.
	 * @return Distance (in meters) between current position and away.
	 */
	public double getDistance(Coordinates away){
		
		float[] results = new float[1];
		Location.distanceBetween(lat, lon, away.getLatitude(), away.getLongitude(), results);
		return results[0];
	}

	/**
	 * Analyze a string containing the auditorium name and give its associated coordinates.
	 * 
	 * @param room
	 * 				Auditorium name
	 * @return GPS coordinates if determined, else null.
	 */
	public static Coordinates getCoordinatesFromAuditorium(String room) {
		String acronym;
		String acronymBig = room.trim();
		if (acronymBig.length() < 4)
		{
			acronym = acronymBig.substring(0, acronymBig.length());
		}
		else
		{
			acronym = acronymBig.substring(0, 4);
		}
		Database db = LLNCampus.getDatabase();
		Cursor c = db
				.sqlRawQuery("SELECT Poi.LATITUDE, Poi.LONGITUDE FROM Poi, Auditorium WHERE Poi.ID = Auditorium.BUILDING_ID AND Auditorium.AUDITORIUM_NAME LIKE '"
						+ acronym + "%' LIMIT 1;");
		if (c==null || c.getCount() == 0) {
			return null;
		}
		c.moveToFirst();
		return new Coordinates(c.getFloat(0), c.getFloat(1));
	}
}
