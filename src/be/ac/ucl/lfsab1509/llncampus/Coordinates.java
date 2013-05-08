package be.ac.ucl.lfsab1509.llncampus;

import android.database.Cursor;
import android.location.Location;


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
 * Représentation de coordonnées gps simple.
 */
public class Coordinates {
	private double lat;
	private double lon;

	public Coordinates(double d, double e) {
		this.lat = d;
		this.lon = e;
	}

	public double getLat() {
		return this.lat;
	}

	public double getLon() {
		return this.lon;
	}

	public String toString(){
		return this.lat+","+this.lon;
	}
	
	/**
	 * Retourne la distance en metres entre la coordonnee courante et la
	 * coordonnees placee en argument.
	 */
	public double getDistance(Coordinates b){
		
		float[] results = new float[1];
		Location.distanceBetween(lat, lon, b.getLat(), b.getLon(), results);
		return results[0];
	}

	/**
	 * Analyse une chaine de caractère contenant le nom de l'auditoire et
	 * fournit les coordonnées gps associées.
	 * 
	 * @param room
	 * @return Les coordonnées gps si elles ont pu être déterminé, null sinon.
	 */
	public static Coordinates getCoordinatesFromAuditorium(String room) {
		String sigle = (room.trim()).substring(0, 4);
		Database db = LLNCampus.getDatabase();
		Cursor c = db
				.sqlRawQuery("SELECT Poi.LATITUDE, Poi.LONGITUDE FROM Poi, Auditorium WHERE Poi.ID = Auditorium.BUILDING_ID AND Auditorium.AUDITORIUM_NAME LIKE '"
						+ sigle + "%' LIMIT 1;");
		if (c==null || c.getCount() == 0) {
			return null;
		}
		c.moveToFirst();
		return new Coordinates(c.getFloat(0), c.getFloat(1));
	}
}
