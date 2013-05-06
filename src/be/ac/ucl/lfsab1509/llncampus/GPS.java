package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;


public class GPS implements LocationListener {
	LocationManager locationManager;
	LocationProvider locationProvider;
	Location lastLocation;
	ArrayList<String> providers; //List of enabled providers.
	
	public GPS(){
		Context context = LLNCampus.getContext();
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		providers = new ArrayList<String>();
		providers.addAll(locationManager.getProviders(false));
		Iterator<String> it = providers.iterator();
		while(it.hasNext()){
			locationManager.requestLocationUpdates(it.next(), 0, 0, this);
		}
	}
	public Coordinates getPosition(){
		if(lastLocation == null){
			lastLocation = locationManager.getLastKnownLocation(providers.get(0));
		}
		if(lastLocation == null){ return null;}
		return new Coordinates(lastLocation.getLatitude(),lastLocation.getLongitude());
	}
	
	public boolean isOn(){
		if(providers.isEmpty()){ return false; }
		return (getPosition()!=null);
	}
	public void onLocationChanged(Location location) {
		lastLocation = location;
	}
	public void onProviderDisabled(String provider){
		providers.remove(provider);
	}
	public void onProviderEnabled(String provider) {
		if(!providers.contains(provider)){
			providers.add(provider);
		}
	}
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch(status){
			case LocationProvider.OUT_OF_SERVICE:
				onProviderDisabled(provider);
				break;
			case LocationProvider.AVAILABLE:
				onProviderEnabled(provider);
				break;
//			case LocationProvider.TEMPORARILY_UNAVAILABLE:
//				Rien a faire ?
//				break;
		}		
	}
	public void destroy(){
		locationManager.removeUpdates(this);
		locationManager = null;
	}
}