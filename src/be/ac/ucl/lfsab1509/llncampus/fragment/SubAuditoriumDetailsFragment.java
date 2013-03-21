package be.ac.ucl.lfsab1509.llncampus.fragment;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SubAuditoriumDetailsFragment extends LLNCampusFragment {

	private View viewer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    viewer = inflater.inflate(R.layout.subauditorium_details, container, false);   
	    return viewer;
	}
	
	/**
	 * Retourne oui si value == true, non si false
	 * @param value
	 * @return oui si value == true, non si false
	 */
	private String ouiNon(boolean value)
	{
		if (value)
		{
			return this.getString(R.string.yes);
		}
		return this.getString(R.string.no);
	}
	
	public void updateSubAuditorium(ISubAuditorium subauditorium){
		    String name = subauditorium.getName();
		    boolean access = subauditorium.hasAccess();
		    String places = String.valueOf(subauditorium.getNbPlaces());
		    String network = ouiNon(subauditorium.hasNetwork());
		    String ecran = ouiNon(subauditorium.hasEcran());
		    String retro = ouiNon(subauditorium.hasRetro());
		    String dia = ouiNon(subauditorium.hasDia());
		    String video = subauditorium.getVideo(); // A RETRAVAILLER
		    String sono = ouiNon(subauditorium.hasSono());
		    String cabine = ouiNon(subauditorium.hasCabine());
		    String mobilier = subauditorium.getMobilier(); // A RETRAVAILLER
		    
	    	ImageView imageAccess = (ImageView) viewer.findViewById(R.id.access_picture);
		    if (access)
		    {
		    	imageAccess.setVisibility(View.VISIBLE);
		    }
		    else
		    {
		    	imageAccess.setVisibility(View.INVISIBLE);
		    }
		    
		    TextView textName = (TextView) viewer.findViewById(R.id.subauditorium_name);
		    textName.setText(name);
		    
	        TextView textPlaces = (TextView) viewer.findViewById(R.id.nbplaces_rep);
	        textPlaces.setText(places);
	        
	        TextView textReseau = (TextView) viewer.findViewById(R.id.reseau_rep);
	        textReseau.setText(network);
	        
	        TextView textEcran = (TextView) viewer.findViewById(R.id.ecran_rep);
	        textEcran.setText(ecran);
	        
	        TextView textRetro = (TextView) viewer.findViewById(R.id.retro_rep);
	        textRetro.setText(retro);
	        
	        TextView textDia = (TextView) viewer.findViewById(R.id.dia_rep);
	        textDia.setText(dia);
	        
	        TextView textVideo = (TextView) viewer.findViewById(R.id.video_rep);
	        // CODE VIDEO
	        
	        TextView textSono = (TextView) viewer.findViewById(R.id.sono_rep);
	        textSono.setText(sono);
	        
	        TextView textCabine = (TextView) viewer.findViewById(R.id.cabine_rep);
	        textCabine.setText(cabine);
	        
	        TextView textMobilier = (TextView) viewer.findViewById(R.id.mobilier_rep);
	        // CODE MOBILIER
	        
	        
	}
}
