package barikoi.barikoilocation;

import java.io.Serializable;

/**
 * This class contains the info of a place including code, city, area
 * We use this Class all over the project to set and get a place
 * Created by Sakib on 04-Jan-17.
 */
public class Place implements Serializable {
    private String address, lon,lat,code,city,area,postalcode, type,subType,imglink,route,ward,zone,phoneNumber;
    private float distance;

    public Place(){}
    public Place(String address, String lon, String lat, String code, String city, String area, String postalcode, String type, String subType, String phoneNumber){
        this.address=address;
        this.lat=lat;
        this.lon=lon;
        this.code=code;
        this.city=city;
        this.area=area;
        this.postalcode=postalcode;
        this.type=type;
        this.subType=subType;
        this.distance=-1;
        this.imglink="";
        this.route="";
        this.ward="";
        this.zone="";
        this.phoneNumber=phoneNumber;

    }
    public Place(String address, String lon, String lat, String code, String city, String area, String postalcode, String type, String subType ){
        this.address=address;
        this.lat=lat;
        this.lon=lon;
        this.code=code;
        this.city=city;
        this.area=area;
        this.postalcode=postalcode;
        this.type=type;
        this.subType=subType;
        this.distance=-1;
        this.imglink="";
        this.route="";
        this.ward="";
        this.zone="";
        this.phoneNumber=phoneNumber;

    }
    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getImglink() {
        return imglink;
    }

    public void setImglink(String imglink) {
        this.imglink = imglink;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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
