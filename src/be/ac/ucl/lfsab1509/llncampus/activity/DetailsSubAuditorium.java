package be.ac.ucl.lfsab1509.llncampus.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.SubAuditorium;
import be.ac.ucl.lfsab1509.llncampus.fragment.SubAuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;

/**
 * This class is intended to shows information about an ISuubAuditorium
 * Related with the xml file subauditorium_details_fragment.xml
 * Caution: this class is called only if the device is NOT in landscape!
 * 			Check in the xml file (landscape and normal) for details.
 *
 */
public class DetailsSubAuditorium extends LLNCampusActivity{
	private ISubAuditorium subauditorium;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Finish the activity if the orientation is landscape
		int orientation = this.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			finish();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.subauditorium_details_fragment);

		// Fetch all given information useful for the layout
		int id_parent = getIntent().getIntExtra("ID_PARENT", 0);
		int id = getIntent().getIntExtra("ID", 0);
		String name = getIntent().getStringExtra("NAME");
		int nbplaces = getIntent().getIntExtra("NBPLACES", 0);
		String mobilier = getIntent().getStringExtra("MOBILIER");
		boolean cabine = getIntent().getBooleanExtra("CABINE", false);
		boolean ecran = getIntent().getBooleanExtra("ECRAN", false);
		boolean retro = getIntent().getBooleanExtra("RETRO", false);
		boolean sono = getIntent().getBooleanExtra("SONO", false);
		boolean dia = getIntent().getBooleanExtra("DIA", false);
		String video = getIntent().getStringExtra("VIDEO");
		boolean network = getIntent().getBooleanExtra("NETWORK", false);
		boolean access = getIntent().getBooleanExtra("ACCESS", false);

		// Create a new instance of ISubAuditorium
		subauditorium = new SubAuditorium(id_parent, id, name, nbplaces, mobilier, cabine, ecran, sono, retro, dia, video, network, access);

		// Delegate the update operation to the fragment
		SubAuditoriumDetailsFragment viewer = (SubAuditoriumDetailsFragment) getFragmentManager().findFragmentById(R.id.subauditorium_details_fragment);
		viewer.updateSubAuditorium(subauditorium);
	}
}
