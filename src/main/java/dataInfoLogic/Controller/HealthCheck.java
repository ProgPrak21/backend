package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.HelloWorld;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthCheck {

    @CrossOrigin(origins= {"http://localhost:3000"})
    @PostMapping(path = "/helloworld")
    public ResponseEntity<?> HelloWorld(@RequestBody HelloWorld hw){

        System.out.println(hw.getContent());

        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setContent(hw.getContent() + " works");


        return ResponseEntity.ok(helloWorld);
    }
}
