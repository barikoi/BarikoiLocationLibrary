package barikoi.barikoilocation.GeoCode;

import barikoi.barikoilocation.PlaceModels.GeoCodePlace;


/**
 * Interface of receiving  the place using GeoCode API
 */
public interface PlaceGeoCodeListener {
    /**
     * Receives the place from Geocode network call
     * @param place received from the server
     */
    void onGeoCodePlace(GeoCodePlace place);

    /**
     * Receives errors occurred after the network call
     * @param message is the error message
     */
    void onFailure(String message);
}
