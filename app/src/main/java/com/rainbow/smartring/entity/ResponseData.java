package com.rainbow.smartring.entity;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/14.
 */
public class ResponseData {
    private String sid;

    private double T;

    private int hb;

    private int bo;

    private String ctime;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public double getT() {
        return T;
    }

    public void setT(double t) {
        T = t;
    }

    public int getHb() {
        return hb;
    }

    public void setHb(int hb) {
        this.hb = hb;
    }

    public int getBo() {
        return bo;
    }

    public void setBo(int bo) {
        this.bo = bo;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public ResponseData(String sid, double t, int hb, int bo, String ctime) {
        this.sid = sid;
        T = t;
        this.hb = hb;
        this.bo = bo;
        this.ctime = ctime;
    }
}
