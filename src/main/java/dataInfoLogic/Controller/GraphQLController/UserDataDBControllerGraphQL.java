package dataInfoLogic.Controller.GraphQLController;

import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Repositories.UserDataRepository;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Service;


import java.util.List;

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

}
