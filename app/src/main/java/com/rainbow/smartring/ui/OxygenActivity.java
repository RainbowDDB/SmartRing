package com.rainbow.smartring.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rainbow.smartring.R;
import com.rainbow.smartring.core.database.DatabaseManager;
import com.rainbow.smartring.core.log.Logger;
import com.rainbow.smartring.entity.HrData;
import com.rainbow.smartring.entity.HrDataDao;
import com.rainbow.smartring.entity.O2Data;
import com.rainbow.smartring.entity.O2DataDao;
import com.rainbow.smartring.widget.TitleToolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/14.
 */
public class OxygenActivity extends PermissionActivity {

    private LineChart lineChart;
    private TextView tvBloodOxygen;

    private BluetoothDataReceiver receiver;

    private List<O2Data> o2DataTodayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        o2DataTodayList = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.HOUR_OF_DAY, -1);
            c.add(Calendar.MINUTE, i + 30);
            c.set(Calendar.SECOND, 0);
            Date pre = c.getTime();
            c.add(Calendar.MINUTE, 1);
            Date prv = c.getTime();
            List<O2Data> o2Hours = DatabaseManager.getInstance().getO2DataDao().queryBuilder()
                    .where(O2DataDao.Properties.Date.between(pre, prv))
                    .list();
            int ava = 0;

            if (o2Hours.size() != 0) {
                for (int j = 0; j < o2Hours.size(); j++) {
                    ava += o2Hours.get(j).getO2();
                }
                ava = ava / o2Hours.size();
                O2Data d = new O2Data();
                d.setDate(o2Hours.get(0).getDate());
                d.setO2(ava);
                o2DataTodayList.add(d);
            }
        }

        if (o2DataTodayList != null) {
            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0; i < o2DataTodayList.size(); i++) {
                O2Data o2 = o2DataTodayList.get(i);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(o2.getDate());
                values.add(new Entry(i, o2.getO2()));
                calendar.setTimeInMillis(calendar.getTimeInMillis());
                Logger.d(calendar.getTimeInMillis() + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            }
            setChartData(values);
        }
    }

    private void initView() {
        TitleToolbar toolbar = $(R.id.toolbar);
        setSupportActionBar(toolbar.getToolbar());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvBloodOxygen = $(R.id.tv_blood_oxygen);
        lineChart = $(R.id.line_chart);

    }

    private void setChartData(ArrayList<Entry> values) {
        LineDataSet set;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(values, "心率");
//            set.enableDashedLine(10f, 5f, 0f);
//            set.enableDashedHighlightLine(10f, 5f, 0f);
            set.setColor(Color.parseColor("#2196F3"));
            set.setCircleColor(Color.parseColor("#2196F3"));
            set.setLineWidth(2f);
            set.setCircleRadius(3f);
            set.setDrawCircleHole(false);
            set.setValueTextSize(9f);
            set.setFillColor(Color.WHITE);
            set.setValueFormatter(new DefaultValueFormatter(1));
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setDrawFilled(true);
            set.setFillDrawable(getResources().getDrawable(R.drawable.background_gredient));

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMaximum(30f);
            xAxis.setAxisMinimum(0f);
            // 前面xAxis.setEnabled(false);则下面绘制的Grid不会有"竖的线"（与X轴有关）
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.HOUR_OF_DAY, -1);
                    c.add(Calendar.MINUTE, 30 + (int) value);
                    return c.get(Calendar.HOUR_OF_DAY) + ":"
                            + (c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE));
                }
            });
            xAxis.setTextColor(Color.parseColor("#03A9F4"));  // x轴字体颜色
            xAxis.setAxisLineColor(Color.TRANSPARENT); // 底部x轴透明色
            xAxis.setGranularity(1f);
            YAxis rightYAxis = lineChart.getAxisRight();
            rightYAxis.setEnabled(false);
            YAxis leftYAxis = lineChart.getAxisLeft();
            leftYAxis.setAxisMinimum(80f);
            leftYAxis.setAxisMaximum(105f);
            leftYAxis.setDrawGridLines(true);
            leftYAxis.enableGridDashedLine(5f, 5f, 0f); // y轴虚线
            leftYAxis.setAxisLineColor(Color.TRANSPARENT);
            leftYAxis.setTextColor(Color.parseColor("#03A9F4"));
            leftYAxis.setGridColor(Color.parseColor("#335566"));

            LimitLine yLimitLine = new LimitLine(95f,"血氧正常临界");
            yLimitLine.setLineColor(Color.RED);
            yLimitLine.setTextColor(Color.RED);
            leftYAxis.addLimitLine(yLimitLine);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            // 添加数据集
            dataSets.add(set);
            // 创建一个数据集的数据对象
            LineData data = new LineData(dataSets);
            // 设置数据
            lineChart.setData(data);
            lineChart.animateY(500);
            lineChart.setDescription(null);
            lineChart.setScaleXEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setScaleYEnabled(false);
            lineChart.invalidate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BluetoothDataReceiver();
        registerReceiver(receiver, new IntentFilter(BluetoothDataReceiver.DATA_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        receiver = null;
    }

    private class BluetoothDataReceiver extends BroadcastReceiver {
        public static final String DATA_INTENT = "com.rainbow.dataintent";

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("onReceived");
            if (DATA_INTENT.equals(intent.getAction())) {
                int o2 = intent.getIntExtra("o2", -1);
                if (o2 != -1) {
                    tvBloodOxygen.setText(o2 + "%");
                } else {
                    tvBloodOxygen.setText("--%");
                }
            }
        }
    }

    @Override
    protected Object setLayout() {
        return R.layout.activity_o2;
    }
}
