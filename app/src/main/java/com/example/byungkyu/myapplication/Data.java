package com.example.byungkyu.myapplication;

import java.math.BigInteger;

/**
 * Created by ByungKyu on 2017-07-26.
 */

public class Data {
    //Error 처리를 위한 정보
    public final static int checksum=0;
    public final static int byteNull=1;
       /*각각 activity에 보낼 데이터 셋 */


    public static final byte LDR = (byte)0xE6;
    //private HashMap<Byte, Object> LDR;
    /*임시 LOCAL DATA WRITER data set*/
    public static final byte LDW = (byte) 0xE7;
    //private Byte[] LDW = {(byte) 0xA6, (byte) 0xA7, (byte) 0xA8, (byte) 0xA9, (byte) 0xA1, (byte) 0xA2};

    /*임시 Periodic transmission Stop */
    public static final byte PTS = (byte) 0xE8;

    /*임시 EEPROM READ */
    public static final byte ERR = (byte) 0xE9;
    /*임시 EEPROM WRITER */
    public static  final byte ERW = (byte) 0xE1;

    /*임시 ERROR INFORMATION READER */
    public static  final byte EIR = (byte) 0xE2;

    public static  final byte PRS = (byte) 0xE3;


    public static  final byte ANALOG = (byte) 0x01;
    public static  final byte DIGITAL_IO = (byte) 0x0A;
    public static  final byte FUEL_USE_INFO = (byte) 0x12;
    public static  final byte OPERATION_TIME = (byte) 0x04;
    public static  final byte FILTER_USETIME = (byte) 0x08;
    public static  final byte FILTER_INIT = (byte) 0x09;
    public static  final byte FILTER_CHANGE = (byte) 0x20;
    public static  final byte CURRENT_ERROR_INFO = (byte) 0x21;
    public static  final BigInteger reqReadFault= new BigInteger("05a600012100cd",16);
    //64는 1초, 20->0x14은 0.2초, 2초는 c8, 1.5초는 96
    //public static  final BigInteger reqReadFault= new BigInteger("05a66401210031",16);
    //public static  final BigInteger reqReadFault= new BigInteger("05a69601210063",16);
    //public static  final BigInteger reqReadFault= new BigInteger("05a614012100E1",16);
    //public static  final BigInteger reqReadFault= new BigInteger("05a601012100CE",16);
    public static  final BigInteger reqStop= new BigInteger("01a8a9",16);
    public static  final BigInteger reqReadFuel=new BigInteger("05a600011200be",16);
//      public static  final BigInteger reqReadFuel=new BigInteger("05a601011200bf",16);
    public static  final BigInteger reqReadOpTime=new BigInteger("05a600010400b0",16);
//      public static  final BigInteger reqReadOpTime=new BigInteger("05a601010400b1",16);
    public static  final BigInteger reqReadPump=new BigInteger("07a6000101020304b8",16);
    public static  final BigInteger reqReadAnalogRepeat=new BigInteger("05a601010100AE",16);
    public static  final BigInteger reqReadAnalog=new BigInteger("05a600010100AD",16);

//    public static  final BigInteger reqReadDigital=new BigInteger("05a600010A00b6",16);
      public static  final BigInteger reqReadDigital=new BigInteger("05a600010A00b6",16);
    //요청 메시지- Length, SID, 주기, 로컬 데이터 그룹 갯수, 갯수에 따른 ID, ID에 따른 데이터 갯수, 데이터 ID

}