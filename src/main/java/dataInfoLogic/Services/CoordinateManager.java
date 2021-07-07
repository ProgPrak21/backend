package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.Standort;
import dataInfoLogic.Entities.UserCoords;
import dataInfoLogic.Entities.UserCreds;
import dataInfoLogic.Repositories.UserCoordsRepository;
import dataInfoLogic.Repositories.UserCredsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Component
public class CoordinateManager {
    @Autowired
    UserCoordsRepository userCoordsRepository;
    @Async
    public void storeCoordinates(List<Standort> list,UserCredentials userCredentials,String company) {

        for(Standort standort: list){
            UserCoords coords=new UserCoords();
            coords.setCompany(company);
            coords.setLatitude(standort.latitude);
            coords.setLongitude(standort.longitude);
            coords.setCount(standort.anzahl);
            coords.setUser_id(userCredentials.getUid());
            userCoordsRepository.save(coords);
        }
    }
}
