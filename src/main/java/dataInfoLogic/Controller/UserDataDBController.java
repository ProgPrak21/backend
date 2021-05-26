package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserDataDBController {

    @Autowired
    UserDataRepository userDataRepository;


    @GetMapping(path="/data/all")
    public ResponseEntity<?> GetAllUserAddData(){
        return new ResponseEntity<>(userDataRepository.findAll(), HttpStatus.OK);
    }


    //just to visualize how to add data, it is a bullshit request
    @PostMapping(path="/data/create")
    public ResponseEntity<?> CreateData(){

        UserData userData = new UserData();
        userData.setCompany("facebook");
        userData.setUserId("jkhljk");
        userData.setTopic("Internet & Telecom");
        userData.setWeight(1.0);

        try {
            userDataRepository.save(userData);
            return new ResponseEntity<>(userData, HttpStatus.CREATED);
        }catch(Exception exception){

            return new ResponseEntity<>("Storage error", HttpStatus.INSUFFICIENT_STORAGE);
        }
    }
    @PostMapping(path="/data/deleteAll")
    public ResponseEntity<?> ClearData() {
        userDataRepository.deleteAll();
        return  ResponseEntity.ok("cleared table");
    }

    @PostMapping(path="/data/clearUserData")
    public ResponseEntity<?> ClearUserData(UserCredentials credentials) {
        userDataRepository.deleteById(Long.parseLong(credentials.getUid()));
        return  ResponseEntity.ok("cleared table");
    }
}
