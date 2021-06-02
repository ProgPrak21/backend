package dataInfoLogic.Controller.DataManagement;

import dataInfoLogic.DataTypes.CategorizationDTO.CategoryInputString;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.UserCompany;
import dataInfoLogic.Entities.UserCreds;
import dataInfoLogic.Entities.UserData;
import dataInfoLogic.DataTypes.UserDataList;
import dataInfoLogic.Repositories.UserCredsRepository;
import dataInfoLogic.Repositories.UserDataRepository;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@CrossOrigin
@RestController
public class UserCredsDBController {

    @Autowired
    UserCredsRepository userCredsRepository;


    @GetMapping(path="/usercreds/all")
    public ResponseEntity<?> GetAllUserAddData(){
        return new ResponseEntity<>(userCredsRepository.findAll(), HttpStatus.OK);
    }

    /*
    @GetMapping(path="/data/usertopics")
    public  ResponseEntity<?> GetAllUserTopics(@RequestParam("userId")String userId){
        UserDataList userDataList=new UserDataList();
        userDataList.setUserData(userCredsRepository.getUserTopics(userId));
        return new ResponseEntity<>(userDataList,HttpStatus.OK);
    }

     */

    @PostMapping(path="/usercreds/newentry")
    public ResponseEntity<?> newcred(@RequestBody UserCredentials userCredentials){
        LinkedList<UserCreds> userCredslist=userCredsRepository.getUserCreds(userCredentials.getUid());
        if(userCredslist.isEmpty()) {
            UserCreds userCreds = new UserCreds();
            userCreds.setUid(userCredentials.getUid());
            userCreds.setSecret(userCredentials.getSecret());
            userCredsRepository.save(userCreds);
            return ResponseEntity.ok("New User: Uid : " + userCreds.getUid() + "Secret : " + userCreds.getSecret());
        }
        return ResponseEntity.ok("UserId already exists");
    }

    @DeleteMapping(path="/usercreds/deleteAll")
    public ResponseEntity<?> ClearData() {
        userCredsRepository.deleteAll();
        return  ResponseEntity.ok("Table cleared!");
    }

    @GetMapping(path="usercreds/checkpw")
    public ResponseEntity<?> checkpw(@RequestParam("userCreds")String userCreds){
        String returnstring="Incorrect password!";

        LinkedList<UserCreds> userCredslist= userCredsRepository.getUserCreds(userCreds.split(" ")[0]);

        if(userCredslist.get(0).getSecret().equals(userCreds.split(" ")[1])) {
            returnstring=("Correct password!");
        }
        return new ResponseEntity<>(returnstring,HttpStatus.OK);
    }



    @DeleteMapping(path="/usercreds/userdata")
    public ResponseEntity<?> ClearUserData(@RequestParam("userId") String userId) {
        userCredsRepository.clearUserData(userId);
        return  ResponseEntity.ok("Entry for user : "+ userId + " deleted!");
    }
    /*



    @DeleteMapping(path ="/data/usercompanydata")
    public ResponseEntity<?> delUserCompany(@RequestParam("userId") String userId, @RequestParam("companyId") String companyId){
        userDataRepository.clearUserCompany(userId,companyId);
        return ResponseEntity.ok("Entries for user : " + userId + " for" + companyId + " deleted!");
    }

     */
}
