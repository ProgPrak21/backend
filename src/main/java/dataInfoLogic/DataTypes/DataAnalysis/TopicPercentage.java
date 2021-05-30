package dataInfoLogic.DataTypes.DataAnalysis;

public class TopicPercentage {
    String topic;
    double percentage;

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
