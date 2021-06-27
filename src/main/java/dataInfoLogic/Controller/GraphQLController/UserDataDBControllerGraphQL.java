
package dataInfoLogic.Controller.GraphQLController;

import dataInfoLogic.Services.FrontEndRequests;
import dataInfoLogic.DataTypes.DataAnalysis.TopicPercentage;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Repositories.UserDataRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.LinkedList;
import java.util.List;

@CrossOrigin
@Service
@GraphQLApi
public class UserDataDBControllerGraphQL {


    private final UserDataRepository userDataRepository;
    private final FrontEndRequests frontEndRequests;

    public UserDataDBControllerGraphQL(UserDataRepository userDataRepository, FrontEndRequests frontEndRequests) {
        this.userDataRepository = userDataRepository;
        this.frontEndRequests = frontEndRequests;
    }


    @GraphQLQuery(name = "UserData")
    public List<UserData> getAllUserData() {

        return (List<UserData>) userDataRepository.findAll();
    }

    //todo add secret validation


    @GraphQLQuery(name = "UserData")
    public List<UserData> getUserData(@GraphQLArgument(name = "userId") String userId, @GraphQLArgument(name = "secret") String secret) {
        return userDataRepository.getUserTopics(userId);
    }


    @GraphQLQuery(name = "UserDataAnalyzed")
    public LinkedList<TopicPercentage> getUserDataAnalyzed(@GraphQLArgument(name = "userId") String userId, @GraphQLArgument(name = "secret") String secret) {
        return frontEndRequests.getUserTopicsummarized(userId);
    }


}