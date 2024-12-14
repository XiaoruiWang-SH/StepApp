package com.example.myapplication.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GptTrainingService {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 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"
    })
    @POST("v1/chat/completions")
    Call<GptTrainingResponse> getTrainingPlan(@Body GptTrainingRequest request);
}
