package barikoi.barikoilocation.NearbyPlace;

import java.util.ArrayList;

import barikoi.barikoilocation.Place;

public interface NearbyPlaceListener {
    /**
     * Receives the place from Nearby Place network call
     * @param places is the list of places received from the server
     */
    void onPlaceListReceived(ArrayList<Place> places);

    /**
     * Receives errors occurred after the network call
     * @param message is the error message
     */
     void onFailure(String message);
}
