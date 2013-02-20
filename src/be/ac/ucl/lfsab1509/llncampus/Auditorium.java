package be.ac.ucl.lfsab1509.llncampus;

import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;

/**
 * Implementation of an Auditorium object
 * @author Quentin
 *
 */
public class Auditorium implements IAuditorium {

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
	public Auditorium(String name, double latitude, double longitude, String address){
		this.name=name;
		this.latitude=latitude;
		this.longitude=longitude;
		this.address=address;
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
