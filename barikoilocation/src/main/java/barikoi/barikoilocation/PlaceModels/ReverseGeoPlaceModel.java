package barikoi.barikoilocation.PlaceModels;

import java.io.Serializable;

public class ReverseGeoPlaceModel extends SearchAutoCompletePlaceModel implements Serializable {
    protected String city;
    protected double distance_within_meters;

    public ReverseGeoPlaceModel(){}
    public ReverseGeoPlaceModel(String id, String address, String area, String city, double distance_within_meters){
        this.id=id;
        this.address=address;
        this.area=area;
        this.city=city;
        this.distance_within_meters=distance_within_meters;
    }
    public void setCity(String city){
        this.city=city;
    }
    public String getCity() {
        return city;
    }

    public double getDistance_within_meters() {
        return distance_within_meters;
    }

    public void setDistance_within_meters(float distance_within_meters) {
        this.distance_within_meters = distance_within_meters;
    }
    @Override
    public String toString() {
        String str;
        str=address;
        if(!(area.equals(null)||area.equals("")||area.equals("null")))
            str+=", "+area;
        if(!(city.equals(null)||city.equals("")||city.equals("null")))
            str+=", "+city;
        return str;
    }
}
