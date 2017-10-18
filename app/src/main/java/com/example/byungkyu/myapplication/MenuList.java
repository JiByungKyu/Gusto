package com.example.byungkyu.myapplication;

import java.util.ArrayList;

/**
 * Created by USER on 2017-08-16.
 */

public class MenuList {
    public String function;
    public ArrayList<String> item = new ArrayList<String>();

    public MenuList(String function) {
        this.function = function;
    }

    public String toString() {
        return function;
    }
}