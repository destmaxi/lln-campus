package be.ac.ucl.lfsab1509.llncampus;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class LLNCampus extends Application{
	private static Context APPLICATION_CONTEXT;
	private static Database DB;

	@Override
	public void onCreate() {
		super.onCreate();

		//initialization for the application context here
		setContext(this);
	}
	public static void setContext(Context c){
		APPLICATION_CONTEXT = c;
	}
	
	public synchronized void close(){
		if(DB!=null){
			DB.close();
			DB = null;
		}
	}
	
	
	private static void openDatabase(){
		Log.d("DEBUG", "Application context = "+APPLICATION_CONTEXT);
		DB = new Database(APPLICATION_CONTEXT);
		DB.open();
	}
	
	public static Database getDatabase() {
		if(DB == null){ openDatabase();	}
		return DB;
	}
	
	public static Context getContext() {
		return APPLICATION_CONTEXT;
	}
}