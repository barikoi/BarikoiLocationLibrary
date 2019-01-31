package barikoi.barikoilocation.PlaceModels;

import java.io.Serializable;

public class GeoCodePlaceModel extends SearchAutoCompletePlaceModel implements Serializable {

    private String latitude,longitude,type,subType,city,postalcode, route_description, imglink;

    public GeoCodePlaceModel(){}

    public GeoCodePlaceModel(String address, String lon, String lat, String code, String city, String area, String postalcode, String type, String subType ){
        this.address=address;
        this.latitude=lat;
        this.longitude=lon;
        this.code=code;
        this.city=city;
        this.area=area;
        this.postalcode=postalcode;
        this.type=type;
        this.subType=subType;
        this.imglink="";
        this.route_description="";
    }
    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
    public String getRoute() {
        return route_description;
    }

    public void setRoute(String route_description) {
        this.route_description = route_description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImglink() {
        return imglink;
    }

    public void setImglink(String imglink) {
        this.imglink = imglink;
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
