package dataInfoLogic.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataInfoLogic.DataTypes.CategoryInputString;
import dataInfoLogic.DataTypes.CategoryItem;
import dataInfoLogic.DataTypes.CategoryList;
import dataInfoLogic.DataTypes.PersonalData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

        //todo retrieve phone number

        return ResponseEntity.ok(personalData);
    }

    @PostMapping(path = "/facebook/advertisement")
    public ResponseEntity<?> ProfileInformation(@RequestBody CategoryInputString categoryInputString)  {


        try {
            RestTemplate restTemplate = new RestTemplate();
            String uriHubspotCompany = "https://datainfo.gwhy.de/categorization";
            HttpEntity<CategoryInputString> requestHubspotProvider = new HttpEntity<>(categoryInputString);
            ResponseEntity<CategoryList> response = restTemplate.exchange(uriHubspotCompany, HttpMethod.POST, requestHubspotProvider, CategoryList.class);

            CategoryList categoryList = response.getBody();
            LinkedList<CategoryItem> categoryItems = categoryList.getCategories();

            for(int i=0; i<categoryItems.size(); i++){

                CategoryItem categoryItem = categoryItems.get(i);

                System.out.println("Category Name: "+categoryItem.getName()+" Category confidence: "+categoryItem.getConfidence());
            }

            return ResponseEntity.ok(categoryItems);

        }catch (Exception exception){
            System.out.println(exception);

            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
    }

}
