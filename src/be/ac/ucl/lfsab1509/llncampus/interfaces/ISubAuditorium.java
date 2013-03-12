package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * Interface representing a SubAuditorium
 *
 */
public interface ISubAuditorium extends IBuilding{
	
	/**
	 * Return the IAuditorium to which the current ISubAuditorium belongs
	 * @return IAuditorium to which the current ISubAuditorium belongs
	 */
	public IAuditorium getIAuditorium();
	
	/**
	 * Set the IAuditorium to which the current ISubAuditorium belongs to aud
	 * @param aud
	 */
	public void setIAuditorium(IAuditorium aud);
	
	/**
	 * Return the ID of the ISubAuditorium in the DB
	 * @return ID of the ISubAuditorium in the DB
	 */
	public int getID();
	
	/**
	 * Set the ID of the ISubAuditorium in the DB to id
	 * @param id
	 */
	public void setID(int id);
	
	/**
	 * Get the name of the ISubAuditorium
	 * @return name of the ISubAuditorium
	 */
	public String getName();
	
	/**
	 * Set the name of the ISubAuditorium to newName
	 * @param newName
	 */
	public void setName(String newName);
	
	/**
	 * Get the number of places in a ISubAuditorium
	 * @return number of places in a ISubAuditorium
	 */
	public int getNbPlaces();
	
	/**
	 * Set the number of places in a ISubAuditorium to nbPlaces
	 * @param nbPlaces
	 */
	public void setNbPlaces(int nbPlaces);
	
	/**
	 * Get some information about furnitures
	 * The result can be the following:
	 * - T : the ISubAuditorium has a mobile table
	 * - Tf : the ISubAuditorium has a fixed table
	 * - G : the ISubAuditorium has a bench (gradin)
	 * - null : the ISubAuditorium has nothing special.
	 * 
	 * 
	 * If it's different, then it's considered as null
	 * @return information about furnitures
	 */
	public String getMobilier();
	
	/**
	 * Set some information about furnitures to mob
	 * mob can be the following:
	 * - T : the ISubAuditorium has a mobile table
	 * - Tf : the ISubAuditorium has a fixed table
	 * - G : the ISubAuditorium has a bench (gradin)
	 * - null : the ISubAuditorium has nothing special.
	 * 
	 * 
	 * If it's different, then it's considered as null
	 * @param mob
	 */
	public void setMobilier(String mob);
	
	
}
