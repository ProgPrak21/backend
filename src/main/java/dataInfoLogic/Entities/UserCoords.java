package dataInfoLogic.Entities;

import javax.persistence.*;

@Entity(name = "coordinate_data")
public class UserCoords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String user_id;
    private String company;
    private int latitude;
    private int longitude;
    private int count;

    public void setCount(int count) {
        this.count = count;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUid() {
        return user_id;
    }

    public void setUid(String uid) {
        this.user_id = uid;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getCount() {
        return count;
    }

    public String getCompany() {
        return company;
    }
}
