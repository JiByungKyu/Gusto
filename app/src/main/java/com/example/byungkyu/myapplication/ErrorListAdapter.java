package com.example.byungkyu.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by USER on 2017-08-28.
 */

public class ErrorListAdapter extends BaseAdapter {
    private ArrayList<ErrorListItem> eList = new ArrayList<ErrorListItem>();

    @Override
    public int getCount() {
        return eList.size();
    }

    @Override
    public Object getItem(int position) {
        return eList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            convertView = inflater.inflate(R.layout.error_list, parent, false);
        }

        ErrorListItem errorListItem = eList.get(position);

        TextView errorIDTextView = (TextView) convertView.findViewById(R.id.error_id) ;
        TextView errorContentTextView = (TextView) convertView.findViewById(R.id.error_content) ;

        errorIDTextView.setText(errorListItem.getErrorID());
        errorContentTextView.setText(errorListItem.getErrorContent());

        return convertView;
    }

    public void addItem(String ID, String content){
        ErrorListItem item = new ErrorListItem();

        item.setErrorID(ID);
        item.setErrorContent(content);
        Log.d("하...",ID);
        eList.add(item);
        this.notifyDataSetChanged();
    }
    public void deleteAllItem(){
        eList.clear();

    }
}
