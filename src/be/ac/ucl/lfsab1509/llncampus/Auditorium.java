package be.ac.ucl.lfsab1509.llncampus;

import android.util.Log;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;


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
 * Implementation of an Auditorium object.
 */
public class Auditorium implements IAuditorium {

	private int ID;
	private String name;
	private double latitude;
	private double longitude;
	private String address;

	/**
	 * Default constructor.
	 */
	public Auditorium() {
		name = null;
		latitude = 0.0;
		longitude = 0.0;
		address = null;
	}

	/**
	 * Constructor. 
	 * 
	 * @param id 
	 * 		Auditorium ID. 
	 * @param name 
	 * 		Auditorium name.
	 * @param latitude 
	 * 		Auditorium latitude.
	 * @param longitude 
	 * 		Auditorium longitude.
	 * @param address 
	 * 		Auditorium address.
	 */
	public Auditorium(final int id, final String name, final double latitude, 
			final double longitude, final String address) {
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
	public final void setName(final String newName) {
		name = newName;
	}

	@Override
	public final double getLatitude() {
		return latitude;
	}

	@Override
	public final void setLatitude(final double newLatitude) {
		latitude = newLatitude;
	}

	@Override
	public final double getLongitude() {
		return longitude;
	}

	@Override
	public final void setLongitude(final double newLongitude) {
		longitude = newLongitude;
	}

	@Override
	public final String getAddress() {
		return address;
	}

	@Override
	public final void setAddress(final String newAddress) {
		address = newAddress;
	}

	@Override
	public final int getPicture() {
		switch (this.ID) {
			case 3 : return R.drawable.agora;
			case 7 : return R.drawable.coubertin;
			case 2 : return R.drawable.croixdusud;
			case 10 : return R.drawable.cyclotron;
			case 8 : return R.drawable.descamps;
			case 9 : return R.drawable.doyens;
			case 17 : return R.drawable.dupriez;
			case 18 : return R.drawable.erasme;
			case 11 : return R.drawable.lavoisier;
			case 19 : return R.drawable.leclercq;
			case 13 : return R.drawable.mariecurie;
			case 12 : return R.drawable.mercator;
			case 6 : return R.drawable.montesquieu;
			case 16 : return R.drawable.pierrecurie;
			case 1 : return R.drawable.saintebarbe;
			case 15 : return R.drawable.sciences;
			case 21 : return R.drawable.socrate; 
			case 37 : return R.drawable.studioagora;
			case 20 : return R.drawable.thomasmore;
			case 14 : return R.drawable.vanhelmont; 
			default :
				Log.e("Auditorium.java", "Didn't found any image for the Auditorium");
				return 0;
		}
	 }

	@Override
	public int getMiniPicture() {
		switch (this.ID) {
		case 3 : return R.drawable.agora_mini;
		case 7 : return R.drawable.coubertin_mini;
		case 2 : return R.drawable.croixdusud_mini;
		case 10 : return R.drawable.cyclotron_mini;
		case 8 : return R.drawable.descamps_mini;
		case 9 : return R.drawable.doyens_mini;
		case 17 : return R.drawable.dupriez_mini;
		case 18 : return R.drawable.erasme_mini;
		case 11 : return R.drawable.lavoisier_mini;
		case 19 : return R.drawable.leclercq_mini;
		case 13 : return R.drawable.mariecurie_mini;
		case 12 : return R.drawable.mercator_mini;
		case 6 : return R.drawable.montesquieu_mini;
		case 16 : return R.drawable.pierrecurie_mini;
		case 1 : return R.drawable.saintebarbe_mini;
		case 15 : return R.drawable.sciences_mini;
		case 21 : return R.drawable.socrate_mini; 
		case 37 : return R.drawable.studioagora_mini;
		case 20 : return R.drawable.thomasmore_mini;
		case 14 : return R.drawable.vanhelmont_mini; 
		default :
			Log.e("Auditorium.java", "Didn't found any mini image for the Auditorium");
			return 0;
		}
	}

}
