package com.rainbow.smartring.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/13.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setLayout() instanceof Integer) {
            setContentView((int) setLayout());
        } else if (setLayout() instanceof View) {
            setContentView((View) setLayout());
        } else {
            throw new RuntimeException("setLayout() is error");
        }
    }

    protected abstract Object setLayout();

    protected <T extends View> T $(@IdRes int resId) {
        return findViewById(resId);
    }

    protected void start(Class<?> activity, int[] flags, Bundle data) {
        Intent intent = new Intent(this, activity);
        for (int flag : flags) {
            intent.addFlags(flag);
        }
        if (data != null) {
            intent.putExtras(data);
        }
        startActivity(intent);
    }

    protected void start(Class<?> activity, Bundle data) {
        start(activity, new int[]{Intent.FLAG_ACTIVITY_NEW_TASK}, data);
    }

    protected void start(Class<?> activity, int[] flags) {
        start(activity, flags, null);
    }

    protected void start(Class<?> activity) {
        start(activity, new int[]{Intent.FLAG_ACTIVITY_NEW_TASK}, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
