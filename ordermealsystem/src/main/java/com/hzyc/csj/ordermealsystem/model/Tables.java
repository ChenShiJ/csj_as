package com.hzyc.csj.ordermealsystem.model;

/**
 * Created by 小柿子 on 2018/8/3.
 */
public class Tables {
    private int id;
    private String number;
    private String state;
    private String max;
    private String now;
    public String getNow() {
        return now;
    }
    public void setNow(String now) {
        this.now = now;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getMax() {
        return max;
    }
    public void setMax(String max) {
        this.max = max;
    }
    @Override
    public String toString() {
        return "Tables [id=" + id + ", number=" + number + ", state=" + state + ", max=" + max + ", now=" + now + "]";
    }
}
