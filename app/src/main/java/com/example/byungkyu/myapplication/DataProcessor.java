package com.example.byungkyu.myapplication;

import java.util.HashMap;

/**
 * Created by YJ on 2017-08-16.
 */

public interface DataProcessor {
    void processingMsg(HashMap<Byte, Object> dataSet);
}
