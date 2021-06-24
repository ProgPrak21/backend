package dataInfoLogic.DataTypes.DataAnalysis;

public class TopicAmount {
    String topic;
    double amount;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int compareTo(TopicAmount otherTopicAmount){
        if(otherTopicAmount.getAmount()<this.getAmount()){
            return 1;
        }else{
            return -1;
        }
    }
    public int compareTo(Object o) {
        return compareTo((TopicAmount) o);
    }
}
