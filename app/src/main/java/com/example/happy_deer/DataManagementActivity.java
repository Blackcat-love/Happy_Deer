package com.example.happy_deer;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManagementActivity extends AppCompatActivity {

    private LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_management);

        chart = findViewById(R.id.chart);
        setupChart();
        setRadarChart();
        setScatterChart();

    }

//    设定图表
    private void setupChart(){
        ArrayList<Entry> entries = new ArrayList<>();
        // 添加数据点
        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 3));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 5));
        entries.add(new Entry(4, 4));

        // 创建数据集
        LineDataSet dataSet = new LineDataSet(entries, "频率数据");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));

        // 创建 LineData 对象
        LineData lineData = new LineData(dataSet);

        // 设置数据到图表
        chart.setData(lineData);
        chart.invalidate(); // 刷新图表显示
    }

    private void setRadarChart(){
        RadarChart radarChart = findViewById(R.id.Radar_chart);
        HealthRecordManager healthRecordManager = new HealthRecordManager(DataManagementActivity.this);
        // 准备数据 - 假设这些是每个月的频率
        float[] monthlyFrequencies = {4, 5, 3, 7, 2, 6, 8, 5, 4, 9, 7, 6};
        String[] monthLabels = {"1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"};
        // 循环获取每个月的记录数量
        for (int month = 1; month <= 12; month++) {
            int recordCountForMonth = healthRecordManager.getRecordCountForMonth(2024, month);
            monthlyFrequencies[month - 1] = recordCountForMonth; // 将结果存储到数组中
            Log.d("Monthly Frequency", "Number of records for " + monthLabels[month - 1] + ": " + recordCountForMonth);
        }


// 创建雷达条目
        List<RadarEntry> entries = new ArrayList<>();
        for (int i = 0; i < monthlyFrequencies.length; i++) {
            entries.add(new RadarEntry(monthlyFrequencies[i])); // 每个月的频率
        }

// 创建数据集
        RadarDataSet dataSet = new RadarDataSet(entries, "每月频率");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawFilled(true); // 可选：填充颜色

// 创建雷达数据
        RadarData radarData = new RadarData(dataSet);
        radarChart.setData(radarData);

// 设置月份标签
        radarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(Arrays.asList(monthLabels)));
    }
    
    private void setScatterChart(){
        ScatterChart scatterChart = findViewById(R.id.Scatter_Chart);
        // 准备数据
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1f, 2f)); // (x=1, y=2)
        entries.add(new Entry(2f, 5f));
        entries.add(new Entry(3f, 3f));
        entries.add(new Entry(4f, 7f));
        entries.add(new Entry(5f, 6f));

        // 创建数据集
        ScatterDataSet dataSet = new ScatterDataSet(entries, "年率g");
        dataSet.setColor(Color.BLUE); // 设置点的颜色
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE); // 设置形状

        // 创建 ScatterData
        ScatterData scatterData = new ScatterData(dataSet);

        // 将数据设置到 ScatterChart
        scatterChart.setData(scatterData);

        // 刷新图表
        scatterChart.invalidate(); // refresh
    }

    

}