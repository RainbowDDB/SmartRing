package com.rainbow.smartring.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/14.
 */
public class AnalysisData {
    @SerializedName("temporate_diff")
    private double temporateDiff;

    @SerializedName("fever_zone")
    private ArrayList<String[]> feverZone;

    @SerializedName("low_temporate_zone")
    private ArrayList<String[]> lowTemporateZone;

    @SerializedName("low_bo")
    private int lowBo;

    @SerializedName("low_bo_zone")
    private ArrayList<String[]> lowBoZone;

    @SerializedName("high_hb")
    private int highHb;

    @SerializedName("low_hb")
    private int lowHb;

    @SerializedName("high_hb_zone")
    private ArrayList<String[]> highHbZone;

    @SerializedName("low_hb_zone")
    private ArrayList<String[]> lowHbZone;

    @SerializedName("req_state")
    private int reqState;

    public AnalysisData(double temporateDiff, ArrayList<String[]> feverZone, ArrayList<String[]> lowTemporateZone, int lowBo, ArrayList<String[]> lowBoZone, int highHb, int lowHb, ArrayList<String[]> highHbZone, ArrayList<String[]> lowHbZone, int reqState) {
        this.temporateDiff = temporateDiff;
        this.feverZone = feverZone;
        this.lowTemporateZone = lowTemporateZone;
        this.lowBo = lowBo;
        this.lowBoZone = lowBoZone;
        this.highHb = highHb;
        this.lowHb = lowHb;
        this.highHbZone = highHbZone;
        this.lowHbZone = lowHbZone;
        this.reqState = reqState;
    }

    public double getTemporateDiff() {
        return temporateDiff;
    }

    public void setTemporateDiff(double temporateDiff) {
        this.temporateDiff = temporateDiff;
    }

    public ArrayList<String[]> getFeverZone() {
        return feverZone;
    }

    public void setFeverZone(ArrayList<String[]> feverZone) {
        this.feverZone = feverZone;
    }

    public ArrayList<String[]> getLowTemporateZone() {
        return lowTemporateZone;
    }

    public void setLowTemporateZone(ArrayList<String[]> lowTemporateZone) {
        this.lowTemporateZone = lowTemporateZone;
    }

    public int getLowBo() {
        return lowBo;
    }

    public void setLowBo(int lowBo) {
        this.lowBo = lowBo;
    }

    public ArrayList<String[]> getLowBoZone() {
        return lowBoZone;
    }

    public void setLowBoZone(ArrayList<String[]> lowBoZone) {
        this.lowBoZone = lowBoZone;
    }

    public int getHighHb() {
        return highHb;
    }

    public void setHighHb(int highHb) {
        this.highHb = highHb;
    }

    public int getLowHb() {
        return lowHb;
    }

    public void setLowHb(int lowHb) {
        this.lowHb = lowHb;
    }

    public ArrayList<String[]> getHighHbZone() {
        return highHbZone;
    }

    public void setHighHbZone(ArrayList<String[]> highHbZone) {
        this.highHbZone = highHbZone;
    }

    public ArrayList<String[]> getLowHbZone() {
        return lowHbZone;
    }

    public void setLowHbZone(ArrayList<String[]> lowHbZone) {
        this.lowHbZone = lowHbZone;
    }

    public int getReqState() {
        return reqState;
    }

    public void setReqState(int reqState) {
        this.reqState = reqState;
    }
}
