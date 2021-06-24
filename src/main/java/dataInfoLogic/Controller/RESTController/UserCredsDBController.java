package dataInfoLogic.Controller.DataManagement;

import dataInfoLogic.Entities.UserCreds;
import dataInfoLogic.Repositories.UserCredsRepository;
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
            Encryptor encryptor= new Encryptor();
            try {
                userCreds.setSecret(encryptor.generateStorngPasswordHash(userCredsString[1]));
            }catch (Exception e){
                e.printStackTrace();
            }
            //userCreds.setSecret(userCredsString[1]);
            try {
                userCredsRepository.save(userCreds);
            }catch (Exception e){
                return new ResponseEntity<>("Storage error", HttpStatus.INSUFFICIENT_STORAGE);
            }
            return ResponseEntity.ok("New User: Uid : " + userCreds.getUid() + "Secret : " + userCredsString[1]);
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
        if(userCreds.isEmpty())return new ResponseEntity<>("No Id inserted",HttpStatus.OK);
        LinkedList<UserCreds> userCredslist= userCredsRepository.getUserCreds(userCreds.split(" ")[0]);
        Encryptor encryptor=new Encryptor();
        if(userCredslist.isEmpty())return new ResponseEntity<>(returnstring,HttpStatus.OK);
        try {
            if(encryptor.validatePassword(userCreds.split(" ")[1],userCredslist.get(0).getSecret() )){
                returnstring=("Correct password!");
            }
        }catch (Exception e){
            e.printStackTrace();
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
        Encryptor encryptor= new Encryptor();
        try {
            userCreds.setSecret(encryptor.generateStorngPasswordHash(userCredsString[1]));
        }catch (Exception e){
            e.printStackTrace();
        }
        userCredsRepository.save(userCreds);

        return ResponseEntity.ok("Password changed for user: " + userCredsString[0]);
    }
}