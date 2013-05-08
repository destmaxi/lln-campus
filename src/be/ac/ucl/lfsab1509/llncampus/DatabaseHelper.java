package be.ac.ucl.lfsab1509.llncampus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;



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
 * Class that allow some operation on a database using a context
 * */
public class DatabaseHelper extends SQLiteOpenHelper {
	/** Nom du fichier de la db. */
    private static final String DB_NAME = "database.sql";
    /** Nom d'une table requise (pour tester si la db est deja copiee). */ 
    private static final String REQUIRED_TABLE_NAME = "Poi"; 
    private SQLiteDatabase db; 
    private final Context context;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to 
     * the application assets and resources.
     * @param context Contexte de l'application.
     */
    public DatabaseHelper(final Context context) {
    	super(context, DB_NAME, null, 1);
        this.context = context;
    }	
 
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * @throws IOException If a IOException occur
     */
    public final void createDatabase() throws IOException { 
    	if (!dbExist()) {
        	db = this.getWritableDatabase(); // Pr creer la db sur l'appareil
   			copyDB(db); // Pour copier le .sql dans la db.
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying 
     * the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean dbExist() {
    	SQLiteDatabase db = null;
    	try {
    		db = this.getReadableDatabase();
    	} catch (SQLiteException e) {
    		return false;
    	}
 
    	if (db != null) {
    		try {
    			db.query(REQUIRED_TABLE_NAME, null, null, null, null, null, null, null);
    		} catch (SQLException e) {
    			return false;
    		}
    		db.close();
    		return true;
    	}
    	return false;
     }
 
    /**
     * Ouvre le fichier .sql et créer la base de donnée. 
     * @param db The database.
     * */
    private void copyDB(SQLiteDatabase db) {
    	BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets().open(DB_NAME)));
			String line;
			String instruction = "";
			while ((line = in.readLine()) != null) {
				instruction += line;
				if (line.trim().charAt(line.trim().length() - 1) == ';') {
					if (!instruction.isEmpty()) {
						db.execSQL(instruction);
					}
					instruction = "";
				}
			}
			if (!instruction.isEmpty()) {
				db.execSQL(instruction);
			}
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la copie de la base de donnee", e);
		}
    }
 
    /**
     * 
     * @return the SQLiteDatabase
     * @throws SQLException If a SQL Exception occur
     */
    public final SQLiteDatabase open() throws SQLException {
    	return db = this.getWritableDatabase();
    }
 
    @Override
	public final synchronized void close() {
    	if (db != null) {
    		db.close();
    	}	
    	super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//
	}
 
	@Override
	public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		reset();
	}
	
	/**
	 * Reset the database. 
	 */
	public final void reset() {
		//FIXME : A tester
		context.deleteDatabase(DB_NAME);
		try {
			this.createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}