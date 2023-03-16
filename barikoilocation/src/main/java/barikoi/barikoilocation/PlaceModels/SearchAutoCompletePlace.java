package barikoi.barikoilocation.PlaceModels;

import java.io.Serializable;

public class SearchAutoCompletePlace implements Serializable {
    protected String id, address,code,area,addressBn, areaBn, cityBn;
    protected double longitude, latitude;

    public SearchAutoCompletePlace(){}

    public SearchAutoCompletePlace(String id, String address, String code, String area){
        this.id=id;
        this.address=address;
        this.code=code;
        this.area=area;
    }

    public SearchAutoCompletePlace(String id, String address, String code, String area, double longitude, double latitude){
        this.id=id;
        this.address=address;
        this.code=code;
        this.area=area;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public String getAddressBn() {
        return addressBn;
    }

    public void setAddressBn(String addressBn) {
        this.addressBn = addressBn;
    }

    public String getAreaBn() {
        return areaBn;
    }

    public void setAreaBn(String areaBn) {
        this.areaBn = areaBn;
    }

    public String getCityBn() {
        return cityBn;
    }

    public void setCityBn(String cityBn) {
        this.cityBn = cityBn;
    }

    public void setId(String id){
        this.id=id;
    }
    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        String str;
        str=address;
        if(!(area.equals(null)||area.equals("")||area.equals("null")))
            str+=", "+area;
        return str;
    }

}
