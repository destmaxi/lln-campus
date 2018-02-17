package be.ac.ucl.lfsab1509.llncampus.activity.adapter;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Class intended to make a list view of Events.
 * Related with event_list_item.xml.
 * */
public class EventListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
 
    private ArrayList<Event> list;
 
    /**
	 * Constructor.
	 * @param context
	 * 			Context to find the LayoutInflater.
	 * @param list
	 * 			The list of Events.
	 */
    public EventListAdapter(Context context, ArrayList<Event> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_list_item, parent, false);
        }
        TextView time = (TextView) convertView.findViewById(R.id.course_time);
        TextView room = (TextView) convertView.findViewById(R.id.course_room);
        TextView code = (TextView) convertView.findViewById(R.id.course_code_edit);
        TextView title = (TextView) convertView.findViewById(R.id.course_title);        
 
        Event info = list.get(position);
        time.setText(info.getTime());
        room.setText(info.getDetail(Event.ROOM));
        code.setText(info.getDetail(Event.COURSE));
       	title.setText(info.getDetail(Event.TITLE));
       	int textColor;
       	Resources res = LLNCampus.getContext().getResources();
       	// Different blues for even and odd lines.
       	if (position % 2 == 1) {
       		convertView.findViewById(R.id.event_list_item).setBackgroundColor(res
       				.getColor(android.R.color.holo_blue_light));
       		textColor = res.getColor(android.R.color.primary_text_dark);
       	} else {
       		convertView.findViewById(R.id.event_list_item).setBackgroundColor(res.
       					getColor(android.R.color.holo_blue_dark));
       		textColor = res.getColor(android.R.color.primary_text_dark);
       	}
       	time.setTextColor(textColor);
       	room.setTextColor(textColor);
       	code.setTextColor(textColor);
       	title.setTextColor(textColor);
        
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