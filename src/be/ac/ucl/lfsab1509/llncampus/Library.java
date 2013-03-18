package be.ac.ucl.lfsab1509.llncampus;

import android.util.Log;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ILibrary;

/**
 * Implementation of an Library object
 * 
 * A ameliorer en integrant les horaires des bibilotheques
 */
public class Library implements ILibrary {
	private int ID;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private String schedule;
	
	/**
	 * Constructeur.
	 */
	public Library() {
		name = null;
		latitude = 0.0;
		longitude = 0.0;
		address = null;
		schedule = null;
	}
	
	/**
	 * 
	 * @param id ID de la bibliotheque
	 * @param name Nom
	 * @param latitude Latitude
	 * @param longitude Longitutude
	 * @param address
	 */
	public Library(int id, String name, double latitude, double longitude, String address, String schedule){
		this.ID = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.schedule = schedule;
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
	
	
	
	

}
