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

import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.ISubAuditorium;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	        // CODE MOBILIER
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
	        
	        String nameT = subauditorium.getName();
	        
	        picture.setImageResource(R.drawable.sablier);
	        new PictureUtilityTask().execute(nameT);
	        
	}
	
	public class PictureUtilityTask extends AsyncTask<String, Void, Bitmap>{


		    String nameExist = null;
		    
		@Override
		protected Bitmap doInBackground(String... params) {
			return downloadImage(parseToPicName(params[0]));
		}
		
		private String parseToPicName(String name)
		{
			String newName = name.toLowerCase();
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
		
		private void copyBitmap(File f, String name, Bitmap bitmap)
		{
			//create a file to write bitmap data

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private Bitmap downloadImage(String name) {
			
			nameExist = null;
			
			Log.d("Name", name);

			Bitmap bitmap = null;

			try {

			File f = new File("/" + Environment.getExternalStorageDirectory().getPath() + "/" + LLNCampus.LLNREPOSITORY + "/" + name);
			
			if (f.exists())
			{
				nameExist = name;
				return null;
			}
			
			Log.d("Fichier", "n'existe pas");
			
			URL urlImage = new URL(SUBPIC + name + ".gif");

			HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();

			InputStream inputStream = connection.getInputStream();

			bitmap = BitmapFactory.decodeStream(inputStream);
			
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
