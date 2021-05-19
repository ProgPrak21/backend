package dataInfoLogic.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dataInfoLogic.DTO.FrontendDTO.UserCredentials;
import dataInfoLogic.DTO.PersonalData;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

@CrossOrigin
@RestController
public class FacebookData {

    @PostMapping(path = "/facebook/profile_information")
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


    @PostMapping(path = "/facebook/advertisement")
    public ResponseEntity<?> submit(@RequestParam(value = "facebook") MultipartFile file, ModelMap modelMap,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("facebook", file);

        JsonNode content = null;

        //retrieve json content
        if (!file.isEmpty()) {

            ObjectMapper objectMapper = new ObjectMapper();
            final ObjectReader objectReader = objectMapper.reader();

            content = objectReader.readTree(file.getBytes());
        }

        System.out.println(content);

        UserCredentials userCredentials = new UserCredentials();
        if(uid != null && secret != null){
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);
        }
        else{
            userCredentials.setUid("New");
            userCredentials.setSecret("credentials");
        }

        return ResponseEntity.ok(userCredentials);
    }

}
