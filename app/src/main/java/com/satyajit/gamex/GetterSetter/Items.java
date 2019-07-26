package com.satyajit.gamex.GetterSetter;

public class Items {
    private String name,image,count,id;  //Our Members


    public Items(String name, String image, String count) {
        this.name = name;
        this.image = image;
        this.count = count;
    }

    public Items(String name, String image, String count,String id) {
        this.name = name;
        this.image = image;
        this.count = count;
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getCount() {
        return count;
    }

    public String getId() { return id; }





}