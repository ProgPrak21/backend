package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



@Component
@Async
public class CoordinateAnalyser {
    @Autowired
    CoordinateManager coordinateManager;
    public void storeCoordinates(HashMap<Integer,HashMap<Integer, Location>> locationsHashMap, UserCredentials userCredentials){
        LinkedList<Location> list=new LinkedList<>();
        for(HashMap<Integer, Location> hashMap:locationsHashMap.values()){
            list.addAll(hashMap.values());
        }
        Collections.sort(list);
        List<Location> list1000=list.subList(0,Math.min(1000,list.size()));
        LinkedList<Location> sumList=new LinkedList<>();
        int putin=0;
        for(Location location : list1000){
            for(Location compare: sumList){
                if(compare(location.latitude,compare.latitude) && compare(location.longitude,compare.longitude)){
                    compare.anzahl+= location.anzahl;
                    putin=1;
                    break;
                }
            }
            if(putin==0){
                sumList.add(location);
            }
            putin=0;
        }
        Collections.sort(sumList);
        List<Location> list100=sumList.subList(0,Math.min(100,sumList.size()));

        coordinateManager.storeCoordinates(list100,userCredentials);
    }
    public boolean compare(int a,int b){
        return a < b + 20 && a > b - 20;
    }
}
