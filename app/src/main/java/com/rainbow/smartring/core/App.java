package com.rainbow.smartring.core;

import android.app.Application;

import com.rainbow.smartring.core.database.DatabaseManager;
import com.rainbow.smartring.core.log.Logger;
import com.vise.baseble.ViseBle;

/**
 * SmartRing
 * Created By Rainbow on 2020/5/24.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(this, true);

        //蓝牙相关配置修改
        ViseBle.config()
                .setScanTimeout(20000)//扫描超时时间，这里设置为永久扫描
                .setConnectTimeout(10 * 1000)//连接超时时间
                .setOperateTimeout(5 * 1000)//设置数据操作超时时间
                .setConnectRetryCount(3)//设置连接失败重试次数
                .setConnectRetryInterval(2000)//设置连接失败重试间隔时间
                .setOperateRetryCount(3)//设置数据操作失败重试次数
                .setOperateRetryInterval(1000)//设置数据操作失败重试间隔时间
                .setMaxConnectCount(3);//设置最大连接设备数量
        //蓝牙信息初始化，全局唯一，必须在应用初始化时调用
        ViseBle.getInstance().init(this);

        DatabaseManager.getInstance().init(this);
    }
}
