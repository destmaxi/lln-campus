package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * Interface of an Library object
 *
 */
public interface ILibrary extends IBuilding{
	
	/**
	 * classe qui servira à par exemple les heures d'ouverture des bibliothèques
	 * ou des infos spécifiques aux bibliothèques
	 */
	
	/**
	 * 
	 * @return l horaire d ouverture de la bibliotheque
	 */
	public String getSchedule();
	
	/**
	 * 
	 * @param schedule  nouvel horaire à modifier
	 */
	public void setSchedule(String schedule);

}
