package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.DataAnalysis.TopicAmount;
import dataInfoLogic.DataTypes.DataAnalysis.TopicAmountByCompany;
import dataInfoLogic.DataTypes.DataAnalysis.TopicPercentage;
import dataInfoLogic.DataTypes.Device;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.Location;
import dataInfoLogic.DataTypes.UserDataList;
import dataInfoLogic.Entities.UserCoords;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Entities.UserDevice;
import dataInfoLogic.Repositories.UserCoordsRepository;
import dataInfoLogic.Repositories.UserDataRepository;
import dataInfoLogic.Repositories.UserDeviceRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Repository
public class FrontEndRequests {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    UserCoordsRepository userCoordsRepository;

    @Autowired
    UserDeviceRepository userDeviceRepository;

    public LinkedList<TopicPercentage> getUserTopicsummarized(String userId) {
        UserDataList userDataList = new UserDataList();
        userDataList.setUserData(userDataRepository.getUserTopics(userId));
        LinkedList<TopicPercentage> topicsPercentages = analysetopicdistribution(userDataList);
        LinkedList<TopicPercentage> topicsPercentageSummarized = new LinkedList<>();
        int used;
        for (TopicPercentage topicPercentage : topicsPercentages) {
            used = 0;
            for (TopicPercentage topicPercentageSumm : topicsPercentageSummarized) {
                if (topicPercentageSumm.getTopic().equals(topicPercentage.getTopic().split("/")[1])) {
                    topicPercentageSumm.setPercentage(topicPercentageSumm.getPercentage() + topicPercentage.getPercentage());
                    used = 1;
                    break;
                }
            }
            if (used != 1) {
                topicPercentage.setTopic(topicPercentage.getTopic().split("/")[1]);
                topicsPercentageSummarized.add(topicPercentage);

            }
        }
        return topicsPercentageSummarized;
    }

    public LinkedList<TopicPercentage> analysetopicdistribution(UserDataList userDataList) {
        int putin = 0;
        int putin1 = 0;
        //Resulting percentage
        LinkedList<TopicPercentage> topicsPercentages = new LinkedList<>();
        //All topics sorted by company
        LinkedList<TopicAmountByCompany> topicAmountByCompanies = new LinkedList<>();

        for (UserData userData : userDataList.getUserData()) {
            putin = 0;
            putin1 = 0;
            for (TopicAmountByCompany topicAmountByCompany : topicAmountByCompanies) {
                if (topicAmountByCompany.getCompany().equals(userData.getCompany())) {
                    LinkedList<TopicAmount> topicAmounts = topicAmountByCompany.topicAmounts;
                    for (TopicAmount topicAmount : topicAmounts) {
                        if (topicAmount.getTopic().equals(userData.getTopic())) {
                            topicAmount.setAmount(topicAmount.getAmount() + userData.getWeight());
                            putin = 1;
                            break;
                        }
                    }
                    if (putin != 1) {
                        TopicAmount topicAmount = new TopicAmount();
                        topicAmount.setTopic(userData.getTopic());
                        topicAmount.setAmount(userData.getWeight());
                        topicAmounts.add(topicAmount);
                    }
                    putin1 = 1;
                }
            }
            if (putin1 != 1) {
                TopicAmountByCompany topicAmountByCompany = new TopicAmountByCompany();
                LinkedList<TopicAmount> topicAmounts = new LinkedList<>();
                TopicAmount topicAmount = new TopicAmount();
                topicAmount.setTopic(userData.getTopic());
                topicAmount.setAmount(userData.getWeight());
                topicAmounts.add(topicAmount);
                topicAmountByCompany.setTopicAmounts(topicAmounts);
                topicAmountByCompany.setCompany(userData.getCompany());
                topicAmountByCompanies.add(topicAmountByCompany);
            }
        }
        int companies = topicAmountByCompanies.size();
        for (TopicAmountByCompany topicAmountByCompany : topicAmountByCompanies) {
            LinkedList<TopicAmount> topicAmounts1 = topicAmountByCompany.getTopicAmounts();
            Double totalweight = topicAmounts1.stream().mapToDouble(x -> x.getAmount()).reduce(0, (a, b) -> a + b);
            //For every topic in a company
            for (TopicAmount topicAmount : topicAmounts1) {
                putin = 0;
                for (TopicPercentage topicPercentage : topicsPercentages) {
                    if (topicPercentage.getTopic().equals(topicAmount.getTopic())) {
                        putin = 1;
                        topicPercentage.setPercentage(topicPercentage.getPercentage() + topicAmount.getAmount() / totalweight / companies);
                    }
                }
                if (putin != 1) {
                    TopicPercentage topicPercentage = new TopicPercentage();
                    topicPercentage.setPercentage(topicAmount.getAmount() / totalweight / companies);
                    topicPercentage.setTopic(topicAmount.getTopic());
                    topicsPercentages.add(topicPercentage);
                }
            }
        }
        return topicsPercentages;
    }

    public List<Location> getUserCoordsSummarized(String userId) {
        LinkedList<UserCoords> userCoords = userCoordsRepository.getUserCoords(userId);
        System.out.println(userCoords.size());
        LinkedList<Location> locations = new LinkedList<>();
        HashMap<Integer, HashMap<Integer, HashMap<String,Location>>> locationsHashMap = new HashMap<>();
        //For all userCoords in database:
        for (UserCoords userCoords1 : userCoords) {
            //Look if there is an entry in hashtable
            Location location = getLocationInRange(userCoords1, locationsHashMap);
            //if yes:
            if (location != null) {
                //if the entry in hashtable already got a name
                System.out.println("NAME : " + userCoords1.getName());
                if (location.getName() != null && userCoords1.getName()!=null) {
                    location.anzahl+=userCoords1.getCount();
                }
                //otherwise put the name of usercoords in there
                else if(location.getName()==null && userCoords1.getName()!=null) {
                    System.out.println(userCoords1.getName());
                    location.name = userCoords1.getName();
                    location.anzahl += userCoords1.getCount();
                }
            //if not:
            }else{
                //Put it into the hashtable
                Location location1=new Location(userCoords1.getLatitude(),userCoords1.getLongitude());
                location1.anzahl=userCoords1.getCount();
                location1.company=userCoords1.getCompany();
                if(userCoords1.getName()!=null){
                    location1.name=userCoords1.getName();
                }
                put(location1.latitude,location1.longitude,location1.company,location1,locationsHashMap);
            }
        }
        //extract all locations from hashtable
        for(HashMap<Integer, HashMap<String,Location>> locat: locationsHashMap.values())
            for(HashMap<String,Location> locationHashMap: locat.values()){
                for(Location location: locationHashMap.values()){
                    locations.add(location);
                }
            }
        Collections.sort(locations);
        List<Location> locationList=locations.subList(0,100);
        return locationList;
    }

    public LinkedList<Device> getUserDevicesAnalyzed(String userId) {
        LinkedList<UserDevice> userDevices = userDeviceRepository.getUserDevice(userId);
        LinkedList<Device> userDevice = new LinkedList<>();
        for (UserDevice userDevice1 : userDevices) {
            Device device = new Device();
            device.setCount(userDevice1.getCount());
            device.setName(userDevice1.getPlatform());
            device.setCompany(userDevice1.getCompany());
            userDevice.add(device);
        }
        return userDevice;
    }

    public void put(Integer key1, Integer key2, String key3, Location value, HashMap<Integer, HashMap<Integer, HashMap<String,Location>>> locationsHashMap) {
        HashMap<Integer, HashMap<String,Location>> map = locationsHashMap.get(key1);
        if (map == null) {
            map = new HashMap<>();
            locationsHashMap.put(key1,map);
            put2(key2,key3,value,map);
        }else{
            put2(key2,key3,value,map);
        }
    }

    public void put2(Integer key1, String key2, Location value, HashMap<Integer, HashMap<String,Location>> mMap) {
        HashMap<String, Location> map = mMap.get(key1);
        if (map == null) {
            map = new HashMap<>();
            mMap.put(key1, map);
        }
        map.put(key2, value);
    }

    public Location get(Integer key1, Integer key2, String key3, HashMap<Integer, HashMap<Integer, HashMap<String,Location>>> locationsHashMap) {
        HashMap<Integer, HashMap<String,Location>> map = locationsHashMap.get(key1);
        if (map == null) {
            return null;
        } else {
            HashMap<String,Location> map1=map.get(key2);
            if(map1==null){
                return null;
            }else{
                return map1.get(key3);
            }
        }
    }

    public Location getLocationInRange(UserCoords userCoords,HashMap<Integer, HashMap<Integer, HashMap<String,Location>>> locationsHashMap){
        Location location=null;
        for(int i=-100;i<100;i++) {
            for (int k = -20; k < 20; k++) {
                location = get(userCoords.getLatitude() + i, userCoords.getLongitude() + k, userCoords.getCompany(), locationsHashMap);
                if (location != null) return location;
            }
        }

        return location;
    }
}
