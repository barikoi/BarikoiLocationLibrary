package barikoi.barikoilocation.ReverseGeo;

import barikoi.barikoilocation.PlaceModels.ReverseGeoPlace;


/**
 * Created by Sakib on 3/8/2018.
 */

public interface ReverseGeoAPIListener {
     /**
      * Receives the place from Reverse geo network call
      * @param place received from the server
      */
      void reversedAddress(ReverseGeoPlace place);

    /**
     * Receives errors occurred after the network call
     * @param message is the error message
     */
      void onFailure(String message);

}
