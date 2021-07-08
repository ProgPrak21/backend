package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.Device;
import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.DataTypes.Location;
import dataInfoLogic.Entities.UserCoords;
import dataInfoLogic.Entities.UserDevice;
import dataInfoLogic.Repositories.UserCoordsRepository;
import dataInfoLogic.Repositories.UserDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceManager {
    @Autowired
    UserDeviceRepository userDeviceRepository;
    @Async
    public void storeDevices(List<Device> list, UserCredentials userCredentials) {
        for(Device device : list){
            UserDevice userDevice=new UserDevice();
            userDevice.setCompany(device.getCompany());
            userDevice.setPlatform(device.getName());
            userDevice.setCount(device.getCount());
            userDevice.setUser_id(userCredentials.getUid());
            userDeviceRepository.save(userDevice);
        }
    }
}