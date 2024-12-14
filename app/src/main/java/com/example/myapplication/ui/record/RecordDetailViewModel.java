package com.example.myapplication.ui.record;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecordDetailViewModel extends ViewModel {

    private final MutableLiveData<String> titleText;

    public RecordDetailViewModel() {
        titleText = new MutableLiveData<>();
        titleText.setValue("Overview");
    }

    public LiveData<String> getTitle() {
        return titleText;
    }
}