package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * Interface of an Library object
 *
 */
public interface ILibrary extends IBuilding{
	
	/**
	 * classe qui servira � par exemple les heures d'ouverture des biblioth�ques
	 * ou des infos sp�cifiques aux biblioth�ques
	 */
	
	/**
	 * 
	 * @return l horaire d ouverture de la bibliotheque
	 */
	public String getSchedule();
	
	/**
	 * 
	 * @param schedule  nouvel horaire � modifier
	 */
	public void setSchedule(String schedule);

}
