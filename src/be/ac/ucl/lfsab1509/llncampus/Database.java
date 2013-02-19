package be.ac.ucl.lfsab1509.llncampus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Database {
		private SQLiteDatabase db;
		private final String filename;
		private final String DB_PATH;
		private final Context context;
		/*
		 * Constructor.
		 */
		public Database(String filename, Context context) {
			this.filename = filename;
			this.context = context;
			DB_PATH  = context.getFilesDir().getPath()+context.getApplicationContext().getPackageName()+ "/databases/";
			createDatabase();
		}

		/*
		 * Open the database
		 * @return True in case of success, false otherwise.
		 */
		public boolean open() {
			if(isOpen()){ return true; }
			try{
				SQLiteDatabase.openDatabase(DB_PATH+filename, null, SQLiteDatabase.OPEN_READWRITE);
			} catch(SQLiteException e){
				Log.e("Database.java - open",e.getMessage());
				return false;
			}
			return true;
		}

		public boolean isOpen(){
			if(db == null){
				return false;
			}
			return db.isOpen();
		}

		/*
		 * Insert a row in a table. 
		 * 
		 * @param table : Name of the table
		 * @param values : Values to insert.
		 * @return : ID of the new row. An integer <0 in case of error.
		 */
		public long insert(String table, ContentValues values){
			if(!isOpen() && !open()){
				Log.e("Database.java - insert", "Unable to open database");
				return -1;
			}
			
			try{
				return db.insert(table, null, values);
			} catch (SQLiteConstraintException e){
				Log.e("Database.java - insert","SQL Constraint error : "+e.getMessage());
				return -2;
			}
		}

		/*
		 * Update a row (or more) in a table
		 * @param table : Name of the table
		 * @param values : Values to update
		 * @param whereMask : Mask of where clause (example : "ID = ? AND TYPE = 'auditoire'")
		 * @param whereVal : Values to put on the mask.
		 * @return The number of rows affected or -1 in case of error. 
		 */
		public int update(String table, ContentValues values,String whereMask, String[] whereVal){
			if(!isOpen() && !open()){
				Log.e("Database.java - update", "Unable to open database");
				return -1;
			}
			return db.update(table, values, whereMask, whereVal);
		}

		/*
		 * Remove a row in a table
		 * @params @see update
		 * @return The number of rows deleted or -1 in case of error.
		 */
		public int delete(String table, String whereMask, String[] whereVal){
			if(!isOpen() && !open()){
				Log.e("Database.java - delete", "Unable to open database");
				return -1;
			}
			return db.delete(table, whereMask,whereVal);
		}

		/*
		 * Close the database.
		 */
		public void close(){
			db.close();
		}

		/*
		 * Execute a sql raw query.
		 * @param query a SQL query
		 * @param values (optional) Values to insert on the query (if your query contains ? [mask])
		 * @return A result set of type Cursor.
		 * 
		 * see also select
		 */
		public Cursor sqlRawQuery(String query){
			return sqlRawQuery(query,null);
		}
		public Cursor sqlRawQuery(String query, String[] values){
			if(!isOpen() && !open()){
				Log.e("Database.java - sqlQuery", "Unable to open database");
				return null;
			}
			return db.rawQuery(query, null);
		}

		/*
		 * Execute a select request
		 * @param table	The table name to compile the query against.
		 * @param columns	A list of which columns to return. Passing null will return all columns, 
		 * 		which is discouraged to prevent reading data from storage that isn't going to be used.
		 * @param selection	A filter declaring which rows to return, formatted as an SQL WHERE clause 
		 * 	(excluding the WHERE itself). Passing null will return all rows for the given table.
		 * @param selectionArgs	You may include ?s in selection, which will be replaced by the values 
		 * 	from selectionArgs, in order that they appear in the selection. The values will be bound 
		 * 	as Strings.
		 * @param groupBy	A filter declaring how to group rows, formatted as an SQL GROUP BY clause 
		 * 	(excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
		 * @param having	A filter declare which row groups to include in the cursor, if row grouping 
		 * 	is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null
		 * 	will cause all row groups to be included, and is required when row grouping is not being used.
		 * @param orderBy	How to order the rows, formatted as an SQL ORDER BY clause (excluding the 
		 * 	ORDER BY itself). Passing null will use the default sort order, which may be unordered.
		 * @param limit	Limits the number of rows returned by the query, formatted as LIMIT clause. 
		 * 	Passing null denotes no LIMIT clause.
		 * @return A Cursor object, which is positioned before the first entry. Note that Cursors 
		 * 	are not synchronized, see the documentation for more details. And Null in case of error.

		 */
		public Cursor select(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
			if(!isOpen() && !open()){
				Log.e("Database.java - select", "Unable to open database");
				return null;
			}
			return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		}
		
		
		
		
		public void reset(){
			try {
				copyDB();
			} catch (IOException e) {
				Log.e("Database.java - reset","Impossible de reinitialise la db "+e.getMessage());
			}
		}
		public void createDatabase(){
			if(dbExist()) {
				Log.d("Database.java - createDatabase", "La DB existe deja");//FIXME
		    } else {
		    	try {
		    		copyDB();
		    	} catch(IOException e) {
		    		Log.e("Database.java - createDatabase", "Erreur lors de la copie de la db "+e.getMessage());
		    	}
		    }
		}
		
		private boolean dbExist(){
			SQLiteDatabase checkDB = null;
	    	try{
	    		checkDB = SQLiteDatabase.openDatabase(DB_PATH + filename, null, SQLiteDatabase.OPEN_READWRITE);
	    		checkDB.query("poi", null, null, null, null, null, null, "0");//FIXME
	    	}catch(SQLiteException e){
	    		return false;
	    	}
	    	if(checkDB != null){
	    		checkDB.close();
	    	}
	    	return (checkDB != null);
		}

		/**
		 * Copie la DB depuis le dossier assets
		 */
		 private void copyDB() throws IOException{
			 InputStream in = context.getAssets().open(filename);
			 OutputStream out = new FileOutputStream(DB_PATH + filename);

			 byte[] buffer = new byte[1024];
			 int length;
			 while ((length = in.read(buffer))>0) {
				 out.write(buffer,0,length);
			 }
			 in.close();
			 out.flush();
			 out.close();
		 }
}