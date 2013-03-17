package be.ac.ucl.lfsab1509.llncampus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import be.ac.ucl.lfsab1509.llncampus.R;

/**
 * Edit settings
 * @author Damien
 */
public class SettingsActivity extends LLNCampusActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
     // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Message")
               .setTitle("Title");
        
     // Add the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User clicked OK button
                   }
               });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        
        dialog.show();
        
        
    }
    
    @Override  
    protected void editActionBar(){
    	//Nothing to do because it's a dialog!
    }
    
}