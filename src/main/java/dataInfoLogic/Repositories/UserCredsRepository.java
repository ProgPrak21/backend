package dataInfoLogic.Repositories;

import dataInfoLogic.Entities.UserCreds;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;
import java.util.LinkedList;

public interface UserCredsRepository extends CrudRepository<UserCreds, Long> {

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM user_credentials WHERE user_id= ?1",nativeQuery = true)
    void clearUserData(String userId);

    @Query(value="SELECT * FROM user_credentials WHERE user_id like ?1 ",nativeQuery = true)
    LinkedList<UserCreds> getUserCreds(String userid);

}
