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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataManagementActivity extends AppCompatActivity {

    private LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_management);

        chart = findViewById(R.id.chart);

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR); // 获取当前年份
        int month = calendar.get(Calendar.MONTH) + 1; // 获取当前月份（注意：月份从0开始，所以要加1）
        int day = calendar.get(Calendar.DAY_OF_MONTH); // 获取当前日期

        // 打印当前日期
        Log.i("CurrentDate", "当前日期: 年:" + year + " 月:" + month + " 日:" + day);

        // 调用绘图方法
        setupChart();
        setRadarChart(year);
//        setScatterChart(year, month); // 将年和月作为参数传递

    }

//    设定图表
private void setupChart() {
    ArrayList<Entry> entries = new ArrayList<>();

    HealthRecordManager healthRecordManager = new HealthRecordManager(DataManagementActivity.this);
    String currentDateFormatted = healthRecordManager.getCurrentDateFormatted();
    String dayOfWeek = healthRecordManager.getDayOfWeek(currentDateFormatted);

    // 获取当前周的所有日期
    String[] weekDates = getWeekDates(dayOfWeek, currentDateFormatted);

    // 假设您会在这里遍历 weekDates 数组来获取频率数据
    float[] frequencies = new float[7]; // 存储频率数据
    for (int i = 0; i < weekDates.length; i++) {
        // 这里假设您有一个方法 getFrequencyByDate() 来根据日期获取频率
        frequencies[i] = getFrequencyByDate(weekDates[i]); // 根据日期获取频率
        entries.add(new Entry(i, frequencies[i])); // i 作为 x 值，频率作为 y 值
    }

    // 创建数据集
    LineDataSet dataSet = new LineDataSet(entries, "当前一周频率");
    dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
    dataSet.setValueTextColor(getResources().getColor(android.R.color.black));

    // 可以设置平滑曲线和其他样式
    dataSet.setDrawCircles(true);
    dataSet.setDrawValues(true);

    // 创建 LineData 对象
    LineData lineData = new LineData(dataSet);

    // 设置数据到图表
    chart.setData(lineData);

    // 创建 X 轴标签
    String[] daysOfWeek = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    XAxis xAxis = chart.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(daysOfWeek));

    // 刷新图表显示
    chart.invalidate();
}

    // 根据当前日期和星期几获取本周的所有日期
    private String[] getWeekDates(String dayOfWeek, String currentDate) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            // 将当前日期设置到日历
            Date date = sdf.parse(currentDate);
            calendar.setTime(date);

            // 计算当前周的星期一的日期
            int offset = 0; // 偏移量
            switch (dayOfWeek) {
                case "星期一":
                    offset = 0;
                    break;
                case "星期二":
                    offset = -1;
                    break;
                case "星期三":
                    offset = -2;
                    break;
                case "星期四":
                    offset = -3;
                    break;
                case "星期五":
                    offset = -4;
                    break;
                case "星期六":
                    offset = -5;
                    break;
                case "星期日":
                    offset = -6;
                    break;
            }

            // 将当前日期设置为周一
            calendar.add(Calendar.DAY_OF_MONTH, offset);

            // 创建一个数组来存储这一周的所有日期
            String[] weekDates = new String[7];

            // 将周一到周日的日期存储到数组中
            for (int i = 0; i < 7; i++) {
                weekDates[i] = sdf.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1); // 移动到下一天
            }

            return weekDates;

        } catch (Exception e) {
            e.printStackTrace();
            return new String[0]; // 返回空数组以防止错误
        }
    }

    // 假设这是您获取频率数据的方法，您需要根据实际情况实现这个方法
    private float getFrequencyByDate(String date) {
        // 解析日期字符串，假设格式为 yyyy-MM-dd
        String[] dateParts = date.split("-");
        if (dateParts.length != 3) {
            return 0; // 处理错误情况，返回 0 作为默认值
        }

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // 月份从 0 开始
        int day = Integer.parseInt(dateParts[2]);

        // 创建 HealthRecordManager 实例
        HealthRecordManager healthRecordManager = new HealthRecordManager(DataManagementActivity.this);

        // 获取该日期的记录数量
        int recordCount = healthRecordManager.getRecordCountForDate(year, month, day);

        // 返回频率数据，直接返回记录数量
        return (float) recordCount; // 将 int 转换为 float
    }


    private void setRadarChart(int year){
        RadarChart radarChart = findViewById(R.id.Radar_chart);
        HealthRecordManager healthRecordManager = new HealthRecordManager(DataManagementActivity.this);
        // 准备数据 - 假设这些是每个月的频率
        float[] monthlyFrequencies = {4, 5, 3, 7, 2, 6, 8, 5, 4, 9, 7, 6};
        String[] monthLabels = {"1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"};
        // 循环获取每个月的记录数量                                                  注意需要设置年份否则统计数据会不一样
        for (int month = 1; month <= 12; month++) {
            int recordCountForMonth = healthRecordManager.getRecordCountForMonth(year, month);
            monthlyFrequencies[month - 1] = recordCountForMonth; // 将结果存储到数组中
            Log.d("Monthly Frequency", "Number of records for " + monthLabels[month - 1] + ": " + recordCountForMonth);
        }


        // 创建雷达条目
        List<RadarEntry> entries = new ArrayList<>();
        for (int i = 0; i < monthlyFrequencies.length; i++) {
            entries.add(new RadarEntry(monthlyFrequencies[i])); // 每个月的频率
        }

        // 创建数据集
        RadarDataSet dataSet = new RadarDataSet(entries, year + "年");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawFilled(true); // 可选：填充颜色

        // 创建雷达数据
        RadarData radarData = new RadarData(dataSet);
        radarChart.setData(radarData);

        // 设置月份标签
        radarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(Arrays.asList(monthLabels)));
    }

    private void setScatterChart(int year, int month) {
        ScatterChart scatterChart = findViewById(R.id.Scatter_Chart);

        // 查询数据库中指定年月的数据
        HealthRecordManager healthRecordManager = new HealthRecordManager(DataManagementActivity.this);
        ArrayList<Entry> entries = healthRecordManager.getDataFromDatabase(year, month);

        Log.d("DataEntries", "Entries: " + entries.toString());

        if (entries.size() == 0) {
            Log.e("ChartError", "没有可绘制的数据");
            return; // 处理没有数据的情况
        }

        // 验证每个 Entry 的有效性
        for (Entry entry : entries) {
            if (entry.getX() < 0 || entry.getY() < 0) {
                Log.e("ChartError", "无效数据点: " + entry.toString());
                return; // 处理无效数据的情况
            }
        }

        // 创建数据集
        ScatterDataSet dataSet = new ScatterDataSet(entries, "年份:" + year + "月份:" + month);
        dataSet.setColor(Color.BLUE); // 设置点的颜色
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE); // 设置形状

        // 创建 ScatterData
        ScatterData scatterData = new ScatterData(dataSet);

        // 将数据设置到 ScatterChart
        scatterChart.setData(scatterData);

        // 设置 X 轴标签
        XAxis xAxis = scatterChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置 X 轴位置
        xAxis.setDrawGridLines(false); // 如果不需要网格线，可以关闭

        // 设置 Y 轴
        YAxis yAxis = scatterChart.getAxisLeft();
        yAxis.setDrawGridLines(true); // 根据需要设置网格线

        // 如果需要，您可以关闭右侧 Y 轴
        scatterChart.getAxisRight().setEnabled(false);

        // 刷新图表
        scatterChart.invalidate(); // refresh
    }



}