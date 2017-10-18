package com.example.byungkyu.myapplication;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by YJ on 2017-09-26.
 */

public class GraphDataProcess implements DataProcessor {
    private static GraphDataProcess graphDataProcess;

    private String[] analogParsedData;
    private HashMap<Byte,Object> processedData;

    private GraphDataProcess(){
        analogParsedData = null;
    }

    public static GraphDataProcess getInstance(){
        if(graphDataProcess == null){
            graphDataProcess = new GraphDataProcess();
        }
        return graphDataProcess;
    }

    public void analogDataProcessing(int[] analogParsedData) {
        if(analogParsedData == null || analogParsedData.length == 0){

        }
        this.analogParsedData = new String[analogParsedData.length];


        this.analogParsedData[0] = (Double.valueOf(DBHelper.analogMap.get(0x00+"")[1]) * analogParsedData[0])+"";
        this.analogParsedData[1] = (Double.valueOf(DBHelper.analogMap.get(0x01+"")[1]) * analogParsedData[1])+"";
        this.analogParsedData[2] = (Double.valueOf(DBHelper.analogMap.get(0x02+"")[1]) * analogParsedData[2])+"";
        this.analogParsedData[3] = (Double.valueOf(DBHelper.analogMap.get(0x03+"")[1]) * analogParsedData[3])+"";
        this.analogParsedData[4] = (Double.valueOf(DBHelper.analogMap.get(0x04+"")[1]) * analogParsedData[4])+"";
        this.analogParsedData[5] = (Double.valueOf(DBHelper.analogMap.get(0x05+"")[1]) * analogParsedData[5])+"";
        this.analogParsedData[6] = (Double.valueOf(DBHelper.analogMap.get(0x06+"")[1]) * analogParsedData[6])+"";
        this.analogParsedData[7] = (Double.valueOf(DBHelper.analogMap.get(0x07+"")[1]) * analogParsedData[7])+"";
        this.analogParsedData[8] = (Double.valueOf(DBHelper.analogMap.get(0x08+"")[1]) * analogParsedData[8])+"";
        this.analogParsedData[9] = (Double.valueOf(DBHelper.analogMap.get(0x09+"")[1]) * analogParsedData[9])+"";
        this.analogParsedData[10] = (Double.valueOf(DBHelper.analogMap.get(0x010+"")[1]) * analogParsedData[10])+"";

        Log.i("아날로그","왓어");
        processedData.put(Data.ANALOG,this.analogParsedData);
    }

    @Override
    public void processingMsg(HashMap<Byte, Object> dataSet) {
        if(dataSet == null){
            Log.i("오류", "겠찌");
        }
        processedData = new HashMap<Byte, Object>();

        if(dataSet.containsKey(Data.ANALOG)) {
            analogDataProcessing((int[]) dataSet.get(Data.ANALOG));
        }
        Log.i("그래프로","갔음");
        CommunicationManager.socketActivity.receiveMsg(this.processedData);
    }
}