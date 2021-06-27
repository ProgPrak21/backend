package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import dataInfoLogic.Entities.UserCreds;
import dataInfoLogic.Repositories.UserCredsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Random;

@Component
public class CredentialsManager {
    @Autowired
    UserCredsRepository userCredsRepository;

    public boolean checkPw(UserCredentials userCredentials) {
        if(userCredentials.getUid().isEmpty())return false;
        LinkedList<UserCreds> userCredList= userCredsRepository.getUserCreds(userCredentials.getUid());
        Encryptor encryptor=new Encryptor();
        if(userCredList.isEmpty())return false;
        try {
            if(encryptor.validatePassword(userCredentials.getSecret(),userCredList.get(0).getSecret() )){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public UserCredentials randomUserCred(){
        try {
            UserCredentials userCredentials= new UserCredentials();

            //FROM https://www.baeldung.com/java-random-string
            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            Random random = new Random();

            int idLength=random.nextInt()%10+10;
            String generatedId = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(idLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            int secretLength=random.nextInt()%10+10;
            String generatedSecret = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(secretLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            userCredentials.setUid(generatedId);
            userCredentials.setSecret(generatedSecret);
            if(newUserCred(userCredentials))return userCredentials;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return randomUserCred();
    }
    public boolean newUserCred(UserCredentials userCredentials){
        if(userCredentials.getUid().isEmpty() ||userCredentials.getSecret().isEmpty())return false;
        if(userCredsRepository.getUserCreds(userCredentials.getUid()).isEmpty()){
            UserCreds userCreds = new UserCreds();
            userCreds.setUid(userCredentials.getUid());
            Encryptor encryptor= new Encryptor();
            try {
                userCreds.setSecret(encryptor.generateStorngPasswordHash(userCredentials.getSecret()));
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                userCredsRepository.save(userCreds);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            return false;
        }
        return true;
    }
}
