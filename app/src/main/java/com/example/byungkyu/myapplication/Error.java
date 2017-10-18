package com.example.byungkyu.myapplication;

import android.util.Log;

/**
 * Created by ByungKyu on 2017-09-21.
 */

public class Error {

    public static void communication(int type) {
            switch (type){
                case Data.checksum:
                    break;
                case Data.byteNull:
                    Log.d("아직", "오지 않음");
                    break;

            }
    }
}
