package com.example.myapplication.ui.record;

import static java.security.AccessController.getContext;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecordViewModel extends ViewModel {

    public enum SearchType {
        WEEK, MONTH, YEAR
    };

    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public RecordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Record fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<RunningRecord> getRecordList(Context context, SearchType searchType) {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(context);
//        List<RunningRecord> runningRecords = dataBaseHelper.loadAllRecords(getContext());
//        Log.d("RecordDetailFragment", "runningRecords: " + runningRecords.stream().count());


        Calendar calendar = Calendar.getInstance();
        switch (searchType) {
            case WEEK:
                calendar.add(Calendar.DATE, -7);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, -30);
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, -365);
                break;
        }
        Date yesterday = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return dataBaseHelper.loadRecordsByTimestamp(context, sdf.format(yesterday));
    }

    public double getTotalDistance(List<RunningRecord> runningRecords) {
        // add all distance from the runningRecords
        return runningRecords.stream().mapToDouble(record -> Double.parseDouble(record.getDistance())).sum();
    }

}