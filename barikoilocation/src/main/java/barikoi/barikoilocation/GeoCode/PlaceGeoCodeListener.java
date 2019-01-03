package barikoi.barikoilocation.GeoCode;

import barikoi.barikoilocation.Place;

/**
 * Interface of receiving  the place using GeoCode API
 */
public interface PlaceGeoCodeListener {
    /**
     * Receives the place from Geocode network call
     * @param place received from the server
     */
    void GeoCodePlace(Place place);

    /**
     * Receives errors occurred after the network call
     * @param Message is the error message
     */
    void OnFailure(String Message);
}
