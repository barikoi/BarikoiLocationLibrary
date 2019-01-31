package barikoi.barikoilocation.PlaceModels;

import java.io.Serializable;

public class NearbyPlacesModel extends ReverseGeoPlaceModel implements Serializable {
    protected String  longitude,latitude,type,subType;
    public NearbyPlacesModel(){}
    public NearbyPlacesModel(String id, double distance_within_meters, String longitude, String latitude, String address, String city, String area, String type, String subType, String code){
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
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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
