package com.example.happy_deer;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiManager {

    // 定义接口
    public interface ApiCallback {
        void onSuccess(String result); // 请求成功时调用
        void onFailure(Exception e);    // 请求失败时调用
    }

    public static void GetJiTang(ApiCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.oick.cn/yiyan/api.php"; // 替换为你的 API URL

        Request request = new Request.Builder()
                .url(url)
                .build();

        // 使用 enqueue 进行异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                callback.onFailure(e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    final String responseBody = response.body().string();
                    callback.onSuccess(responseBody);
                }else {
                    Log.e("UpdateApp","无法获取json");
                }
            }
        });
    }


}
