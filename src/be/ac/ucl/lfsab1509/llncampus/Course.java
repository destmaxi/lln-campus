package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;


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
 * Representation of a course (name, code).
 */
public final class Course {
	private final String courseCode;
	private final String courseName;
	private static boolean listChanged = false;

	/**
	 * Constructor.
	 * 
	 * @param code
	 *            Course code.
	 * @param name
	 *            Course name.
	 */
	public Course(String code, String name) {
		courseCode = code;
		courseName = name;
	}

	/**
	 * Get the course code.
	 * 
	 * @return Course code.
	 */
	public String getCourseCode() {
		return courseCode;
	}

	/**
	 * Get the course name.
	 * 
	 * @return Course name.
	 */
	public String getCoursName() {
		return courseName;
	}

	@Override
	public String toString() {
		return "Code : " + courseCode + " - Name : " + courseName;
	}

	/**
	 * Indicate if the course list changed.
	 * 
	 * @return true if the course list was modified, else false.
	 */
	public static boolean listChanged() {
		return listChanged;
	}

	/**
	 * Indicate that the last change in the course list was taken into account.
	 */
	public static void setListChangeSeen() {
		listChanged = false;
	}

	/**
	 * Get the course list.
	 * 
	 * @return Course list.
	 */
	public static ArrayList<Course> getList() {
		ArrayList<Course> courses = new ArrayList<Course>();
		try { // If we fetch the table whereas it doesn't exist yet, return an empty list.
			Cursor c = LLNCampus.getDatabase().select("Courses",
					new String[] {"CODE", "NAME"}, null, null, null, null, null, null);
			while (c.moveToNext()) {
				courses.add(new Course(c.getString(0), c.getString(1)));
			}
			c.close();
		}
		catch(RuntimeException e)
		{
			return new ArrayList<Course>();
		}
		return courses;
	}

	/**
	 * Add a new course in database.
	 * 
	 * @param code
	 *            Course code.
	 * @param name
	 *            Course name.
	 * @return true if adding succeed, else false.
	 */
	public static boolean add(String code, String name) {
		listChanged = true;
		Database db = LLNCampus.getDatabase();
		ContentValues values = new ContentValues();
		values.put("CODE", code);
		values.put("NAME", name);
		if (db.insert("Courses", values) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Remove a course from database.
	 * 
	 * @param course
	 *            Course to remove.
	 * @return true if removal succeed, else false.
	 */
	public static boolean remove(Course course) {
		listChanged = true;
		Database db = LLNCampus.getDatabase();
		return db.delete("Courses", "CODE = ?",
				new String[] {course.courseCode}) > 0;
	}
}