package barikoi.barikoilocation.SearchAutoComplete;

import java.util.ArrayList;

import barikoi.barikoilocation.Place;

public interface SearchAutoCompleteListener {
    void OnPlaceListReceived(ArrayList<Place> places);
    void OnFailure(String Message);
}
