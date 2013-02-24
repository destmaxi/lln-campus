package be.ac.ucl.lfsab1509.llncampus.activity;


import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Class intended for showing some information about an Auditorium
 * @author Anh Tuan
 *
 */
public class DetailsAuditorium extends LLNCampusActivity{
	private TextView name=null;
	private Auditorium auditorium;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.auditorium_details);
	        
	        
	        /*name of Auditorium*/
	        this.name = (TextView) findViewById(R.id.auditorium_detail_name);
			Bundle extras = getIntent().getExtras(); 
			if(extras !=null)
			{
				/* TODO passer le int et pas le nom */
				String nameAuditorium = extras.getString("NAME");
				Log.d("NAME", nameAuditorium);
				String[] cols = {"ID","NAME","LATITUDE", "LONGITUDE", "ADDRESS"};
				Cursor c = super.db.select("poi", cols, "NAME = "+ "'"+nameAuditorium+"'", null, null, null, null, null);
				c.moveToFirst();
				auditorium = new Auditorium(c.getInt(0), c.getString(1), c.getDouble(2), c.getDouble(3), c.getString(4));
			}
			this.name.setText(auditorium.getName());
	    }
	 
	 
}
