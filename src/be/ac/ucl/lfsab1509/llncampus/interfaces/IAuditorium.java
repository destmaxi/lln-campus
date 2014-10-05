package be.ac.ucl.lfsab1509.llncampus.interfaces;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier

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
 * 
 * Interface of an Auditorium object extends IBuilding
 */
public interface IAuditorium extends IBuilding{
	
	/**
	 * Fournit l'image miniature de l auditoire.
	 * @return la ressource ID de l'image miniature
	 */
	public int getMiniPicture();
	
	
}
