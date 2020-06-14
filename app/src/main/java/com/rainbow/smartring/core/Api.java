package com.rainbow.smartring.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rainbow.smartring.core.database.DatabaseManager;
import com.rainbow.smartring.core.log.Logger;
import com.rainbow.smartring.core.net.RestClient;
import com.rainbow.smartring.core.net.callback.ISuccess;
import com.rainbow.smartring.entity.ResponseData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.WeakHashMap;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/14.
 */
public class Api {
    public static void sendData(String mac, double t, int hr, int o2) {
        WeakHashMap<String, Object> params = new WeakHashMap<>();
        if (t != -1) {
            params.put("T", t);
        }
        if (hr != -1) {
            params.put("hb", hr);
        }
        if (o2 != -1) {
            params.put("bo", o2);
        }
        params.put("sid", mac);
        new RestClient.Builder()
                .url("/")
                .params(params)
                .success(response -> {
                    Logger.d(response);
                })
                .error((code, msg) -> Logger.e("code:" + code + ";msg:" + msg))
                .build().post();
    }

    public static void queryData(String mac) {
        int days = 30;
        new RestClient.Builder()
                .url(mac + "/" + days)
                .success(response -> {
                    // 先清库，后存储
                    DatabaseManager.getInstance().deleteAll();
                    Logger.d(response);
                    JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();

                    for (JsonElement json : jsonArray) {
                        ResponseData data = new Gson().fromJson(json, ResponseData.class);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss+08:00", Locale.US);
                        Date date = null;
                        try {
                            date = sdf.parse(data.getCtime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DatabaseManager.getInstance().insertT(date, mac, data.getT());
                        DatabaseManager.getInstance().insertHr(date, mac, data.getHb());
                        DatabaseManager.getInstance().insertO2(date, mac, data.getBo());
                    }
                })
                .error((code, msg) -> {
                    Logger.e("code:" + code + ";msg:" + msg);
                })
                .failure(() -> Logger.e("fail"))
                .build().get();
    }

    public static void analyze(String mac, ISuccess success) {
        new RestClient.Builder()
                .url("analyze/" + mac)
                .success(success)
                .error((code, msg) -> {
                    Logger.e("code:" + code + ";msg:" + msg);
                })
                .build()
                .get();
    }
}
