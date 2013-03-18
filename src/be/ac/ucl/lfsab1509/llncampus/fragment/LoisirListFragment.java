package be.ac.ucl.lfsab1509.llncampus.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LoisirListFragment extends LLNCampusListFragment {
	
	ArrayList<String> values = null;
	private ArrayAdapter<String> adapter;
	private OnCategorySelectedListener catSelectedListener;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.values = new ArrayList<String>();
		values.add("Cinema");
		values.add("Sports");
		values.add("Restaurants");
		values.add("Kaps");

		adapter=new ArrayAdapter<String>(
	            this.getActivity(),android.R.layout.simple_list_item_1, values){

	        @Override
	        public View getView(int position, View convertView,
	                ViewGroup parent) {
	            View view =super.getView(position, convertView, parent);

	            TextView textView=(TextView) view.findViewById(android.R.id.text1);
	            textView.setTextColor(Color.WHITE);

	            return view;
	        }
	    };
		
        setListAdapter(adapter);
        setHasOptionsMenu(true);
	} // end onCreate
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            catSelectedListener = (OnCategorySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCategorySelectedListener");
        }
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	    String content = values.get(position);

	    catSelectedListener.onCategorySelected(content);
	}
	
	
	public interface OnCategorySelectedListener {
	    public void onCategorySelected(String cat);
	}

}
