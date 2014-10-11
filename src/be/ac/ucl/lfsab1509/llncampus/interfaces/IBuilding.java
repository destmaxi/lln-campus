package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier
    Cpyright (C) 2014 Quentin De Coninck

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
 * Interface of an Building where courses are given.
 */
public interface IBuilding {
	/**
	 * Get the ID of the IBuilding object.
	 * 
	 * @return ID of the IBuilding.
	 */
	public int getID();
	
	/**
	 * Return the name of the IBuilding object.
	 * 
	 * @return Name of the IBuilding object.
	 */
	public String getName();
	
	/**
	 * Set the name of the IBuilding object to newName.
	 * 
	 * @param newName
	 * 			The name to set to the IBuilding.
	 */
	public void setName(String newName);
	
	/**
	 * Get the latitude of the IBuilding object.
	 * @return Latitude of the IBuilding.
	 */
	public double getLatitude();
	
	/**
	 * Set the latitude of the IBuilding object to newLatitude.
	 * 
	 * @param newLatitude
	 * 			The latitude to set to the IBuilding.
	 */
	public void setLatitude(double newLatitude);
	
	/**
	 * Get the longitude of the IBuilding object.
	 * 
	 * @return Longitude of the IBuilding.
	 */
	public double getLongitude();
	
	/**
	 * Set the longitude of an IBuilding object to newLongitude.
	 * 
	 * @param newLongitude
	 * 			The longitude to set to the IBuilding.
	 */
	public void setLongitude(double newLongitude);
	
	/**
	 * Get the address of the IBuilding object.
	 * 
	 * @return Address of the IBuilding.
	 */
	public String getAddress();
	
	/**
	 * Set the address of an IBuilding object to newAdress.
	 * 
	 * @param newAddress
	 * 			The address to set to the IBuilding.
	 */
	public void setAddress(String newAddress);
	
	/**
	  * Return the ID of the IBuilding picture.
	  * 
	  * @return ID of the IBuilding picture.
	  */
	public int getPicture();

}
