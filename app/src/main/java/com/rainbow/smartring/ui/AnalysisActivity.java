package com.rainbow.smartring.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.google.gson.Gson;
import com.rainbow.smartring.R;
import com.rainbow.smartring.core.log.Logger;
import com.rainbow.smartring.entity.AnalysisData;
import com.rainbow.smartring.widget.TitleToolbar;

import java.util.List;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/14.
 */
public class AnalysisActivity extends PermissionActivity {

    private TextView tempDiff;
    private TextView feverZone;
    private TextView lowTempZone;
    private TextView lowBo;
    private TextView lowBoZone;
    private TextView highHb;
    private TextView lowHb;
    private TextView highHbZone;
    private TextView lowHbZone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
        TitleToolbar toolbar = $(R.id.toolbar);
        setSupportActionBar(toolbar.getToolbar());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tempDiff = $(R.id.tv_temp_diff);
        feverZone = $(R.id.tv_fever_zone);
        lowTempZone = $(R.id.tv_low_temp_zone);
        lowBo = $(R.id.tv_low_bo);
        lowBoZone = $(R.id.tv_low_bo_zone);
        highHb = $(R.id.tv_high_hb);
        highHbZone = $(R.id.tv_high_hb_zone);
        lowHb = $(R.id.tv_low_hb);
        lowHbZone = $(R.id.tv_low_hb_zone);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String res = bundle.getString("response");
            AnalysisData data = new Gson().fromJson(res, AnalysisData.class);
            Logger.d(data.toString());
            if (data.getReqState() != 200) return;
            tempDiff.setText(String.valueOf(data.getTemporateDiff()));
            lowBo.setText(String.valueOf(data.getLowBo()));
            highHb.setText(String.valueOf(data.getHighHb()));
            lowHb.setText(String.valueOf(data.getLowHb()));

            List<String[]> feverZoneList = data.getFeverZone();
            feverZone.setText(getArrayListString(feverZoneList));

            List<String[]> highHbZoneList = data.getHighHbZone();
            highHbZone.setText(getArrayListString(highHbZoneList));

            List<String[]> lowBoZoneList = data.getLowBoZone();
            lowBoZone.setText(getArrayListString(lowBoZoneList));

            List<String[]> lowTempZoneList = data.getLowTemporateZone();
            lowTempZone.setText(getArrayListString(lowTempZoneList));

            List<String[]> lowHbZoneList = data.getLowHbZone();
            lowHbZone.setText(getArrayListString(lowHbZoneList));
        }
    }

    @Override
    protected Object setLayout() {
        return R.layout.activity_analysis;
    }

    private String getArrayListString(List<String[]> list) {
        if (list.size() != 0) {
            StringBuilder b = new StringBuilder();
            for (String[] ss : list) {
                b.append("[").append(ss[0]).append(",").append(ss[1]).append("]");
            }
            return b.toString();
        } else {
            return "[]";
        }
    }
}
