
package dataInfoLogic.Controller.GraphQLController;

import dataInfoLogic.DataTypes.Device;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.Location;
import dataInfoLogic.Services.CredentialsManager;
import dataInfoLogic.Services.FrontEndRequests;
import dataInfoLogic.DataTypes.DataAnalysis.TopicPercentage;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.LinkedList;

@CrossOrigin
@Service
@GraphQLApi
public class UserDataDBControllerGraphQL {

    @Autowired
    CredentialsManager credentialsManager;

    private final FrontEndRequests frontEndRequests;

    public UserDataDBControllerGraphQL(FrontEndRequests frontEndRequests) {
        this.frontEndRequests = frontEndRequests;
    }

    @GraphQLQuery(name = "UserDataAnalyzed")
    public LinkedList<TopicPercentage> getUserDataAnalyzed(@GraphQLArgument(name = "userId") String userId, @GraphQLArgument(name = "secret") String secret) {

        //validate credentials, return null if wrong credentials
        if (userId != null && secret != null) {

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(userId);
            userCredentials.setSecret(secret);

            //check if user credentials are correct if they are provided
            if (!credentialsManager.checkPw(userCredentials)) {
                return null;
            }

        }

        return frontEndRequests.getUserTopicsummarized(userId);
    }

    @GraphQLQuery(name = "UserCoordsAnalyzed")
    public LinkedList<Location> getUserCoordsAnalyzed(@GraphQLArgument(name = "userId") String userId, @GraphQLArgument(name = "secret") String secret) {

        //validate credentials, return null if wrong credentials
        if (userId != null && secret != null) {

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(userId);
            userCredentials.setSecret(secret);

            //check if user credentials are correct if they are provided
            if (!credentialsManager.checkPw(userCredentials)) {
                return null;
            }

        }

        return frontEndRequests.getUserCoordsSummarized(userId);
    }
    @GraphQLQuery(name = "UserDevicesAnalyzed")
    public LinkedList<Device> getUserDevicesAnalyzed(@GraphQLArgument(name = "userId") String userId, @GraphQLArgument(name = "secret") String secret) {

        //validate credentials, return null if wrong credentials
        if (userId != null && secret != null) {

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(userId);
            userCredentials.setSecret(secret);

            //check if user credentials are correct if they are provided
            if (!credentialsManager.checkPw(userCredentials)) {
                return null;
            }

        }

        return frontEndRequests.getUserDevicesAnalyzed(userId);
    }


}