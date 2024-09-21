package com.example.happy_deer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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


}
