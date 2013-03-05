package be.ac.ucl.lfsab1509.llncampus;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Database {
		private SQLiteDatabase db;
		private DatabaseHelper dbh;
		/*
		 * Constructor.
		 */
		public Database(final Context context) {
			this.dbh = new DatabaseHelper(context);
	        try {
	        	dbh.createDatabase();
	        } catch (IOException e) {
	        	Log.e("Database.java", "Unable to create database. "+e.getMessage());
	        }
		}

		/*
		 * Open the database
		 * @return True in case of success, false otherwise.
		 */
		public boolean open() {
			if (isOpen()) { return true; }
			try{
				db = dbh.open();
			} catch (SQLiteException e) {
				Log.e("Database.java - open", e.getMessage());
				return false;
			}
			return true;
		}

		/*
		 * Check if the database is open
		 * @return true if db is open, false otherwise
		 */
		public boolean isOpen(){
			if (db == null) {
				return false;
			}
			return db.isOpen();
		}

		/**
		 * Insert a row in a table. 
		 * 
		 * @param table : Name of the table
		 * @param values : Values to insert.
		 * @return : ID of the new row. An integer <0 in case of error.
		 */
		public long insert(final String table, final ContentValues values) {
			if(!isOpen() && !open()) {
				Log.e("Database.java - insert", "Unable to open database");
				return -1;
			}
			
			try {
				return db.insert(table, null, values);
			} catch (SQLiteConstraintException e) {
				Log.e("Database.java - insert", "SQL Constraint error : "+e.getMessage());
				return -2;
			}
		}

		/**
		 * Update a row (or more) in a table
		 * @param table : Name of the table
		 * @param values : Values to update
		 * @param whereMask : Mask of where clause (example : "ID = ? AND TYPE = 'auditoire'")
		 * @param whereVal : Values to put on the mask.
		 * @return The number of rows affected or -1 in case of error. 
		 */
		public int update(final String table, final ContentValues values, final String whereMask, final String[] whereVal){
			if (!isOpen() && !open()) {
				Log.e("Database.java - update", "Unable to open database");
				return -1;
			}
			return db.update(table, values, whereMask, whereVal);
		}

		/**
		 * Remove a row in a table
		 * @params @see update
		 * @return The number of rows deleted or -1 in case of error.
		 */
		public int delete(final String table, final String whereMask, final String[] whereVal) {
			if (!isOpen() && !open()) {
				Log.e("Database.java - delete", "Unable to open database");
				return -1;
			}
			return db.delete(table, whereMask, whereVal);
		}

		/**
		 * Close the database.
		 */
		public void close() {
			db.close();
		}

		/**
		 * Execute a sql raw query.
		 * @param query a SQL query
		 * @param values (optional) Values to insert on the query (if your query contains ? [mask])
		 * @return A result set of type Cursor.
		 * 
		 * see also select
		 */
		public Cursor sqlRawQuery(final String query) {
			return sqlRawQuery(query,null);
		}
		
		public Cursor sqlRawQuery(final String query, final String[] values) {
			if (!isOpen() && !open()) {
				Log.e("Database.java - sqlQuery", "Unable to open database");
				return null;
			}
			return db.rawQuery(query, null);
		}

		/**
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
		public Cursor select(final String table, final String[] columns, final String selection, final String[] selectionArgs, final String groupBy, final String having, final String orderBy, final String limit) {
			if (!isOpen() && !open()) {
				Log.e("Database.java - select", "Unable to open database");
				return null;
			}
			return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		}

		public void reset() {
			dbh.reset();
			// TODO Auto-generated method stub
			
		}
		

	
}