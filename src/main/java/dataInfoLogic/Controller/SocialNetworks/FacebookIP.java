
package dataInfoLogic.Controller.SocialNetworks;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dataInfoLogic.Controller.DataManagement.DataManagementController;
import dataInfoLogic.DataTypes.CategorizationDTO.CategoryInputString;
import dataInfoLogic.DataTypes.CategorizationDTO.CategoryList;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;

@CrossOrigin
@RestController
public class FacebookIP {

    @Autowired
    DataManagementController dataManagementController;

    @PostMapping(path = "/data/facebook/IP")
    public ResponseEntity<?> submit(@RequestParam(value = "facebookIP") MultipartFile file, ModelMap modelMap,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("facebookIP", file);

        JsonNode content = null;

        //retrieve json content
        if (!file.isEmpty()) {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader objectReader = objectMapper.reader();

            content = objectReader.readTree(file.getBytes());
            content = content.at("/used_ip_address_v2");

            objectReader = objectMapper.readerFor(new TypeReference<LinkedList<String>>() {});
            System.out.println(content);
            String text=content.toString();
            String[] Data=text.split(",");
            Data[0]=Data[0].substring(1,Data[0].length());

            LinkedList<String> ips= new LinkedList<String>();
            int count=3;
            for(String ip : Data){
                if(count>2) {
                    ip = ip.substring(7, ip.length()-1);
                    ips.add(ip);
                    count=0;
                }
                count ++;
            }

            RestTemplate restTemplate = new RestTemplate();
            //todo API blocks if too many requests are send ,Implement some kind of wait mechanism
            for(String ip:ips) {
                String uri = "http://ip-api.com/json/"+ip+"?fields=241";
                HttpEntity<String> request = new HttpEntity<>(ip);
                ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
                if(response.getBody()!=null) {
                    String string = response.getBody().toString();
                    System.out.println(string);
                }
            }

            //call to DataManagementController
            //create request body
            /*
            SQLData sqlData = new SQLData();
            sqlData.setStringList(stringList);
            sqlData.setCompany("facebook");

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            sqlData.setCredentials(userCredentials);

            //final call
            dataManagementController.ProfileInformation(sqlData);
            */

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

}