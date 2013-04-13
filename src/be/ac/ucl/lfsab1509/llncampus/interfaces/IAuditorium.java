package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * Interface of an Auditorium object extends IBuilding
 */
public interface IAuditorium extends IBuilding{
	
	/**
	 * Fournit l'image miniature de l auditoire.
	 * @return la ressource ID de l'image miniature
	 */
	public int getImgMini();
	
	
}
