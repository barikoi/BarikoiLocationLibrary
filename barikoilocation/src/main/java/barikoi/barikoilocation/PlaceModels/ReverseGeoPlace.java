package barikoi.barikoilocation.PlaceModels;

import java.io.Serializable;

public class ReverseGeoPlace extends SearchAutoCompletePlace implements Serializable {
    public String city;
    public String country;
    public String division;

    public String district;
    public String subDistrict;
    public String union;
    public String pauroshova;
    public String locationType;
    public String addressBN;
    public String areaBN;
    public String cityBN;
    public double distance_within_meters;

    public AreaComponents areaComponents;
    public AddressComponents addressComponents;

    public ReverseGeoPlace(){}
    public ReverseGeoPlace(String id, String address, String area, String city, double distance_within_meters){
        this.id=id;
        this.address=address;
        this.area=area;
        this.city=city;
        this.distance_within_meters=distance_within_meters;
    }

    public String getCountry() {
        return country;
    }

    public String getDivision() {
        return division;
    }

    public String getDistrict() {
        return district;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public String getUnion() {
        return union;
    }

    public String getPauroshova() {
        return pauroshova;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getAddressBN() {
        return addressBN;
    }

    public String getAreaBN() {
        return areaBN;
    }

    public String getCityBN() {
        return cityBN;
    }

    public AreaComponents getAreaComponents() {
        return areaComponents;
    }

    public AddressComponents getAddressComponents() {
        return addressComponents;
    }

    public void setAreaComponents(AreaComponents areaComponents) {
        this.areaComponents = areaComponents;
    }

    public void setAddressComponents(AddressComponents addressComponents) {
        this.addressComponents = addressComponents;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public void setPauroshova(String pauroshova) {
        this.pauroshova = pauroshova;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setAddressBN(String addressBN) {
        this.addressBN = addressBN;
    }

    public void setAreaBN(String areaBN) {
        this.areaBN = areaBN;
    }

    public void setCityBN(String cityBN) {
        this.cityBN = cityBN;
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

