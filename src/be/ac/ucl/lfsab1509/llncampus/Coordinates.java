package be.ac.ucl.lfsab1509.llncampus;

import android.database.Cursor;

public class Coordinates {
	private float lat;
	private float lon;

	public Coordinates(float lat, float lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public float getLat() {
		return this.lat;
	}

	public float getLon() {
		return this.lon;
	}

	public String toString(){
		return this.lat+","+this.lon;
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
