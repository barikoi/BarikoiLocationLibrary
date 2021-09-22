package barikoi.barikoilocation.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import barikoi.barikoilocation.GeoCode.GeoCodeAPI;
import barikoi.barikoilocation.GeoCode.PlaceGeoCodeListener;
import barikoi.barikoilocation.NearbyPlace.NearbyPlaceAPI;
import barikoi.barikoilocation.NearbyPlace.NearbyPlaceListener;
import barikoi.barikoilocation.PlaceModels.GeoCodePlace;
import barikoi.barikoilocation.PlaceModels.NearbyPlace;
import barikoi.barikoilocation.PlaceModels.ReverseGeoPlace;
import barikoi.barikoilocation.PlaceModels.SearchAutoCompletePlace;
import barikoi.barikoilocation.ReverseGeo.ReverseGeoAPI;
import barikoi.barikoilocation.ReverseGeo.ReverseGeoAPIListener;
import barikoi.barikoilocation.Rupantor.RupantorAPI;
import barikoi.barikoilocation.Rupantor.RupantorPlaceListener;
import barikoi.barikoilocation.SearchAutoComplete.SearchAutocompleteFragment;

public class MainActivity extends AppCompatActivity  {
    Button geoCode,nearby,reverseGeo,rupantorSearch;
    EditText lat,lon,geo,type,rupantorquery;
    SearchAutocompleteFragment searchAutocompleteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nearby=findViewById(R.id.nearby);
        reverseGeo=findViewById(R.id.reversegeo);
        geoCode=findViewById(R.id.geoCode);
        geo=findViewById(R.id.geoId);
        lat=findViewById(R.id.lat);
        lon=findViewById(R.id.lon);
        type=findViewById(R.id.type);
        rupantorquery=findViewById(R.id.rupantor_query);
        rupantorSearch=findViewById(R.id.rupantor_search);
        searchAutocompleteFragment =(SearchAutocompleteFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        searchAutocompleteFragment.setPlaceSelectionListener(new SearchAutocompleteFragment.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(SearchAutoCompletePlace place) {
                Toast.makeText(MainActivity.this, ""+place.getAddress()+ " \n lat: "+ place.getLatitude()+"\nlon: "+place.getLongitude() , Toast.LENGTH_SHORT).show();
                Log.d("MainActivity",""+place.getAddress());
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        searchAutocompleteFragment.setTextHint("search for a place");
        reverseGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReverseGeoAPI.builder(getApplicationContext())
                        .setLatLng(Double.parseDouble(lat.getText().toString()),Double.parseDouble(lon.getText().toString()))
                        .build()
                        .getAddress(new ReverseGeoAPIListener() {

                            @Override
                            public void reversedAddress(ReverseGeoPlace place) {
                                Toast.makeText(MainActivity.this, ""+place.getAddress(), Toast.LENGTH_SHORT).show();
                                Log.d("ReverseGeoPlace",""+place.getAddress());
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    NearbyPlaceAPI.builder(getApplicationContext())
                            .setDistance(.5)
                            .setLimit(10)
                            .setLatLng(Double.parseDouble(lat.getText().toString()),Double.parseDouble(lon.getText().toString()))
                            .setType("Bank")
                            .build()
                            .generateNearbyPlaceListByType(new NearbyPlaceListener() {
                                @Override
                                public void onPlaceListReceived(ArrayList<NearbyPlace> places) {
                                    Toast.makeText(MainActivity.this, ""+places.get(0).getAddress(), Toast.LENGTH_SHORT).show();
                                    Log.d("Nearby",""+places.size());
                                }

                                @Override
                                public void onFailure(String message) {
                                    Toast.makeText(MainActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            });
            }
        });
        geoCode.setOnClickListener(view -> GeoCodeAPI.builder(getApplicationContext())
                .idOrCode(geo.getText().toString())
                .build()
                .generateList(new PlaceGeoCodeListener() {

                    @Override
                    public void onGeoCodePlace(GeoCodePlace place) {
                        Toast.makeText(MainActivity.this, ""+place.getAddress(), Toast.LENGTH_SHORT).show();
                        Log.d("onGeoCodePlace",""+place.getAddress());
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(MainActivity.this, ""+ message, Toast.LENGTH_SHORT).show();
                    }
                }));
        rupantorSearch.setOnClickListener(view -> {
            RupantorAPI.builder(this)
                    .rawAddress(rupantorquery.getText().toString())
                    .build()
                    .getRupantorPlace(new RupantorPlaceListener() {
                        @Override
                        public void onRupantorPlaceReceived(GeoCodePlace place, String fixedAddress, boolean isCompleteAddress) {
                            Toast.makeText(MainActivity.this,fixedAddress+"\n"+place.getAddress() , Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onRupantorPlaceReceivedWithScore(GeoCodePlace place, String fixedAddress, boolean isCompleteAddress, String confidenceScore) {
                            //Toast.makeText(MainActivity.this,confidenceScore+"\n"+place.getAddress() , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String messege) {
                            Toast.makeText(MainActivity.this,messege, Toast.LENGTH_SHORT).show();

                        }
                    });

        });
    }


}
