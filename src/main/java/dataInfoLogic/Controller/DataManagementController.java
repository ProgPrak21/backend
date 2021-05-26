package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.CategorizationDTO.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataInfoLogic.DataTypes.SQLData;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

public class DataManagementController {

    @Autowired
    UserDataRepository userDataRepository;

    @PostMapping(path = "/data/importsql")
    public ResponseEntity<?> ProfileInformation(@RequestBody SQLData sqlData) {
        //Receives a Linkedlist of Strings
        //For each word it sends a message to https://datainfo.gwhy.de/categorization to get categories of the word
        //Returns them in a Category List (later probably not needed)

        LinkedList<CategoryItem> categoryItems= new LinkedList<>();
        CategoryList return_categoryList=new CategoryList();
        for (String word :sqlData.getStringlist()) {
            try {
                String string=wordToString(word);
                CategoryInputString categoryInputString= new CategoryInputString();
                categoryInputString.setCategoryInput(string);
                RestTemplate restTemplate = new RestTemplate();
                String uri = "https://dara.gwhy.de/categorization";
                HttpEntity<CategoryInputString> request = new HttpEntity<>(categoryInputString);
                ResponseEntity<CategoryList> response = restTemplate.exchange(uri, HttpMethod.POST, request, CategoryList.class);

                CategoryList categoryList = response.getBody();
                for(CategoryItem item: categoryList.getCategories()){
                    categoryItems.add(item);
                }

            } catch (Exception exception) {
                System.out.println(exception);
            }
        }
        return_categoryList.setCategories(categoryItems);

        for(CategoryItem item:return_categoryList.getCategories()){
            UserData data = new UserData();
            data.setTopic(item.getName());
            data.setCompany(sqlData.getCompany());
            data.setUserId(sqlData.getCredentials().getUid());
            data.setWeight(1.0);
            //....
            userDataRepository.save(data);

            System.out.println(item.getName());
        }
        return ResponseEntity.ok("Inserted data successfully");
    }
    public String wordToString(String word){
        String string="";
        for(int i=0;i<20;i++){
            string+= word;
            string+= " ";
        }
        return string;
    }
}