package com.example.byungkyu.myapplication;

import android.media.Image;

/**
 * Created by USER on 2017-08-17.
 */

public class GraphListViewItem {
    private int type;               //리스트 아이템 타입
    private String date;            //날짜, 시간
    private int graphNo;            //저장된 그래프 개수
    private String equipment;       //장비명
    private Image img;            //그래프 데이터 이미지
    private String num;                 //맨아래 숫자(의미 모름)

    public void setType(int type){
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGraphNo(int graphNo) {
        this.graphNo = graphNo;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public int getGraphNo() {
        return graphNo;
    }

    public String getNum() {
        return num;
    }

    public String getEquipment() {
        return equipment;
    }

    public Image getImg() {
        return img;
    }
}
