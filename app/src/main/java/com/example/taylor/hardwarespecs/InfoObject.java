package com.example.taylor.hardwarespecs;

public class InfoObject {
    private String name;
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public InfoObject(String name, String info) {
        this.name = name;
        this.info = info;
    }
}
