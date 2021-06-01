package dataInfoLogic.Controller.SocialNetworks;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import dataInfoLogic.Controller.DataManagement.DataManagementController;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import org.springframework.beans.factory.annotation.Autowired;

@CrossOrigin
@RestController
public class linkedinData {

    @Autowired
    DataManagementController dataManagementController;

    @PostMapping(path = "/data/linkedin/advertisement")
    public ResponseEntity<?> submit(@RequestParam(value = "linkedin") MultipartFile file, ModelMap modelMap,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("linkedin", file);

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
                        stringList.add(array[i]);
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
            sqlData.setCompany("linkedin");

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            sqlData.setCredentials(userCredentials);

            //final call
            //todo seems not to work
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

}


