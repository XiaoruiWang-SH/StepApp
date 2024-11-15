package com.example.myapplication.dataBase;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class RunningRecord {
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

    public RunningRecord(int id, String timestamp, String day, String month, String year, String place, String trainingDuration, String calories, String distance, String averageSpeed, String detailKms, String mapInfo) {
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

    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getPlace() {
        return place;
    }

    public String getTrainingDuration() {
        return trainingDuration;
    }

    public String getCalories() {
        return calories;
    }

    public String getDistance() {
        return distance;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public String getDetailKms() {
        return detailKms;
    }

    public String getMapInfo() {
        return mapInfo;
    }

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
