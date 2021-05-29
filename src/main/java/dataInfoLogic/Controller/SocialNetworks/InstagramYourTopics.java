package dataInfoLogic.Controller.SocialNetworks;


import dataInfoLogic.Controller.DataManagement.DataManagementController;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.PersonalData;
import dataInfoLogic.DataTypes.SQLData;
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
public class InstagramYourTopics {
    @Autowired
    DataManagementController dataManagementController;

    @CrossOrigin
    @PostMapping(path = "/Instagram/your_topics")
    public ResponseEntity<?> submit(@RequestParam(value = "instagram") MultipartFile file, ModelMap modelMap,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("instagram", file);

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
            String htmlparts[]=doc.split("</td>");
            LinkedList<String> topic_list=(get_topics(htmlparts));

            SQLData sqlData = new SQLData();
            sqlData.setStringList(topic_list);
            sqlData.setCompany("instagram");

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            sqlData.setCredentials(userCredentials);

            //final call
            //todo seems not to work
            dataManagementController.ProfileInformation(sqlData);

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
    public LinkedList<String> get_topics(String[] htmlparts){
        LinkedList list=new LinkedList();
        for(int i=0;i<htmlparts.length;i++){
            if(htmlparts[i].contains("Name")){
                i++;
                list.add(htmlparts[i].split("div>")[1].split("<")[0]);
            }
        }
        return list;
    }

}
