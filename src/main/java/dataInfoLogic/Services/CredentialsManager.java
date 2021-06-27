package dataInfoLogic.Services;

import dataInfoLogic.DataTypes.FrontendDTO.UserCredentials;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Component
public class CredentialsManager {
    public boolean checkPw(UserCredentials userCredentials) {
        if(userCredentials.getUid().isEmpty())return false;
        try {
            HttpEntity<UserCredentials> request = new HttpEntity<>(userCredentials);
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:8080/usercreds/checkpw?userCreds={userCreds}";
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class, userCredentials.getUid() + " " + userCredentials.getSecret());
            String answer = response.getBody();
            if (answer.equals("Correct password!")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public UserCredentials RandomUserCreds(){
        try {

            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:8080/usercreds/newentry?userCreds={userCreds}";
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
            String generatedsecret = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(secretLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            userCredentials.setUid(generatedId);
            userCredentials.setSecret(generatedsecret);

            HttpEntity<UserCredentials> request = new HttpEntity<>(userCredentials);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class, userCredentials.getUid() + " " + userCredentials.getSecret());

            if(response.equals("UserId already exists")){
                return RandomUserCreds();
            }else if(response.equals("Storage error")){
                userCredentials.setUid("Storage");
                userCredentials.setSecret("error");
            }
            return userCredentials;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
