package com.example.happy_deer;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class DataManagementActivity extends AppCompatActivity {

    private LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_management);

        chart = findViewById(R.id.chart);
        setupChart();

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

}