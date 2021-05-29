package dataInfoLogic.Controller.SocialNetworks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@CrossOrigin
@RestController
public class linkedinData {

    @PostMapping(path = "/linkedin/Ad_Targeting")
    public void submit(@RequestParam(value = "linkedin") MultipartFile file, ModelMap modelMap,
                       @RequestParam(value = "uid", required = false) String uid,
                       @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("linkedin", file);
       // String v = file.getOriginalFilename();
        //System.out.println(v);
       // BufferedReader br = new BufferedReader(new FileReader((File) file));


        /*String content;
        content = FileReader.read(file);
        System.out.println(content); */

       if (!file.isEmpty()) {
           System.out.println(file.getBytes().toString());


            String line = "";
            try {
                BufferedReader br = new BufferedReader(new FileReader((File) file));

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


