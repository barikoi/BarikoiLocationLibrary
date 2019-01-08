package barikoi.barikoilocation.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import java.util.ArrayList;

import barikoi.barikoilocation.BarikoiAPI;
import barikoi.barikoilocation.GeoCode.GeoCodeAPI;
import barikoi.barikoilocation.GeoCode.PlaceGeoCodeListener;
import barikoi.barikoilocation.NearbyPlace.NearbyPlaceAPI;
import barikoi.barikoilocation.NearbyPlace.NearbyPlaceListener;
import barikoi.barikoilocation.ReverseGeo.ReverseGeoAPI;
import barikoi.barikoilocation.ReverseGeo.ReverseGeoAPIListener;
import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.SearchAutoComplete.BarikoiSearchAutocomplete;
import barikoi.barikoilocation.SearchAutoComplete.SearchAutoCompleteAPI;
import barikoi.barikoilocation.SearchAutoComplete.SearchAutoCompleteListener;

public class MainActivity extends AppCompatActivity implements BarikoiSearchAutocomplete.GetSelectedPlaceListener {
    Button geoCode,nearby,reverseGeo;
    EditText lat,lon,geo;
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


        reverseGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReverseGeoAPI.builder(getApplicationContext())
                        .SetLatLng(Double.parseDouble(lat.getText().toString()),Double.parseDouble(lon.getText().toString()))
                        .build()
                        .getAddress(new ReverseGeoAPIListener() {
                            @Override
                            public void reversedAddress(Place place) {
                                Toast.makeText(MainActivity.this, ""+place.getAddress(), Toast.LENGTH_SHORT).show();
                                Log.d("ReverseGeoPlace",""+place.getAddress());
                            }

                            @Override
                            public void onFailure(String message) {

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
                        .build()
                        .generateNearbyPlaceList(new NearbyPlaceListener() {
                            @Override
                            public void OnPlaceListReceived(ArrayList<Place> places) {
                                Toast.makeText(MainActivity.this, ""+places.get(0).getAddress(), Toast.LENGTH_SHORT).show();
                                Log.d("NearbyAPILIST",""+places.size());
                            }
                            @Override
                            public void OnFailure(String message) {

                            }
                        });
            }
        });
      geoCode.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              GeoCodeAPI.builder(getApplicationContext())
                      .nameOrCode(geo.getText().toString())
                      .build()
                      .generatelist(new PlaceGeoCodeListener() {
                          @Override
                          public void onGeoCodePlace(Place place) {
                              Toast.makeText(MainActivity.this, ""+place.getAddress(), Toast.LENGTH_SHORT).show();
                              Log.d("onGeoCodePlace",""+place.getAddress());
                          }

                          @Override
                          public void onFailure(String message) {
                              Toast.makeText(MainActivity.this, ""+ message, Toast.LENGTH_SHORT).show();

                          }
                      });

          }
      });

    }
    @Override
    public void getSelectedPlaceListener(Place place) {
        Toast.makeText(MainActivity.this, place.getAddress(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getError(String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
    }
}
