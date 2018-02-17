package be.ac.ucl.lfsab1509.llncampus;


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
 * Represent an "offer" from UCL. An "offer" is related to a course program followed during
 * an academic year.
 */
public final class Offer {
	private final int academicYear;
	private final int offerNumber;
	private final String offerCode;
	private final String offerName;

	/**
	 * Constructor.
	 * 
	 * @param academicYear
	 *            Academic year (e.g. : 2012 for 2012-2013 academic year).
	 * @param offerNumber
	 *            Offer number (e.g. : 4932).
	 * @param offerCode
	 *            Offer code (e.g. : FSA13BA).
	 * @param offerName
	 *            Offer name (e.g. : Third Year of Bachelor in engineering sciences, 
	 *            civil engineer orientation).
	 */
	public Offer(int academicYear, int offerNumber, String offerCode, String offerName) {
		this.academicYear = academicYear;
		this.offerNumber = offerNumber;
		this.offerCode = offerCode;
		this.offerName = offerName;
	}

	/**
	 * Get the academic year. For instance, 2012 for the 2012-2013 academic year.
	 * 
	 * @return Academic year.
	 */
	public int getAcademicYear() {
		return academicYear;
	}

	/**
	 * Get the offer number. For instance, 4932.
	 * 
	 * @return Offer number.
	 */
	public int getOfferNumber() {
		return offerNumber;
	}

	/**
	 * Get the offer code. For instance, FSA13BA.
	 * 
	 * @return Offer code.
	 */
	public String getOfferCode() {
		return offerCode;
	}

	/**
	 * Get the offer name. For instance, Third Year of Bachelor in engineering sciences, 
	 * civil engineer orientation.
	 * 
	 * @return Offer name
	 */
	public String getOfferName() {
		return offerName;
	}

	@Override
	public String toString() {
		return "academicYear : " + academicYear + " - offerNumber : " + offerNumber
				+ " - offerCode : " + offerCode + " - offerName : " + offerName;
	}

}