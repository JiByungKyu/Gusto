package com.example.byungkyu.myapplication;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by YJ on 2017-08-14.
 */

public class MainDataProcess implements DataProcessor {
    private static MainDataProcess mainDataProcess;

    private String[] analogParsedData;
    private String[] fuelUseInfoParsedData;
    private String[] operationTimeParsedData;
    private String[][] currentErrorInfoParsedData;
    private HashMap<Byte,Object> processedData;

    private MainDataProcess(){
        analogParsedData = null;
        fuelUseInfoParsedData = null;
        currentErrorInfoParsedData = null;
        operationTimeParsedData = null;
        processedData = null;
    }

    public static MainDataProcess getInstance(){
        if(mainDataProcess == null){
            mainDataProcess = new MainDataProcess();
        }
        return mainDataProcess;
    }

    public void analogDataProcessing(int[] analogParsedData) {
        if(analogParsedData == null || analogParsedData.length == 0){
            Log.i("아날로그","null"+this.analogParsedData[0]+" : "+this.analogParsedData[1]);
            processedData.put(Data.ANALOG,null);
        }
        else {
            this.analogParsedData = new String[analogParsedData.length];
            this.analogParsedData[0] = (Double.valueOf(DBHelper.analogMap.get(0x03 + "")[1]) * analogParsedData[0]) + "";
            this.analogParsedData[1] = (Double.valueOf(DBHelper.analogMap.get(0x04 + "")[1]) * analogParsedData[1]) + "";
            Log.i("아날로그","왓어"+this.analogParsedData[0]+" : "+this.analogParsedData[1]);
        }
        processedData.put(Data.ANALOG,this.analogParsedData);
    }
    public void  fuelUseInfoDataProcessing(int[] fuelParsedData){
        if(fuelParsedData == null || fuelParsedData.length == 0){

        }
        this.fuelUseInfoParsedData = new String[fuelParsedData.length];
        for(int i = 0; i < fuelParsedData.length; i++){
            this.fuelUseInfoParsedData[i] = ((Double.valueOf(DBHelper.fuelMap.get(i+"")[1])).doubleValue() * fuelParsedData[i]+"ℓ");
        }

        processedData.put(Data.FUEL_USE_INFO,this.fuelUseInfoParsedData);
    }
    public void currentErrorInfoProcessing(int[][] parsingData){
        if(parsingData == null){
            processedData.put(Data.CURRENT_ERROR_INFO,null);
        }
        else {
            this.currentErrorInfoParsedData = new String[parsingData.length][3];

            //해당 값 투입
            for (int i = 0; i < parsingData.length; i++) {
                this.currentErrorInfoParsedData[i] = DBHelper.ceiMap.get(parsingData[i][0] + "" + parsingData[i][1]);
                for (String s[]:this.currentErrorInfoParsedData) {
                    for(String t:s){
                        Log.d("fault 값: ",t+"목록 개수:"+parsingData.length);
                    }
                }
            }

            processedData.put(Data.CURRENT_ERROR_INFO, this.currentErrorInfoParsedData);
            //디비에 저장할것
        }
    }
    public void operationTimeProcessing(int[] operationTimeParsedData){
        if(operationTimeParsedData == null || operationTimeParsedData.length == 0){

        }
        int hour = 0;
        int min = 0;
        this.operationTimeParsedData = new String[operationTimeParsedData.length];

        for(int i = 0; i < operationTimeParsedData.length; i++){
            hour = operationTimeParsedData[i]/60;
            min = operationTimeParsedData[i] - (hour * 60);
            this.operationTimeParsedData[i] = (hour + "h " + min + "m") ;

        }

        processedData.put(Data.OPERATION_TIME,this.operationTimeParsedData);
    }
    @Override
    public void processingMsg(HashMap<Byte, Object> dataSet) {
        if(dataSet == null){
            Log.i("오류", "겠찌");
        }
        processedData = new HashMap<Byte, Object>();

        if(dataSet.containsKey(Data.CURRENT_ERROR_INFO)){
            if(dataSet.get(Data.CURRENT_ERROR_INFO)!=null) {
                currentErrorInfoProcessing((int[][]) dataSet.get(Data.CURRENT_ERROR_INFO));
                Log.d("고장","핸들러로 전송");
            }
            else
                currentErrorInfoProcessing(null);
        }else if(dataSet.containsKey(Data.ANALOG)){
            analogDataProcessing((int[])dataSet.get(Data.ANALOG));
        }else if(dataSet.containsKey(Data.FUEL_USE_INFO)){
            fuelUseInfoDataProcessing((int[])dataSet.get(Data.FUEL_USE_INFO));
        }else if(dataSet.containsKey(Data.OPERATION_TIME)){
            operationTimeProcessing((int[])dataSet.get(Data.OPERATION_TIME));
        }

        Log.i("메인으로","갔음");
        CommunicationManager.socketActivity.receiveMsg(this.processedData);
    }
}
