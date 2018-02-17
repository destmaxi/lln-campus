package be.ac.ucl.lfsab1509.llncampus.interfaces;

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
 * Interface representing a SubAuditorium (actual room where courses are given).
 */
public interface ISubAuditorium {
	
	/**
	 * Return the Id of the IAuditorium to which the current ISubAuditorium belongs.
	 * 
	 * @return Id of the IAuditorium to which the current ISubAuditorium belongs.
	 */
	public int getParentId();
	
	/**
	 * Set the Id of the IAuditorium to which the current ISubAuditorium belongs to parentId.
	 * 
	 * @param parentId
	 * 			Id of the parent IAuditorium.
	 */
	public void setParentId(int parentId);
	
	/**
	 * Return the ID of the ISubAuditorium in the database.
	 * 
	 * @return ID of the ISubAuditorium in the DB.
	 */
	public int getId();
	
	/**
	 * Set the ID of the ISubAuditorium in the database to id.
	 * 
	 * @param id
	 * 			Id of the ISubAuditorium in the database.
	 */
	public void setId(int id);
	
	/**
	 * Get the name of the ISubAuditorium.
	 * 
	 * @return Name of the ISubAuditorium.
	 */
	public String getName();
	
	/**
	 * Set the name of the ISubAuditorium to newName.
	 * 
	 * @param newName
	 * 			Name of the ISubAuditorium.
	 */
	public void setName(String newName);
	
	/**
	 * Get the number of places in a ISubAuditorium.
	 * 
	 * @return Number of places in a ISubAuditorium.
	 */
	public int getPlaces();
	
	/**
	 * Set the number of places in a ISubAuditorium to nbPlaces.
	 * 
	 * @param nbPlaces
	 * 			Number of places in a ISubAuditorium.
	 */
	public void setPlaces(int nbPlaces);
	
	/**
	 * Get some information about furniture of the ISubAuditorium.
	 * 
	 * The result can be the following:
	 * <ul>
	 * <li> T : the ISubAuditorium has a mobile table. </li>
	 * <li> Tf : the ISubAuditorium has a fixed table. </li>
	 * <li> G : the ISubAuditorium has a bench. </li>
	 * <li> null : the ISubAuditorium has nothing special. </li>
	 * </ul>
	 * 
	 * 
	 * If it's different, then it's considered as nothing special.
	 * 
	 * @return String describing information about furniture of the ISubAuditorium.
	 */
	public String getFurniture();
	
	/**
	 * Set some information about furniture to furniture.
	 * 
	 * furniture can be the following:
	 * <ul>
	 * <li> T : the ISubAuditorium has a mobile table. </li>
	 * <li> Tf : the ISubAuditorium has a fixed table. </li>
	 * <li> G : the ISubAuditorium has a bench. </li>
	 * <li> null : the ISubAuditorium has nothing special. </li>
	 * </ul>
	 * 
	 * If it's different, then it's considered as nothing special.
	 * 
	 * @param furniture
	 * 			Code describing the furniture of the ISubAuditorium.
	 */
	public void setFurniture(String furniture);
	
	/**
	 * Return true iff the current object has a cabin.
	 * 
	 * @return true iff the current object has a cabin, false otherwise
	 */
	public boolean hasCabin();
	
	/**
	 * Set if the current object has a cabin. If cab == "C", then it sets the instance
	 * variable to true. Otherwise (including null), it sets to false.
	 * 
	 * @param cab
	 * 			Code describing the cabin of the ISubAuditorium.
	 */
	public void setCabin(String cab);
	
	/**
	 * Return true iff the current object has a screen.
	 * 
	 * @return true iff the current object has a screen.
	 */
	public boolean hasScreen();
	
	/**
	 * Set if the current object has a screen.
	 * 
	 * If screen == "E", then it sets the instance variable to true.
	 * Otherwise (including null), it sets to false.
	 * 
	 * @param screen
	 * 			Code describing the screen of the ISubAuditorium.
	 */
	public void setScreen(String screen);
	
	/**
	 * Return true iff the current object has sound devices.
	 * 
	 * @return true iff the current object has sound devices.
	 */
	public boolean hasSound();
	
	/**
	 * Set if the current object has sound devices.
	 * 
	 * If sound == "S", then it sets the instance variable to true.
	 * Otherwise (including null), it sets to false.
	 * 
	 * @param sound
	 * 			Code describing the sound devices of the ISubAuditorium.
	 */
	public void setSound(String sound);
	
	/**
	 * Return true iff the current object has a retroprojector.
	 * 
	 * @return true iff the current object has a retroprojector.
	 */
	public boolean hasRetro();
	
	/**
	 * Set if the current object has a retroprojector.
	 * 
	 * If ret == "R", then it sets the instance variable to true.
	 * Otherwise (including null), it sets to false.
	 * 
	 * @param ret
	 * 			Code describing the retroprojector of the ISubAuditorium.
	 */
	public void setRetro(String ret);
	
	/**
	 * Return true iff the current object has slides.
	 * 
	 * @return true iff the current object has slides.
	 */
	public boolean hasSlide();
	
	/**
	 * Set if the current object has slides.
	 * If slide == "D", then it sets the instance variable to true.
	 * Otherwise (including null), it sets to false.
	 * 
	 * @param slide
	 * 			Code describing the slides of a ISubAuditorium.
	 */
	public void setSlide(String slide);
	
	/**
	 * Get some information about video devices.
	 * 
	 * The result can be the following:
	 * <ul>
	 * <li> VF : the ISubAuditorium has a video projector. </li>
	 * <li> VD : the ISubAuditorium has a video projector data. </li>
	 * <li> MD : the ISubAuditorium has a monitor data. </li>
	 * <li> TV : the ISubAuditorium has a television. </li>
	 * <li> null : the ISubAuditorium has nothing special. </li>
	 * </ul>
	 * 
	 * If it's different, then it's considered as nothing special.
	 * 
	 * @return Code describing information about video devices of the ISubAuditorium.
	 */
	public String getVideo();
	
	/**
	 * Set some information about video devices.
	 * 
	 * video can be the following:
	 * <ul>
	 * <li> VF : the ISubAuditorium has a video projector. </li>
	 * <li> VD : the ISubAuditorium has a video projector data. </li>
	 * <li> MD : the ISubAuditorium has a monitor data. </li>
	 * <li> TV : the ISubAuditorium has a television. </li>
	 * <li> null : the ISubAuditorium has nothing special. </li>
	 * </ul>
	 * 
	 * If it's different, then it's considered as nothing special.
	 * 
	 * @param video
	 * 			Code describing video devices of the ISubAuditorium.
	 */
	public void setVideo(String video);
	
	/**
	 * Return true iff there is network in the current object.
	 * 
	 * @return true iff there is network in the current object.
	 */
	public boolean hasNetwork();
	
	/**
	 * Set if there is network in the current object.
	 * If net != null, then it's true, else false.
	 * 
	 * @param net
	 * 			Code describing the network of the ISubAuditorium.
	 */
	public void setNetwork(String net);
	
	/**
	 * Return true iff there is access for disabled person.
	 * 
	 * @return true iff there is access for disabled person.
	 */
	public boolean hasAccess();
	
	/**
	 * Set the accessibility for disabled person to acc (TRUE ==> true).
	 * 
	 * @param acc
	 * 			Code describing the accessibility to disabled person of the ISubAuditorium.
	 */
	public void setAccess(String acc);
}
