package be.ac.ucl.lfsab1509.llncampus.activity;


import java.io.File;

import be.ac.ucl.lfsab1509.llncampus.ExternalAppUtility;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Class that will be executed when starting the application.
 * Related with the XML main_title.xml
 *
 */
public class Launcher extends LLNCampusActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainTitle.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
    }
}
