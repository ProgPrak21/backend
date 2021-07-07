package dataInfoLogic.Repositories;

import dataInfoLogic.Entities.UserCoords;
import dataInfoLogic.Entities.UserCreds;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;
import java.util.LinkedList;

public interface UserCoordsRepository extends CrudRepository<UserCoords, Long> {

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM coordinate_data WHERE user_id= ?1",nativeQuery = true)
    void clearUserCoords(String userId);

    @Query(value="SELECT * FROM coordinate_data WHERE user_id like ?1 ",nativeQuery = true)
    LinkedList<UserCoords> getUserCoords(String userid);

}
