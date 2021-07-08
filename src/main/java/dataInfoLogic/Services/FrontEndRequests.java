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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public class FrontEndRequests {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    UserCoordsRepository userCoordsRepository;

    @Autowired
    UserDeviceRepository userDeviceRepository;

    public LinkedList<TopicPercentage> getUserTopicsummarized(String userId){
        UserDataList userDataList=new UserDataList();
        userDataList.setUserData(userDataRepository.getUserTopics(userId));
        LinkedList<TopicPercentage> topicsPercentages= analysetopicdistribution(userDataList);
        LinkedList<TopicPercentage> topicsPercentageSummarized=new LinkedList<>();
        int used;
        for(TopicPercentage topicPercentage: topicsPercentages){
            used=0;
            for(TopicPercentage topicPercentageSumm: topicsPercentageSummarized){
                if(topicPercentageSumm.getTopic().equals(topicPercentage.getTopic().split("/")[1])){
                    topicPercentageSumm.setPercentage(topicPercentageSumm.getPercentage()+topicPercentage.getPercentage());
                    used=1;
                    break;
                }
            }
            if(used!=1){
                topicPercentage.setTopic(topicPercentage.getTopic().split("/")[1]);
                topicsPercentageSummarized.add(topicPercentage);

            }
        }
        return topicsPercentageSummarized;
    }

    public LinkedList<TopicPercentage> analysetopicdistribution(UserDataList userDataList){
        int putin=0;
        int putin1=0;
        //Resulting percentage
        LinkedList<TopicPercentage> topicsPercentages=new LinkedList<>();
        //All topics sorted by company
        LinkedList<TopicAmountByCompany> topicAmountByCompanies=new LinkedList<>();

        for(UserData userData: userDataList.getUserData()){
            putin=0;
            putin1=0;
            for(TopicAmountByCompany topicAmountByCompany: topicAmountByCompanies) {
                if(topicAmountByCompany.getCompany().equals(userData.getCompany())) {
                    LinkedList<TopicAmount> topicAmounts=topicAmountByCompany.topicAmounts;
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
                    putin1=1;
                }
            }
            if(putin1!=1){
                TopicAmountByCompany topicAmountByCompany=new TopicAmountByCompany();
                LinkedList<TopicAmount> topicAmounts=new LinkedList<>();
                TopicAmount topicAmount= new TopicAmount();
                topicAmount.setTopic(userData.getTopic());
                topicAmount.setAmount(userData.getWeight());
                topicAmounts.add(topicAmount);
                topicAmountByCompany.setTopicAmounts(topicAmounts);
                topicAmountByCompany.setCompany(userData.getCompany());
                topicAmountByCompanies.add(topicAmountByCompany);
            }
        }
        int companies=topicAmountByCompanies.size();
        for(TopicAmountByCompany topicAmountByCompany: topicAmountByCompanies) {
            LinkedList<TopicAmount> topicAmounts1=topicAmountByCompany.getTopicAmounts();
            Double totalweight = topicAmounts1.stream().mapToDouble(x -> x.getAmount()).reduce(0, (a, b) -> a + b);
            //For every topic in a company
            for (TopicAmount topicAmount : topicAmounts1) {
                putin=0;
                for (TopicPercentage topicPercentage : topicsPercentages) {
                    if(topicPercentage.getTopic().equals(topicAmount.getTopic())){
                        putin=1;
                        topicPercentage.setPercentage(topicPercentage.getPercentage() + topicAmount.getAmount()/totalweight/companies);
                    }
                }
                if(putin!=1){
                    TopicPercentage topicPercentage = new TopicPercentage();
                    topicPercentage.setPercentage(topicAmount.getAmount() / totalweight/companies);
                    topicPercentage.setTopic(topicAmount.getTopic());
                    topicsPercentages.add(topicPercentage);
                }
            }
        }
        return topicsPercentages;
    }
    public LinkedList<Location> getUserCoordsSummarized(String userId){
        LinkedList<UserCoords> userCoords=userCoordsRepository.getUserCoords(userId);
        LinkedList<Location> locations =new LinkedList<>();
        for(UserCoords userCoords1: userCoords){
            Location newLocation =new Location(userCoords1.getLatitude(),userCoords1.getLongitude());
            newLocation.anzahl=userCoords1.getCount();
            newLocation.company=userCoords1.getCompany();
            locations.add(newLocation);

        }
        return locations;
    }
    public LinkedList<Device> getUserDevicesAnalyzed(String userId){
        LinkedList<UserDevice> userDevices=userDeviceRepository.getUserDevice(userId);
        LinkedList<Device> userDevice=new LinkedList<>();
        for(UserDevice userDevice1: userDevices){
            Device device=new Device();
            device.setCount(userDevice1.getCount());
            device.setName(userDevice1.getPlatform());
            device.setCompany(userDevice1.getCompany());
            userDevice.add(device);
        }
        return userDevice;
    }
}
