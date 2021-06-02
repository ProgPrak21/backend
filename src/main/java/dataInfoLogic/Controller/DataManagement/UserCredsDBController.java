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
    @PostMapping(path="/usercreds/create")
    public ResponseEntity<?> CreateData(){

        UserCreds userCreds = new UserCreds();
        userCreds.setUid("Donut");
        userCreds.setSecret("1234");

        try {
            userCredsRepository.save(userCreds);
            return new ResponseEntity<>(userCreds, HttpStatus.CREATED);
        }catch(Exception exception){

            return new ResponseEntity<>("Storage error", HttpStatus.INSUFFICIENT_STORAGE);
        }
    }

    @PostMapping(path="/usercreds/newentry")
    public ResponseEntity<?> newcred(@RequestParam("userCreds")String userCredsparam){
        String userCredsString[]= userCredsparam.split(" ");
        LinkedList<UserCreds> userCredslist=userCredsRepository.getUserCreds(userCredsString[0]);
        if(userCredslist.isEmpty()) {
            UserCreds userCreds = new UserCreds();
            userCreds.setUid(userCredsString[0]);
            userCreds.setSecret(userCredsString[1]);
            try {
                userCredsRepository.save(userCreds);
            }catch (Exception e){
                return new ResponseEntity<>("Storage error", HttpStatus.INSUFFICIENT_STORAGE);
            }
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



    @DeleteMapping(path="/usercreds/delusercreds")
    public ResponseEntity<?> delUserCreds(@RequestParam("userId") String userId) {
        userCredsRepository.clearUserData(userId);
        return  ResponseEntity.ok("Entry for user : "+ userId + " deleted!");
    }
    @PostMapping(path="/usercreds/changepassword")
    public ResponseEntity<?> Changepassword(@RequestParam("userCreds") String userCredsParam){
        String userCredsString[]=userCredsParam.split(" ");

        userCredsRepository.clearUserData(userCredsString[0]);
        UserCreds userCreds= new UserCreds();
        userCreds.setUid(userCredsString[0]);
        userCreds.setSecret(userCredsString[1]);
        userCredsRepository.save(userCreds);

        return ResponseEntity.ok("Password changed for user: " + userCredsString[0]);
    }
}
