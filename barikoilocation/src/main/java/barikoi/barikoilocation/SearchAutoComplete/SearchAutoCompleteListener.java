package barikoi.barikoilocation.SearchAutoComplete;

import java.util.ArrayList;

import barikoi.barikoilocation.Place;

public interface SearchAutoCompleteListener {
    default void OnPlaceListReceived(ArrayList<Place> places){};
    default void OnFailure(String Message){};
}
