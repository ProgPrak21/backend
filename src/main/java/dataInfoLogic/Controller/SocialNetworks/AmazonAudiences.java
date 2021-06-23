package dataInfoLogic.Controller.SocialNetworks;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import dataInfoLogic.Controller.RESTController.DataManagementController;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import org.springframework.beans.factory.annotation.Autowired;

@CrossOrigin
@RestController
public class AmazonAudiences {

    @Autowired
    DataManagementController dataManagementController;

    @PostMapping(path = "/data/amazon/advertisement")
    public ResponseEntity<?> submit(@RequestParam(value = "amazonAudiences") MultipartFile file, ModelMap modelMap,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("amazonAudiences", file);

        if (!file.isEmpty()) {
            System.out.println(file.getContentType());
            BufferedReader br;
            List<String> result = new ArrayList<>();
            LinkedList<String> stringList = new LinkedList<>();

            try {

                String line;
                InputStream is = file.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    result.add(line);
                }

                List <String> content = new ArrayList<>(result.subList(1, result.indexOf(result.get(result.size() - 1)) + 1));
                for (String string: content) {
                    String array [] = string.split(";");
                    for (int i = 0; i<array.length; i++) {
                        stringList.add(topicCleanUp(array[i]));
                    }
                }
                System.out.println(stringList);


            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            //call to DataManagementController
            //create request body
            SQLData sqlData = new SQLData();
            sqlData.setStringList(stringList);
            sqlData.setCompany("Amazon");

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            sqlData.setCredentials(userCredentials);

            dataManagementController.ProfileInformation(sqlData);

        }

        UserCredentials userCredentials = new UserCredentials();
        if (uid != null && secret != null) {
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);
        } else {
            userCredentials.setUid("New");
            userCredentials.setSecret("credentials");
        }

        return ResponseEntity.ok(userCredentials);
    }

    public String topicCleanUp(String topic){
        String array [] = topic.split(":");
        String result="";
        //removes Prefixes such as "in_Market:" as they might alter what Category is returned
        for(int i=1;i< array.length;i++){
            result+=" "+array[i];
        }
        //removes last "
        if(result.length()>0) {
            result = result.substring(0, result.length() - 1);
        }
        System.out.println(result);
        return result;
    }

}


