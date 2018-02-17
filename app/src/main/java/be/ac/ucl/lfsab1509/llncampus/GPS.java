package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier
    Copyright (C) 2014 Quentin De Coninck

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Class intended to manage the GPS of a device.
 */
public class GPS implements LocationListener {
	LocationManager locationManager;
	LocationProvider locationProvider;
	Location lastLocation;
	ArrayList<String> providers; //List of enabled providers.
	
	/**
	 * Default constructor.
	 */
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
	
	/**
	 * Get the current position of the device.
	 * 
	 * @return Current position of the device.
	 */
	public Coordinates getPosition(){
		if(lastLocation == null){
			lastLocation = locationManager.getLastKnownLocation(providers.get(0));
		}
		if(lastLocation == null){ 
			return null;
		}
		return new Coordinates(lastLocation.getLatitude(),lastLocation.getLongitude());
	}
	
	/**
	 * Check if the GPS is on.
	 * 
	 * @return true if the position of the device is determined, else false.
	 */
	public boolean isOn(){
		if(providers.isEmpty()){ 
			return false; 
		}
		return (getPosition() != null);
	}
	
	/**
	 * Destroy the GPS object.
	 */
	public void destroy(){
		locationManager.removeUpdates(this);
		locationManager = null;
	}
	
	// These following methods are specified by LocationListener
	
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
			default:
				// Nothing to do
				break;
		}		
	}
}