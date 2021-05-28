package dataInfoLogic.Controller.DataManagement;

import dataInfoLogic.DataTypes.CategorizationDTO.CategoryInputString;
import dataInfoLogic.DataTypes.UserCompany;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.DataTypes.UserDataList;
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

    @GetMapping(path="/data/usertopics")
    public  ResponseEntity<?> GetAllUserTopics(@RequestParam("userId")String userId){
        UserDataList userDataList=new UserDataList();
        userDataList.setUserData(userDataRepository.getUserTopics(userId));
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

    /* Not needed
    @DeleteMapping(path="/data/deleteAll")
    public ResponseEntity<?> ClearData() {
        userDataRepository.deleteAll();
        return  ResponseEntity.ok("Table cleared!");
    }

     */


    @DeleteMapping(path="/data/userdata")
    public ResponseEntity<?> ClearUserData(@RequestParam("userId") String userId) {
        userDataRepository.clearUserData(userId);
        return  ResponseEntity.ok("Entries for user : "+ userId + " deleted!");
    }



    @DeleteMapping(path ="/data/usercompanydata")
    public ResponseEntity<?> delUserCompany(@RequestParam("userId") String userId, @RequestParam("companyId") String companyId){
        userDataRepository.clearUserCompany(userId,companyId);
        return ResponseEntity.ok("Entries for user : " + userId + " for" + companyId + " deleted!");
    }
}
