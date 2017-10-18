package com.example.byungkyu.myapplication;

import java.util.HashMap;

/**
 * Created by YJ on 2017-08-10.
 */

public interface SocketActivity {
    public void receiveMsg(HashMap<Byte, Object> dataSet);
    public void receiveConnect();
}
