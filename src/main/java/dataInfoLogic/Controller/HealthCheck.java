package dataInfoLogic.Controller;

import dataInfoLogic.Entities.UserData;
import dataInfoLogic.Repositories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class HealthCheck {


    @GetMapping(path = "/data/health")
    public ResponseEntity<?> HealthCheck(){

        return ResponseEntity.ok("ok");
    }

}
