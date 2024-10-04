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


    public interface VersionCallback {
        void onVersionFetched(String version);
    }

    public static void fetchJsonData(VersionCallback callback) {
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
                // 调用回调并传入错误信息
                callback.onVersionFetched("error");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    // 在这里处理你的 JSON 数据
                    String version = parseJson(responseBody);
                    // 调用回调并传入版本信息
                    callback.onVersionFetched(version);
                } else {
                    Log.e("UpdateApp", "无法获取json");
                    // 调用回调并传入错误信息
                    callback.onVersionFetched("error");
                }
            }
        });
    }

    public static String parseJson(String responseBody) {
        try {
            // 将响应体转换为 JSONObject
            JSONObject jsonObject = new JSONObject(responseBody);

            // 获取 "tag_name" 和 "name" 的值
            String tagName = jsonObject.getString("tag_name");
            String name = jsonObject.getString("name");
            // 输出结果
            Log.i("TAG", "Tag Name: " + tagName);
            Log.i("TAG", "Version Name: " + name);
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

}
