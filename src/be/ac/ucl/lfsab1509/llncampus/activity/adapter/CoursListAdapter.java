package be.ac.ucl.lfsab1509.llncampus.activity.adapter;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.llncampus.Cours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CoursListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
 
    private ArrayList<Cours> list;
 
    public CoursListAdapter(Context context, ArrayList<Cours> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(android.R.id.text1);
        TextView sub = (TextView) convertView.findViewById(android.R.id.text2);
 
        Cours info = list.get(position);
        title.setText(info.coursCode);
        sub.setText(info.coursName);
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