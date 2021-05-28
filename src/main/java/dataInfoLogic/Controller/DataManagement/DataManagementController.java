
package dataInfoLogic.Controller.DataManagement;

import dataInfoLogic.DataTypes.CategorizationDTO.*;
import dataInfoLogic.DataTypes.SQLData;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class DataManagementController {

    @Autowired
    UserDataRepository userDataRepository;

    //removed controller, this function can be called directly from others
    public void ProfileInformation(SQLData sqlData) {

        System.out.println(sqlData.getStringList().get(0));

        //Receives a Linkedlist of Strings
        //For each word it sends a message to https://datainfo.gwhy.de/categorization to get categories of the word
        //Returns them in a Category List (later probably not needed)
        for (String word :sqlData.getStringList()) {
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
                    UserData data = new UserData();
                    data.setTopic(item.getName());
                    data.setCompany(sqlData.getCompany());
                    data.setUserId(sqlData.getCredentials().getUid());
                    data.setWeight(1.0/categoryList.getCategories().size());
                    //....
                    userDataRepository.save(data);
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