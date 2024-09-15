package com.example.happy_deer;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_style);

        TextView upload_main_btn = findViewById(R.id.upload_main_button);
        main_backgound_img = findViewById(R.id.main_background_update);

        upload_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                main_backgound_img.setImageURI(Uri.parse(internalPath));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) throws IOException {
        // 获取输入流
        InputStream inputStream = getContentResolver().openInputStream(imageUri);

        // 创建内部存储文件
        File internalFile = new File(getFilesDir(), "background_image.jpg");

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

        // 返回内部存储中文件的 URI
        return internalFile.getAbsolutePath();
    }
}