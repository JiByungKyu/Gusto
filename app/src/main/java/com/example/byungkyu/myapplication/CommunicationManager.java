package com.example.byungkyu.myapplication;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static android.content.ContentValues.TAG;

/**
 * Created by ByungKyu on 2017-08-03.
 */

public class CommunicationManager {
    Thread connectThread;
    Thread receiveThread;
    Thread sendThread;
    public static SocketActivity socketActivity;
    private String ip = "10.1.0.1";
    private int port = 5000;
    int receive=0;
    private Socket socket;
    byte[] buffer = new byte[500];
    byte[] bytes;
    int gap=300;
    public static boolean mainReceived=false;
    public static boolean graphReceived = false;
    public static boolean stopReceived = false;
    public static boolean analogReceived = false;
    private InputStream inputStream;
    private OutputStream outputStream = null;
    public boolean Isconnected=false;
    private ParsingData parsingData = ParsingData.getInstance();
    private static final CommunicationManager communicationManager = new CommunicationManager();
    private CommunicationManager(){
        try {
            start();
        }
        catch(Exception e){
            Log.e("생성자","에러 : "+e);
        }
    }
    public void setSocketActivity(SocketActivity socketActivity){
        this.socketActivity=socketActivity;
    }
    public static CommunicationManager getInstance(){return communicationManager;}
    public boolean start(){
        connectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!connectThread.isInterrupted()) {
                        socket = new Socket(ip, port);
                        outputStream = socket.getOutputStream();
                        inputStream = socket.getInputStream();
                        Isconnected = true;
                        receive();
                        Log.d("보임?", "" + Isconnected);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e);
                }
            }
        });
        connectThread.start();
        return true;
    }
    public void disconnect()throws IOException{
        if(socket!=null) {
            sendThread.interrupt();
            receiveThread.interrupt();
            connectThread.interrupt();
            socket.getOutputStream().close();
            socket.getInputStream().close();
            socket.close();
        }
    }
    public void sendMsg(){
        sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(socketActivity instanceof MainActivity) {
                        socketActivity.receiveConnect();
                        Log.d("보낼 때처음", "receive: " + Integer.toString(receive));
                            while (!mainReceived&&!sendThread.isInterrupted()) {
                                outputStream.write(Data.reqReadFault.toByteArray());
                                Thread.sleep(gap);
                                outputStream.write(Data.reqReadFuel.toByteArray());
                                Thread.sleep(gap);
                                outputStream.write(Data.reqReadOpTime.toByteArray());
                                Thread.sleep(gap);
                                outputStream.write(Data.reqReadPump.toByteArray());
                                Thread.sleep(gap);
                                Log.d("receivethread?", "" + mainReceived);
                                       //isReceived = false;
                                        //Thread.sleep(5000);
//                                        Log.d("보낸다, 연결됨?", "" + Isconnected);
                            }mainReceived = false;
                        }
                    else if(socketActivity instanceof BandGraphActivity) {
                        while (!graphReceived && !sendThread.isInterrupted()) {
                            Log.i("그래프","왓나");
                            outputStream.write(Data.reqReadAnalogRepeat.toByteArray());
                            Thread.sleep(gap);
                        }
                        while(stopReceived && !sendThread.isInterrupted()){
                            outputStream.write(Data.reqStop.toByteArray());
                            Thread.sleep(gap);
                        }
                    }else if (socketActivity instanceof AnalogActivity){
                        while(!analogReceived&&!sendThread.isInterrupted()){
                            outputStream.write(Data.reqReadAnalog.toByteArray());
                            Thread.sleep(gap);
                        }
                    }
                    else {
                        receive = 0;
                        sendMsg();
                    }
                }
                catch(Exception e){
                    if(e instanceof InterruptedException) {
                        Log.e(TAG, "ERROR : " + e);
                    }
                    else{

                    }
                }
            }
        });
        sendThread.start();
    }
    public synchronized byte[] recvMsg()throws IOException {
        byte sum;
        int length;
        int read;
        int sid=0;
        while((length=inputStream.read())==-1);
        sum=(byte)length;
        byte[] bytes=new byte[length];
        for(int i=0;i<length;i++) {
            read=inputStream.read();
            sum+=read;
            if(i==2)    // sid를 체크
                sid=read;
            bytes[i]=(byte)read;
        }
//        byte[] bytes=new byte[68];
//        inputStream.read(buffer,0,68);
//        sum=buffer[0];
//        System.arraycopy(buffer,1,bytes,0,buffer[0]);
//        for(int i=1;i<=buffer[0];i++) {
//            //bytes[i-1]=buffer[i]; 복사 방법2
//            sum+=buffer[i];
//        }
        Log.d("받은 것: "+buffer[buffer[0]+1],"더한 것:"+sum+" receive:"+Integer.toString(receive));
        Log.d("SID 확인함",Integer.toString(sid));
        //if(sum==buffer[buffer[0]+1]) {
        receive++;
        int check=inputStream.read();
       if(sum==(byte)check) {

            Log.d("받을 때", "receive : "+Integer.toString(receive));
            return bytes;
        }
        else {
           Log.d("Error, Sum"+sum, "checksum 오류"+check+"receive: "+receive);
            return null;
        }
    }
    public void receive(){
        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                    try{
                        Log.d("진입중?", "컥");
                        while(Isconnected&&!receiveThread.isInterrupted()) {
                            //Log.d("연결됨", "컥");
                            bytes=recvMsg();
                            if(bytes!=null){
                                Log.d("됩니다?", "컥");
                                parsingData.parsingMsg(bytes);
                            }
                            else
                                Log.d("receive thread:","byte가 null입니다. ");
                        }
                    }
                    catch(Exception e)  {

                    }
                }
        });
        receiveThread.start();
    }
}
