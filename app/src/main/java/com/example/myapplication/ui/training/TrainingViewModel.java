package com.example.myapplication.ui.training;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.TrainingPlan;

import android.content.Context;
import java.util.List;

public class TrainingViewModel extends ViewModel {

    private final MutableLiveData<List<TrainingPlan>> trainingPlans;
    private final MutableLiveData<String> updateStatus;

    public TrainingViewModel() {
        trainingPlans = new MutableLiveData<>();
        updateStatus = new MutableLiveData<>();
    }

    // 获取训练计划的 LiveData
    public LiveData<List<TrainingPlan>> getTrainingPlans() {
        return trainingPlans;
    }

    // 获取更新状态的 LiveData
    public LiveData<String> getUpdateStatus() {
        return updateStatus;
    }

    // 从数据库加载所有训练计划
    public void loadTrainingPlans(Context context) {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(context);
        List<TrainingPlan> plans = dataBaseHelper.loadAllTrainingPlans(context);
        trainingPlans.setValue(plans);
    }

    // 请求新的训练计划
    public void requestTrainingPlan() {
        // 假设这里是对 GPT-4 的请求，可以用网络请求的方式来实现
        // 在请求完成后调用 updateTrainingPlans() 更新计划

        // 模拟请求过程（可以使用 Retrofit 或其他网络库）
        new Thread(() -> {
            try {
                // 模拟网络请求的延迟
                Thread.sleep(2000);
                // 请求完成，假设成功返回新训练计划
                updateStatus.postValue("Update Successful");
                // 在此处可以进一步更新训练计划
            } catch (InterruptedException e) {
                updateStatus.postValue("Update Failed");
            }
        }).start();
    }

    // 更新训练计划
    public void updateTrainingPlans(List<TrainingPlan> newPlans) {
        trainingPlans.setValue(newPlans);
    }
}