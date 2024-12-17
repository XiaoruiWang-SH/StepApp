package com.example.myapplication.dataBase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class RunningRecord implements Parcelable {
    private int id;
    private String timestamp;
    private String day;
    private String month;
    private String year;
    private String place;
    private String trainingDuration;
    private String calories;
    private String distance;
    private String averageSpeed;
    private String detailKms;
    private String mapInfo;

    // Constructor
    public RunningRecord(int id, String timestamp, String day, String month, String year,
                         String place, String trainingDuration, String calories, String distance,
                         String averageSpeed, String detailKms, String mapInfo) {
        this.id = id;
        this.timestamp = timestamp;
        this.day = day;
        this.month = month;
        this.year = year;
        this.place = place;
        this.trainingDuration = trainingDuration;
        this.calories = calories;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.detailKms = detailKms;
        this.mapInfo = mapInfo;
    }

    // Parcelable Constructor to recreate object
    protected RunningRecord(Parcel in) {
        id = in.readInt();
        timestamp = in.readString();
        day = in.readString();
        month = in.readString();
        year = in.readString();
        place = in.readString();
        trainingDuration = in.readString();
        calories = in.readString();
        distance = in.readString();
        averageSpeed = in.readString();
        detailKms = in.readString();
        mapInfo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(timestamp);
        dest.writeString(day);
        dest.writeString(month);
        dest.writeString(year);
        dest.writeString(place);
        dest.writeString(trainingDuration);
        dest.writeString(calories);
        dest.writeString(distance);
        dest.writeString(averageSpeed);
        dest.writeString(detailKms);
        dest.writeString(mapInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RunningRecord> CREATOR = new Creator<RunningRecord>() {
        @Override
        public RunningRecord createFromParcel(Parcel in) {
            return new RunningRecord(in);
        }

        @Override
        public RunningRecord[] newArray(int size) {
            return new RunningRecord[size];
        }
    };

    // Getters
    public int getId() { return id; }
    public String getTimestamp() { return timestamp; }
    public String getDay() { return day; }
    public String getMonth() { return month; }
    public String getYear() { return year; }
    public String getPlace() { return place; }
    public String getTrainingDuration() { return trainingDuration; }
    public String getCalories() { return calories; }
    public String getDistance() { return distance; }
    public String getAverageSpeed() { return averageSpeed; }
    public String getDetailKms() { return detailKms; }
    public String getMapInfo() { return mapInfo; }

    // Converters
    public static RunningRecord convertFormMap(Map<String, String> map) {
        return new RunningRecord(
                Integer.parseInt(map.get("id")),
                map.get("timestamp"),
                map.get("day"),
                map.get("month"),
                map.get("year"),
                map.get("place"),
                map.get("trainingDuration"),
                map.get("calories"),
                map.get("distance"),
                map.get("averageSpeed"),
                map.get("detailKms"),
                map.get("mapInfo")
        );
    }

    public static Map<String, String> convertToMap(RunningRecord record) {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(record.getId()));
        map.put("timestamp", record.getTimestamp());
        map.put("day", record.getDay());
        map.put("month", record.getMonth());
        map.put("year", record.getYear());
        map.put("place", record.getPlace());
        map.put("trainingDuration", record.getTrainingDuration());
        map.put("calories", record.getCalories());
        map.put("distance", record.getDistance());
        map.put("averageSpeed", record.getAverageSpeed());
        map.put("detailKms", record.getDetailKms());
        map.put("mapInfo", record.getMapInfo());
        return map;
    }
}
