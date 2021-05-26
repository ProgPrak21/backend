package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.CategorizationDTO.CategoryInputString;
import dataInfoLogic.DataTypes.DelUserCompany;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Entities.UserDataList;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserDataDBController {

    @Autowired
    UserDataRepository userDataRepository;


    @GetMapping(path="/data/all")
    public ResponseEntity<?> GetAllUserAddData(){
        return new ResponseEntity<>(userDataRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping(path="/data/getusertopics")
    public  ResponseEntity<?> GetAllUserTopics(@RequestBody CategoryInputString userid){
        UserDataList userDataList=new UserDataList();
        userDataList.setUserData(userDataRepository.getUserTopics(userid.getCategoryInput()));
        return new ResponseEntity<>(userDataList,HttpStatus.OK);
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
        return  ResponseEntity.ok("Table cleared!");
    }

    @PostMapping(path="/data/clearuserdata")
    public ResponseEntity<?> ClearUserData(@RequestBody CategoryInputString categoryInputString) {
        userDataRepository.clearUserData(categoryInputString.getCategoryInput());
        return  ResponseEntity.ok("Entries for user : "+ categoryInputString.getCategoryInput() + " deleted!");
    }
    @PostMapping(path ="data/delusercompany")
    public ResponseEntity<?> delUserCompany(@RequestBody DelUserCompany delUserCompany){
        userDataRepository.clearUserCompany(delUserCompany.getUserid(),delUserCompany.getCompany());
        return ResponseEntity.ok("Entries for user : " + delUserCompany.getUserid() + " for" + delUserCompany.getCompany() + " deleted!");
    }
}
