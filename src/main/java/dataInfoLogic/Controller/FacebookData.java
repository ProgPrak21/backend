package dataInfoLogic.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataInfoLogic.DataTypes.PersonalData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedList;


@RestController
public class FacebookData {

    @CrossOrigin
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

        //todo retrieve phone number


        return ResponseEntity.ok(personalData);
    }

}
