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
 * @author Damien
 */
public class DatabaseHelper extends SQLiteOpenHelper{
	//Nom du fichier de la db.
    private static final String DB_NAME = "database.sql";
    //Nom d'une table requise (pour tester si la db est deja copiee) 
    private static final String REQUIRED_TABLE_NAME = "poi"; 
    private SQLiteDatabase db; 
    private final Context context;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     * @param filename 
     */
    public DatabaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.context = context;
    }	
 
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * 
     */
    public void createDatabase() throws IOException{ 
    	if(!dbExist()){
        	db = this.getWritableDatabase(); // Pr creer la db sur l'appareil
   			copyDB(db); // Pour copier le .sql dans la db.
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean dbExist(){
    	SQLiteDatabase db = null;
    	try{
    		db = this.getReadableDatabase();//SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    	} catch(SQLiteException e){
    		return false;
    	}
 
    	if(db != null){
    		try{
    			db.query(REQUIRED_TABLE_NAME, null, null, null, null, null, null, null);
    		} catch(SQLException e){
    			return false;
    		}
    		db.close();
    		return true;
    	}
    	return false;
     }
 
    /**
     * Ouvre le fichier .sql et créer la base de donnée. 
     * @param db2 
     * */
    private void copyDB(SQLiteDatabase db){
    	BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets().open(DB_NAME)));
			String line;
			while((line = in.readLine()) != null) {
				//FIXME : Il faut que dans le fichier .sql, les requetes ne soit pas sur plusieurs lignes...
				//Log.d("DB",line);
				if(!line.equals("")){
					db.execSQL(line);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la copie de la base de donnee",e);
		}
    }
 
    public SQLiteDatabase open() throws SQLException{
    	return db = this.getWritableDatabase();//SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }
 
    @Override
	public synchronized void close() {
    	if(db != null){
    		db.close();
    	}	
    	super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		reset();
	}

	public void reset() {
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