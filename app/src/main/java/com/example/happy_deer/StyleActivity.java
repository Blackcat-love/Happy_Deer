package com.example.happy_deer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StyleActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView main_backgound_img;
    private ImageView main_sidebar_bg;
    private int flag;
    private Toast currentToast; // 声明一个 Toast 对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_style);

        TextView upload_main_btn = findViewById(R.id.upload_main_button);
        main_backgound_img = findViewById(R.id.main_background_update);
        main_sidebar_bg = findViewById(R.id.main_Sidebar_background_update);
        TextView upload_main_Sidebar_btn = findViewById(R.id.upload_main_Sidebar_button);

        //加载背景图片
        load_settings_bg();

        upload_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                openGallery();
            }
        });

        upload_main_Sidebar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                openGallery();
            }
        });



    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            try {
                // 将选中的图像保存到内部存储
                String internalPath = saveImageToInternalStorage(imageUri);

                // 显示保存后的图像
                if (flag == 1){
                    main_backgound_img.setImageURI(Uri.parse(internalPath));
                }else {
                    main_sidebar_bg.setImageURI(Uri.parse(internalPath));
                }
                SharedPreferences sharedPreferences = getSharedPreferences("path", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                if (flag == 1){
                    edit.putString("mian_background_image_path",internalPath);
                }else {
                    edit.putString("mian_background_Sidebar_image_path",internalPath);
                }
                edit.apply();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) throws IOException {
        // 获取输入流
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        File internalFile = null;
       if (flag == 1){
           // 创建内部存储文件
           internalFile = new File(getFilesDir(), "main_background_image.jpg");
       }else {
           internalFile = new File(getFilesDir(), "main_background_Sidebar_image.jpg");
       }

        // 创建输出流
        FileOutputStream outputStream = new FileOutputStream(internalFile);

        // 将输入流中的数据写入输出流
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // 关闭流
        outputStream.flush();
        outputStream.close();
        inputStream.close();

//        Log
        Log.d("StyleActivity","当前Image保存到了:" + internalFile.getAbsolutePath());
        String hint = getString(R.string.Style_hint_text);
        showToast(hint);

        // 返回内部存储中文件的 URI
        return internalFile.getAbsolutePath();
    }

    private void load_settings_bg(){
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
            main_backgound_img.setImageURI(imageUri);
        } else {
            // 处理路径为 null 的情况，比如设置一个默认图像
            Log.d("StyleActivity","检测到没有设置背景图片，加载默认图片");
        }

        if (path_sidebar != null){
            File imageFile = new File(path_sidebar);
            // 转换为 Uri
            Uri imageUri = Uri.fromFile(imageFile);

            // 设置 ImageView 的图像
            main_sidebar_bg.setImageURI(imageUri);
        }else {
            // 处理路径为 null 的情况，比如设置一个默认图像
            Log.d("StyleActivity","检测到没有设置背景图片，加载默认图片");
        }
    }

    private void showToast(String message) {
        // 如果当前有 Toast 在显示，取消它
        if (currentToast != null) {
            currentToast.cancel(); // 取消当前的 Toast
        }
        // 创建新 Toast
        currentToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        currentToast.show(); // 显示新 Toast
    }

}