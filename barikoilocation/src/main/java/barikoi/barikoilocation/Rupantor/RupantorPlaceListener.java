package barikoi.barikoilocation.Rupantor;

import barikoi.barikoilocation.PlaceModels.GeoCodePlace;

public interface RupantorPlaceListener {
    void onRupantorPlaceReceived(GeoCodePlace place,String fixedAddress, boolean isCompleteAddress);

    default void onRupantorPlaceReceivedWithScore(GeoCodePlace place,String fixedAddress, boolean isCompleteAddress, String confidenceScore){

    }
    default void onFailure(String messege){

    }
}
