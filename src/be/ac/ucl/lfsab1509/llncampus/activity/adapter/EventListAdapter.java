package be.ac.ucl.lfsab1509.llncampus.activity.adapter;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.Event;
import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
 
    private ArrayList<Event> list;
 
    public EventListAdapter(Context context, ArrayList<Event> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_list_item, parent, false);
        }
        TextView time = (TextView) convertView.findViewById(R.id.cours_time);
        TextView room = (TextView) convertView.findViewById(R.id.cours_room);
        TextView code = (TextView) convertView.findViewById(R.id.cours_code);
        TextView title = (TextView) convertView.findViewById(R.id.cours_title);        
 
        Event info = list.get(position);
        time.setText(info.getTime());
        room.setText(info.getDetail("room"));
        code.setText(info.getDetail("course"));
       	title.setText(info.getDetail("title"));
       	int txtColor;
       	if (position % 2 == 1) {
       		convertView.findViewById(R.id.event_list_item).setBackgroundColor(LLNCampus.getContext().getResources().getColor(android.R.color.holo_blue_light));
       		txtColor = LLNCampus.getContext().getResources().getColor(android.R.color.primary_text_dark);
       	} else {
       		convertView.findViewById(R.id.event_list_item).setBackgroundColor(LLNCampus.getContext().getResources().getColor(android.R.color.holo_blue_dark));
       		txtColor = LLNCampus.getContext().getResources().getColor(android.R.color.primary_text_dark);
       	}
       	time.setTextColor(txtColor);
       	room.setTextColor(txtColor);
       	code.setTextColor(txtColor);
       	title.setTextColor(txtColor);
        
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