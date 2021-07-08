package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.Location;
import dataInfoLogic.Entities.UserCoords;
import dataInfoLogic.Repositories.UserCoordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoordinateManager {
    @Autowired
    UserCoordsRepository userCoordsRepository;
    @Async
    public void storeCoordinates(List<Location> list, UserCredentials userCredentials) {

        for(Location location : list){
            UserCoords coords=new UserCoords();
            coords.setCompany(location.company);
            coords.setLatitude(location.latitude);
            coords.setLongitude(location.longitude);
            coords.setCount(location.anzahl);
            coords.setName(location.name);
            coords.setUser_id(userCredentials.getUid());
            userCoordsRepository.save(coords);
        }
    }
}
