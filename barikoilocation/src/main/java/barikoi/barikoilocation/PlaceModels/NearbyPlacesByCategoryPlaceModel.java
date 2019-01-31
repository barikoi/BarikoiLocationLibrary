package barikoi.barikoilocation.PlaceModels;

import java.io.Serializable;

public class NearbyPlacesByCategoryPlaceModel extends NearbyPlacesModel implements Serializable {
    protected String phoneNumber;

    public NearbyPlacesByCategoryPlaceModel(){}
    public NearbyPlacesByCategoryPlaceModel(String id, double distance_within_meters, String longitude, String latitude, String address, String city, String area, String type, String subType, String code, String phoneNumber){
        this.id=id;
        this.distance_within_meters=distance_within_meters;
        this.longitude=longitude;
        this.latitude=latitude;
        this.address=address;
        this.city=city;
        this.area=area;
        this.type=type;
        this.subType=subType;
        this.code=code;
        this.phoneNumber=phoneNumber;
    }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

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
