package com.example.byungkyu.myapplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.scichart.charting.model.dataSeries.XyDataSeries;
import com.scichart.charting.modifiers.LegendModifier;
import com.scichart.charting.modifiers.ModifierGroup;
import com.scichart.charting.modifiers.SourceMode;
import com.scichart.charting.visuals.SciChartSurface;
import com.scichart.charting.visuals.axes.IAxis;
import com.scichart.charting.visuals.pointmarkers.EllipsePointMarker;
import com.scichart.charting.visuals.renderableSeries.IRenderableSeries;
import com.scichart.core.annotations.Orientation;
import com.scichart.core.framework.UpdateSuspender;
import com.scichart.drawing.utility.ColorUtil;
import com.scichart.extensions.builders.SciChartBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class BandGraphActivity extends AppCompatActivity implements SocketActivity {
    private String[] strings;
    private  Timer timer;
    CommunicationManager communicationManager = CommunicationManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        communicationManager.setSocketActivity(this);
        communicationManager.graphReceived=false;
        communicationManager.stopReceived=false;
        communicationManager.sendMsg();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_band_graph);

        // Sci 차트의 도면을 생성한다.
        final SciChartSurface surface = new SciChartSurface(this);

        //  "activity_band_graph.xml" by id 를 가져온다
        LinearLayout chartLayout = (LinearLayout) findViewById(R.id.band_graph);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.graph_toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);*/

        // Chart를 그릴 Layout에다 Sci 차트 도면을 추가한다.
        chartLayout.addView(surface);

        // SciChartBuilder 생성자를 해당 엑티비티로 초기화 시킴
        SciChartBuilder.init(this);

        // 엑티비티로 초기화 된 빌더로 Instance를 얻어냄
        final SciChartBuilder sciChartBuilder = SciChartBuilder.instance();


        // 도면에 그릴 X축의 값들을 설정한다.
        final IAxis xAxis = sciChartBuilder.newNumericAxis()
                .withAxisTitle("Time") // X축 명
                .withVisibleRange(0, 10)// 한눈에 보여질 X 범위
                .build();

        // 도면에 그릴 Y축의 값들을 설정한다.
        final IAxis yAxis = sciChartBuilder.newNumericAxis()
                .withAxisTitle("Data").withVisibleRange(0, 2).build();

        // 그래프를 조작하는 기능들을 설정한다. (줌 기능)
        ModifierGroup chartModifiers = sciChartBuilder.newModifierGroup()
                .withPinchZoomModifier().withReceiveHandledEvents(true).build()
                .withZoomPanModifier().withReceiveHandledEvents(true).build()
                .build();


        // Y축과 Y축에 설정된 값들을 추가한다.
        Collections.addAll(surface.getYAxes(), yAxis);

        // X축과 X축에 설정된 값들을 추가한다.
        Collections.addAll(surface.getXAxes(), xAxis);

        // 생성된 수정 기능들을 모두 추가한다.
        Collections.addAll(surface.getChartModifiers(), chartModifiers);


        //라인 시리즈 추가

        // 이전 데이터의 버퍼 용량을 설정해줌
        final int fifoCapacity = 500;

        // X축의 형식과 Y축의 형식을 설정한 뒤 생성

        final XyDataSeries EngineRPM = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("EngineRPM").withFifoCapacity(100).build();
        final XyDataSeries EngineOil = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("EngineOil").withFifoCapacity(100).build();
        final XyDataSeries FuelT = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("FuelT").withFifoCapacity(100).build();
        final XyDataSeries FPumpP = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("FPumpP").withFifoCapacity(100).build();
        final XyDataSeries RPumpP = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("RPumpP").withFifoCapacity(100).build();
        final XyDataSeries Pump1P = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("Pump1P").withFifoCapacity(100).build();
        final XyDataSeries Pump2P = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("Pump2P").withFifoCapacity(100).build();
        final XyDataSeries PumpV1 = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("PumpV1").withFifoCapacity(100).build();
        final XyDataSeries PumpV2 = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("PumpV2").withFifoCapacity(100).build();
        final XyDataSeries CoolFenV = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("CoolFenV").withFifoCapacity(100).build();
        final XyDataSeries ControlV = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).withSeriesName("ControlV").withFifoCapacity(100).build();


        // 그래프에 쓰일 라인 설정을 하면서 생성

        final IRenderableSeries rs1 = sciChartBuilder.newLineSeries()
                .withDataSeries(EngineRPM)
                .withStrokeStyle(ColorUtil.Gold, 2f, true)
                .build();

        final IRenderableSeries rs2 = sciChartBuilder.newLineSeries()
                .withDataSeries(EngineOil)
                .withStrokeStyle(ColorUtil.Green, 2f, true)
                .build();
        final IRenderableSeries rs3 = sciChartBuilder.newLineSeries()
                .withDataSeries(FuelT)
                .withStrokeStyle(ColorUtil.Cyan, 2f, true)
                .build();
        final IRenderableSeries rs4 = sciChartBuilder.newLineSeries()
                .withDataSeries(FPumpP)
                .withStrokeStyle(ColorUtil.Brown, 2f, true)
                .build();
        final IRenderableSeries rs5 = sciChartBuilder.newLineSeries()
                .withDataSeries(RPumpP)
                .withStrokeStyle(ColorUtil.Blue, 2f, true)
                .build();
        final IRenderableSeries rs6 = sciChartBuilder.newLineSeries()
                .withDataSeries(Pump1P)
                .withStrokeStyle(ColorUtil.Red, 2f, true)
                .build();
        final IRenderableSeries rs7 = sciChartBuilder.newLineSeries()
                .withDataSeries(Pump2P)
                .withStrokeStyle(ColorUtil.Grey, 2f, true)
                .build();

        final IRenderableSeries rs8 = sciChartBuilder.newLineSeries()
                .withDataSeries(PumpV1)
                .withStrokeStyle(ColorUtil.Maroon, 2f, true)
                .build();
        final IRenderableSeries rs9 = sciChartBuilder.newLineSeries()
                .withDataSeries(PumpV2)
                .withStrokeStyle(ColorUtil.Navy, 2f, true)
                .build();
        final IRenderableSeries rs10 = sciChartBuilder.newLineSeries()
                .withDataSeries(CoolFenV)
                .withStrokeStyle(ColorUtil.Purple, 2f, true)
                .build();
        final IRenderableSeries rs11 = sciChartBuilder.newLineSeries()
                .withDataSeries(ControlV)
                .withStrokeStyle(ColorUtil.Olive, 2f, true)
                .build();

        // 그래프에 쓰일 포인터를 설정하면서 생성
        EllipsePointMarker pointMarker = sciChartBuilder
                .newPointMarker(new EllipsePointMarker())
                .withFill(ColorUtil.LightBlue)
                .withStroke(ColorUtil.Green, 2f)
                .withSize(10)
                .build();


        // 도면에 그래를 추가시킴
        surface.getRenderableSeries().add(rs1);
        surface.getRenderableSeries().add(rs2);
        surface.getRenderableSeries().add(rs3);
        surface.getRenderableSeries().add(rs4);
        surface.getRenderableSeries().add(rs5);
        surface.getRenderableSeries().add(rs6);
        surface.getRenderableSeries().add(rs7);
        surface.getRenderableSeries().add(rs8);
        surface.getRenderableSeries().add(rs9);
        surface.getRenderableSeries().add(rs10);
        surface.getRenderableSeries().add(rs11);

        // 레전드 추가(기록들 보기) - 처음부터 가릴수가 없다.
        LegendModifier legendModifier = new LegendModifier(this);
        legendModifier.setShowLegend(true);
        legendModifier.setShowSeriesMarkers(true);
        legendModifier.setSourceMode(SourceMode.AllVisibleSeries);
        legendModifier.setOrientation(Orientation.VERTICAL);

        // Add the modifier to the surface
        surface.getChartModifiers().add(legendModifier);


        TimerTask updateDataTask = new TimerTask() {
            private int x = 0;

            @Override
            public void run() {
                //스레드가 돌아가면서 데이터가 저장되지 않았을 때 업데이트하는 것을 방지해주는 서스펜더 클래스
                UpdateSuspender.using(surface, new Runnable() {
                    @Override
                    public void run() {
                        EngineRPM.append(x, Double.parseDouble(strings[0]));
                        EngineOil.append(x, Double.parseDouble(strings[1]));
                        FuelT.append(x, Double.parseDouble(strings[2]));
                        FPumpP.append(x, Double.parseDouble(strings[3]));
                        RPumpP.append(x, Double.parseDouble(strings[4]));
                        Pump1P.append(x, Double.parseDouble(strings[5]));
                        Pump2P.append(x, Double.parseDouble(strings[6]));
                        PumpV1.append(x, Double.parseDouble(strings[7]));
                        PumpV2.append(x, Double.parseDouble(strings[8]));
                        CoolFenV.append(x, Double.parseDouble(strings[9]));
                        ControlV.append(x, Double.parseDouble(strings[10]));

                        // Zoom series to fit the viewport
                        surface.zoomExtents();

                        ++x;
                    }
                });
            }
        };

        //다이터를 설정하여 지속저인 스케쥴링을 할 수 잇도록 해준다.
        timer = new Timer();
        long delay = 0;
        long interval = 10;
        timer.schedule(updateDataTask, delay, interval);


        //그려진 그래프 부분으로 이동시켜 보여줌
        //surface.zoomExtents();
    }

    @Override
    public void receiveMsg(final HashMap<Byte, Object> dataSet) {
        if (dataSet.containsKey(Data.ANALOG)) {
                    CommunicationManager.graphReceived = true;
                    Message msg= Message.obtain();
                    msg.what=Data.ANALOG;
                    strings = (String[]) dataSet.get(Data.ANALOG);
                    msg.obj=strings;
                    Log.i("그래프", strings[0]);
                    msgHandler.sendMessage(msg);
        }
    }
    Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            strings= (String[])msg.obj;
            Log.i("그래프",strings[0]);

            //bundle.putStringArray("data",strings);
        }
    };
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("종료","하자");
        timer.cancel();
        CommunicationManager.graphReceived=true;
        CommunicationManager.stopReceived=true;
        communicationManager.sendMsg();
        getDelegate().onDestroy();

    }
    public void receiveConnect(){

    }
}