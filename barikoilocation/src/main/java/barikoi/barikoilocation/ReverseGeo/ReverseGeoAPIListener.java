package barikoi.barikoilocation.ReverseGeo;

import android.location.Location;

import barikoi.barikoilocation.Place;


/**
 * Created by Sakib on 3/8/2018.
 */

public interface ReverseGeoAPIListener {
     default void reversedAddress(Place address){};
     default void onFailure(String message){};

}
