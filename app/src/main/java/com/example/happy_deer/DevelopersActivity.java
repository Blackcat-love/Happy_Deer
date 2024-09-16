package com.example.happy_deer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        TextView delAllFile = findViewById(R.id.delAllFile);
        TextView SelectFilesAllDate = findViewById(R.id.SelectFilesAllDate);

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

        delAllFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllFilesInInternalStorage();
            }
        });

        SelectFilesAllDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> files = listAllFilesInInternalStorage();
                StringBuilder fileLog = new StringBuilder(); // 使用 StringBuilder 来构建文件列表
                // 遍历文件名列表
                for (String fileName : files) {
                    Log.d("File", fileName); // 打印文件名
                    fileLog.append(fileName).append("\n"); // 将文件名添加到 StringBuilder，并换行
                }
                //打印到TextView
                logTextView.setText(fileLog.toString());
                // 显示文件数量
                Toast.makeText(DevelopersActivity.this, "Total files: " + files.size(), Toast.LENGTH_SHORT).show();
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

    public void deleteAllFilesInInternalStorage() {
        // 获取内部存储的 files 目录
        File directory = getFilesDir();

        // 检查目录是否存在且是一个目录
        if (directory.exists() && directory.isDirectory()) {
            // 获取目录下所有文件
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    // 如果是文件，则删除
                    if (file.isFile()) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            Log.d("Delete File", file.getName() + " deleted successfully.");
                        } else {
                            Log.d("Delete File", "Failed to delete " + file.getName());
                        }
                    }
                }
            }
        }
    }

    public List<String> listAllFilesInInternalStorage() {
        List<String> fileList = new ArrayList<>();

        // 获取内部存储的 files 目录
        File directory = getFilesDir();

        // 检查目录是否存在且是一个目录
        if (directory.exists() && directory.isDirectory()) {
            // 获取目录下所有文件
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    // 将文件名添加到列表中
                    fileList.add(file.getName());
                }
            }
        }

        return fileList; // 返回包含所有文件名的列表
    }

}