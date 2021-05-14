package dataInfoLogic.Controller;


import dataInfoLogic.DataTypes.PersonalData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstagramPersonalData {
    @CrossOrigin
    @PostMapping(path = "/Instagram/profile_information")
    public ResponseEntity<?> ProfileInformation(@RequestBody String profile) throws Exception {
        PersonalData personalData = new PersonalData();
        System.out.println(profile);
        String htmlparts[]=profile.split("\n");
        personalData.seteMail(delete_backslashs(get_data(htmlparts,"E-Mail-Adresse")));


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
    public String delete_backslashs(String string){
        String backslash="\\t";

        String stringhelper[]=string.split(backslash);
        for(int i=0;i<stringhelper.length;i++){
            if(stringhelper[i].length()>2){
                int length=stringhelper[i].length();
                return stringhelper[i].substring(0,length-1);
            }
        }
        return null;
    }

}
