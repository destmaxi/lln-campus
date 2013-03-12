package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
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

}
