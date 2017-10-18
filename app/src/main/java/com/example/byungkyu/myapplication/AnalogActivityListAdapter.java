package com.example.byungkyu.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by USER on 2017-08-24.
 */

public class AnalogActivityListAdapter extends BaseAdapter {
    private ArrayList<AnalogActivityItem> aList = new ArrayList<AnalogActivityItem>();

    @Override
    public int getCount() {
        return aList.size();
    }

    @Override
    public Object getItem(int position) {
        return aList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.analog_list, parent, false);
        }

        AnalogActivityItem analogListItem = aList.get(position);

        TextView dataIdView = (TextView) convertView.findViewById(R.id.analog_data_id);
        TextView valueView = (TextView) convertView.findViewById(R.id.analog_data_value);
        ProgressBar progressBarView = (ProgressBar) convertView.findViewById(R.id.analog_data_progressBar);

        dataIdView.setText(analogListItem.getAnalogID());
        valueView.setText(analogListItem.getValue());
        progressBarView.setProgress((int) analogListItem.getProgressBar());

        return convertView;
    }

    public void addItem(String analogID, String value, double progressBar){
        AnalogActivityItem item = new AnalogActivityItem();

        item.setAnalogID(analogID);
        item.setValue(value);
        item.setProgressBar(progressBar);

        aList.add(item);
        notifyDataSetChanged();
    }

    public void deleteAllitem(){
        aList.clear();
        notifyDataSetChanged();
    }
}
