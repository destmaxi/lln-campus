package be.ac.ucl.lfsab1509.llncampus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



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
 * Class that allow some operation on a database using a context
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	/** Database file name. */
    private static final String DB_NAME = "database.sql";
    /** Name of one of required tables (to test if the database is already copied). */ 
    private static final String REQUIRED_TABLE_NAME = "Poi"; 
    
    private SQLiteDatabase db; 
    private final Context context;
 
    /**
     * Constructor
     * Take and keep a reference of the passed context in order to access to 
     * the application assets and resources.
     * 
     * @param context 
     * 			Application context.
     */
    public DatabaseHelper(final Context context) {
    	super(context, DB_NAME, null, 1);
        this.context = context;
    }	
 
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * 
     * @throws IOException 
     * 			if an IOException occur
     */
    public final void createDatabase() throws IOException { 
    	if (!dbExist()) {
        	db = this.getWritableDatabase(); // To create the database on the device
   			copyDB(db); // To copy the .sql file in the database.
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying 
     * the file each time you open the application.
     * 
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
     * Open the .sql file and create the database.
     * 
     * @param db 
     * 			The database.
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
			throw new RuntimeException("Error when copying the database", e);
		} catch (SQLException e) {
			Log.e("DATABASE HELPER", e + "");
		}
		
    }
 
    /**
     * Create and/or open a SQLiteDatabase
     * 
     * @return The SQLiteDatabase
     * @throws SQLException 
     * 				If a SQL Exception occur
     * @see SQLiteOpenHelper#getWritableDatabase()
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
		// Do nothing
	}
 
	@Override
	public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		reset();
	}
	
	/**
	 * Reset the database. 
	 */
	public final void reset() {
		context.deleteDatabase(DB_NAME);
		try {
			this.createDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}