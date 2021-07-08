package dataInfoLogic.DataTypes;

public class Location implements Comparable<Location> {
    public int latitude;
    public int longitude;
    public int anzahl=0;
    public String company;

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
}
