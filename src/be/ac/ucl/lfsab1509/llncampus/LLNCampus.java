package be.ac.ucl.lfsab1509.llncampus;

import android.app.Application;

public class LLNCampus extends Application{
	private static LLNCampus APPLICATION_CONTEXT;
	private static Database DB;
	private static String DATABASE_FILENAME = "database.sqlite"; 

	@Override
	public void onCreate() {
		super.onCreate();

		//initialization for the application context here
		APPLICATION_CONTEXT = this;
	}
	public Database getDatabase() {
		if(DB == null){
			DB = new Database(DATABASE_FILENAME, this);
			DB.open();
		}
		return DB;
	}
	
	public static LLNCampus getContext() {
		return APPLICATION_CONTEXT;
	}
}
