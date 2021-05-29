package dataInfoLogic.Controller.SocialNetworks;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class linkedinData {

    @PostMapping(path = "/linkedin/Ad_Targeting")
    public ResponseEntity<?> submit(@RequestParam(value = "linkedin") MultipartFile file, ModelMap modelMap,
                                 @RequestParam(value = "uid", required = false) String uid,
                                 @RequestParam(value = "secret", required = false) String secret) throws IOException {

        modelMap.addAttribute("linkedin", file);

       if (!file.isEmpty()) {
           System.out.println(file.getContentType());
           BufferedReader br;
           List<String> result = new ArrayList<>();
           try {

               String line;
               InputStream is = file.getInputStream();
               br = new BufferedReader(new InputStreamReader(is));
               while ((line = br.readLine()) != null) {
                   result.add(line);
               }
               System.out.println(result);

           } catch (IOException e) {
               System.err.println(e.getMessage());
           }
       }
       return null;
    }
}


