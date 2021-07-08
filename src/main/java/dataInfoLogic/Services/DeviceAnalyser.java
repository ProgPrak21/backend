package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.Device;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.Location;
import dataInfoLogic.Repositories.UserDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



@Component
@Async
public class DeviceAnalyser {
    @Autowired
    DeviceManager deviceManager;
    public void storeDevices(HashMap<String, Device> deviceHashMap, UserCredentials userCredentials){
        List<Device> deviceList=new LinkedList<>();
        for(Device device: deviceHashMap.values()){
            deviceList.add(device);
        }
        deviceManager.storeDevices(deviceList,userCredentials);
    }
}
