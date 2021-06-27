
package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.CategorizationDTO.*;
import dataInfoLogic.DataTypes.SQLData;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@EnableAsync
public class DataManagement {

    @Autowired
    private UserDataRepository userDataRepository;

    @Async
    public void ProfileInformation(SQLData sqlData){
        ProfileInformationWorker(sqlData);
    }

    public void ProfileInformationWorker(SQLData sqlData) {

        //Receives a Linkedlist of Strings
        //For each word it sends a message to https://datainfo.gwhy.de/categorization to get categories of the word
        //Returns them in a Category List (later probably not needed)
        for (String word :sqlData.getStringList()) {
            System.out.println(word);
            try {
                String string=wordToString(word);
                CategoryInputString categoryInputString= new CategoryInputString();
                categoryInputString.setCategoryInput(string);
                RestTemplate restTemplate = new RestTemplate();
                String uri = "https://dara.gwhy.de/categorization";
                HttpEntity<CategoryInputString> request = new HttpEntity<>(categoryInputString);
                ResponseEntity<CategoryList> response = restTemplate.exchange(uri, HttpMethod.POST, request, CategoryList.class);


                CategoryList categoryList = response.getBody();
                if(categoryList != null) {
                    for (CategoryItem item : categoryList.getCategories()) {
                        UserData data = new UserData();

                        data.setTopic(item.getName());
                        data.setCompany(sqlData.getCompany());
                        data.setUserId(sqlData.getCredentials().getUid());
                        data.setWeight(1.0 / categoryList.getCategories().size());
                        System.out.println(data.getTopic());
                        //....
                        userDataRepository.save(data);
                    }
                }

            } catch (Exception exception) {
                System.out.println(exception);
            }
        }
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