package be.ac.ucl.lfsab1509.llncampus;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
 * Class to manage a Database.
 */
public class Database {
	private SQLiteDatabase db;
	private DatabaseHelper dbh;

	/**
	 * Constructor. 
	 * 
	 * @param context 
	 * 			Application context.
	 */
	public Database(final Context context) {
		this.dbh = new DatabaseHelper(context);
	}

	/**
	 * Open the database.
	 * 
	 * @return true if opening succeed, else false.
	 */
	public final boolean open() {
		if (isOpen()) { return true; }
		try {
			dbh.createDatabase();
			db = dbh.open();
		}
		catch (IOException e) {
			Log.e("Database.java - open", e.getMessage());
			return false;
		}
		catch (SQLiteException e) {
			Log.e("Database.java - open", e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Check if the database is open.
	 * 
	 * @return true if the database is open, else false.
	 */
	public final boolean isOpen() {
		if (db == null) {
			return false;
		}
		return db.isOpen();
	}

	/**
	 * Insert a row in a table. 
	 * 
	 * @param table 
	 * 			Table name.
	 * @param values 
	 * 			Values to insert.
	 * @return ID of the new row if insertion succeed, else an integer lower than 0.
	 */
	public final long insert(final String table, final ContentValues values) {
		if (!isOpen() && !open()) {
			Log.e("Database.java - insert", "Unable to open database");
			return -1;
		}

		try {
			return db.insert(table, null, values);
		} catch (SQLiteConstraintException e) {
			Log.e("Database.java - insert", "SQL Constraint error : " + e.getMessage());
			return -1;
		}
	}

	/**
	 * Update a row (or more) in a table.
	 * 
	 * @param table
	 * 			Table name.
	 * @param values
	 * 			Values to update.
	 * @param whereMask
	 * 			Mask of where clause (example : "ID = ? AND TYPE = 'auditorium'")
	 * @param whereVal
	 * 			Values to put on the mask.
	 * @return The number of rows affected if update succeed, else -1 (in case of error). 
	 */
	public final int update(final String table, final ContentValues values, 
			final String whereMask, final String[] whereVal) {
		if (!isOpen() && !open()) {
			Log.e("Database.java - update", "Unable to open database");
			return -1;
		}
		return db.update(table, values, whereMask, whereVal);
	}

	/**
	 * Remove a row in a table.
	 * 
	 * @param table
	 * 			Table name.
	 * @param whereMask
	 * 			Mask of where clause (example : "ID = ? AND TYPE = 'auditorium'")
	 * @param whereVal
	 * 			Values to put on the mask. 
	 * @return The number of rows deleted if delete succeed or -1 in case of error.
	 * @see Database#update
	 */
	public final int delete(final String table, final String whereMask, 
			final String[] whereVal) {
		if (!isOpen() && !open()) {
			Log.e("Database.java - delete", "Unable to open database");
			return -1;
		}
		return db.delete(table, whereMask, whereVal);
	}

	/**
	 * Close the database.
	 */
	public final void close() {
		db.close();
	}

	/**
	 * Execute a SQL raw query.
	 * 
	 * @param query 
	 * 			SQL query.
	 * @return A result set of type Cursor.
	 * 
	 * @see Database#select
	 */
	public final Cursor sqlRawQuery(final String query) {
		return sqlRawQuery(query, null);
	}

	/**
	 * Execute a raw query.
	 * 
	 * @param query 
	 * 			The raw query.
	 * @param values 
	 * 			Values to insert on the query (if your query contains ? [mask]).
	 * @return Cursor of this raw query.
	 * @see Database#select
	 */
	public final Cursor sqlRawQuery(final String query, final String[] values) {
		if (!isOpen() && !open()) {
			Log.e("Database.java - sqlQuery", "Unable to open database");
			return null;
		}
		return db.rawQuery(query, null);
	}

	/**
	 * Execute a select request.
	 * 
	 * @param table	
	 * 			The table name to compile the query against.
	 * @param columns
	 * 			A list of which columns to return. Passing null will return 
	 * all columns, which is discouraged to prevent reading data from storage that 
	 * isn't going to be used.
	 * @param selection	
	 * 			A filter declaring which rows to return, formatted as an SQL 
	 * WHERE clause (excluding the WHERE itself). Passing null will return all rows 
	 * for the given table.
	 * @param selectionArgs 
	 * 			You may include ?s in selection, which will be replaced 
	 * by the values from selectionArgs, in order that they appear in the selection. 
	 * The values will be bound as Strings.
	 * @param groupBy 
	 * 			A filter declaring how to group rows, formatted as an SQL GROUP BY 
	 * clause (excluding the GROUP BY itself). Passing null will cause the rows to not 
	 * be grouped.
	 * @param having 
	 * 			A filter declare which row groups to include in the cursor, if row 
	 * grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING 
	 * itself). Passing null will cause all row groups to be included, and is required 
	 * when row grouping is not being used.
	 * @param orderBy	
	 * 			How to order the rows, formatted as an SQL ORDER BY clause 
	 * (excluding the ORDER BY itself). Passing null will use the default sort order, 
	 * which may be unordered.
	 * @param limit	
	 * 			Limits the number of rows returned by the query, formatted as LIMIT 
	 * clause. Passing null denotes no LIMIT clause.
	 * @return A Cursor object, which is positioned before the first entry. Note that Cursors 
	 * 	are not synchronized, see the documentation for more details. And Null in case of error.
	 * @see Cursor
	 */
	public final Cursor select(final String table, final String[] columns, 
			final String selection, final String[] selectionArgs, final String groupBy,
			final String having, final String orderBy, final String limit) {
		if (!isOpen() && !open()) {
			Log.e("Database.java - select", "Unable to open database");
			return null;
		}
		return db.query(table, columns, selection, selectionArgs, groupBy, 
				having, orderBy, limit);
	}
	
	/**
	 * Reset the database. The database must first be opened.
	 */
	public final void reset() {
		close();
		dbh.reset();
	}

}