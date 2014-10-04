package be.ac.ucl.lfsab1509.llncampus.activity.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.Library;
import be.ac.ucl.lfsab1509.llncampus.R;

/**
 * LLNCampus. A application for students at the UCL (Belgium).
    Copyright (C) 2013 Benjamin Baugnies, Quentin De Coninck, Ahn Tuan Le Pham and Damien Mercier

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
 * Class intended to create a listview of Bibliotheque
 * Related with bibliotheque_list_item.xml
 */
public class LibraryListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<Library> list;

	public LibraryListAdapter(Context context,
			ArrayList<Library> list) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.bibliotheque_list_item,
					parent, false);
		}

		TextView name = (TextView) convertView
				.findViewById(R.id.bibliotheque_item_name);
		TextView address = (TextView) convertView
				.findViewById(R.id.bibliotheque_item_address);
		ImageView openclose = (ImageView) convertView
				.findViewById(R.id.bibliotheque_item_openclose);

		Library bib = list.get(position);
		name.setText(bib.getName());
		address.setText(bib.getAddress());
		Resources r = LLNCampus.getContext().getResources();
		if (bib.isOpen()) {
			openclose.setImageDrawable(r
					.getDrawable(android.R.drawable.presence_online));
		} else {
			openclose.setImageDrawable(r
					.getDrawable(android.R.drawable.presence_busy));
		}

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
