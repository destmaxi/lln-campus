package be.ac.ucl.lfsab1509.llncampus;

import android.util.Log;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;

/**
 * Implementation of an Auditorium object
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
	
	@Override
	public int takePicture(){
		  
		if(this.ID == 3) {return R.drawable.agora;}
		else if(this.ID == 7) {return R.drawable.coubertin;}
		else if(this.ID == 2) {return R.drawable.croixdusud;}
		else if(this.ID == 10) {return R.drawable.cyclotron;}
		else if(this.ID == 8) {return R.drawable.descamps;}
		else if(this.ID == 9) {return R.drawable.doyens;}
		else if(this.ID == 17) {return R.drawable.dupriez;}
		else if(this.ID == 18) {return R.drawable.erasme;}
		else if(this.ID == 11) {return R.drawable.lavoisier;}
		else if(this.ID == 19) {return R.drawable.leclercq;}
		else if(this.ID == 13) {return R.drawable.mariecurie;}
		else if(this.ID == 12) {return R.drawable.mercator;}
		else if(this.ID == 6) {return R.drawable.montesquieu;}
		else if(this.ID == 16) {return R.drawable.pierrecurie;}
		else if(this.ID == 1) {return R.drawable.saintebarbe;}
		else if(this.ID == 15) {return R.drawable.sciences;}
		else if(this.ID == 21) {return R.drawable.socrate;}
		else if(this.ID == 37) {return R.drawable.studioagora;}
		else if(this.ID == 20) {return R.drawable.thomasmore;}
		else if(this.ID == 14) {return R.drawable.vanhelmont;}
		else {
			Log.e("Auditorium.java", "Ne trouve pas l'image vers l auditoire de Takepicture");
			return 0;
		}
		
	 }
	 

}
