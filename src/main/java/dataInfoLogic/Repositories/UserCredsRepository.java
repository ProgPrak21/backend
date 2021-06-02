package dataInfoLogic.Repositories;

import dataInfoLogic.Entities.UserCreds;
import dataInfoLogic.Entities.UserData;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.Entities.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserCredsRepository extends CrudRepository<UserCreds, Long> {

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM add_data WHERE user_id= ?1",nativeQuery = true)
    void clearUserData(String userId);

    @Query(value="SELECT * FROM user_credentials WHERE user_id like ?1 ",nativeQuery = true)
    LinkedList<UserCreds> getUserCreds(String userid);

}
