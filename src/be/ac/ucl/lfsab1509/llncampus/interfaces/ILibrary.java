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
	
	/**
	 * 
	 * @return le sigle de la bibliothèque
	 */
	public String getSigle();
	
	/**
	 * 
	 * @param sigle
	 */
	public void setSigle (String sigle);
	
	/**
	 * 
	 * @return l url des horaires spéciaux de la bibliothèque
	 */
	public String getScheduleUrl();
	
	/**
	 * 
	 * @param scheduleUrl
	 */
	public void setScheduleUrl(String scheduleUrl);
	

}
