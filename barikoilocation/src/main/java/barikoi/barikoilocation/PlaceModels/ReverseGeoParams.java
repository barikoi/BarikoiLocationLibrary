package barikoi.barikoilocation.PlaceModels;

public enum ReverseGeoParams {
    COUNTRY("country"),
    DIVISION("sub_district"),
    DISTRICT("district"),
    SUB_DISTRICT("sub_district"),
    UNION("union"),
    PAURASHOVA("pauroshova"),
    LOCATION_TYPE("location_type"),
    ADDRESS_COMPONENTS("address"),
    AREA_COMPONENTS("area"),
    BANGLA("bangla");


    String name;
    private ReverseGeoParams(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return name;
    }
}
