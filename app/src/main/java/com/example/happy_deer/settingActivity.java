package com.example.happy_deer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;


public class settingActivity extends AppCompatActivity {
    private int Developers_flag = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        TextView ThemeSetting = findViewById(R.id.themeSetting);
        TextView StyleSetting = findViewById(R.id.styleSetting);
        TextView dateSetting = findViewById(R.id.dateSetting);
        TextView version = findViewById(R.id.version);
        LinearLayout warning = findViewById(R.id.warning);

        ThemeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("提示","当前功能开发中");
            }
        });

        StyleSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("提示","当前功能开发中");
            }
        });

        dateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("提示","当前功能开发中");
            }
        });

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Developers_flag++;
                if (Developers_flag == 7){
                    Log.d("Setting","进入开发者模式");
                    warning.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"当前是开发者模式",Toast.LENGTH_LONG).show();
                }
                // 获取应用的包管理器
                PackageManager packageManager = getPackageManager();
                String packageName = getPackageName();

                try {
                    // 获取应用的信息
                    PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                    String versionName = packageInfo.versionName; // 获取版本名称
                    int versionCode = packageInfo.versionCode;   // 获取版本号

                    // 显示版本信息（可以根据需要选择显示方式，例如Toast或者对话框）
                    Toast.makeText(getApplicationContext(),
                            "当前版本: " + versionName,
                            Toast.LENGTH_LONG).show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Developers = new Intent(settingActivity.this, DevelopersActivity.class);
                startActivity(Developers);
            }
        });

    }

    public void showAlertDialog(String title,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确定”按钮后的处理逻辑（可选）
                        dialog.dismiss(); // 关闭对话框
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // 关闭对话框
                    }
                });
        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}