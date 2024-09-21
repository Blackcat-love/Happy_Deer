package com.example.happy_deer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private final Handler handler = new Handler();
    private Runnable runnable;
    private TextView btn_start;
    private TextView home;
    private TextView navSettings;
    private TextView navAbout;
    private DrawerLayout drawerLayout;
    private ImageView open_menu;
//    恢复时间
// 设置恢复时间为 3 天, 2 小时, 5 分钟
    private int restoreDays;
    private int restoreHours;
    private int restoreMinutes;
    private ImageView main_bg;
    private ImageView main_sidbar_bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn_start = findViewById(R.id.button_start);
        home = findViewById(R.id.nav_home);
        navSettings = findViewById(R.id.nav_settings);
        navAbout = findViewById(R.id.nav_about);
        drawerLayout = findViewById(R.id.drawer_layout);
        open_menu = findViewById(R.id.start_menu);
        main_bg = findViewById(R.id.main_background);
        main_sidbar_bg = findViewById(R.id.main_sidbar_bg);


        // 执行第一次检查
        checkAndUpdateData();

        //执行背景检测
        load_main_bg();

        // 每60秒执行一次
        runnable = new Runnable() {
            @Override
            public void run() {
                checkAndUpdateData();
                handler.postDelayed(this, 60000); // 60秒后再次执行
            }
        };
        handler.postDelayed(runnable, 60000); // 初始延迟60秒开始执行

//        点击开始锻炼按钮后
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime now = LocalDateTime.now();
                    HashMap<String, String> latestRecord = selectNewDate();
                    if (!latestRecord.isEmpty()) {
                        String id = latestRecord.get("ID");
                        String date = latestRecord.get("Date");
                        String time = latestRecord.get("Time");
                        String frequency = latestRecord.get("Frequency");
                        String lastDatetime = latestRecord.get("Last_datetime");
                        String intervalTime = latestRecord.get("Interval_time");
                        String remarks = latestRecord.get("Remarks");
                        // 拼接字符串
                        StringBuilder logMessage = new StringBuilder();
                        logMessage.append("最新一条数据:\n")
                                .append("ID: ").append(id).append("\n")
                                .append("Date: ").append(date).append("\n")
                                .append("Time: ").append(time).append("\n")
                                .append("Frequency: ").append(frequency).append("\n")
                                .append("Last Datetime: ").append(lastDatetime).append("\n")
                                .append("Interval Time: ").append(intervalTime).append("\n")
                                .append("Remarks: ").append(remarks);
                        Log.d("最新一条数据",logMessage.toString());
//                        新数据添加
                        saveDateTime(date,time,frequency);
                        //添加完成后禁用
                        btn_start.setEnabled(false);
                        btn_start.setText("今日已锻炼");
                        checkAndUpdateData();
                    } else {
                        // 没有数据的处理逻辑
                        Log.d("警告", "没有查询到数据");
                        Log.d("ADD","准备开始添加第一条数据");
                            // 提取日期和时间
                            String datePart = now.toLocalDate().toString(); // 获取日期部分（yyyy-MM-dd）
                            String timePart = now.toLocalTime().toString(); // 获取时间部分（HH:mm:ss）
                            // 自定义格式化
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            // 格式化日期和时间
                            String formattedDate = now.toLocalDate().format(dateFormatter);
                            String formattedTime = now.toLocalTime().format(timeFormatter);
                            // 输出结果
                            System.out.println("当前日期: " + formattedDate);
                            System.out.println("当前时间: " + formattedTime);
//                   创建数据库
                            DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this, "HealthRecords.db", null, 1);
                            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//                   创建存放数据的ConTenValues对象
                            ContentValues values = new ContentValues();
                            values.put("Date",formattedDate);
                            values.put("Time",formattedTime);
                            values.put("Frequency",1);
                            values.put("Remarks","测试数据");
                            db.insert("HealthRecords",null,values);
//                            添加完成后禁用
                        btn_start.setEnabled(false);
                    }
                }
            }
        });

//        点击Home按钮
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("点击触发","Home被点击了");
                bounceAnimation(home);
            }
        });

//        点击Settings按钮
        navSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("点击触发","Settings被点击了");
                bounceAnimation(navSettings);
                Intent setting = new Intent(MainActivity.this, settingActivity.class);
                startActivity(setting);
            }
        });

//        点击About按钮
        navAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("点击触发","About被点击了");
                bounceAnimation(navAbout);
                Intent about = new Intent(MainActivity.this, aboutActivity.class);
                startActivity(about);
            }
        });

//          打开菜单
        open_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

    }

    //    保存数据
    public void saveDateTime(String Date,String Time,String Frequency){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // 获取当前日期和时间
            LocalDateTime now = LocalDateTime.now();
            // 提取日期和时间
            String datePart = now.toLocalDate().toString(); // 获取日期部分（yyyy-MM-dd）
            String timePart = now.toLocalTime().toString(); // 获取时间部分（HH:mm:ss）
            // 自定义格式化
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            // 格式化日期和时间
            String formattedDate = now.toLocalDate().format(dateFormatter);
            String formattedTime = now.toLocalTime().format(timeFormatter);
            // 输出结果
            System.out.println("当前日期: " + formattedDate);
            System.out.println("当前时间: " + formattedTime);
//                   创建数据库
            DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this, "HealthRecords.db", null, 1);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//                   创建存放数据的ConTenValues对象
            ContentValues values = new ContentValues();
            values.put("Date",formattedDate);
            values.put("Time",formattedTime);
            if (formattedDate.equals(Date)){
//                如果是当天
                values.put("Frequency",String.valueOf(Integer.parseInt(Frequency) + 1));
            }else {
                values.put("Frequency",1);
            }
//            以前的时间
            String lastDatetime = Date + " " + Time;
//            现在的时间
            String surDatetime = formattedDate + " " + formattedTime;
            values.put("Last_datetime",lastDatetime);
//            计算间隔时间
            int minute = calculateMinutesDifference(lastDatetime, surDatetime);
            values.put("Interval_time",minute);
            Log.d("分钟间隔",String.valueOf(minute));
            values.put("Remarks","测试数据");
            db.insert("HealthRecords",null,values);
        }
    }

    // 查询最新的一条数据并返回
    public HashMap<String, String> selectNewDate() {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this, "HealthRecords.db", null, 1);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        // 修改查询，只获取最新的一条记录
        Cursor healthRecords = db.query("HealthRecords",
                new String[]{"ID", "Date", "Time", "Frequency", "Last_datetime", "Interval_time", "Remarks"},
                null, null, null, null, "Last_datetime DESC", "1"); // 按 Last_datetime 倒序排序，并限制为 1 条记录
        HashMap<String, String> recordData = new HashMap<>();
        if (healthRecords.moveToFirst()) { // 如果有数据
            @SuppressLint("Range") String id = healthRecords.getString(healthRecords.getColumnIndex("ID"));
            @SuppressLint("Range") String date = healthRecords.getString(healthRecords.getColumnIndex("Date"));
            @SuppressLint("Range") String time = healthRecords.getString(healthRecords.getColumnIndex("Time"));
            @SuppressLint("Range") String frequency = healthRecords.getString(healthRecords.getColumnIndex("Frequency"));
            @SuppressLint("Range") String lastDatetime = healthRecords.getString(healthRecords.getColumnIndex("Last_datetime"));
            @SuppressLint("Range") String intervalTime = healthRecords.getString(healthRecords.getColumnIndex("Interval_time"));
            @SuppressLint("Range") String remarks = healthRecords.getString(healthRecords.getColumnIndex("Remarks"));
            // 将数据加入 HashMap
            recordData.put("ID", id);
            recordData.put("Date", date);
            recordData.put("Time", time);
            recordData.put("Frequency", frequency);
            recordData.put("Last_datetime", lastDatetime);
            recordData.put("Interval_time", intervalTime);
            recordData.put("Remarks", remarks);
            String va = "id=" + id + " date:" + date + " time:" + time + " frequency:" + frequency +
                    " last_datetime:" + lastDatetime + " interval_time:" + intervalTime +
                    " 备注:" + remarks;
            Log.i("数据表", va);
        }
        healthRecords.close();
        return recordData; // 返回存储最新记录的 HashMap，可能是空的
    }

//    计算间隔时间返回分钟
public static int calculateMinutesDifference(String lastDateTime, String currentDateTime) {
    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
        // 解析字符串为日期对象
        Date lastDate = sdf.parse(lastDateTime);
        Date currentDate = sdf.parse(currentDateTime);
        // 计算时间差（毫秒）
        long diffInMillis = currentDate.getTime() - lastDate.getTime();
        // 转换为分钟并返回
        return (int) TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
    } catch (ParseException e) {
        e.printStackTrace(); // 打印异常信息
        return 0; // 返回0或根据需要处理错误
    }
}

//删除表中所有数据
    public void deleteAllRecords() {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this, "HealthRecords.db", null, 1);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        // 开始事务，以确保操作的原子性
        db.beginTransaction();
        try {
            // 执行删除所有记录的 SQL 语句
            db.execSQL("DELETE FROM HealthRecords");
            // 设置事务成功
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息，可以根据需要进行处理
        } finally {
            // 结束事务
            db.endTransaction();
        }
        db.close(); // 关闭数据库连接
    }

//    检查时间和数据的更新情况
    private void checkAndUpdateData() {
        //        获取文本组件-当前时间距离
        TextView textDistance = (TextView) findViewById(R.id.text_distance);
        //        获取文本组件-当前建议
        TextView text_hp = findViewById(R.id.text_hp);
        //        开机获取上次最新数据然后计算间隔
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime First_time  = LocalDateTime.now();
            // 自定义格式化
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            // 格式化日期和时间
            String formattedDate = First_time.toLocalDate().format(dateFormatter);
            String formattedTime = First_time.toLocalTime().format(timeFormatter);
            HashMap<String, String> latestRecord = selectNewDate();
            if (!latestRecord.isEmpty()) {
                String id = latestRecord.get("ID");
                String date = latestRecord.get("Date");
                String time = latestRecord.get("Time");
                String frequency = latestRecord.get("Frequency");
                String lastDatetime = latestRecord.get("Last_datetime");
                String intervalTime = latestRecord.get("Interval_time");
                String remarks = latestRecord.get("Remarks");
                String last_time = date + " " + time;
                String now_time = formattedDate + " " + formattedTime;
                int totalMinutes = calculateMinutesDifference(last_time, now_time);
                Log.d("距离上次锻炼",String.valueOf(totalMinutes));
//                计算打印到主页的时间
                loadRestoreTime();
//                获取设定好的时间阈值
                SharedPreferences sharedPreferences = getSharedPreferences("Interval_time", MODE_PRIVATE);
                String share_Days = sharedPreferences.getString("Days", "3");
                String share_Hours = sharedPreferences.getString("Hours", "0");
                String share_Minutes = sharedPreferences.getString("Minutes", "0");
                Log.i("MainActivity","获取时间阈值:" + share_Days + "Day" + share_Hours + "Hours" + share_Minutes + "Minutes");
                restoreDays = Integer.parseInt(share_Days);
                restoreHours = Integer.parseInt(share_Hours);
                restoreMinutes = Integer.parseInt(share_Minutes);
                // 将恢复时间转化为总分钟
                int requiredRestoreMinutes = (restoreDays * 24 * 60) + (restoreHours * 60) + restoreMinutes;
                // 计算小时数和剩余分钟数
                int hours = totalMinutes / 60;
                int remainingMinutes = totalMinutes % 60;
                // 计算天数和剩余小时数
                int days = hours / 24;
                int remainingHours = hours % 24;
                Log.d("Last_time",last_time);
                Log.d("now_time",now_time);
//                从资源中获取字符串
                String Interval_Time = getResources().getString(R.string.Interval_time);
                String formattedText = String.format(Interval_Time, days, remainingHours, remainingMinutes);
                textDistance.setText(formattedText);
//                获取状态字符串
                String be_ready = getResources().getString(R.string.Be_ready);
                String not_ready = getResources().getString(R.string.Not_ready);
                // 判断是否恢复 HP
                if (totalMinutes >= requiredRestoreMinutes) {
                    text_hp.setText(be_ready);
                } else {
                    text_hp.setText(not_ready);
                }
            }else {
                Log.d("警告", "没有查询到数据");
                Log.d("判断","第一次使用软件");
                textDistance.setText("欢迎使用小鹿健康小助手");
                text_hp.setText("点击按钮进行记录");
            }
        }
    }

    // 方法用于加载恢复时间设置
    private void loadRestoreTime() {
        SharedPreferences sharedPreferences = getSharedPreferences("recovery_time", MODE_PRIVATE);
        restoreDays = sharedPreferences.getInt("restoreDays", 3); // 默认值为 3
        restoreHours = sharedPreferences.getInt("restoreHours", 2); // 默认值为 2
        restoreMinutes = sharedPreferences.getInt("restoreMinutes", 5); // 默认值为 5
    }

    // 方法用于保存恢复时间设置
    private void saveRestoreTime(int days, int hours, int minutes) {
        SharedPreferences sharedPreferences = getSharedPreferences("recovery_time", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("restoreDays", days);
        editor.putInt("restoreHours", hours);
        editor.putInt("restoreMinutes", minutes);
        editor.apply(); // 应用更改
    }

    private void bounceAnimation(View view) {
        // 放大动画
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f); // 放大到1.2倍
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f); // 放大到1.2倍
        scaleUpX.setDuration(100); // 动画持续时间
        scaleUpY.setDuration(100);

        // 向上移动动画
        ObjectAnimator animatorUp = ObjectAnimator.ofFloat(view, "translationY", -30f);
        animatorUp.setDuration(100); // 动画持续时间

        // 向下恢复的缩小动画
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f); // 恢复到原始大小
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f); // 恢复到原始大小
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);

        // 向下恢复的位移动画
        ObjectAnimator animatorDown = ObjectAnimator.ofFloat(view, "translationY", 0f);
        animatorDown.setDuration(100);

        // 动画顺序执行
        scaleUpX.start();
        scaleUpY.start();

        // 在放大动画结束后开始向上移动动画
        AnimatorSet upAnimatorSet = new AnimatorSet();
        upAnimatorSet.playTogether(scaleUpX, scaleUpY, animatorUp);
        upAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 在向上移动结束后开始缩小并向下恢复
                AnimatorSet downAnimatorSet = new AnimatorSet();
                downAnimatorSet.playTogether(scaleDownX, scaleDownY, animatorDown);
                downAnimatorSet.start();
            }
        });

        upAnimatorSet.start();
    }

    private void load_main_bg(){
        SharedPreferences sharedPreferences = getSharedPreferences("path", MODE_PRIVATE);
        String path_main = sharedPreferences.getString("mian_background_image_path", null);
        String path_sidebar = sharedPreferences.getString("mian_background_Sidebar_image_path", null);
        // 检查 path 是否有效
        if (path_main != null) {
            // 创建 File 对象
            File imageFile = new File(path_main);

            // 转换为 Uri
            Uri imageUri = Uri.fromFile(imageFile);

            // 设置 ImageView 的图像
            main_bg.setImageURI(imageUri);
        } else {
            // 处理路径为 null 的情况，比如设置一个默认图像
            Log.d("MainActivity","检测到没有设置背景图片，加载默认图片");
        }

        if (path_sidebar != null){
            File imageFile = new File(path_sidebar);
            // 转换为 Uri
            Uri imageUri = Uri.fromFile(imageFile);

            // 设置 ImageView 的图像
            main_sidbar_bg.setImageURI(imageUri);
        }else {
            // 处理路径为 null 的情况，比如设置一个默认图像
            Log.d("MainActivity","检测到没有设置背景图片，加载默认图片");
        }
    }


}