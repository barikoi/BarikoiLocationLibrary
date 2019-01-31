package barikoi.barikoilocation.NearbyPlace;

import java.util.ArrayList;

import barikoi.barikoilocation.PlaceModels.NearbyPlacesByCategoryPlaceModel;
import barikoi.barikoilocation.PlaceModels.NearbyPlacesModel;

public interface NearbyPlaceListener {
    /**
     * Receives the place from Nearby Place network call
     * @param places is the list of places received from the server
     */
    default void onPlaceListReceived(ArrayList<NearbyPlacesModel> places){};
    default void onPlaceListReceivedByCategory(ArrayList<NearbyPlacesByCategoryPlaceModel> places){};

    /**
     * Receives errors occurred after the network call
     * @param message is the error message
     */
     void onFailure(String message);
}
