
package dataInfoLogic.Controller.RESTController.SocialNetworks;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dataInfoLogic.DataTypes.Device;
import dataInfoLogic.Services.CredentialsManager;
import dataInfoLogic.Services.DataManagement;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.SQLData;
import dataInfoLogic.Services.DeviceAnalyser;
import dataInfoLogic.Services.DeviceManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@CrossOrigin
@RestController
public class Instagram {

    @Autowired
    DataManagement dataManagement;

    @Autowired
    CredentialsManager credentialsManager;

    @Autowired
    DeviceAnalyser deviceAnalyser;

    @PostMapping(path = "/data/instagram/advertisement")
    public ResponseEntity<?> submit(@RequestParam(value = "file1", required = false) MultipartFile file1,
                                    @RequestParam(value = "file2", required = false) MultipartFile file2,
                                    @RequestParam(value = "file3", required = false) MultipartFile file3,
                                    @RequestParam(value = "file4", required = false) MultipartFile file4,
                                    @RequestParam(value = "file5", required = false) MultipartFile file5,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {


        if (file1 == null) {
            new ResponseEntity<>("No file attached", HttpStatus.BAD_REQUEST);
        }

        //check if user credentials are provided or assign new ones
        UserCredentials userCredentials = new UserCredentials();
        if (uid != null && secret != null) {
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            //check if user credentials are correct if they are provided
            if (!credentialsManager.checkPw(userCredentials)) {
                return new ResponseEntity<>("Wrong user credentials", HttpStatus.BAD_REQUEST);
            }

        } else {
            userCredentials = credentialsManager.randomUserCred();
        }

        //create helpers
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader();

        //loop to retrieve content from all files
        for (int i = 0; i < 5; i++) {

            MultipartFile currentFile;
            if (i == 0) {
                currentFile = file1;
            } else if (i == 2) {
                currentFile = file2;
            } else if (i == 3) {
                currentFile = file3;
            } else if (i == 4) {
                currentFile = file4;
            } else {
                currentFile = file5;
            }

            //retrieve json content
            if (!(currentFile == null) && !currentFile.isEmpty()) {
                //read data from file
                InputStream initialStream = currentFile.getInputStream();
                byte[] buffer = new byte[initialStream.available()];
                initialStream.read(buffer);
                String json = new String(buffer, StandardCharsets.UTF_8);

                if (Objects.equals(currentFile.getOriginalFilename(), "your_reels_topics.json")) {
                    LinkedList<String> stringList = new LinkedList<>();
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("topics_your_reels_topics");
                        for (int k = 0; k < array.length(); k++) {
                            String topic = array.getJSONObject(k).getJSONObject("string_map_data").getJSONObject("Name").getString("value");
                            stringList.add(topic);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //save data into database
                    saveAddData(stringList, userCredentials);


                } else if (Objects.equals(currentFile.getOriginalFilename(), "your_topics.json")) {
                    LinkedList<String> stringList = new LinkedList<>();
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("topics_your_topics");
                        for (int k = 0; k < array.length(); k++) {
                            String topic = array.getJSONObject(k).getJSONObject("string_map_data").getJSONObject("Name").getString("value");
                            stringList.add(topic);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //save data into database
                    saveAddData(stringList, userCredentials);
                } else if (Objects.equals(currentFile.getOriginalFilename(), "devices.json")) {
                    HashMap<String, Device> devicesHashMap = new HashMap<>();
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("devices_devices");

                        getDevices(array, devicesHashMap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    deviceAnalyser.storeDevices(devicesHashMap, userCredentials);

                }
            }
        }

        return new ResponseEntity<>(userCredentials, HttpStatus.OK);
    }

    public void saveAddData(LinkedList<String> stringList, UserCredentials userCredentials) {
        SQLData sqlData = new SQLData();
        sqlData.setStringList(stringList);
        sqlData.setCompany("instagram");
        sqlData.setCredentials(userCredentials);
        dataManagement.ProfileInformation(sqlData);
    }
    public void getDevices(JSONArray array, HashMap<String, Device> devicesHashMap) throws Exception {
        for (int k = 0; k < array.length(); k++) {
            if (array.getJSONObject(k).has("string_map_data")) {
                if (array.getJSONObject(k).getJSONObject("string_map_data").has("User Agent")) {
                    if (array.getJSONObject(k).getJSONObject("string_map_data").getJSONObject("User Agent").has("value")) {
                        String platform = array.getJSONObject(k).getJSONObject("string_map_data").getJSONObject("User Agent").getString("value");
                        String[] platformParts = platform.split(";");
                        String deviceName = platformParts[3].substring(1) + " " + platformParts[4].substring(1);
                        if (devicesHashMap.get(deviceName) == null) {
                            Device device = new Device();
                            device.setCount(1);
                            device.setName(deviceName);
                            device.setCompany("instagram");
                            devicesHashMap.put(deviceName, device);
                        } else {
                            devicesHashMap.get(deviceName).setCount(devicesHashMap.get(deviceName).getCount() + 1);
                        }

                    }
                }
            }
        }
    }
}