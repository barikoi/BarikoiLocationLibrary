package barikoi.barikoilocation.SearchAutoComplete;

import java.util.ArrayList;

import barikoi.barikoilocation.Place;

public interface SearchAutoCompleteListener {
    void OnPlaceListRecieved(ArrayList<Place> places);
}
