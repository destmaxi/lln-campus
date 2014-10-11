package be.ac.ucl.lfsab1509.llncampus;

import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier
	Copyright (C) 2014 Quentin De Coninck

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
 */

/**
 * Represents a sub-auditorium (a room in a set of auditoriums).
 */
public class SubAuditorium implements ISubAuditorium {

	private int parentId;
	private int id;
	private String name;
	private int places;
	private String furniture;
	private boolean cabin;
	private boolean screen;
	private boolean sound;
	private boolean retro;
	private boolean slide;
	private String video;
	private boolean network;
	private boolean access;
	
	// Empty constructor
	public SubAuditorium() {
		parentId = 0;
		id = 0;
		name = null;
		places = 0;
		furniture = null;
		cabin = false;
		screen = false;
		retro = false;
		sound = false;
		slide = false;
		video = null;
		network = false;
		access = false;
	}
	
	/**
	 * Constructor for a SubAuditorium. This one is intended to take directly data from the DB.
	 * 
	 * @param parentId
	 * 			ID of the Auditorium.
	 * @param id
	 * 			ID of the SubAudotorium.
	 * @param name
	 * 			Name of the SubAuditorium.
	 * @param nbplaces
	 * 			Number of places in the SubAuditorium.
	 * @param furniture
	 * 			Furniture of the SubAuditorium.
	 * @param cabin
	 * 			If the SubAuditorium has a cabin.
	 * @param screen
	 * 			If the SubAuditorium has a screen
	 * @param sound
	 * 			If the SubAuditorium has sound devices.
	 * @param retro
	 * 			If the SubAuditorium has a projector.
	 * @param slide
	 * 			If the SubAuditorium can project slides.
	 * @param video
	 * 			If the SubAuditorium has a video projector.
	 * @param network
	 * 			If the SubAuditorium provides WiFi access.
	 * @param access
	 * 			If the SubAuditorium is accessible to disabled persons.
	 */
	public SubAuditorium(int parentId, int id, String name, int nbplaces, String furniture,
			String cabin, String screen, String sound, String retro, String slide,
			String video, String network, String access) {
		setParentId(parentId);
		setId(id);
		setName(name);
		setPlaces(nbplaces);
		setFurniture(furniture);
		setCabin(cabin);
		setScreen(screen);
		setSound(sound);
		setRetro(retro);
		setSlide(slide);
		setVideo(video);
		setNetwork(network);
		setAccess(access);
	}
	
	/**
	 * Constructor for a SubAuditorium (in its direct form).
	 * @param parentId
	 * 			ID of the Auditorium.
	 * @param id
	 * 			ID of the SubAudotorium.
	 * @param name
	 * 			Name of the SubAuditorium.
	 * @param nbplaces
	 * 			Number of places in the SubAuditorium.
	 * @param furniture
	 * 			Furniture of the SubAuditorium.
	 * @param cabin
	 * 			If the SubAuditorium has a cabin.
	 * @param screen
	 * 			If the SubAuditorium has a screen
	 * @param sound
	 * 			If the SubAuditorium has sound devices.
	 * @param retro
	 * 			If the SubAuditorium has a projector.
	 * @param slide
	 * 			If the SubAuditorium can project slides.
	 * @param video
	 * 			If the SubAuditorium has a video projector.
	 * @param network
	 * 			If the SubAuditorium provides WiFi access.
	 * @param access
	 * 			If the SubAuditorium is accessible to disabled persons.
	 */
	public SubAuditorium(int parentId, int id, String name, int places, String furniture,
			boolean cabin, boolean screen, boolean sound, boolean retro, boolean slide,
			String video, boolean network, boolean access) {
		this.parentId = parentId;
		this.id = id;
		this.name = name;
		this.places = places;
		this.furniture = furniture;
		this.cabin = cabin;
		this.screen = screen;
		this.sound = sound;
		this.retro = retro;
		this.slide = slide;
		this.video = video;
		this.network = network;
		this.access = access;
	}

	@Override
	public int getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(int id_parent) {
		this.parentId = id_parent;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
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
	public int getPlaces() {
		return places;
	}

	@Override
	public void setPlaces(int nbPlaces) {
		places = nbPlaces;
	}

	@Override
	public String getFurniture() {
		return furniture;
	}

	@Override
	public void setFurniture(String mob) {
		furniture = mob;
	}

	@Override
	public boolean hasCabin() {
		return cabin;
	}

	@Override
	public void setCabin(String cab) {
		cabin = (cab != null && cab.compareTo("C") == 0);
	}

	@Override
	public boolean hasScreen() {
		return screen;
	}

	@Override
	public void setScreen(String ecr) {
		screen = (ecr != null && ecr.compareTo("E") == 0);
	}

	@Override
	public boolean hasSound() {
		return sound;
	}

	@Override
	public void setSound(String sono) {
		this.sound = (sono != null && sono.compareTo("S") == 0);
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
	public boolean hasSlide() {
		return slide;
	}

	@Override
	public void setSlide(String dia) {
		this.slide = (dia != null && dia.compareTo("D") == 0);
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
	public void setAccess(String acc) {
		access = (acc.compareToIgnoreCase("TRUE") == 0);
	}

}
