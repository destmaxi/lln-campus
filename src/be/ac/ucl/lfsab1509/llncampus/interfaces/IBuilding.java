package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier

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
 * 
 * Interface of an IBuilding
 */
public interface IBuilding {
	/**
	 * Get the Id of an IBuilding object
	 */
	public int getID();
	
	/**
	 * Return the name of an IBuilding object
	 * @return this.name
	 */
	public String getName();
	
	/**
	 * Set the name of an IBuilding object to newName
	 */
	public void setName(String newName);
	
	/**
	 * Get the latitude of an IBuilding object
	 */
	public double getLatitude();
	
	/**
	 * Set the latitude of an IBuilding object to newLatitude
	 */
	public void setLatitude(double newLatitude);
	
	/**
	 * Get the latitude of an IBuilding object
	 * @return this.longitude
	 */
	public double getLongitude();
	
	/**
	 * Set the longitude of an IBuilding object to newLongitude
	 */
	public void setLongitude(double newLongitude);
	
	/**
	 * Get the address of an IBuilding object
	 * @return this.adress
	 */
	public String getAddress();
	
	/**
	 * Set the address of an IBuilding object to newAdress
	 */
	public void setAddress(String newAddress);
	
	/**
	  * Return the path integer of the building picture
	  * 
	  */
	public int takePicture();

}
