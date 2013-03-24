package be.ac.ucl.lfsab1509.llncampus;

import android.util.Log;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ILibrary;

/**
 * Implementation of an Library object
 * 
 */
public class Library implements ILibrary {
	private int ID;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private String schedule;
	private String sigle;
	private String scheduleUrl;
	
	/**
	 * Constructeur.
	 */
	public Library() {
		name = null;
		latitude = 0.0;
		longitude = 0.0;
		address = null;
		schedule = null;
		sigle =  null;
		scheduleUrl= null;
	}
	
	/**
	 * 
	 * @param id ID de la bibliotheque
	 * @param name Nom
	 * @param latitude Latitude
	 * @param longitude Longitutude
	 * @param address
	 * @param schedule : horaire
	 * @param sigle
	 * @param scheduleUrl : lien url vers les horaires spéciaux
	 */
	public Library(int id, String name, double latitude, double longitude, String address, String schedule, 
				String sigle, String scheduleUrl){
		this.ID = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.schedule = schedule;
		this.sigle =  sigle;
		this.scheduleUrl= scheduleUrl;
	}

	@Override
	public final int getID() {
		return this.ID;
	}
	
	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final void setName(String newName) {
		name = newName;
	}

	@Override
	public final double getLatitude() {
		return latitude;
	}

	@Override
	public final void setLatitude(double newLatitude) {
		latitude = newLatitude;
	}

	@Override
	public final double getLongitude() {
		return longitude;
	}

	@Override
	public final void setLongitude(double newLongitude) {
		longitude = newLongitude;
	}

	@Override
	public final String getAddress() {
		return address;
	}

	@Override
	public final void setAddress(String newAddress) {
		address = newAddress;
	}
	
	@Override
	public final int takePicture() {
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

	@Override
	public String getSchedule() {
		return this.schedule;
	}

	@Override
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	@Override
	public String getSigle() {
		return this.sigle;
	}

	@Override
	public void setSigle(String sigle) {
		this.sigle = sigle;
	}

	@Override
	public String getScheduleUrl() {
		return this.scheduleUrl;
	}

	@Override
	public void setScheduleUrl(String scheduleUrl) {
		this.scheduleUrl = scheduleUrl;
	}
	
	
	
	

}
