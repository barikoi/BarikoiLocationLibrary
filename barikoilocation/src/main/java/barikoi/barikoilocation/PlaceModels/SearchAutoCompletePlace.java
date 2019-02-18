package barikoi.barikoilocation.PlaceModels;

import java.io.Serializable;

public class SearchAutoCompletePlace implements Serializable {
    protected String id, address,code,area;

    public SearchAutoCompletePlace(){}

    public SearchAutoCompletePlace(String id, String address, String code, String area){
        this.id=id;
        this.address=address;
        this.code=code;
        this.area=area;
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

    @Override
    public String toString() {
        String str;
        str=address;
        if(!(area.equals(null)||area.equals("")||area.equals("null")))
            str+=", "+area;
        return str;
    }

}
