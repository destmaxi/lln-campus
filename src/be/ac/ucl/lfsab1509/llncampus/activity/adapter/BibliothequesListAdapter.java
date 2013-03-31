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
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.Bibliotheque;
import be.ac.ucl.lfsab1509.llncampus.R;

public class BibliothequesListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<Bibliotheque> list;

	public BibliothequesListAdapter(Context context,
			ArrayList<Bibliotheque> list) {
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

		Bibliotheque bib = list.get(position);
		name.setText(bib.getName());
		address.setText(bib.getAddress());
		Resources r = LLNCampus.getContext().getResources();
		if (bib.isOpen()) {
			openclose.setImageDrawable(LLNCampus.getContext().getResources()
					.getDrawable(android.R.drawable.presence_online));
		} else {
			openclose.setImageDrawable(LLNCampus.getContext().getResources()
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
