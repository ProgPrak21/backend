package dataInfoLogic.Controller.RESTController;

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
