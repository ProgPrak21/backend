package dataInfoLogic.DataTypes;

public class Location implements Comparable<Location> {
    public int latitude;
    public int longitude;
    public int anzahl=0;
    public String company;
    public String name;

    public Location(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public int compareTo(Location st){
        if(st.anzahl==anzahl)
            return 0;
        else if(anzahl<st.anzahl)
            return 1;
        else
            return -1;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }
}
