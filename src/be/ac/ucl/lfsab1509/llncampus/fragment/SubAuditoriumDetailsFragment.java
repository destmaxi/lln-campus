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
 * This class is intended to manage information shown about details
 * of an subauditorium selected.
 * Related with the xml file subauditorium_list_fragment.xml or subauditorium_details_fragment.xml
 * Note: a fragment is called by the xml file!
 *
 */
public class SubAuditoriumDetailsFragment extends LLNCampusFragment {

	private View viewer;
	private static String SUBPIC = "http://www.uclouvain.be/cps/ucl/doc/audi/images/";
	private ImageView picture;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    viewer = inflater.inflate(R.layout.subauditorium_details, container, false);   
	    return viewer;
	}
	
	/**
	 * Retourne oui si value == true, non si false (le tout dans la bonne langue)
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
	
	/**
	 * Update information shown about an ISubAuditorium
	 * @pre subAuditorium is not null
	 * @post the layout shows information about subAuditorium
	 * @param subAuditorium
	 */
	public void updateSubAuditorium(ISubAuditorium subAuditorium){
		    String name = subAuditorium.getName();
		    boolean access = subAuditorium.hasAccess();
		    String places = String.valueOf(subAuditorium.getNbPlaces());
		    String network = ouiNon(subAuditorium.hasNetwork());
		    String ecran = ouiNon(subAuditorium.hasEcran());
		    String retro = ouiNon(subAuditorium.hasRetro());
		    String dia = ouiNon(subAuditorium.hasDia());
		    String video = subAuditorium.getVideo();
		    String sono = ouiNon(subAuditorium.hasSono());
		    String cabine = ouiNon(subAuditorium.hasCabine());
		    String mobilier = subAuditorium.getMobilier();
		    
		    // If access is true, then shows the picture for disabled people, otherwise not
		    picture = (ImageView) viewer.findViewById(R.id.subauditorium_picture);
	    	ImageView imageAccess = (ImageView) viewer.findViewById(R.id.access_picture);
		    if (access)
		    {
		    	imageAccess.setVisibility(View.VISIBLE);
		    }
		    else
		    {
		    	imageAccess.setVisibility(View.INVISIBLE);
		    }
		    
		    // Set all the text on the layout
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
	        if (video == null)
	        	textVideo.setText(getString(R.string.rien));
	        else {
	        	if (video.compareTo("VF") == 0)
	        	{
	        		textVideo.setText(getString(R.string.video_projecteur));
	        	}
	        	
	        	else if (video.compareTo("VD") == 0)
	        	{
	        		textVideo.setText(getString(R.string.video_projecteur_data));
	        	}
	        	
	        	else if (video.compareTo("MD") == 0)
	        	{
	        		textVideo.setText(getString(R.string.moniteur_data));
	        	}
	        	
	        	else if (video.compareTo("TV") == 0)
	        	{
	        		textVideo.setText(getString(R.string.tele));
	        	}
	        	
	        	else
	        	{
	        		textVideo.setText(getString(R.string.rien));
	        	}
	        }
	        TextView textSono = (TextView) viewer.findViewById(R.id.sono_rep);
	        textSono.setText(sono);
	        
	        TextView textCabine = (TextView) viewer.findViewById(R.id.cabine_rep);
	        textCabine.setText(cabine);
	        
	        TextView textMobilier = (TextView) viewer.findViewById(R.id.mobilier_rep);
	      
	        if (mobilier == null)
	        	textMobilier.setText(getString(R.string.rien));
	        else {
	        	if (mobilier.compareTo("T") == 0)
	        	{
	        		textMobilier.setText(getString(R.string.table));
	        	}
	        	
	        	else if (mobilier.compareTo("Tf") == 0)
	        	{
	        		textMobilier.setText(getString(R.string.table_fixe));
	        	}
	        	
	        	else if (mobilier.compareTo("G") == 0)
	        	{
	        		textMobilier.setText(getString(R.string.gradin));
	        	}
	        	
	        	else
	        	{
	        		textMobilier.setText(getString(R.string.rien));
	        	}
	        }
	        
	        String nameT = subAuditorium.getName();
	        
	        // Fetch the picture of the subauditorium on the device or on the Internet
	        picture.setImageResource(R.drawable.sablier);
	        new PictureUtilityTask().execute(nameT);
	        
	}
	
	/**
	 * This class is intended to fetch the picture of the subauditorium.
	 * First, it will go on the device, then on the Internet if not on the device.
	 * Note: all this will run on background!
	 *
	 */
	public class PictureUtilityTask extends AsyncTask<String, Void, Bitmap>{

		String nameExist = null;

		// In background
		@Override
		protected Bitmap doInBackground(String... params) {
			return downloadImage(parseToPicName(params[0]));
		}

		/**
		 * Return name with lower cases and without any '.' or ' '
		 * @pre name != null
		 * @post name is unchanged
		 * @param name
		 * @return name with lower cases and without any '.' or ' '
		 */
		private String parseToPicName(String name)
		{
			String newName = name.toLowerCase(Locale.getDefault());
			String returnName = null;
			for (int i = 0; i < newName.length(); i++)
			{
				if ((newName.charAt(i) != '.') && (newName.charAt(i) != ' '))
				{
					if (returnName == null)
					{
						returnName = "" + newName.charAt(i);
					}
					else
					{
						returnName = returnName + "" + newName.charAt(i);
					}
				}
			}
			return returnName;
		}

		// After the background execution
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) // Si vient d'etre cree
			{
				picture.setImageBitmap(bitmap);
			}
			else if (nameExist != null) // Si existait deja
			{
				picture.setImageDrawable(Drawable.createFromPath("/" + Environment.getExternalStorageDirectory().getPath() + "/" + LLNCampus.LLNREPOSITORY + "/" + nameExist));
			}
			else // Si n'existe pas
			{
				/* Relance la procedure mais avec le nom "non_disponible".
				 * Soit on a l'image en memoire, et on va la chercher, soit on
				 * la telecharge tout en la copiant en memoire
				 */
				new PictureUtilityTask().execute("non_disponible");
			}
		}

		//create a file on the device to write bitmap data downloaded
		private void copyBitmap(File f, String name, Bitmap bitmap)
		{

			//Convert bitmap to byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
			byte[] bitmapdata = bos.toByteArray();

			//write the bytes in file
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

		// Go download the picture (only if the picture is NOT on the device)
		private Bitmap downloadImage(String name) {
			nameExist = null;
			Bitmap bitmap = null;
			try {
				File f = new File("/" + Environment.getExternalStorageDirectory().getPath() + "/" + LLNCampus.LLNREPOSITORY + "/" + name);
				// Check if the file is already on the device
				if (f.exists())
				{
					nameExist = name;
					return null;
				}
				// So, the picture is not on the device: go download!
				URL urlImage = new URL(SUBPIC + name + ".gif");
				HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
				// Take the inputstream and transform it to bitmap
				InputStream inputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
				// Create the bitmap on the device
				copyBitmap(f, name, bitmap);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
	}

}
