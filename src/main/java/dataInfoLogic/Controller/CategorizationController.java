package dataInfoLogic.Controller;

import dataInfoLogic.DataTypes.CategoryInputString;
import dataInfoLogic.InternalServices.Categorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
public class CategorizationController {

    @PostMapping(path = "/categorization")
    public ResponseEntity<?> Categorization(@RequestBody CategoryInputString categoryInputString) throws IOException {

        Categorization categorization = new Categorization();

        String output = categorization.categorizeString(categoryInputString.getCategoryInput());

        return ResponseEntity.ok(output);
    }

}
