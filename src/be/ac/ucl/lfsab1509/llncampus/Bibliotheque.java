package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

/**
 * Implementation of an Library object
 * 
 */
public class Bibliotheque {
	private int ID;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private BibliothequeHoraire horaire;
	private String sigle;
	private String scheduleUrl;
	private static ArrayList<Bibliotheque> bibliotheques = null;

	/**
	 * Constructeur
	 * @param id ID de la bibliotheque
	 * @param name Nom
	 * @param latitude Latitude
	 * @param longitude Longitutude
	 * @param address
	 * @param schedule : horaire
	 * @param sigle
	 * @param scheduleUrl : lien url vers les horaires spéciaux
	 */
	private Bibliotheque(int id, String name, double latitude, double longitude, String address, BibliothequeHoraire horaire, 
				String sigle, String scheduleUrl){
		this.ID = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.horaire = horaire;
		this.sigle =  sigle;
		this.scheduleUrl= scheduleUrl;
	}

	public final int getID() {
		return this.ID;
	}
	public final String getName() {
		return name;
	}
	public final double getLatitude() {
		return latitude;
	}
	public final double getLongitude() {
		return longitude;
	}
	public final String getAddress() {
		return address;
	}	
	public final int getImg() {
		switch (this.ID) {
			case 28 : return R.drawable.bst;
			case 29 : return R.drawable.bdrt;
			case 30 : return R.drawable.bpsp;
			case 31 : return R.drawable.pasencore;
			case 32 : return R.drawable.pasencore;
			case 33 : return R.drawable.bisp;
			case 34 : return R.drawable.bsm;
			case 35 : return R.drawable.bspo;
			case 36 : return R.drawable.pasencore;
			default :
				Log.e("Library.java", "Ne trouve pas l'image vers l'auditoire de Takepicture");
				return 0;
		}
	 }

	public BibliothequeHoraire getHoraire() {
		return this.horaire;
	}
	public String getSigle() {
		return this.sigle;
	}
	public String getScheduleUrl() {
		return this.scheduleUrl;
	}
	public boolean isOpen() {
		return getHoraire().isOpen();
	}
	public static ArrayList<Bibliotheque> getBibliothequesList(){
		if (bibliotheques != null) {
			return bibliotheques;
		}
		bibliotheques = new ArrayList<Bibliotheque>();
		Cursor c = LLNCampus.getDatabase().sqlRawQuery(
				"SELECT " +
						"Poi.ID, " +
						"Poi.NAME, " +
						"Poi.LATITUDE, " +
						"Poi.LONGITUDE, " +
						"Poi.ADDRESS, " +
						"Bibliotheque.SIGLE, " +
						"Bibliotheque.URL_SCHEDULE" + 
				" FROM " +
					"Poi, Bibliotheque " +
				" WHERE " +
					"Poi.ID = Bibliotheque.BUILDING_ID " +
				"ORDER BY " +
					"Poi.NAME ASC");
		Bibliotheque bib;
		while(c.moveToNext()){
			bib = new Bibliotheque(c.getInt(0), 
					c.getString(1), 
					c.getLong(2), c.getLong(3), 
					c.getString(4), 
					new BibliothequeHoraire(c.getInt(0)), 
					c.getString(5), c.getString(6));
			bibliotheques.add(bib);
		}
		c.close();
		return bibliotheques;
	}

}
