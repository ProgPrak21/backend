
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;

@CrossOrigin
@RestController
public class FacebookData {

    @Autowired
    DataManagementController dataManagementController;

    @Autowired
    CredentialsManager credentialsManager;

    @PostMapping(path = "/data/facebook/advertisement")
    public ResponseEntity<?> submit(@RequestParam(value = "facebook") MultipartFile file, ModelMap modelMap,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("facebook", file);

        JsonNode content = null;

        UserCredentials userCredentials = new UserCredentials();
        if (uid != null && secret != null) {
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);
        } else {
            userCredentials=credentialsManager.RandomUserCreds();
        }

        //retrieve json content
        if (!file.isEmpty()) {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader();

            content = objectReader.readTree(file.getBytes());
            content = content.at("/custom_audiences_v2");

            objectReader = objectMapper.readerFor(new TypeReference<LinkedList<String>>() {
            });
            LinkedList<String> stringList = objectReader.readValue(content);

            if (credentialsManager.checkPw(userCredentials)) {
                SQLData sqlData = new SQLData();
                sqlData.setStringList(stringList);
                sqlData.setCompany("facebook");
                sqlData.setCredentials(userCredentials);
                dataManagementController.ProfileInformation(sqlData);
                return ResponseEntity.ok(userCredentials);
            } else {
                return ResponseEntity.ok("User credentials were wrong!");
            }
        }
        return ResponseEntity.ok("There is no file attached!");
    }


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