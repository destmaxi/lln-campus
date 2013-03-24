package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

public class Cours {
	public String coursCode;
	public String coursName;
	public Cours (String c, String n) {
		coursCode = c;
		coursName = n;
	}
	public String toString(){
		return "Code : " + coursCode
				+ " - Nom : " + coursName;
	}
	
	public static ArrayList<Cours> getList() {
		ArrayList<Cours> courses = new ArrayList<Cours>();
		Cursor c = LLNCampus.getDatabase().select(
						"Courses", new String[]{"CODE", "NAME"}, 
						null, null, null, null, null, null);
		while (c.moveToNext()) {
			courses.add(new Cours(c.getString(0), c.getString(1)));
		}
		c.close();
		return courses;
	}
	public static boolean add(String code, String name) {
		Database db = LLNCampus.getDatabase();
		ContentValues values = new ContentValues();
		values.put("CODE", code);
		values.put("NAME", name);
		if (db.insert("Courses", values) > 0) {
			return true;
		} 
		return false;
	}
	public static boolean remove(Cours cours) {
		Database db = LLNCampus.getDatabase();
		return db.delete("Courses", "CODE = ?", new String[]{cours.coursCode}) > 0;
	}
}
