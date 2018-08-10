package com.hzyc.csj.ordermealsystem.model;

/**
 * Created by 小柿子 on 2018/8/4.
 */
public class DingDan {
    private int id;
    private int mId;
    private int mnum;
    private String state;
    private int tId;
    private String time;
    private String beizhu;
    public String getBeizhu() {
        return beizhu;
    }
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getmId() {
        return mId;
    }
    public void setmId(int mId) {
        this.mId = mId;
    }
    public int getMnum() {
        return mnum;
    }
    public void setMnum(int mnum) {
        this.mnum = mnum;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public int gettId() {
        return tId;
    }
    public void settId(int tId) {
        this.tId = tId;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "DingDan [id=" + id + ", mId=" + mId + ", mnum=" + mnum + ", state=" + state + ", tId=" + tId + ", time="
                + time + ", beizhu=" + beizhu + "]";
    }
}
