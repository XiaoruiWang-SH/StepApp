package com.example.myapplication.network;

import java.util.List;

public class GptTrainingRequest {
    private String model;
    private List<Message> messages;
    private int max_tokens;
    private double temperature;

    public GptTrainingRequest(List<RunningRecord> recentRecords) {
        this.model = "gpt-4";
        this.messages = createMessages(recentRecords);
        this.max_tokens = 1000; // 控制返回内容的长度
        this.temperature = 0.7; // 控制生成内容的随机性
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public int getMaxTokens() {
        return max_tokens;
    }

    public double getTemperature() {
        return temperature;
    }

    private List<Message> createMessages(List<RunningRecord> recentRecords) {
        StringBuilder userContent = new StringBuilder("以下是最近的跑步记录：\n");

        // 添加跑步记录到消息中
        if (recentRecords.isEmpty()) {
            userContent.append("无最近记录，请根据以下要求生成一个新的训练计划。\n");
        } else {
            for (RunningRecord record : recentRecords) {
                userContent.append(String.format(
                        "日期: %s, 距离: %s km, 配速: %s, 地点: %s, 持续时间: %s, 卡路里: %s\n",
                        record.getTimestamp(),
                        record.getDistance(),
                        record.getAveSpeed(),
                        record.getPlace(),
                        record.getTrainingDuration(),
                        record.getCalories()
                ));
            }
        }

        // 添加固定格式要求
        userContent.append("\n请根据以上数据生成一个包含每天训练目标的 JSON 格式的训练计划。返回的 JSON 必须严格遵循以下结构：\n");
        userContent.append("[\n" +
                "    {\n" +
                "        \"date\": \"日期（yyyy-MM-dd）\",\n" +
                "        \"distance\": \"距离（公里）\",\n" +
                "        \"pace\": \"配速（分钟/公里，格式为 mm:ss）\",\n" +
                "        \"type\": \"类型（例如 Easy Run, Tempo Run, Long Run 等）\",\n" +
                "        \"duration\": \"持续时间（分钟或小时）\",\n" +
                "        \"notes\": \"备注（可选，训练的具体说明）\"\n" +
                "    },\n" +
                "    ...\n" +
                "]");

        // 提供生成的示例
        userContent.append("\n\n以下是一个符合上述要求的示例：\n");
        userContent.append("[\n" +
                "    {\n" +
                "        \"date\": \"2024-12-13\",\n" +
                "        \"distance\": \"5\",\n" +
                "        \"pace\": \"6:00\",\n" +
                "        \"type\": \"Easy Run\",\n" +
                "        \"duration\": \"30 mins\",\n" +
                "        \"notes\": \"Keep a steady, comfortable pace.\"\n" +
                "    }\n" +
                "]");

        return List.of(
                new Message("system", "你是一名专业的跑步教练，请按照指示生成训练计划。"),
                new Message("user", userContent.toString())
        );
    }

    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }

    public static class RunningRecord {
        private String timestamp;
        private String distance;
        private String aveSpeed;
        private String place;
        private String trainingDuration;
        private String calories;

        public RunningRecord(String timestamp, String distance, String aveSpeed, String place, String trainingDuration, String calories) {
            this.timestamp = timestamp;
            this.distance = distance;
            this.aveSpeed = aveSpeed;
            this.place = place;
            this.trainingDuration = trainingDuration;
            this.calories = calories;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getDistance() {
            return distance;
        }

        public String getAveSpeed() {
            return aveSpeed;
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
    }
}
