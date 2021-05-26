package dataInfoLogic.Controller;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
/*
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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
                       @RequestParam(value = "secret", required = false) String secret) throws IOException, CsvException {

        modelMap.addAttribute("linkedin", file);
        String v = file.getOriginalFilename();
        System.out.println(v);

       try (CSVReader reader = new CSVReader(new FileReader((File) file))) {
            List<String[]> r = reader.readAll();
            r.forEach(x ->System.out.println(Arrays.toString(x)));
        }
    }
}
*/

