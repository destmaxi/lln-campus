package be.ac.ucl.lfsab1509.llncampus.activity.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.Auditorium;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.Bibliotheque;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;

/**
 * Class intended to create a listview of Auditoriums
 * Related with auditorium_list_item.xml
 *
 */
public class AuditoriumListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<IAuditorium> list;

	public AuditoriumListAdapter(Context context,
			ArrayList<IAuditorium> list) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.auditorium_list_item,
					parent, false);
		}

		TextView name = (TextView) convertView
				.findViewById(R.id.auditorium_item_name);
		TextView address = (TextView) convertView
				.findViewById(R.id.auditorium_item_address);
		ImageView picture = (ImageView) convertView
				.findViewById(R.id.auditorium_item_picture);

		IAuditorium aud = list.get(position);
		name.setText(aud.getName());
		address.setText(aud.getAddress());
		picture.setImageResource(aud.getImgMini());
		
		return convertView;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
