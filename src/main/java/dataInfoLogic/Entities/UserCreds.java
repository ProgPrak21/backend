package dataInfoLogic.Entities;

import javax.persistence.*;

@Entity(name = "user_credentials")
public class UserCreds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private Long id;
    private String user_id;
    private String secret;

    public String getUid() {
        return user_id;
    }

    public void setUid(String uid) {
        this.user_id = uid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
