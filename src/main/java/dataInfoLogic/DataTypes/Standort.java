package dataInfoLogic.DataTypes;

public class Standort implements Comparable<Standort> {
    public int latitude;
    public int longitude;
    public int anzahl=0;
    public String company;

    public Standort(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public int compareTo(Standort st){
        if(st.anzahl==anzahl)
            return 0;
        else if(anzahl<st.anzahl)
            return 1;
        else
            return -1;
    }
}
