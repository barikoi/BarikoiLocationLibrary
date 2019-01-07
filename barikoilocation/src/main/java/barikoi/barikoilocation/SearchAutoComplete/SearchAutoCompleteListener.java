package barikoi.barikoilocation.SearchAutoComplete;

import java.util.ArrayList;

import barikoi.barikoilocation.Place;

public interface SearchAutoCompleteListener {
    /**
     * Receives the place from Search Autocomplete Place network call
     * @param places is the list of places received from the server
     */
    void OnPlaceListReceived(ArrayList<Place> places);

    /**
     * Receives errors occurred after the network call
     * @param message is the error message
     */
    void OnFailure(String message);
}
