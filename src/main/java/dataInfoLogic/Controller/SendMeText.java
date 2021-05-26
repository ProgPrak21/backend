package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.CategorizationDTO.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataInfoLogic.DataTypes.DelUserCompany;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Entities.UserDataList;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


@CrossOrigin
@RestController


public class SendMeText {

    @Autowired
    UserDataRepository userDataRepository;

    @PostMapping(path = "data/text")
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
            sqlData.setStringlist(stringlist);

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
            DelUserCompany delUserCompany = new DelUserCompany();
            delUserCompany.setUserid(strings[0]);
            delUserCompany.setCompany(strings[1]);
            HttpEntity<DelUserCompany> request = new HttpEntity<>(delUserCompany);
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
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }
}