package com.example.myapplication.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;


public class ApiClient {
    private static final String BASE_URL = "https://api.openai.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // 创建日志拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 添加自定义拦截器
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // 打印请求和响应详细信息
                    .addInterceptor(new CustomInterceptor()) // 打印自定义日志
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
