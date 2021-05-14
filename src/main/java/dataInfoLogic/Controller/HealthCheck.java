package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.Content;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class HealthCheck {

    @PostMapping(path = "/helloworld")
    public ResponseEntity<?> HelloWorld(@RequestBody Content hw){

        Content content = new Content();
        content.setContent(hw.getContent() + " works");

        return ResponseEntity.ok(content);
    }

    @GetMapping(path = "/health")
    public ResponseEntity<?> HealthCheck(){

        return ResponseEntity.ok("ok");
    }
}
