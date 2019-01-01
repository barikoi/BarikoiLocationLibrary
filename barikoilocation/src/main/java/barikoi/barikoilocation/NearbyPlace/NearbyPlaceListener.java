package barikoi.barikoilocation.NearbyPlace;

import java.util.ArrayList;

import barikoi.barikoilocation.Place;

public interface NearbyPlaceListener {
    default void OnPlaceListReceived(ArrayList<Place> places){};
    default void OnFailure(String Message){};
}
