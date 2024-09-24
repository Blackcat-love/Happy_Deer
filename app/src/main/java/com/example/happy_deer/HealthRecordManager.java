package com.example.happy_deer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HealthRecordManager {
    private DBOpenHelper dbOpenHelper;

    // 构造函数
    public HealthRecordManager(Context context) {
        // 初始化 DBOpenHelper
        dbOpenHelper = new DBOpenHelper(context, "HealthRecords.db", null, 1);
    }

//    根据年份月份查询所有数据
    public List<String> getRecordsForMonth(int year, int month) {
        List<String> records = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        // 格式化年份和月份以便于在SQL中使用
        String yearStr = String.valueOf(year);
        String monthStr = String.format("%02d", month); // 确保两位数格式，例如01, 02等

        // 查询语句
        String query = "SELECT * FROM HealthRecords WHERE Date LIKE ?";

        // 生成查询参数，确保年份和月份被格式化
        String[] selectionArgs = { yearStr + "-" + monthStr + "%" };

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                // 根据列索引获取数据，这里假设你只想要Date和Remarks列
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("Date"));
                @SuppressLint("Range") String remarks = cursor.getString(cursor.getColumnIndex("Remarks"));

                records.add("Date: " + date + ", Remarks: " + remarks);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }

//    根据年份月份查询当前数据的数量
public int getRecordCountForMonth(int year, int month) {
    int count = 0;
    SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

    // 格式化年份和月份以便于在SQL中使用
    String yearStr = String.valueOf(year);
    String monthStr = String.format("%02d", month); // 确保两位数格式，例如01, 02等

    // 查询语句，使用 COUNT 来获取记录数量
    String query = "SELECT COUNT(*) FROM HealthRecords WHERE Date LIKE ?";

    // 生成查询参数
    String[] selectionArgs = { yearStr + "-" + monthStr + "%" };

    Cursor cursor = db.rawQuery(query, selectionArgs);

    if (cursor.moveToFirst()) {
        // 获取 COUNT 的结果
        count = cursor.getInt(0); // 第一列是 COUNT(*)
    }

    cursor.close();
    db.close();
    return count;
}



    // 方法：根据输入的日期字符串返回星期几
    public static String getDayOfWeek(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            // 解析输入的日期字符串
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String day;

            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    day = "星期日";
                    break;
                case Calendar.MONDAY:
                    day = "星期一";
                    break;
                case Calendar.TUESDAY:
                    day = "星期二";
                    break;
                case Calendar.WEDNESDAY:
                    day = "星期三";
                    break;
                case Calendar.THURSDAY:
                    day = "星期四";
                    break;
                case Calendar.FRIDAY:
                    day = "星期五";
                    break;
                case Calendar.SATURDAY:
                    day = "星期六";
                    break;
                default:
                    day = "";
                    break;
            }

            return day;
        } catch (Exception e) {
            e.printStackTrace();
            return "无效日期"; // 返回无效日期提示
        }
    }





}
