package dataInfoLogic.Controller.SocialNetworks;


import dataInfoLogic.Controller.DataManagement.DataManagementController;
import dataInfoLogic.DataTypes.DataAnalysis.TopicAmount;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class GoogleMAMaps {
    @Autowired
    DataManagementController dataManagementController;

    @CrossOrigin
    @PostMapping(path = "/google/MAMaps")
    public ResponseEntity<?> submit(@RequestParam(value = "google") MultipartFile file, ModelMap modelMap,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("google", file);

        if (!file.isEmpty()) {
            System.out.println(file.getContentType());
            BufferedReader br;
            List<String> result = new ArrayList<>();
            try {

                String line;
                InputStream is = file.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    result.add(line);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            String doc="";
            for(String string: result){
                doc+=string;
            }
            String htmlparts[]=doc.split("Route");
            LinkedList<TopicAmount> topiclist=new LinkedList<>();
            LinkedList<String> topic_list=(routes(htmlparts,topiclist));

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            for(String string : topic_list){
                System.out.println(string);
            }
            topiclist.sort(TopicAmount::compareTo);
            for(TopicAmount topicAmount : topiclist){
                System.out.println(topicAmount.getTopic() + " " + topicAmount.getAmount() + "x");
            }


        }
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
    //Searches in HTML-document for the given words
    public LinkedList<String> routes(String[] htmlparts, LinkedList<TopicAmount> topiclist){
        LinkedList list=new LinkedList();
        int used=0;
        LinkedList<String> places= new LinkedList<>();
        for(int i=1;i<htmlparts.length;i++){
            String parts[]=htmlparts[i].split("<br>");
            for(int k=1;k<3;k++) {
                used=0;
                String kaktus=parts[k];
                String kaktusse[]=kaktus.split(",");
                for(TopicAmount topicAmount : topiclist){
                    if(topicAmount.getTopic().split(",")[0].equals(kaktusse[0])){
                        topicAmount.setAmount(topicAmount.getAmount()+1);
                        used=1;
                    }
                }
                if(used==0){
                    TopicAmount topicAmount=new TopicAmount();
                    topicAmount.setTopic(kaktus);
                    topicAmount.setAmount(1);
                    topiclist.add(topicAmount);
                }
            }
            list.add(parts[1] + " to " + parts[2] + " on " + parts[3].split("MESZ")[0]);
            }
        return list;
    }

}
