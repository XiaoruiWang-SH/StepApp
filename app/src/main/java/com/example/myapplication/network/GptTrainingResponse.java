package com.example.myapplication.network;

import java.util.List;

public class GptTrainingResponse {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }
    }

    public static class Message {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }

    public static class TrainingPlan {
        private String date;
        private String distance;
        private String pace;
        private String type;
        private String duration;
        private String notes;

        public TrainingPlan(String date, String distance, String pace, String type, String duration, String notes) {
            this.date = date;
            this.distance = distance;
            this.pace = pace;
            this.type = type;
            this.duration = duration;
            this.notes = notes;
        }

        // Getter 方法
        public String getTrainingDate() {
            return date;
        }

        public String getDistance() {
            return distance;
        }

        public String getPace() {
            return pace;
        }

        public String getTrainingType() {
            return type;
        }

        public String getEstimatedDuration() {
            return duration;
        }

        public String getNotes() {
            return notes;
        }
    }
}
