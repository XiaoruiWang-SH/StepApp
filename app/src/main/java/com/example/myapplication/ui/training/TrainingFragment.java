package com.example.myapplication.ui.training;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.TrainingPlan;
import com.example.myapplication.databinding.FragmentTrainingBinding;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.GptTrainingRequest;
import com.example.myapplication.network.GptTrainingResponse;
import com.example.myapplication.network.GptTrainingService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrainingFragment extends Fragment {

    private TrainingViewModel mViewModel;
    private FragmentTrainingBinding binding;
    private ViewPager2 viewPager;
    private FloatingActionButton fabUpdatePlan;
    private DataBaseHelper dataBaseHelper;
    private ImageView imageBackground; // 使用成员变量 ImageView 作为背景图片

    public static TrainingFragment newInstance() {
        return new TrainingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        warmUpRequest();
        // 初始化数据库帮助类
        dataBaseHelper = DataBaseHelper.getInstance(requireContext());

        if (dataBaseHelper.loadAllTrainingPlans(requireContext()).isEmpty()) {
            dataBaseHelper.addMockTrainingPlans(requireContext());
        }

        // 获取 ViewModel 实例
        mViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);

        // 绑定视图
        binding = FragmentTrainingBinding.inflate(inflater, container, false);
        ConstraintLayout root = binding.getRoot();

        // 获取 ImageView 作为背景图片，使用成员变量
        imageBackground = root.findViewById(R.id.imageBackground);

        // 初始化 ViewPager2
        viewPager = binding.viewPagerTraining;
        setupViewPager(viewPager);

        // 初始化 FloatingActionButton
        fabUpdatePlan = binding.fabUpdatePlan;
        fabUpdatePlan.setOnClickListener(v -> {
            // 点击按钮请求新的训练计划
            requestNewTrainingPlan();
        });

        return root;
    }

    private void warmUpRequest() {
        GptTrainingService gptService = ApiClient.getRetrofitInstance().create(GptTrainingService.class);

        GptTrainingRequest request = new GptTrainingRequest(Collections.emptyList());
        gptService.getTrainingPlan(request).enqueue(new Callback<GptTrainingResponse>() {
            @Override
            public void onResponse(@NonNull Call<GptTrainingResponse> call, @NonNull Response<GptTrainingResponse> response) {
                Log.d("WarmUp", "Warm-up request successful.");
            }

            @Override
            public void onFailure(@NonNull Call<GptTrainingResponse> call, @NonNull Throwable t) {
                Log.w("WarmUp", "Warm-up request failed.", t);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    // 配置 ViewPager2，设置适配器
    private void setupViewPager(ViewPager2 viewPager) {
        List<TrainingPlan> plans = dataBaseHelper.loadAllTrainingPlans(requireContext());
        TrainingPagerAdapter adapter = new TrainingPagerAdapter(this, plans);
        viewPager.setAdapter(adapter);
    }

    private String extractJsonFromContent(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }

        int startIndex = content.indexOf("[");
        int endIndex = content.lastIndexOf("]");

        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
            Log.e("JsonExtraction", "Failed to find valid JSON array in content.");
            return null;
        }

        return content.substring(startIndex, endIndex + 1);
    }

    private void handleNewTrainingPlans(List<GptTrainingResponse.TrainingPlan> newPlans) {
        new Thread(() -> {
            for (GptTrainingResponse.TrainingPlan plan : newPlans) {
                TrainingPlan trainingPlan = new TrainingPlan(
                        0,
                        plan.getTrainingDate(),
                        plan.getDistance(),
                        plan.getPace(),
                        plan.getTrainingType(),
                        plan.getEstimatedDuration(),
                        plan.getNotes()
                );
                dataBaseHelper.addTrainingPlan(trainingPlan);
            }

            // 更新 UI
            requireActivity().runOnUiThread(() -> {
                List<TrainingPlan> updatedPlans = dataBaseHelper.loadAllTrainingPlans(requireContext());
                if (viewPager.getAdapter() instanceof TrainingPagerAdapter) {
                    ((TrainingPagerAdapter) viewPager.getAdapter()).updateData(updatedPlans);
                } else {
                    Log.e("AdapterError", "Adapter is not an instance of TrainingPagerAdapter.");
                }
            });
        }).start();
    }


    private void requestNewTrainingPlan() {
        List<GptTrainingRequest.RunningRecord> recentData = dataBaseHelper.getRecentTrainingData();
        GptTrainingRequest request = new GptTrainingRequest(recentData);

        // 使用 ApiClient 获取 Retrofit 实例
        GptTrainingService gptService = ApiClient.getRetrofitInstance().create(GptTrainingService.class);

        gptService.getTrainingPlan(request).enqueue(new Callback<GptTrainingResponse>() {
            @Override
            public void onResponse(@NonNull Call<GptTrainingResponse> call, @NonNull Response<GptTrainingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 获取 choices 列表
                    List<GptTrainingResponse.Choice> choices = response.body().getChoices();
                    if (choices == null || choices.isEmpty()) {
                        Log.e("ResponseError", "Choices are null or empty.");
                        return;
                    }

                    // 获取 content
                    String content = choices.get(0).getMessage().getContent();
                    if (content == null || content.isEmpty()) {
                        Log.e("ResponseError", "Content is null or empty.");
                        return;
                    }

                    // 提取 JSON 数据
                    String extractedJson = extractJsonFromContent(content);
                    if (extractedJson == null) {
                        Log.e("JsonExtractionError", "Failed to extract JSON from content.");
                        return;
                    }

                    try {
                        // 将提取的 JSON 转换为 Java 对象
                        List<GptTrainingResponse.TrainingPlan> newPlans = new Gson().fromJson(
                                extractedJson,
                                new TypeToken<List<GptTrainingResponse.TrainingPlan>>() {}.getType()
                        );

                        if (newPlans != null) {
                            handleNewTrainingPlans(newPlans);
                        } else {
                            Log.e("JsonParseError", "Parsed training plans are null.");
                        }
                    } catch (JsonSyntaxException e) {
                        Log.e("JsonParseError", "Failed to parse JSON: " + extractedJson, e);
                    }
                } else {
                    Log.e("APIError", "Response is not successful or body is null.");
                }
            }



            @Override
            public void onFailure(@NonNull Call<GptTrainingResponse> call, @NonNull Throwable t) {
                Log.e("GPT", "Request error", t);
                Toast.makeText(requireContext(), "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
