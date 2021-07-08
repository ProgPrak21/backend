package dataInfoLogic.Repositories;

import dataInfoLogic.Entities.UserCoords;
import dataInfoLogic.Entities.UserCreds;

import dataInfoLogic.Entities.UserDevice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;
import java.util.LinkedList;

public interface UserDeviceRepository extends CrudRepository<UserDevice, Long> {

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM device_data WHERE user_id= ?1",nativeQuery = true)
    void clearUserDevice(String userId);

    @Query(value="SELECT * FROM device_data WHERE user_id like ?1 ",nativeQuery = true)
    LinkedList<UserCoords> getUserDevice(String userid);

}
