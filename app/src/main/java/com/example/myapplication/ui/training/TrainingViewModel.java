package com.example.myapplication.ui.training;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrainingViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public TrainingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Training fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}