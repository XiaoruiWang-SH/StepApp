package com.example.myapplication.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import android.util.Log;

public class CustomInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 获取请求信息
        String requestInfo = "Sending request to " + request.url() +
                "\nHeaders: " + request.headers() +
                "\nBody: " + getRequestBody(request);
        Log.d("CustomInterceptor", requestInfo);

        // 执行请求并获取响应
        Response response = chain.proceed(request);

        // 获取响应信息
        String responseInfo = "Received response for " + response.request().url() +
                "\nStatus Code: " + response.code() +
                "\nResponse Body: " + getResponseBody(response);
        Log.d("CustomInterceptor", responseInfo);

        return response;
    }

    // 打印请求体
    private String getRequestBody(Request request) {
        try {
            if (request.body() != null) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                return buffer.readUtf8();
            }
        } catch (IOException e) {
            return "Error reading request body";
        }
        return "No Body";
    }

    // 打印响应体（限制大小）
    private String getResponseBody(Response response) {
        try {
            return response.peekBody(1024).string(); // 限制为 1024 字节
        } catch (IOException e) {
            return "Error reading response body";
        }
    }
}
