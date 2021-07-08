package dataInfoLogic.Controller.RESTController.SocialNetworks;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dataInfoLogic.Services.CredentialsManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import dataInfoLogic.Services.DataManagement;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import org.springframework.beans.factory.annotation.Autowired;

@CrossOrigin
@RestController
public class LinkedIn {

    @Autowired
    DataManagement dataManagement;

    @Autowired
    CredentialsManager credentialsManager;

    @PostMapping(path = "/data/linkedin/advertisement")
    public ResponseEntity<?> submit(@RequestParam(value = "file1", required = false) MultipartFile file1,
                                    @RequestParam(value = "file2", required = false) MultipartFile file2,
                                    @RequestParam(value = "file3", required = false) MultipartFile file3,
                                    @RequestParam(value = "file4", required = false) MultipartFile file4,
                                    @RequestParam(value = "file5", required = false) MultipartFile file5,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {


        if(file1 == null){
            new ResponseEntity<>("No file attached", HttpStatus.BAD_REQUEST);
        }

        //check if user credentials are provided or assign new ones
        UserCredentials userCredentials = new UserCredentials();
        if (uid != null && secret != null) {
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            //check if user credentials are correct if they are provided
            if (!credentialsManager.checkPw(userCredentials)) {
                return new ResponseEntity<>("Wrong user credentials", HttpStatus.BAD_REQUEST);
            }

        } else {
            userCredentials=credentialsManager.randomUserCred();
        }

        //create helpers
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader();

        //loop to retrieve content from all files
        for(int i = 0; i<5; i++){

            MultipartFile currentFile;
            if(i==0){
                currentFile = file1;
            }else if(i==2){
                currentFile = file2;
            }else if(i==3){
                currentFile = file3;
            }else if(i==4){
                currentFile = file4;
            }else{
                currentFile = file5;
            }

            //retrieve json content
            if (!(currentFile == null) && !currentFile.isEmpty()) {

                //retrieve json content
                if(Objects.equals(currentFile.getOriginalFilename(), "Ad_Targeting.csv")){

                    BufferedReader br;
                    List<String> result = new ArrayList<>();
                    LinkedList<String> stringList = new LinkedList<>();

                    try {

                        String line;
                        InputStream is = currentFile.getInputStream();
                        br = new BufferedReader(new InputStreamReader(is));
                        while ((line = br.readLine()) != null) {
                            result.add(line);
                        }

                        List <String> content = new ArrayList<>(result.subList(1, result.indexOf(result.get(result.size() - 1)) + 1));
                        for (String string: content) {
                            String array [] = string.split(";");
                            for (int k = 0; k<array.length; k++) {
                                stringList.add(array[k]);
                            }
                        }
                        System.out.println(stringList);


                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }


                    //call data management controller for categorization
                    SQLData sqlData = new SQLData();
                    sqlData.setStringList(stringList);
                    sqlData.setCompany("linkedin");
                    sqlData.setCredentials(userCredentials);
                    dataManagement.ProfileInformation(sqlData);

                }
            }
        }

        return new ResponseEntity<>(userCredentials, HttpStatus.OK);
    }
}


