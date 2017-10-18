package com.example.byungkyu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalogActivity extends AppCompatActivity implements View.OnClickListener, SocketActivity {

    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private String[] strings;
    NavigationView leftNavigation;
    NavigationView rightNavigation;
    private GraphListViewAdapter listAdapter;
    private  AnalogActivityListAdapter analogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog);

        CommunicationManager communicationManager = CommunicationManager.getInstance();
        communicationManager.setSocketActivity(this);
        communicationManager.sendMsg();

        drawerLayout = (DrawerLayout) findViewById(R.id.analog_layout);
        leftNavigation = (NavigationView) findViewById(R.id.nav_view_left);
        rightNavigation = (NavigationView) findViewById(R.id.nav_view_right);

        final Intent graphActivity = new Intent(this, BandGraphActivity.class);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(mToolbar);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.left_drawer);
        final ArrayList<MenuList> mMenu = setExpandableListData();
        ExpandableListViewAdapter expandableListAdapter = new ExpandableListViewAdapter(this, mMenu);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener(){
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch(groupPosition){
                    case 3:
                        startActivity(graphActivity);
                }
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch(groupPosition){
                    case 1:

                        break;
                    //Monitoring
                    case 2:
                        switch (childPosition){
                            //Analog In-Out
                            case 1:
                                break;
                        }
                        break;
                }

                return false;
            }
        });
        ListView listView = (ListView) findViewById(R.id.right_drawer);
        listAdapter = new GraphListViewAdapter();
        listView.setAdapter(listAdapter);
        setListData(listAdapter);

        ListView analogList = (ListView) findViewById(R.id.analogList);
        analogAdapter = new AnalogActivityListAdapter();
        analogList.setAdapter(analogAdapter);
        setAnalogListData(analogAdapter);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.main_menu:
                drawerLayout.openDrawer(leftNavigation);
                break;
            case R.id.saved_graph:
                drawerLayout.openDrawer(rightNavigation);
                break;
        }
    }
    private ArrayList<MenuList> setExpandableListData(){
        MenuList m1 = new MenuList("DX380LC-3");
        MenuList m2 = new MenuList("Home");
        MenuList m3 = new MenuList("Monitoring");
        MenuList m4 = new MenuList("Graph Output");

        m3.item.add("Digital In-Out");
        m3.item.add("Analog In-Out");

        ArrayList<MenuList> allMenu = new ArrayList();
        allMenu.add(m1);        allMenu.add(m2);
        allMenu.add(m3);        allMenu.add(m4);

        return allMenu;
    }
    private void setListData(GraphListViewAdapter listAdapter) {
        listAdapter.addItem("2017.08.21", 2);
        listAdapter.addItem("2017 08 21 / 15:57:10", "DX380LC-3", "000000");
        listAdapter.addItem("2017 08 21 / 15:57:10", "DX380LC-3", "000000");

        listAdapter.addItem("2017.08.22", 2);
        listAdapter.addItem("2017 08 22 / 15:57:10", "DX380LC-3", "000000");
        listAdapter.addItem("2017 08 22 / 15:57:10", "DX380LC-3", "000000");

        listAdapter.addItem("2017.08.22", 2);
        listAdapter.addItem("2017 08 22 / 15:57:10", "DX380LC-3", "000000");
        listAdapter.addItem("2017 08 22 / 15:57:10", "DX380LC-3", "000000");

        listAdapter.addItem("2017.08.23", 2);
        listAdapter.addItem("2017 08 23 / 15:57:10", "DX140W-ACE", "000000");
        listAdapter.addItem("2017 08 23 / 17:40:10", "DX140W-ACE", "000000");
    }

    private void setAnalogListData(AnalogActivityListAdapter listAdapter){
        listAdapter.addItem("Engine Speed","0",0);
        listAdapter.addItem("Engine Oil Pressure","0",0);
        listAdapter.addItem("Engine Oil Temperature","0",0);
        listAdapter.addItem("프론트 펌프 압력","0",0);
        listAdapter.addItem("리어 펌프 압력","0",0);
        listAdapter.addItem("펌프 1 파일롯 압력","0",0);
        listAdapter.addItem("펌프 2 파일롯 압력","0",0);
        listAdapter.addItem("펌프 밸브1","0",0);
        listAdapter.addItem("펌프 밸브2","0",0);
        listAdapter.addItem("쿨링 팬 밸브","0",0);
        listAdapter.addItem("유량제어 밸브","0",0);
    }

    @Override
    public void receiveMsg(final HashMap<Byte, Object> dataSet) {
        if (dataSet.containsKey(Data.ANALOG)) {
            Message msg= Message.obtain();
            msg.what=Data.ANALOG;
            strings = (String[]) dataSet.get(Data.ANALOG);
            msg.obj=strings;
            Log.i("그래프", strings[0]);
            msgHandler.sendMessage(msg);
        }
    }

    @Override
    public void receiveConnect() {

    }

    Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            strings= (String[])msg.obj;
            CommunicationManager.analogReceived=true;
            analogAdapter.deleteAllitem();
            analogAdapter.addItem("Engine Speed",(int)Double.parseDouble(strings[0])+"",100*Double.parseDouble(strings[0])/65535);
            analogAdapter.addItem("Engine Oil Pressure",(int)Double.parseDouble(strings[1])+"",100*Double.parseDouble(strings[1])/65535);
            analogAdapter.addItem("Engine Oil Temperature",(int)Double.parseDouble(strings[2])+"",100*Double.parseDouble(strings[2])/65535);
            analogAdapter.addItem("프론트 펌프 압력",(int)Double.parseDouble(strings[3])+"",100*Double.parseDouble(strings[3])/65535);
            analogAdapter.addItem("리어 펌프 압력",(int)Double.parseDouble(strings[4])+"",100*Double.parseDouble(strings[4])/65535);
            analogAdapter.addItem("펌프 1 파일롯 압력",(int)Double.parseDouble(strings[5])+"",100*Double.parseDouble(strings[5])/65535);
            analogAdapter.addItem("펌프 2 파일롯 압력",(int)Double.parseDouble(strings[6])+"",100*Double.parseDouble(strings[6])/65535);
            analogAdapter.addItem("펌프 밸브1",(int)Double.parseDouble(strings[7])+"",100*Double.parseDouble(strings[7])/65535);
            analogAdapter.addItem("펌프 밸브2",(int)Double.parseDouble(strings[8])+"",100*Double.parseDouble(strings[8])/65535);
            analogAdapter.addItem("쿨링 팬 밸브",(int)Double.parseDouble(strings[9])+"",100*Double.parseDouble(strings[9])/65535);
            analogAdapter.addItem("유량제어 밸브",(int)Double.parseDouble(strings[10])+"",100*Double.parseDouble(strings[10])/65535);





        }
    };
    protected void onDestroy(){
        super.onDestroy();
        CommunicationManager.mainReceived=false;
        CommunicationManager.analogReceived=false;
        getDelegate().onDestroy();

    }
}
