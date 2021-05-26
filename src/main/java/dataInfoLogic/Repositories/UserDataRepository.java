package dataInfoLogic.Repositories;

import dataInfoLogic.Entities.UserData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserDataRepository extends CrudRepository<UserData, Long> {
    /*@Query(value = "select slotId from slot where providerId = ?1")
    Long[] findSlotByProviderId(Long providerId);

     */

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM add_data WHERE user_id= ?1",nativeQuery = true)
    void clearUserData(String userId);


    /*
    @Modifying
    @Query(value= "DELETE FROM userdata WHERE userId= :userid")
    void clearUserData(@Param("userid") Long userId);

     */
}
