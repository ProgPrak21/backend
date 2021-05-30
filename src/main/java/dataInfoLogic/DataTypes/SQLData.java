package dataInfoLogic.DataTypes;

import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;

import java.util.LinkedList;

public class SQLData {
    private LinkedList<String> stringList;
    private UserCredentials credentials;
    private String company;

    public LinkedList<String> getStringList() {
        return stringList;
    }

    public void setStringList(LinkedList<String> stringList) {
        this.stringList = stringList;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
