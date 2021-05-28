package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.CategorizationDTO.*;
import dataInfoLogic.DataTypes.DataAnalysis.TopicAmount;
import dataInfoLogic.DataTypes.DataAnalysis.TopicAmountByCompany;
import dataInfoLogic.DataTypes.DataAnalysis.TopicPercentage;
import dataInfoLogic.DataTypes.UserCompany;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.DataTypes.UserDataList;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;


@CrossOrigin
@RestController


public class SendMeText {

    @Autowired
    UserDataRepository userDataRepository;

    @PostMapping(path = "data/newentry")
    public ResponseEntity<?> ProfileInformation(@RequestBody String string) {
        //Gets Text, for example: Google Facebook
        //Puts the given words into a list
        //Sends them to DataManagementController and receives Answer back
        //Simulates other controllers sending Linkedlists of Strings to the DataManagementController

        String list[] = string.split(" ");
        LinkedList<String> stringlist = new LinkedList<>();
        for (int i = 2; i < list.length; i++) {
            stringlist.add(list[i]);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:8080/data/importsql";
            SQLData sqlData = new SQLData();
            sqlData.setCompany(list[0]);
            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(list[1]);
            userCredentials.setSecret("Hallo");
            sqlData.setCredentials(userCredentials);
            sqlData.setStringList(stringlist);

            HttpEntity<SQLData> request = new HttpEntity<>(sqlData);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

            return ResponseEntity.ok(response);

        } catch (Exception exception) {
            System.out.println(exception);
        }

        return null;
    }

    @PostMapping(path = "data/deluseridtest")
    public ResponseEntity<?> delUserId(@RequestBody String string) {

        CategoryInputString categoryInputString = new CategoryInputString();
        categoryInputString.setCategoryInput(string);
        try {
            HttpEntity<CategoryInputString> request = new HttpEntity<>(categoryInputString);
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:8080/data/clearuserdata";
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "data/delusercompanytest")
    public ResponseEntity<?> delUserCompany(@RequestBody String string) {
        String strings[] = string.split(" ");
        try {
            UserCompany delUserCompany = new UserCompany();
            delUserCompany.setUserid(strings[0]);
            delUserCompany.setCompany(strings[1]);
            HttpEntity<UserCompany> request = new HttpEntity<>(delUserCompany);
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:8080/data/delusercompany";
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "data/getusertopicstest")
    public ResponseEntity<?> getusertopics(@RequestBody String string) {
        CategoryInputString categoryInputString = new CategoryInputString();
        categoryInputString.setCategoryInput(string);
        try {
            HttpEntity<CategoryInputString> request = new HttpEntity<>(categoryInputString);
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:8080/data/getusertopics";
            ResponseEntity<UserDataList> response = restTemplate.exchange(uri, HttpMethod.POST, request, UserDataList.class);
            UserDataList userDataList=response.getBody();
            LinkedList<TopicPercentage> topicsPercentages= analysetopicdistribution(userDataList);
            return ResponseEntity.ok(topicsPercentages);
        } catch (Exception e) {
            return null;
        }
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
}