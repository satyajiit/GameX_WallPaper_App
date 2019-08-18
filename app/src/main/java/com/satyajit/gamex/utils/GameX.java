package com.satyajit.gamex.utils;

import android.app.Application;

import com.satyajit.gamex.GetterSetter.Items;

import java.util.ArrayList;
import java.util.List;

public class GameX extends Application {

    private List<Items> namesList = new ArrayList<>();

    public List<Items> getList() {
        return namesList;
    }

    public void setList(List<Items> nameList) {
        this.namesList = nameList;
    }
}