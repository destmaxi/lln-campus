package be.ac.ucl.lfsab1509.llncampus.activity.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.R;
import be.ac.ucl.lfsab1509.llncampus.interfaces.IAuditorium;

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
 * Class intended to create a list view of IAuditoriums.
 * Related with auditorium_list_item.xml.
 */
public class AuditoriumListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<IAuditorium> list;

	/**
	 * Constructor.
	 * @param context
	 * 			Context to find the LayoutInflater.
	 * @param list
	 * 			The list of IAuditoriums.
	 */
	public AuditoriumListAdapter(Context context, ArrayList<IAuditorium> list) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.auditorium_list_item, parent, false);
		}

		TextView name = (TextView) convertView.findViewById(R.id.auditorium_item_name);
		TextView address = (TextView) convertView.findViewById(R.id.auditorium_item_address);
		ImageView picture = (ImageView) convertView.findViewById(R.id.auditorium_item_picture);

		IAuditorium aud = list.get(position);
		name.setText(aud.getName());
		address.setText(aud.getAddress());
		picture.setImageResource(aud.getMiniPicture());
		
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
