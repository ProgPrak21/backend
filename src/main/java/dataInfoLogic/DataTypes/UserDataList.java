package dataInfoLogic.DataTypes;

import dataInfoLogic.Entities.UserData;
import org.apache.catalina.User;

import java.util.List;

public class UserDataList {
    List<UserData> userData;

    public List<UserData> getUserData() {
        return userData;
    }

    public void setUserData(List<UserData> userData) {
        this.userData = userData;
    }
}
