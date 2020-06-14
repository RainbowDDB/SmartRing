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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rainbow.smartring.R;
import com.rainbow.smartring.core.database.DatabaseManager;
import com.rainbow.smartring.core.log.Logger;
import com.rainbow.smartring.entity.TData;
import com.rainbow.smartring.entity.TDataDao;
import com.rainbow.smartring.widget.TitleToolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * SmartRing
 * Created By Rainbow on 2020/6/13.
 */
public class TempActivity extends PermissionActivity {

    private LineChart lineChart;
    private TextView tvTemperature;

    private BluetoothDataReceiver receiver;

    private List<TData> tDataTodayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        tDataTodayList = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        Date today = calendar.getTime();
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        Date tomorrow = calendar.getTime();

        for (int i = 0; i < 24; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.HOUR_OF_DAY, i);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            Date pre = c.getTime();
            c.add(Calendar.HOUR_OF_DAY, 1);
            Date prv = c.getTime();
            List<TData> tHours = DatabaseManager.getInstance().getTDataDao().queryBuilder()
                    .where(TDataDao.Properties.Date.between(pre, prv))
                    .list();
            double ava = 0;

            if (tHours.size() != 0) {
                for (int j = 0; j < tHours.size(); j++) {
                    ava += tHours.get(j).getTemperature();
                }
                ava = ava / tHours.size();
                TData d = new TData();
                d.setDate(tHours.get(0).getDate());
                d.setTemperature(ava);
                tDataTodayList.add(d);
            }
        }

        if (tDataTodayList != null) {
            ArrayList<Entry> values = new ArrayList<>();
            for (TData d : tDataTodayList) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d.getDate());
                values.add(new Entry(
                        calendar.get(Calendar.HOUR_OF_DAY),
                        d.getTemperature().floatValue()));
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

        tvTemperature = $(R.id.tv_temperature);
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
            set = new LineDataSet(values, "体温");
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

            String[] xData = new String[]{"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00"
                    , "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
                    "21:00", "22:00", "23:00", "24:00"};
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMaximum(24f);
            xAxis.setAxisMinimum(0f);
            // 前面xAxis.setEnabled(false);则下面绘制的Grid不会有"竖的线"（与X轴有关）
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xData));
            xAxis.setTextColor(Color.parseColor("#03A9F4"));  // x轴字体颜色
            xAxis.setAxisLineColor(Color.TRANSPARENT); // 底部x轴透明色
            xAxis.setGranularity(1f);
            YAxis rightYAxis = lineChart.getAxisRight();
            rightYAxis.setEnabled(false);
            YAxis leftYAxis = lineChart.getAxisLeft();
            leftYAxis.setAxisMinimum(28f);
            leftYAxis.setAxisMaximum(41f);
            leftYAxis.setDrawGridLines(true);
            leftYAxis.enableGridDashedLine(5f, 5f, 0f); // y轴虚线
            leftYAxis.setAxisLineColor(Color.TRANSPARENT);
            leftYAxis.setTextColor(Color.parseColor("#03A9F4"));
            leftYAxis.setGridColor(Color.parseColor("#335566"));

//            LimitLine yLimitMinLine = new LimitLine(36f,"体温最低");
//            LimitLine yLimitMaxLine = new LimitLine(37.5f,"体温预警");
//            yLimitMinLine.setLineColor(Color.RED);
//            yLimitMinLine.setTextColor(Color.RED);
//            yLimitMaxLine.setLineColor(Color.RED);
//            yLimitMaxLine.setTextColor(Color.RED);
//            leftYAxis.addLimitLine(yLimitMinLine);
//            leftYAxis.addLimitLine(yLimitMaxLine);

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

    @Override
    protected Object setLayout() {
        return R.layout.activity_temp;
    }

    private class BluetoothDataReceiver extends BroadcastReceiver {
        public static final String DATA_INTENT = "com.rainbow.dataintent";

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("onReceived");
            if (DATA_INTENT.equals(intent.getAction())) {
                double temp = intent.getDoubleExtra("temperature", -1);
                if (temp != -1) {
                    tvTemperature.setText(String.format("%s℃", String.valueOf(temp)));
                } else {
                    tvTemperature.setText("--℃");
                }
            }
        }
    }
}
