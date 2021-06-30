package dataInfoLogic.Repositories;

import dataInfoLogic.Entities.UserData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDataRepository extends CrudRepository<UserData, Long> {


    @Modifying
    @Transactional
    @Query(value = "delete from add_data where userId = ?1")
    void clearUserData(String userId);


    @Modifying
    @Transactional
    @Query(value = "delete from add_data where userId = ?1 and company = ?2")
    void clearUserCompany(String userId, String company);


    @Query(value="SELECT * FROM add_data WHERE user_id like ?1",nativeQuery = true)
    List<UserData> getUserTopics(String userid);

}
