package be.ac.ucl.lfsab1509.llncampus;

import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;

/**
 * Implementation of an Auditorium object
 * @author Quentin & Anh Tuan
 *
 */
public class Auditorium implements IAuditorium {
	
	private int ID;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	
	/**
	 * Default constructor
	 */
	public Auditorium(){
		name=null;
		latitude=0.0;
		longitude=0.0;
		address=null;
	}
	
	/**
	 * Constructor with arguments
	 */
	public Auditorium(int id, String name, double latitude, double longitude, String address){
		this.ID=id;
		this.name=name;
		this.latitude=latitude;
		this.longitude=longitude;
		this.address=address;
	}
	
	@Override
	public int getID(){
		return this.ID;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) {
		name=newName;
	}

	@Override
	public double getLatitude() {
		return latitude;
	}

	@Override
	public void setLatitude(double newLatitude) {
		latitude=newLatitude;
	}

	@Override
	public double getLongitude() {
		return longitude;
	}

	@Override
	public void setLongitude(double newLongitude) {
		longitude=newLongitude;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String newAddress) {
		address=newAddress;
	}

}
