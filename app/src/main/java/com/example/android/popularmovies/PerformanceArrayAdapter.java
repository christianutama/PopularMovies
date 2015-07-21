package com.example.android.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Christian Utama on 7/16/2015.
 */
public class PerformanceArrayAdapter extends ArrayAdapter<String> {
    private Activity mContext;
    private String[] values;

    public PerformanceArrayAdapter(Activity context, String[] values){
        super(context, R.layout.trailer_list_layout, values);
        mContext = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            listView = inflater.inflate(R.layout.trailer_list_layout, null);
        }
        TextView link = (TextView) listView.findViewById(R.id.trailer_text_view);
        link.setText(values[position]);
        return listView;
    }
}
