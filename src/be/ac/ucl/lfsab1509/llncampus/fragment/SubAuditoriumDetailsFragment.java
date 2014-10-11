package be.ac.ucl.lfsab1509.llncampus.fragment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
 * 
 * This class is intended to manage information shown about details on an subauditorium.
 * Related with the XML file subauditorium_list_fragment.xml or subauditorium_details_fragment.xml.
 * Note: a fragment is called by the XML file!
 *
 */
public class SubAuditoriumDetailsFragment extends LLNCampusFragment {

	/** URL to get the pictures of the subauditoriums. */
	private static final String URL_SUBPIC = "http://www.uclouvain.be/cps/ucl/doc/audi/images/";
	/** Name of the "unavailable" picture. */
	private static final String UNAVAILABLE = "non_disponible";
	
	private View viewer;
	private ImageView picture;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    viewer = inflater.inflate(R.layout.subauditorium_details, container, false);   
	    return viewer;
	}
	
	/**
	 * Return the translated "yes" String if value is true, else the translated "no" String.
	 * 
	 * @return The translated "yes" String if value is true, else the translated "no" String.
	 */
	private String booleanToString(boolean value)
	{
		return value ? this.getString(R.string.yes) : this.getString(R.string.no);
	}
	
	/**
	 * Update information shown by the layout about an ISubAuditorium.
	 * 
	 * @param subAuditorium
	 * 			The ISubAuditorium to show information about.
	 */
	public void updateSubAuditorium(ISubAuditorium subAuditorium){
		    String name = subAuditorium.getName();
		    boolean access = subAuditorium.hasAccess();
		    String places = String.valueOf(subAuditorium.getPlaces());
		    String network = booleanToString(subAuditorium.hasNetwork());
		    String screen = booleanToString(subAuditorium.hasScreen());
		    String retro = booleanToString(subAuditorium.hasRetro());
		    String slide = booleanToString(subAuditorium.hasSlide());
		    String video = subAuditorium.getVideo();
		    String sound = booleanToString(subAuditorium.hasSound());
		    String cabin = booleanToString(subAuditorium.hasCabin());
		    String furniture = subAuditorium.getFurniture();
		    
		    // If access is true, then shows the picture for disabled people, otherwise not.
		    picture = (ImageView) viewer.findViewById(R.id.subauditorium_picture);
	    	ImageView imageAccess = (ImageView) viewer.findViewById(R.id.access_picture);
	    	imageAccess.setVisibility(access ? View.VISIBLE : View.INVISIBLE);
		    
		    // Set all the text on the layout.
		    TextView textName = (TextView) viewer.findViewById(R.id.subauditorium_name);
		    textName.setText(name);
	        TextView textPlaces = (TextView) viewer.findViewById(R.id.nbplaces_value);
	        textPlaces.setText(places);
	        TextView textNetwork = (TextView) viewer.findViewById(R.id.network_value);
	        textNetwork.setText(network);
	        TextView textScreen = (TextView) viewer.findViewById(R.id.screen_value);
	        textScreen.setText(screen);
	        TextView textRetro = (TextView) viewer.findViewById(R.id.retro_value);
	        textRetro.setText(retro);
	        TextView textSlide = (TextView) viewer.findViewById(R.id.slide_value);
	        textSlide.setText(slide);
	        TextView textVideo = (TextView) viewer.findViewById(R.id.video_value);
	        textVideo.setText(getString(video == null ? R.string.nothing :
	        	video.compareTo("VF") == 0 ? R.string.video_projector :
	        		video.compareTo("VD") == 0 ? R.string.video_projector_data :
	        			video.compareTo("MD") == 0 ? R.string.monitor_data :
	        				video.compareTo("TV") == 0 ? R.string.television : R.string.nothing));

	        TextView textSound = (TextView) viewer.findViewById(R.id.sound_value);
	        textSound.setText(sound);
	        TextView textCabin = (TextView) viewer.findViewById(R.id.cabin_value);
	        textCabin.setText(cabin);
	        
	        TextView textFurniture = (TextView) viewer.findViewById(R.id.furniture_value);
	        textFurniture.setText(getString(furniture == null ? R.string.nothing :
	        	furniture.compareTo("T") == 0 ? R.string.table :
	        		furniture.compareTo("Tf") == 0 ? R.string.fixed_table :
	        			furniture.compareTo("G") == 0 ? R.string.bench : R.string.nothing));
	        
	        // Fetch the picture of the subauditorium on the device or on the Internet
	        picture.setImageResource(R.drawable.sandglass);
	        new PictureUtilityTask().execute(name);
	}
	
	/**
	 * This class is intended to fetch the picture of the SubAuditorium.
	 * First, it will go on the device, then on the Internet if not on the device.
	 * Note: all this will run on background!
	 */
	private class PictureUtilityTask extends AsyncTask<String, Void, Bitmap>{

		String nameExist = null;

		// In background.
		@Override
		protected Bitmap doInBackground(String... params) {
			return downloadPicture(parseToPictureName(params[0]));
		}

		/**
		 * Return name with lower cases and without any '.' or ' '.
		 * 
		 * @param name
		 * 			The name to parse. 
		 * @return Name with lower cases and without any '.' or ' '.
		 */
		private String parseToPictureName(String name)
		{
			String newName = name.toLowerCase(Locale.getDefault());
			String returnName = "";
			for (int i = 0; i < newName.length(); i++)
			{
				if ((newName.charAt(i) != '.') && (newName.charAt(i) != ' '))
				{
					returnName += newName.charAt(i);
				}
			}
			return returnName;
		}

		// After the background execution.
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) // If it was just created.
			{
				picture.setImageBitmap(bitmap);
			}
			else if (nameExist != null) // If already there.
			{
				picture.setImageDrawable(Drawable.createFromPath("/" 
						+ Environment.getExternalStorageDirectory().getPath() 
						+ "/" + LLNCampus.LLNREPOSITORY + "/" + nameExist));
			}
			else // If it doesn't exist.
			{
				/* 
				 * Launch again the process but with the "non_disponible" name.
				 * Either the picture is on the device, or it will download it and store it
				 * on the device.
				 */
				new PictureUtilityTask().execute(UNAVAILABLE);
			}
		}

		/**
		 *  Create a file on the device to write bitmap data downloaded.
		 * @param f
		 * 		The file object to store the copy.
		 * @param bitmap
		 * 		The Bitmap to copy.
		 */
		private void copyBitmap(File f, Bitmap bitmap)
		{
			// Convert bitmap to byte array.
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
			byte[] bitmapdata = bos.toByteArray();

			// Write the bytes in file.
			FileOutputStream fos;
			try {
				f.createNewFile();
				fos = new FileOutputStream(f);
				fos.write(bitmapdata);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Go download the picture with specified name (only if the picture is NOT on the device).
		 * @param name
		 * 		The name of the picture to download.
		 * @return The Bitmap found with name passed as argument.
		 */
		private Bitmap downloadPicture(String name) {
			nameExist = null;
			Bitmap bitmap = null;
			try {
				File f = new File("/" + Environment.getExternalStorageDirectory().getPath() + "/" 
						+ LLNCampus.LLNREPOSITORY + "/" + name);
				// Check if the file is already on the device.
				if (f.exists())
				{
					nameExist = name;
					return null;
				}
				// So, the picture is not on the device: go download it!
				URL urlImage = new URL(URL_SUBPIC + name + ".gif");
				HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
				// Take the InputStream and transform it to bitmap.
				InputStream inputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
				// Create the bitmap on the device.
				copyBitmap(f, bitmap);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
	}

}
