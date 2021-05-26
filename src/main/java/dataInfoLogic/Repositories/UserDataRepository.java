package dataInfoLogic.Repositories;

import dataInfoLogic.Entities.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserDataRepository extends CrudRepository<UserData, Long> {
}
