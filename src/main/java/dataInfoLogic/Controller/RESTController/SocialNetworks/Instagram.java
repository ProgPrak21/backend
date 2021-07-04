
package dataInfoLogic.Controller.RESTController.SocialNetworks;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dataInfoLogic.Services.CredentialsManager;
import dataInfoLogic.Services.DataManagement;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
public class Instagram {

    @Autowired
    DataManagement dataManagement;

    @Autowired
    CredentialsManager credentialsManager;

    @PostMapping(path = "/data/instagram/advertisement")
    public ResponseEntity<?> submit(@RequestParam(value = "file1", required = false) MultipartFile file1,
                                    @RequestParam(value = "file2", required = false) MultipartFile file2,
                                    @RequestParam(value = "file3", required = false) MultipartFile file3,
                                    @RequestParam(value = "file4", required = false) MultipartFile file4,
                                    @RequestParam(value = "file5", required = false) MultipartFile file5,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {


        if(file1 == null){
            new ResponseEntity<>("No file attached", HttpStatus.BAD_REQUEST);
        }

        //check if user credentials are provided or assign new ones
        UserCredentials userCredentials = new UserCredentials();
        if (uid != null && secret != null) {
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            //check if user credentials are correct if they are provided
            if (!credentialsManager.checkPw(userCredentials)) {
                return new ResponseEntity<>("Wrong user credentials", HttpStatus.BAD_REQUEST);
            }

        } else {
            userCredentials=credentialsManager.randomUserCred();
        }

        //create helpers
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader();

        //loop to retrieve content from all files
        for(int i = 0; i<5; i++){

            MultipartFile currentFile;
            if(i==0){
                currentFile = file1;
            }else if(i==2){
                currentFile = file2;
            }else if(i==3){
                currentFile = file3;
            }else if(i==4){
                currentFile = file4;
            }else{
                currentFile = file5;
            }

            //retrieve json content
            if (!(currentFile == null) && !currentFile.isEmpty()) {

                //retrieve json content
                if(Objects.equals(currentFile.getOriginalFilename(), "your_reels_topics.json")){

                    JsonNode content = objectReader.readTree(currentFile.getBytes());

                    //try different possibilities where to find content
                    if(!content.at("/topics_your_reels_topics").isEmpty()){
                        content = content.at("/topics_your_reels_topics");
                    }
                    //read data from file
                    BufferedReader br;
                    List<String> result = new ArrayList<>();
                    String json="";
                    try {

                        String line;
                        InputStream is = currentFile.getInputStream();
                        br = new BufferedReader(new InputStreamReader(is));
                        while ((line = br.readLine()) != null) {
                            result.add(line);
                            json+=line;
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                    LinkedList<String> stringList=new LinkedList<>();
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("topics_your_reels_topics");
                        for (int k = 0; k < array.length(); k++) {
                            String topic = array.getJSONObject(k).getJSONObject("string_map_data").getJSONObject("Name").getString("value");
                            stringList.add(topic);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //call data management controller for categorization
                    SQLData sqlData = new SQLData();
                    sqlData.setStringList(stringList);
                    sqlData.setCompany("instagram");
                    sqlData.setCredentials(userCredentials);
                    dataManagement.ProfileInformation(sqlData);



                }else if(Objects.equals(currentFile.getOriginalFilename(), "your_topics.json")){
                    JsonNode content = objectReader.readTree(currentFile.getBytes());

                    //try different possibilities where to find content
                    if(!content.at("/topics_your_reels_topics").isEmpty()){
                        content = content.at("/topics_your_reels_topics");
                    }
                    //read data from file
                    BufferedReader br;
                    List<String> result = new ArrayList<>();
                    String json="";
                    try {

                        String line;
                        InputStream is = currentFile.getInputStream();
                        br = new BufferedReader(new InputStreamReader(is));
                        while ((line = br.readLine()) != null) {
                            result.add(line);
                            json+=line;
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                    LinkedList<String> stringList=new LinkedList<>();
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("topics_your_topics");
                        for (int k = 0; k < array.length(); k++) {
                            String topic = array.getJSONObject(k).getJSONObject("string_map_data").getJSONObject("Name").getString("value");
                            stringList.add(topic);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //call data management controller for categorization
                    SQLData sqlData = new SQLData();
                    sqlData.setStringList(stringList);
                    sqlData.setCompany("instagram");
                    sqlData.setCredentials(userCredentials);
                    dataManagement.ProfileInformation(sqlData);
                }
            }
        }

        return new ResponseEntity<>(userCredentials, HttpStatus.OK);
    }
}