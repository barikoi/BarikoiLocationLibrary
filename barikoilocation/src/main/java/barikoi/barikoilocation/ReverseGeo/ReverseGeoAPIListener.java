package barikoi.barikoilocation.ReverseGeo;

import android.location.Location;

import barikoi.barikoilocation.Place;


/**
 * Created by Sakib on 3/8/2018.
 */

public interface ReverseGeoAPIListener {
     /**
      * Receives the place from Reverse geo network call
      * @param place received from the server
      */
      void reversedAddress(Place place);

    /**
     * Receives errors occurred after the network call
     * @param message is the error message
     */
      void onFailure(String message);

}
