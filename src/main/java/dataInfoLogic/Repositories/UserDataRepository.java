package dataInfoLogic.Repositories;

import dataInfoLogic.Entities.UserData;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

public interface UserDataRepository extends CrudRepository<UserData, Long> {
    /*@Query(value = "select slotId from slot where providerId = ?1")
    Long[] findSlotByProviderId(Long providerId);

     */

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM add_data WHERE user_id= ?1",nativeQuery = true)
    void clearUserData(String userId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM add_data WHERE user_id= ?1 AND company= ?2",nativeQuery = true)
    void clearUserCompany(String userid, String company);

    @Query(value="SELECT * FROM add_data WHERE user_id like ?1 ",nativeQuery = true)
    List<UserData> getUserTopics(String userid);

    /*
    @Modifying
    @Query(value= "DELETE FROM userdata WHERE userId= :userid")
    void clearUserData(@Param("userid") Long userId);

     */
}
