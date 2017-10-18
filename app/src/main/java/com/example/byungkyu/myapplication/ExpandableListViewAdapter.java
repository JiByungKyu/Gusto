package com.example.byungkyu.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by USER on 2017-08-16.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<MenuList> mList;
    private LayoutInflater inflater;

    public ExpandableListViewAdapter(Context context, ArrayList<MenuList> mList){
        this.mContext = context;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return  mList.get(groupPosition).item.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mList.get(groupPosition).item.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.nav_left_parent,null);
        }

        MenuList mMenu = (MenuList)getGroup(groupPosition);

        String menuName = mMenu.function;

        TextView textView = (TextView) convertView.findViewById(R.id.parentText);
        textView.setText(menuName);

        convertView.setBackgroundColor(Color.WHITE);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.nav_left_child, null);
        }
        String child = (String) getChild(groupPosition, childPosition);
        TextView name = (TextView) convertView.findViewById(R.id.childText);

        name.setText(child);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
