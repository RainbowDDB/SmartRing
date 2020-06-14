package com.rainbow.smartring.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 计时器助手
 * 使用：生成该类对象，复写run()方法
 * 目前只有开始(继续)，暂停，和结束功能
 * Created by Rainbow on 2018/10/13
 */
public abstract class TimerHelper {
    private Timer mTimer;
    private TimerTask mTimerTask;

    protected TimerHelper() {
        mTimer = null;
        mTimerTask = null;
    }

    public abstract void run();

    public void start(int delay, int period) {
        pause();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                TimerHelper.this.run();
            }
        };
        mTimer.schedule(mTimerTask, delay, period);
    }

    public void start(int delay) {
        pause();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                TimerHelper.this.run();
            }
        };
        mTimer.schedule(mTimerTask, delay);
    }

    public void pause() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void stop() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }
}
