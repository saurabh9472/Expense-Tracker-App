package com.example.transaction;

public class Details {
    String name;
    String type;
    String money;
    String date;

    public Details(String name, String type, String money, String date) {
        this.name = name;
        this.type = type;
        this.money = money;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
