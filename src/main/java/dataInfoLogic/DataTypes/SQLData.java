package dataInfoLogic.DataTypes;

import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;

import java.util.LinkedList;

public class SQLData {
    private LinkedList<String> stringlist;
    private UserCredentials credentials;
    private String company;

    public LinkedList<String> getStringlist() {
        return stringlist;
    }

    public void setStringlist(LinkedList<String> stringlist) {
        this.stringlist = stringlist;
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
