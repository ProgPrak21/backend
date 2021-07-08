package dataInfoLogic.Controller.RESTController.SocialNetworks;


import dataInfoLogic.DataTypes.Device;
import dataInfoLogic.DataTypes.Location;
import dataInfoLogic.Services.CoordinateAnalyser;
import dataInfoLogic.Services.CredentialsManager;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.Services.DeviceAnalyser;
import dataInfoLogic.Services.DeviceManager;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class Google {

    @Autowired
    CredentialsManager credentialsManager;

    @Autowired
    CoordinateAnalyser coordinateAnalyser;

    @Autowired
    DeviceAnalyser deviceAnalyser;


    @CrossOrigin
    @PostMapping(path = "/data/google/advertisement")
    public ResponseEntity<?> submit(@RequestParam(value = "file1", required = false) MultipartFile file1,
                                    @RequestParam(value = "file2", required = false) MultipartFile file2,
                                    @RequestParam(value = "file3", required = false) MultipartFile file3,
                                    @RequestParam(value = "file4", required = false) MultipartFile file4,
                                    @RequestParam(value = "file5", required = false) MultipartFile file5,
                                    @RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "secret", required = false) String secret) throws IOException {


        if (file1.getSize() == 0) {
            new ResponseEntity<>("No file attached", HttpStatus.BAD_REQUEST);
        }

        if (file1.isEmpty()) {
            new ResponseEntity<>("File empty", HttpStatus.BAD_REQUEST);
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


                //retrieve json content
                if (Objects.equals(currentFile.getOriginalFilename(), "Standortverlauf.json")) {
                    InputStream initialStream = currentFile.getInputStream();
                    byte[] buffer = new byte[initialStream.available()];
                    initialStream.read(buffer);
                    String s = new String(buffer, StandardCharsets.UTF_8);

                    //create double HashMap
                    HashMap<Integer, HashMap<Integer, Location>> locationsHashMap = new HashMap<>();
                    HashMap<String, Device> devicesHashMap = new HashMap<>();
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONArray array = object.getJSONArray("locations");

                        hashCoordinates(array, locationsHashMap);
                        getDevices(array, devicesHashMap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //give double HashMap and userCredentials to coordinateAnalyser
                    coordinateAnalyser.storeCoordinates(locationsHashMap, userCredentials);
                    deviceAnalyser.storeDevices(devicesHashMap, userCredentials);


                }else if(currentFile.getOriginalFilename().matches("2(.*)_(.*).json")){
                    InputStream initialStream = currentFile.getInputStream();
                    byte[] buffer = new byte[initialStream.available()];
                    initialStream.read(buffer);
                    String s = new String(buffer, StandardCharsets.UTF_8);

                    //create double HashMap
                    HashMap<Integer, HashMap<Integer, Location>> locationsHashMap = new HashMap<>();
                    try {
                        JSONObject object = new JSONObject(s);
                        if(object.has("timelineObjects")) {

                            JSONArray array = object.getJSONArray("timelineObjects");

                            hashCoordinatesYear(array, locationsHashMap);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //give double HashMap and userCredentials to coordinateAnalyser
                    coordinateAnalyser.storeCoordinates(locationsHashMap, userCredentials);
                }
            }
        }


        return new ResponseEntity<>(userCredentials, HttpStatus.OK);

        /*
        modelMap.addAttribute("google", file);

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
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            String doc="";
            for(String string: result){
                doc+=string;
            }
            String htmlparts[]=doc.split("Route");
            LinkedList<TopicAmount> topiclist=new LinkedList<>();
            LinkedList<String> topic_list=(routes(htmlparts,topiclist));

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);

            for(String string : topic_list){
                System.out.println(string);
            }
            topiclist.sort(TopicAmount::compareTo);
            for(TopicAmount topicAmount : topiclist){
                System.out.println(topicAmount.getTopic() + " " + topicAmount.getAmount() + "x");
            }


        }
        UserCredentials userCredentials = new UserCredentials();
        if(uid != null && secret != null){
            userCredentials.setUid(uid);
            userCredentials.setSecret(secret);
        }
        else{
            userCredentials.setUid("New");
            userCredentials.setSecret("credentials");
        }

        return ResponseEntity.ok(userCredentials);
    }
    //Searches in HTML-document for the given words
    public LinkedList<String> routes(String[] htmlparts, LinkedList<TopicAmount> topiclist){
        LinkedList list=new LinkedList();
        int used=0;
        LinkedList<String> places= new LinkedList<>();
        for(int i=1;i<htmlparts.length;i++){
            String parts[]=htmlparts[i].split("<br>");
            for(int k=1;k<3;k++) {
                used=0;
                String kaktus=parts[k];
                String kaktusse[]=kaktus.split(",");
                for(TopicAmount topicAmount : topiclist){
                    if(topicAmount.getTopic().split(",")[0].equals(kaktusse[0])){
                        topicAmount.setAmount(topicAmount.getAmount()+1);
                        used=1;
                    }
                }
                if(used==0){
                    TopicAmount topicAmount=new TopicAmount();
                    topicAmount.setTopic(kaktus);
                    topicAmount.setAmount(1);
                    topiclist.add(topicAmount);
                }
            }
            list.add(parts[1] + " to " + parts[2] + " on " + parts[3].split("MESZ")[0]);
            }
        return list;
    */
    }

    public void put(Integer key1, Integer key2, Location value, HashMap<Integer, HashMap<Integer, Location>> mMap) {
        HashMap<Integer, Location> map = mMap.get(key1);
        if (map == null) {
            map = new HashMap<>();
            mMap.put(key1, map);
        }
        map.put(key2, value);
    }

    public Location get(Integer key1, Integer key2, HashMap<Integer, HashMap<Integer, Location>> mMap) {
        HashMap<Integer, Location> map = mMap.get(key1);
        if (map == null) {
            return null;
        } else {
            return map.get(key2);
        }
    }

    public int round(String coord) {
        return Integer.parseInt(coord.substring(0, 7));
    }

    public void hashCoordinates(JSONArray array, HashMap<Integer, HashMap<Integer, Location>> locationsHashMap) throws Exception {
        for (int k = 0; k < array.length(); k++) {
            String latitudeE7 = array.getJSONObject(k).getString("latitudeE7");
            String longitudeE7 = array.getJSONObject(k).getString("longitudeE7");
            Location ort = get(round(latitudeE7), round(longitudeE7), locationsHashMap);
            if (ort != null) {
                ort.anzahl++;
            } else {
                Location location = new Location(round(latitudeE7), round(longitudeE7));
                location.company = "google";
                put(round(latitudeE7), round(longitudeE7), location, locationsHashMap);
            }
        }
    }

    public void getDevices(JSONArray array, HashMap<String, Device> devicesHashMap) throws Exception {
        for (int k = 0; k < array.length(); k++) {
            if (array.getJSONObject(k).has("platform")) {
                String platform = array.getJSONObject(k).getString("platform");
                String[] platformParts = platform.split("/");
                String deviceName = platformParts[1] + " " + platformParts[2];
                if (devicesHashMap.get(deviceName) == null) {
                    Device device = new Device();
                    device.setCount(1);
                    device.setName(deviceName);
                    device.setCompany("google");
                    devicesHashMap.put(deviceName, device);
                } else {
                    devicesHashMap.get(deviceName).setCount(devicesHashMap.get(deviceName).getCount() + 1);
                }

            }
        }
    }
    public void hashCoordinatesYear(JSONArray array, HashMap<Integer, HashMap<Integer, Location>> locationsHashMap) throws Exception {
        for (int k = 0; k < array.length(); k++) {
            if(array.getJSONObject(k).has("placeVisit")) {
                if (array.getJSONObject(k).getJSONObject("placeVisit").has("location")) {
                    if (array.getJSONObject(k).getJSONObject("placeVisit").getJSONObject("location").has("latitudeE7") &&
                            array.getJSONObject(k).getJSONObject("placeVisit").getJSONObject("location").has("longitudeE7") &&
                            array.getJSONObject(k).getJSONObject("placeVisit").getJSONObject("location").has("name")
                    ) {

                        String latitudeE7 = array.getJSONObject(k).getJSONObject("placeVisit").getJSONObject("location").getString("latitudeE7");
                        String longitudeE7 = array.getJSONObject(k).getJSONObject("placeVisit").getJSONObject("location").getString("longitudeE7");
                        String name=array.getJSONObject(k).getJSONObject("placeVisit").getJSONObject("location").getString("name");
                        Location ort = get(round(latitudeE7), round(longitudeE7), locationsHashMap);
                        if (ort != null) {
                            ort.anzahl++;
                            if(ort.getName()==null){
                                ort.name=name;
                            }
                        } else {
                            Location location = new Location(round(latitudeE7), round(longitudeE7));
                            location.company = "google";
                            location.name=name;
                            put(round(latitudeE7), round(longitudeE7), location, locationsHashMap);
                        }
                    }
                }
            }
        }
    }
}
