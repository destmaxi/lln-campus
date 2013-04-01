package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

/**
 * Objet représentant une bibliothèque.
 */
public final class Bibliotheque {
	private int id;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private BibliothequeHoraire horaire;
	private String sigle;
	private String scheduleUrl;
	private static ArrayList<Bibliotheque> bibliotheques = null;

	/**
	 * Constructeur (privé).
	 * 
	 * @param id
	 *            ID de la bibliotheque
	 * @param name
	 *            Nom de la bibilotheque
	 * @param latitude
	 *            Latitude
	 * @param longitude
	 *            Longitutude
	 * @param address
	 *            Adresse de la bibliotheque
	 * @param horaire
	 *            Horaires d'ouvertures
	 * @param sigle
	 *            Sigle de la bibliothèque
	 * @param scheduleUrl
	 *            Url vers les horaires détaillés (blocus, vacances,...)
	 */
	private Bibliotheque(int id, String name, double latitude,
			double longitude, String address, BibliothequeHoraire horaire,
			String sigle, String scheduleUrl) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.horaire = horaire;
		this.sigle = sigle;
		this.scheduleUrl = scheduleUrl;
	}

	/**
	 * Fournit l'ID de la bibliothèque.
	 * 
	 * @return ID de la bibliothèque.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Fournit le nom de la bibliothèque.
	 * 
	 * @return Nom de la bibliothèque
	 */
	public String getName() {
		return name;
	}

	/**
	 * Fournit la latitude de la bibliothèque.
	 * 
	 * @return Latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Fournit la longitude de la bibliothèque.
	 * 
	 * @return Longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Fournit l'adresse de la bibliothèque.
	 * 
	 * @return Adresse
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Fournit l'image de la bibliothèque.
	 * 
	 * @return la ressource ID de l'image
	 */
	public int getImg() {
		switch (this.id) {
		case 28:
			return R.drawable.bst;
		case 29:
			return R.drawable.bdrt;
		case 30:
			return R.drawable.bpsp;
		case 31:
			return R.drawable.pasencore;
		case 32:
			return R.drawable.pasencore;
		case 33:
			return R.drawable.bisp;
		case 34:
			return R.drawable.bsm;
		case 35:
			return R.drawable.bspo;
		case 36:
			return R.drawable.pasencore;
		default:
			Log.e("Library.java",
					"Ne trouve pas l'image vers l'auditoire de Takepicture");
			return 0;
		}
	}

	/**
	 * Fournit l'horaire d'ouverture de la bibliothèque.
	 * 
	 * @return Horaire d'ouverture
	 */
	public BibliothequeHoraire getHoraire() {
		return this.horaire;
	}

	/**
	 * Fournit le sigle de la bibliothèque.
	 * 
	 * @return Sigle
	 */
	public String getSigle() {
		return this.sigle;
	}

	/**
	 * Fournit l'URL de la page contenant les infos sur l'horaire.
	 * 
	 * @return URL
	 */
	public String getScheduleUrl() {
		return this.scheduleUrl;
	}

	/**
	 * Indique si la bibliothèque est actuellement ouverte ou fermé.
	 * 
	 * @return true si la bibliothèque est ouverte, false sinon.
	 */
	public boolean isOpen() {
		return getHoraire().isOpen();
	}

	/**
	 * Retourne la liste de toutes les biblièthèques.
	 * 
	 * @return Liste des bibliothèques.
	 */
	public static ArrayList<Bibliotheque> getBibliothequesList() {
		if (bibliotheques != null) {
			return bibliotheques;
		}
		bibliotheques = new ArrayList<Bibliotheque>();
		Cursor c = LLNCampus.getDatabase().sqlRawQuery(
				"SELECT " + "Poi.ID, " + "Poi.NAME, " + "Poi.LATITUDE, "
						+ "Poi.LONGITUDE, " + "Poi.ADDRESS, "
						+ "Bibliotheque.SIGLE, " + "Bibliotheque.URL_SCHEDULE"
						+ " FROM " + "Poi, Bibliotheque " + " WHERE "
						+ "Poi.ID = Bibliotheque.BUILDING_ID " + "ORDER BY "
						+ "Poi.NAME ASC");
		Bibliotheque bib;
		while (c.moveToNext()) {
			bib = new Bibliotheque(c.getInt(0), c.getString(1), c.getLong(2),
					c.getLong(3), c.getString(4), new BibliothequeHoraire(
							c.getInt(0)), c.getString(5), c.getString(6));
			bibliotheques.add(bib);
		}
		c.close();
		return bibliotheques;
	}

}
