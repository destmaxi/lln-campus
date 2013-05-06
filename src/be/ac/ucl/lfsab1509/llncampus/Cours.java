package be.ac.ucl.lfsab1509.llncampus;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

public final class Cours {
	private final String coursCode;
	private final String coursName;
	private static boolean listChanged = false;

	/**
	 * Constructeur.
	 * 
	 * @param c
	 *            Code du cours.
	 * @param n
	 *            Nom du cours.
	 */
	public Cours(String c, String n) {
		coursCode = c;
		coursName = n;
	}

	/**
	 * Fournit le code du cours.
	 * 
	 * @return code du cours.
	 */
	public String getCoursCode() {
		return coursCode;
	}

	/**
	 * Fournit le nom du cours.
	 * 
	 * @return nom du cours.
	 */
	public String getCoursName() {
		return coursName;
	}

	@Override
	public String toString() {
		return "Code : " + coursCode + " - Nom : " + coursName;
	}

	/**
	 * Indique si la liste de cours a été changée.
	 * 
	 * @return True si la liste des cours a été modifiée, false sinon.
	 */
	public static boolean listChanged() {
		return listChanged;
	}

	/**
	 * Indique qu'on a pris en compte le dernier changement de la liste des
	 * cours.
	 */
	public static void setListChangeSeen() {
		listChanged = false;
	}

	/**
	 * Retourne la liste des cours.
	 * 
	 * @return Liste des cours.
	 */
	public static ArrayList<Cours> getList() {
		ArrayList<Cours> courses = new ArrayList<Cours>();
		try { // Si on va voir la table alors qu'elle n'existe pas encore, retourner liste vide
		Cursor c = LLNCampus.getDatabase().select("Courses",
				new String[] {"CODE", "NAME"}, null, null, null, null, null,
				null);
		while (c.moveToNext()) {
			courses.add(new Cours(c.getString(0), c.getString(1)));
		}
		c.close();
		}
		catch(RuntimeException e)
		{
			return new ArrayList<Cours>();
		}
		return courses;
	}

	/**
	 * Ajoute un cours (dans la base de donnée).
	 * 
	 * @param code
	 *            Code du cours
	 * @param name
	 *            Nom du cours
	 * @return True en cas de succès, false sinon.
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
	 * Supprime un cours (de la base de donnée).
	 * 
	 * @param cours
	 *            Cours à supprimer.
	 * @return true en cas de succès, false sinon.
	 */
	public static boolean remove(Cours cours) {
		listChanged = true;
		Database db = LLNCampus.getDatabase();
		return db.delete("Courses", "CODE = ?",
				new String[] {cours.coursCode}) > 0;
	}
}