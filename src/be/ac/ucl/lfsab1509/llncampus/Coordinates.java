package be.ac.ucl.lfsab1509.llncampus;

import android.database.Cursor;
import android.location.Location;

/**
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
