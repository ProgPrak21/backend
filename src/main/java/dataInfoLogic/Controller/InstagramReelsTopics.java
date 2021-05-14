package dataInfoLogic.Controller;


import dataInfoLogic.DataTypes.PersonalData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class InstagramReelsTopics {
    @CrossOrigin
    @PostMapping(path = "/Instagram/reels_topics")
    public ResponseEntity<?> ProfileInformation(@RequestBody String profile) throws Exception {
        PersonalData personalData = new PersonalData();
        String htmlparts[]=profile.split("</td>");
        List topic_list=(get_topics(htmlparts));
        for(int i=0;i<topic_list.size();i++){
            System.out.println(topic_list.get(i));
        }

        return ResponseEntity.ok(personalData);
    }
    //Searches in HTML-document for the given words
    public List<String> get_topics(String[] htmlparts){
        List list=new LinkedList();
        for(int i=0;i<htmlparts.length;i++){
            if(htmlparts[i].contains("Name")){
                i++;
                list.add(htmlparts[i].split("div>")[1].split("<")[0]);
            }
        }
        return list;
    }

}
