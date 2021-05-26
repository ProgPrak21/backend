package dataInfoLogic.Controller;

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

}
