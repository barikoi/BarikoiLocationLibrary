package barikoi.barikoilocation.PlaceModels;

public class AddressComponents {
    String placeName, house, road;

    public String getPlaceName() {
        return placeName;
    }

    public String getHouse() {
        return house;
    }

    public String getRoad() {
        return road;
    }

    public AddressComponents(String placeName, String house, String road) {
        this.placeName = placeName;
        this.house = house;
        this.road = road;
    }
}
