package barikoi.barikoilocation.GeoCode;

import barikoi.barikoilocation.Place;

public interface PlaceGeoCodeListener {
    void GeoCodePlace(Place place);
    void OnFailure(String Message);
}
