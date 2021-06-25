
package dataInfoLogic.Controller.SocialNetworks;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dataInfoLogic.Controller.DataManagement.CredentialsManager;
import dataInfoLogic.Controller.DataManagement.DataManagementController;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

@CrossOrigin
@RestController
public class Facebook {

    @Autowired
    DataManagementController dataManagementController;

    @Autowired
    CredentialsManager credentialsManager;

    @PostMapping(path = "/data/facebook/advertisement")
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
            userCredentials=credentialsManager.RandomUserCreds();
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
                if(Objects.equals(currentFile.getOriginalFilename(), "advertisers_who_uploaded_a_contact_list_with_your_information.json")){

                    JsonNode content = objectReader.readTree(currentFile.getBytes());

                    //try different possibilities where to find content
                    if(!content.at("/custom_audiences").isEmpty()){
                        content = content.at("/custom_audiences");
                    }
                    else if(!content.at("/custom_audiences_v2").isEmpty()){
                        content = content.at("/custom_audiences_v2");
                    }

                    //read content from file
                    objectReader = objectMapper.readerFor(new TypeReference<LinkedList<String>>() {
                    });
                    LinkedList<String> stringList = objectReader.readValue(content);


                    //call data management controller for categorization
                    SQLData sqlData = new SQLData();
                    sqlData.setStringList(stringList);
                    sqlData.setCompany("facebook");
                    sqlData.setCredentials(userCredentials);
                    dataManagementController.ProfileInformation(sqlData);

                }
            }
        }

        return new ResponseEntity<>(userCredentials, HttpStatus.OK);
    }


    //functionality to retrieve personal user data
    /*
    @PostMapping(path = "/data/facebook/profile_information")
    public ResponseEntity<?> ProfileInformation(@RequestBody JsonNode profile) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        PersonalData personalData = new PersonalData();

        personalData.setFirstName(objectMapper.writeValueAsString(profile.at("/profile/name/first_name")).replace("\"", ""));
        personalData.setLastName(objectMapper.writeValueAsString(profile.at("/profile/name/last_name")).replace("\"", ""));
        personalData.setCity(objectMapper.writeValueAsString(profile.at("/profile/current_city/name")).replace("\"", ""));

        //format birthday correctly
        String birthday = objectMapper.writeValueAsString(profile.at("/profile/birthday/day")) +"-"
                + objectMapper.writeValueAsString(profile.at("/profile/birthday/month")) +"-"
                + objectMapper.writeValueAsString(profile.at("/profile/birthday/year"));
        personalData.setBirthday(birthday);

        //format emails correctly
        String eMails = objectMapper.writeValueAsString(profile.at("/profile/emails/emails")).replace("\"", "");
        eMails = eMails.replace("[", "");
        eMails = eMails.replace("]", "");
        LinkedList<String> eMailList = new LinkedList<>(Arrays.asList(eMails.split(",")));
        personalData.seteMail(eMailList.get(0));

        return ResponseEntity.ok(personalData);
    }

     */
}