package dataInfoLogic.Controller;


import dataInfoLogic.DataTypes.PersonalData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.HTML;

@RestController
public class InstagramData {
    @CrossOrigin
    @PostMapping(path = "/Instagram/profile_information")
    public ResponseEntity<?> ProfileInformation(@RequestBody HTML profile) throws Exception {

        PersonalData personalData = new PersonalData();
        String htmlstring=profile.toString();
        String htmlparts[]=htmlstring.split("\n");
        personalData.seteMail(get_data(htmlparts,"E-Mail-Adresse"));


        return ResponseEntity.ok(personalData);
    }
    //Searches in HTML-document for the given words
    public String get_data(String[] htmlparts,String search){
        for(int i=0;i<htmlparts.length;i++){
            if(htmlparts[i].contains(search)){
                while(!htmlparts[i].contains("<div>")){
                    i++;
                }
                return delete_empties(htmlparts[i+1]);
            }
        }
        return null;
    }
    //Deletes empties at the beginning of Strings
    public String delete_empties(String string){
        int i=0;
        while(string.charAt(i)==' '){
            i++;
        }
        return string.substring(i);
    }

}
