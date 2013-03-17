package be.ac.ucl.lfsab1509.llncampus;

import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;

public class SubAuditorium implements ISubAuditorium {

	private IAuditorium parent;
	private int id;
	private String name;
	private int nbplaces;
	private String mobilier;
	private boolean cabine;
	private boolean ecran;
	private boolean sono;
	private boolean retro;
	private boolean dia;
	private String video;
	private boolean network;
	private boolean access;
	
	// Empty constructor
	public SubAuditorium() {
		parent = null;
		id = 0;
		name = null;
		nbplaces = 0;
		mobilier = null;
		cabine = false;
		ecran = false;
		retro = false;
		sono = false;
		dia = false;
		video = null;
		network = false;
		access = false;
	}
	
	/**
	 * Constructor for a SubAuditorium. This one is intended to take directly data from the DB.
	 * @param parent
	 * @param id
	 * @param name
	 * @param nbplaces
	 * @param mobilier
	 * @param cabine
	 * @param ecran
	 * @param sono
	 * @param retro
	 * @param dia
	 * @param video
	 * @param network
	 * @param access
	 */
	public SubAuditorium(IAuditorium parent, int id, String name, int nbplaces, String mobilier, String cabine, String ecran, String sono, String retro, String dia, String video, String network, boolean access) {
		setIAuditorium(parent);
		setID(id);
		setName(name);
		setNbPlaces(nbplaces);
		setCabine(cabine);
		setEcran(ecran);
		setSono(sono);
		setRetro(retro);
		setDia(dia);
		setVideo(video);
		setNetwork(network);
		setAccess(access);
	}
	
	@Override
	public IAuditorium getIAuditorium() {
		return parent;
	}

	@Override
	public void setIAuditorium(IAuditorium aud) {
		parent = aud;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public int getNbPlaces() {
		return nbplaces;
	}

	@Override
	public void setNbPlaces(int nbPlaces) {
		nbplaces = nbPlaces;
	}

	@Override
	public String getMobilier() {
		return mobilier;
	}

	@Override
	public void setMobilier(String mob) {
		mobilier = mob;
	}

	@Override
	public boolean hasCabine() {
		return cabine;
	}

	@Override
	public void setCabine(String cab) {
		cabine = (cab != null && cab.compareTo("C") == 0);
	}

	@Override
	public boolean hasEcran() {
		return ecran;
	}

	@Override
	public void setEcran(String ecr) {
		ecran = (ecr != null && ecr.compareTo("E") == 0);
	}

	@Override
	public boolean hasSono() {
		return sono;
	}

	@Override
	public void setSono(String sono) {
		this.sono = (sono != null && sono.compareTo("S") == 0);
	}

	@Override
	public boolean hasRetro() {
		return retro;
	}

	@Override
	public void setRetro(String ret) {
		retro = (ret != null && ret.compareTo("R") == 0);
	}

	@Override
	public boolean hasDia() {
		return dia;
	}

	@Override
	public void setDia(String dia) {
		this.dia = (dia != null && dia.compareTo("D") == 0);
	}

	@Override
	public String getVideo() {
		return video;
	}

	@Override
	public void setVideo(String vid) {
		video = vid;
	}

	@Override
	public boolean hasNetwork() {
		return network;
	}

	@Override
	public void setNetwork(String net) {
		network = (net != null);
	}

	@Override
	public boolean hasAccess() {
		return access;
	}

	@Override
	public void setAccess(boolean acc) {
		access = acc;
	}

}
