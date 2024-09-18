package com.example.happy_deer;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UpdateApp {
    public static void fetchJsonData() {

        OkHttpClient client = new OkHttpClient();
        String url = "https://api.github.com/repos/NANAFREE/Happy_Deer/releases/latest"; // 替换为你的 API URL

        Request request = new Request.Builder()
                .url(url)
                .build();

        // 使用 enqueue 进行异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    final String responseBody = response.body().string();
                    // 在这里处理你的 JSON 数据
                    parseJson(responseBody);
                    // 为了更新 UI，记得回到主线程
                }else {
                    Log.e("UpdateApp","无法获取json");
                }
            }
        });

    }

    public static void parseJson(String responseBody) {
        try {
            // 将响应体转换为 JSONObject
            JSONObject jsonObject = new JSONObject(responseBody);

            // 获取 "tag_name" 的值
            String tagName = jsonObject.getString("tag_name");

            // 输出结果
            Log.i("TAG", "Tag Name: " + tagName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
