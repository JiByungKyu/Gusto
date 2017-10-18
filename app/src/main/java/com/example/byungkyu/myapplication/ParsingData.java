package com.example.byungkyu.myapplication;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by YJ on 2017-08-01.
 */

public class ParsingData {
    /*배열의 첫번째 방은 PRI 지정*/
    private final byte POSITIVE_RS_ID = 0;
    private static ParsingData parsingData;
    /*각각 activity에 보낼 데이터 셋 */
    private byte nextIndex;
    private byte groupCount;
    private byte dataCount;
    private byte msgInfo;
    private byte[] data;
    private DataProcessor dataProcessor;
    private HashMap<Byte, Object> dataSet;

    /*ParsingData class 변수 초기화*/
    static {
        parsingData = null;
    }

    /*데이터 초기화*/
    private ParsingData() {
        data = null;
        dataProcessor = null;
        dataSet = null;
    }

    /*ParsingData 싱글톤 패턴*/
    public static ParsingData getInstance() {
        if (parsingData == null) {
            parsingData = new ParsingData();
        }
        return parsingData;
    }

    /*Parsing 할 data 처리를 위한 Method*/
    public void parsingMsg(byte[] recvMsg) throws Exception {
        if (recvMsg.length == 0 || recvMsg == null) {
            throw new Exception("잘못된 data입니다.");
        }
        data = recvMsg;
        nextIndex = 0;
        groupCount = 0;

        int LSB,MSB,ID,UNIT;

        msgInfo = data[POSITIVE_RS_ID];

        switch (msgInfo){
            case  Data.LDR: {
                groupCount = data[++nextIndex];
                if(groupCount == 0)
                    return ;

                if(CommunicationManager.socketActivity instanceof MainActivity){
                    dataProcessor = MainDataProcess.getInstance();
                }else if(CommunicationManager.socketActivity instanceof BandGraphActivity){
                    dataProcessor = GraphDataProcess.getInstance();
                }else if(CommunicationManager.socketActivity instanceof  AnalogActivity){
                    dataProcessor= AnalogDataProcess.getInstance();
                }
                dataSet = new HashMap<Byte,Object>();
                for(int i = 0; i < groupCount; i++) {
                    //LDR 종류 체크 데이터
                    msgInfo = data[++nextIndex];
                    Log.i("LDR종류",msgInfo+"");
                    //data 개수 파악
                    dataCount = data[++nextIndex];
                    switch (msgInfo) {
                        case Data.ANALOG:
                            int[] analogParsedData = new int[dataCount];
                            //data 값만 가져오기
                            for (int j = 0; j < dataCount; j++) {
                                LSB = data[++nextIndex] & 0xff;
                                MSB = data[++nextIndex] & 0xff;
                                UNIT = (MSB << 8) | LSB;
                                analogParsedData[j] = UNIT;
                            }
                            dataSet.put(Data.ANALOG, analogParsedData);
                            break;
                        case Data.DIGITAL_IO:

                        case Data.FUEL_USE_INFO:
                            int[] fuelParsedData = new int[dataCount / 2];
                            for (int j = 0; j < dataCount / 2; j++) {
                                LSB = data[++nextIndex] & 0xff;
                                MSB = data[++nextIndex] & 0xff;
                                UNIT = (MSB << 8) | LSB;

                                LSB = data[++nextIndex] & 0xff;
                                MSB = data[++nextIndex] & 0xff;
                                UNIT = ((MSB << 24) | (LSB << 16)) | UNIT;
                                fuelParsedData[j] = UNIT;
                            }
                            dataSet.put(Data.FUEL_USE_INFO, fuelParsedData);

                            break;

                        case Data.OPERATION_TIME:
                            int[] operationParsedData = new int[dataCount / 2];
                            for (int j = 0; j < dataCount / 2; j++) {
                                LSB = data[++nextIndex] & 0xff;
                                MSB = data[++nextIndex] & 0xff;
                                UNIT = (MSB << 8) | LSB;

                                LSB = data[++nextIndex] & 0xff;
                                MSB = data[++nextIndex] & 0xff;
                                UNIT = ((MSB << 24) | (LSB << 16)) | UNIT;
                                operationParsedData[j] = UNIT;
                            }
                            dataSet.put(Data.OPERATION_TIME, operationParsedData);
                            break;
                        case Data.FILTER_USETIME:
                        case Data.FILTER_INIT:
                        case Data.FILTER_CHANGE:
                        case Data.CURRENT_ERROR_INFO:
                            LSB = data[++nextIndex];
                            MSB = data[++nextIndex];
                            int numberOfError = ((MSB & 0xff) << 8) + (LSB & 0xff);
                            if(numberOfError!=0) {
                                int[][] ceiParsedData = new int[numberOfError][2];
                                //Error, FMI code
                                for (int j = 0; j < numberOfError; j++) {
                                    LSB = data[++nextIndex];
                                    MSB = data[++nextIndex];
                                    if (LSB != 0 && MSB != 0) {
                                        ceiParsedData[j][0] = LSB & 0xff;
                                        ceiParsedData[j][1] = (MSB >> 3) & 0x1f;
                                    }
                                }
                                dataSet.put(Data.CURRENT_ERROR_INFO, ceiParsedData);
                            }
                            else{
                                dataSet.put(Data.CURRENT_ERROR_INFO, null);
                            }

                            break;

                    }
                    dataProcessor.processingMsg(dataSet);
                }

            }break;
            case  Data.LDW: break;
            case  Data.PTS: break;
            case  Data.ERR: break;
            case  Data.ERW: break;
            case  Data.EIR: break;
            case  Data.PRS: CommunicationManager.stopReceived = false; CommunicationManager.graphReceived = false;
                CommunicationManager.mainReceived = false;
            default: Log.i("여기로","오는가");//throw new Exception("잘못된 데이터 입니다.");
        }

    }
}
