package com.example.happy_deer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;

public class DevelopersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_developers);
        TextView logTextView = findViewById(R.id.logTextView);

        logAllThreads(logTextView);
//        获取按钮
        TextView DelDateBase = findViewById(R.id.DelDateBase);
        EditText inputText = findViewById(R.id.inputText);
        TextView delDateById_btn = findViewById(R.id.delDateById);
        TextView selectDateBase = findViewById(R.id.selectDateBase);

        DelDateBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                删除数据库
                boolean isDeleted = DevelopersActivity.this.deleteDatabase("HealthRecords.db");
                // 检查是否删除成功
                if (isDeleted) {
                    Log.d("Database", "Database deleted successfully");
                } else {
                    Log.d("Database", "Failed to delete database");
                }
            }
        });

        delDateById_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                获取input Text
                String text = inputText.getText().toString();
                if (TextUtils.isEmpty(text)){
//                    文本为空
                    inputText.setText("请输入ID以进行删除操作");
                }else {
//                    文本不为空
                    try {
                        int id = Integer.parseInt(text);
                        // 进一步处理删除记录的逻辑
                        deleteRecordById(id);
                    } catch (NumberFormatException e) {
                        // 输入的文本不是有效的整数
                        inputText.setText("请输入有效的数字作为ID");
                    }
                }
            }
        });

        selectDateBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                查询
                displayAllRecords(logTextView);

            }
        });


    }

    private void logAllThreads(TextView textView) {
        // 创建一个 StringBuilder 用于收集线程信息
        StringBuilder result = new StringBuilder();

        // 获取所有线程及其堆栈跟踪信息
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();

        for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
            Thread thread = entry.getKey();
            StackTraceElement[] stackTraceElements = entry.getValue();

            result.append("Thread Name: ").append(thread.getName()).append("\n");
            result.append("Thread State: ").append(thread.getState()).append("\n");

            for (StackTraceElement element : stackTraceElements) {
                result.append("  at ").append(element.toString()).append("\n");
            }
            result.append("\n"); // 为每个线程之间添加空行
        }

        // 将结果设置到 TextView
        textView.setText(result.toString());
    }

//    通过ID删除对应数据
    public void deleteRecordById(int id) {
        // 创建数据库
        DBOpenHelper dbOpenHelper = new DBOpenHelper(DevelopersActivity.this, "HealthRecords.db", null, 1);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        // 定义删除条件，使用问号占位符
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};

        // 执行删除操作
        int rowsDeleted = db.delete("HealthRecords", whereClause, whereArgs);

        // 检查删除结果
        if (rowsDeleted > 0) {
            Log.d("Database", "Record with ID " + id + " deleted successfully");
        } else {
            Log.d("Database", "No record found with ID " + id);
        }

        // 关闭数据库
        db.close();
    }

//    显示所有数据
public void displayAllRecords(TextView textView) {
    // 创建DBOpenHelper实例
    DBOpenHelper dbOpenHelper = new DBOpenHelper(DevelopersActivity.this, "HealthRecords.db", null, 1);

    // 获取可写数据库
    SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

    // 查询所有记录
    Cursor healthRecords = db.query("HealthRecords",
            new String[]{"ID", "Date", "Time", "Frequency", "Last_datetime", "Interval_time", "Remarks"},
            null, null, null, null, null);

    StringBuilder result = new StringBuilder(); // 用于存储查询结果

    // 遍历Cursor对象，获取每一行的数据
    while (healthRecords.moveToNext()) {
        int id = healthRecords.getInt(0);
        String date = healthRecords.getString(1);
        String time = healthRecords.getString(2);
        String frequency = healthRecords.getString(3);
        String lastDatetime = healthRecords.getString(4);
        String intervalTime = healthRecords.getString(5);
        String remarks = healthRecords.getString(6);

        // 将结果格式化并添加到StringBuilder中
        result.append("ID: ").append(id)
                .append(", Date: ").append(date)
                .append(", Time: ").append(time)
                .append(", Frequency: ").append(frequency)
                .append(", Last Datetime: ").append(lastDatetime)
                .append(", Interval Time: ").append(intervalTime)
                .append(", Remarks: ").append(remarks)
                .append("\n");
    }

    // 关闭Cursor和数据库
    healthRecords.close();
    db.close();

    // 将结果设置给TextView
    textView.setText(result.toString());
}





}