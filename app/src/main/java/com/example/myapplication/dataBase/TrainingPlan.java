package com.example.myapplication.dataBase;

public class TrainingPlan {
    private int id;
    private String trainingDate;
    private String distance;
    private String pace;
    private String trainingType;
    private String estimatedDuration;
    private String notes;

    public TrainingPlan(int id, String trainingDate, String distance, String pace, String trainingType, String estimatedDuration, String notes) {
        this.id = id;
        this.trainingDate = trainingDate;
        this.distance = distance;
        this.pace = pace;
        this.trainingType = trainingType;
        this.estimatedDuration = estimatedDuration;
        this.notes = notes;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public String getDistance() {
        return distance;
    }

    public String getPace() {
        return pace;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public String getNotes() {
        return notes;
    }
}
