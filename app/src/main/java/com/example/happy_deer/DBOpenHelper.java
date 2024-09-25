package com.example.happy_deer;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class DBOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String DATABASE_NAME = "health_records.db";
    private static final int DATABASE_VERSION = 1;

    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

//    使用onCreate可以在初次使用数据库时，自动创建表，第二次启动则不会创建，自带了判断
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库sql语句并执行
        String sql = "CREATE TABLE HealthRecords (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Date DATE," +
                "Time TIME," +
                "Frequency INTEGER DEFAULT 1," +
                "Last_datetime DATETIME," +
                "Interval_time INTEGER," +
                "Remarks TEXT)";
        db.execSQL(sql);
    }
//    ID 是一个整数字段，自动递增，作为表的主键。
//    Date 是一个日期字段，用于存储记录发生的日期。
//    Time 是一个时间字段，用于存储记录发生的具体时间。
//    Frequency 是一个整数字段，用于记录同一天中的操作次数，默认值为1。
//    Last_datetime 是一个日期时间字段，用于存储上一次操作的时间。
//    Interval_time 是一个整数字段，用于存储两次操作之间的时间间隔（以分钟为单位）。
//    Remarks 是一个文本字段，用于存储任何额外的备注信息。

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public void readData() {
        SQLiteDatabase db = this.getReadableDatabase();
        // 使用 db 查询数据...
        db.close(); // 记得在操作完后关闭数据库
    }


}
