package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * Interface of an Auditorium object
 * @author Quentin
 * @version 20/02/2013
 */
public interface IAuditorium {
	
	/**
	 * Get the Id of an IAuditorium object
	 */
	public int getID();
	
	
	/**
	 * Return the name of an IAuditorium object
	 * @return this.name
	 */
	public String getName();
	
	/**
	 * Set the name of an IAuditorium object to newName
	 */
	public void setName(String newName);
	
	/**
	 * Get the latitude of an IAuditorium object
	 */
	public double getLatitude();
	
	/**
	 * Set the latitude of an IAuditorium object to newLatitude
	 */
	public void setLatitude(double newLatitude);
	
	/**
	 * Get the latitude of an IAuditorium object
	 * @return this.longitude
	 */
	public double getLongitude();
	
	/**
	 * Set the longitude of an IAuditorium object to newLongitude
	 */
	public void setLongitude(double newLongitude);
	
	/**
	 * Get the address of an IAuditorium object
	 * @return this.adress
	 */
	public String getAddress();
	
	/**
	 * Set the address of an IAuditorium object to newAdress
	 */
	public void setAddress(String newAddress);

	/**
	  * Return the path integer of the auditorium picture
	  * 
	  */
	public int takePicture();
	
	//TODO probably other methods will be integrated, such as sub-auditoriums,...
}
