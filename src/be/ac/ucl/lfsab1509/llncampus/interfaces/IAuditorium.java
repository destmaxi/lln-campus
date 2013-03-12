package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * Interface of an Auditorium object extends IBuilding
 */
public interface IAuditorium extends IBuilding{
	
	/**
	  * Return the path integer of the auditorium picture
	  * 
	  */
	public int takePicture();
	
	//TODO probably other methods will be integrated, such as sub-auditoriums,...
}
