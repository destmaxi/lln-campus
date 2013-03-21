package be.ac.ucl.lfsab1509.llncampus.activity;

import android.os.Bundle;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.SubAuditorium;
import be.ac.ucl.lfsab1509.llncampus.fragment.SubAuditoriumDetailsFragment;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;

public class DetailsSubAuditorium extends LLNCampusActivity{
	private ISubAuditorium subauditorium;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.subauditorium_details_fragment);
	        // Comment faire pour passer le parent? Fondammentalement, ce n'est pas utile
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

	        
	        subauditorium = new SubAuditorium(id_parent, id, name, nbplaces, mobilier, cabine, ecran, sono, retro, dia, video, network, access);
	        
	        SubAuditoriumDetailsFragment viewer = (SubAuditoriumDetailsFragment) getFragmentManager().findFragmentById(R.id.subauditorium_details_fragment);
	        viewer.updateSubAuditorium(subauditorium);
	    }
}
