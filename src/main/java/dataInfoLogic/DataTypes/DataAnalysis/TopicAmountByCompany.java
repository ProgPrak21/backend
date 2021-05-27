package dataInfoLogic.DataTypes.DataAnalysis;

import java.util.LinkedList;

public class TopicAmountByCompany {
    public LinkedList<TopicAmount> topicAmounts;
    String company;

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setTopicAmounts(LinkedList<TopicAmount> topicAmounts) {
        this.topicAmounts = topicAmounts;
    }

    public LinkedList<TopicAmount> getTopicAmounts() {
        return topicAmounts;
    }
}
