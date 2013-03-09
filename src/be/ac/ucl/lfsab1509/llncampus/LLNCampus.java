package be.ac.ucl.lfsab1509.llncampus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
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
		Log.d("DEBUG", "Application context = " + APPLICATION_CONTEXT);
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
	
	public static void copyAssets() {
	    AssetManager assetManager = getContext().getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("tag", "Failed to get asset file list.", e);
	    }
	    for(String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename);
	          out = new FileOutputStream("/" + Environment.getExternalStorageDirectory().getPath() + "/" + filename);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(IOException e) {
	            Log.e("tag", "Failed to copy asset file: " + filename, e);
	        }       
	    }
	}
	private static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
}