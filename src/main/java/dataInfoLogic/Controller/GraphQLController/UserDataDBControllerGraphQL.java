package dataInfoLogic.Controller.GraphQLController;

import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Repositories.UserDataRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.List;

@CrossOrigin
@Service
@GraphQLApi
public class UserDataDBControllerGraphQL {


    private final UserDataRepository userDataRepository;

    public UserDataDBControllerGraphQL(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }


    @GraphQLQuery(name = "UserData")
    public List<UserData> getAllUserData() {

        return (List<UserData>) userDataRepository.findAll();
    }


    @GraphQLQuery(name = "UserData")
    public List<UserData> getUserData(@GraphQLArgument(name = "userId") String userId, @GraphQLArgument(name = "secret") String secret) {

        return userDataRepository.getUserTopics(userId);
    }

}
