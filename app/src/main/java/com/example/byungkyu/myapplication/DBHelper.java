package com.example.byungkyu.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by YJ on 2017-08-04.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "gusto.db";
    private static final int DATABASE_VERSION = 1;
    public static HashMap<String, String[]> ceiMap;
    public static HashMap<String, String[]> analogMap;
    public static HashMap<String, String[]> fuelMap;
    public static HashMap<String, String[]> operationTimeMap;
    private String[][] info;

    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        analogMap = new HashMap<String, String[]>();
        fuelMap = new HashMap<String, String[]>();
        ceiMap = new HashMap<String, String[]>();
        info = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists positive_response_tb(_id integer primary key autoincrement,ID byte, CONTENT text);");
        db.execSQL("create table if not exists negative_response_tb(_id integer primary key autoincrement,ID byte, CONTENT text);");
        db.execSQL("create table if not exists local_data_group_tb(_id integer primary key autoincrement,ID byte, CONTENT text);");
        //아날로그 테이블은 check 함수를 적용하여 아니면 exception 이 생기게 해야한다.
        db.execSQL("create table if not exists analog_tb(_id integer primary key autoincrement,ID byte, CONTENT text, UNITVALUE real, UNIT text);");
        //연료 사용정보 역시 check 함수를 사용해야할 것 같다.
        db.execSQL("create table if not exists fuel_use_tb(_id integer primary key autoincrement,ID byte, CONTENT text, UNITVALUE real, UNIT text);");
        //가동시간 사용정보 역시 check 함수를 사용해야할 것 같다.
        db.execSQL("create table if not exists operation_time_tb(_id integer primary key autoincrement,ID byte, CONTENT text, UNITVALUE real, UNIT text);");
        //필터오일 사용정보 역시 check 함수를 사용해야할 것 같다.
        db.execSQL("create table if not exists filter_oil_use_tb(_id integer primary key autoincrement,ID byte, CONTENT text, UNITVALUE real, UNIT text);");
        //필터오일 교환 주기정보 역시 check 함수를 사용해야할 것 같다.
        db.execSQL("create table if not exists filter_change_period_tb(_id integer primary key autoincrement,ID byte, CONTENT text, UNITVALUE real, UNIT text);");
        //오류 고장 정보
        db.execSQL("create table if not exists fault_code_list_tb(_id integer primary key autoincrement,ID text, CONTENT_KR text, CONTENT_ER text, FCL_INDEX byte, FMI byte);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exits fault_code_list_tb");
        System.out.println("삭제");
        onCreate(db);
    }

    public void selectCurrentErrorInfo(SQLiteDatabase db){
        Cursor cursor;
        cursor = db.rawQuery("select count(*) from fault_code_list_tb;",null);
        int count = 0;
        while(cursor.moveToNext()){
            count = cursor.getInt(0);
            Log.i("개수",""+count);
        }
        info = new String[count][3];
        int index = 0;
        cursor  = db.rawQuery("select * from fault_code_list_tb;",null);
         while(cursor.moveToNext()){
            String ID = cursor.getString(1);
            String CONTENT_KR = cursor.getString(2);
            String CONTENT_ER = cursor.getString(3);
            int FCL_INDEX = cursor.getInt(4);
            int FMI = cursor.getInt(5);

            info[index][0] = ID;
            info[index][1] = CONTENT_KR;
            info[index][2] = CONTENT_ER;

            //System.out.println("ID :" + ID + ",KR : " + CONTENT_KR + ",ER : " +CONTENT_ER + ",INDEX : " +FCL_INDEX+ "FMI : " + FMI);
             Log.d("DB 찾자!","ID :" + ID + ",KR : " + CONTENT_KR + ",ER : " +CONTENT_ER + ",INDEX : " +FCL_INDEX+ "FMI : " + FMI);
             ceiMap.put(FCL_INDEX+""+FMI,info[index]);
             index++;
        }
    }

    public void selectAnalog(SQLiteDatabase db){
        Cursor cursor;
        cursor = db.rawQuery("select count(*) from analog_tb;",null);
        int count = 0;
        while(cursor.moveToNext()){

            count = cursor.getInt(0);
            Log.i("개수",""+count);
        }
        info = new String[count][3];
        int index = 0;
        cursor  = db.rawQuery("select * from analog_tb;",null);
        System.out.println(cursor);
        while(cursor.moveToNext()){

            int ID = cursor.getInt(1);
            String CONTENT = cursor.getString(2);
            double unitValue = cursor.getDouble(3);
            String unit = cursor.getString(4);
            info[index][0] = CONTENT;
            info[index][1] = unitValue+"";
            info[index][2] = unit+"";

            System.out.println("ID :" + ID + ",Content : " + CONTENT + ",unitValue : " +unitValue + ",unit : " +unit);
            analogMap.put(ID+"",info[index]);
            index++;
        }
    }

    public void selectOperationTime(SQLiteDatabase db){
        Cursor cursor;
        cursor = db.rawQuery("select count(*) from operation_time_tb;",null);
        int count = 0;
        while(cursor.moveToNext()){

            count = cursor.getInt(0);
            Log.i("개수",""+count);
        }
        info = new String[count][3];
        int index = 0;
        cursor  = db.rawQuery("select * from operation_time_tb;",null);
        System.out.println(cursor);
        while(cursor.moveToNext()){

            int ID = cursor.getInt(1);
            String CONTENT = cursor.getString(2);
            double unitValue = cursor.getDouble(3);
            String unit = cursor.getString(4);
            info[index][0] = CONTENT;
            info[index][1] = unitValue+"";
            info[index][2] = unit+"";

            System.out.println("ID :" + ID + ",Content : " + CONTENT + ",unitValue : " +unitValue + ",unit : " +unit);
            operationTimeMap.put(index+"",info[index]);
            index++;
        }
    }

    public void selectFuelUseInfo(SQLiteDatabase db){
        Cursor cursor;
        cursor = db.rawQuery("select count(*) from fuel_use_tb;",null);
        int count = 0;
        while(cursor.moveToNext()){

            count = cursor.getInt(0);
            Log.i("개수",""+count);
        }
        info = new String[count][3];
        int index = 0;
        cursor  = db.rawQuery("select * from fuel_use_tb;",null);
        System.out.println(cursor);
        while(cursor.moveToNext()){

            int ID = cursor.getInt(1);
            String CONTENT = cursor.getString(2);
            double unitValue = cursor.getDouble(3);
            String unit = cursor.getString(4);
            info[index][0] = CONTENT;
            info[index][1] = unitValue+"";
            info[index][2] = unit+"";

            System.out.println("ID :" + ID + ",Content : " + CONTENT + ",unitValue : " +unitValue + ",unit : " +unit);
            fuelMap.put(index+"",info[index]);
            index++;
        }
    }
}
