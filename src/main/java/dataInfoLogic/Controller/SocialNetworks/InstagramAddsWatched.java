package dataInfoLogic.Controller.SocialNetworks;


import dataInfoLogic.DataTypes.PersonalData;
import dataInfoLogic.DataTypes.ViewedAdd;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class InstagramAddsWatched {
    @CrossOrigin
    @PostMapping(path = "/Instagram/adds_watched")
    public ResponseEntity<?> ProfileInformation(@RequestBody String profile) throws Exception {
        PersonalData personalData = new PersonalData();
        String htmlparts[]=profile.split("</td>");
        List<ViewedAdd> list=search_viewed_adds(htmlparts);
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i).by + list.get(i).date);
        }


        return ResponseEntity.ok(personalData);
    }
    //Search through HTML for Viewed adds
    public List<ViewedAdd> search_viewed_adds(String[] parts){
        List adds=new LinkedList();
        for(int i=0;i<parts.length;i++){
            if(parts[i].contains("Autor")){
                i++;
                String autor=delete_empties(parts[i].split("div>")[1].split("<")[0]);
                while(!parts[i].contains("Zeit")){
                    i++;
                }
                String date=parts[i+1].split(">")[1].split("<")[0];
                ViewedAdd add=new ViewedAdd(autor,date);
                adds.add(add);
            }
        }
        return adds;
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
