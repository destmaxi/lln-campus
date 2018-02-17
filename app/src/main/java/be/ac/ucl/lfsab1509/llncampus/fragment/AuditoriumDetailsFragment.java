package be.ac.ucl.lfsab1509.llncampus.fragment;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * This class is intended to manage information shown about details of an Auditorium selected.
 * Related with the XML file auditorium_list_fragment.xml or the auditorium_details_fragment.xml.
 * Note: a fragment is called by the xml file!
 *
 */
public class AuditoriumDetailsFragment extends LLNCampusFragment {

	private View viewer;
	
	// Attach the layout with the fragment.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
	    viewer = inflater.inflate(R.layout.auditorium_details, container, false);   
	    return viewer;
	}
	
	/**
	 * Update information shown by the layout about an IAuditorium.
	 * 
	 * @param auditorium
	 * 			A non-null IAuditorium to show information about.
	 */
	public void updateAuditorium(IAuditorium auditorium){
		    String name = auditorium.getName();
		    String address = auditorium.getAddress();
		    TextView textName = (TextView) viewer.findViewById(R.id.auditorium_name);
		    textName.setText(name);
		    
	        TextView textAddress=(TextView) viewer.findViewById(R.id.auditorium_address);
	        ImageView image = (ImageView) viewer.findViewById(R.id.auditorium_picture);
	        image.setImageResource(auditorium.getPicture());
	        
	        
	        textAddress.setText(this.getString(R.string.auditorium_address_selected) 
	        		+ " " + address);
	}
}
