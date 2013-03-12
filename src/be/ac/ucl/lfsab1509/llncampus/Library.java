package be.ac.ucl.lfsab1509.llncampus;

import be.ac.ucl.lfsab1509.llncampus.interfaces.ILibrary;

/**
 * Implementation of an Library object
 * 
 * A am�liorer en integrant les horaires des bibilotheques
 */
public class Library implements ILibrary {
	private int ID;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	
	/**
	 * Constructeur.
	 */
	public Library() {
		name = null;
		latitude = 0.0;
		longitude = 0.0;
		address = null;
	}
	
	/**
	 * 
	 * @param id ID de la bibliothèque
	 * @param name Nom
	 * @param latitude Latitude
	 * @param longitude Longitutude
	 * @param address
	 */
	public Library(int id, String name, double latitude, double longitude, String address){
		this.ID = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
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
	
	

}
