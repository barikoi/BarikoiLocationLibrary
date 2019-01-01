package barikoi.barikoilocation.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    Button submit;
    TextView tvplace;
    ReverseGeoAPI currentLocation;
    NearbyPlaceAPI nearbyPlaceListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BarikoiAPI.getINSTANCE(this,"MTExMTpKQkZZMzNIQk45");
        submit=findViewById(R.id.submit);
        tvplace=findViewById(R.id.place);
        currentLocation=new ReverseGeoAPI(this, new ReverseGeoAPIListener() {
            @Override
            public void reversedAddress(Place address) {
                tvplace.setText(address.getAddress());
            }
        });
        nearbyPlaceListener=new NearbyPlaceAPI(this, new NearbyPlaceListener() {
            @Override
            public void OnPlaceListReceived(ArrayList<Place> places) {
                Toast.makeText(MainActivity.this, ""+places.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(String Message) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latittude=23.83723803415923;
                double longitude=90.36668110638857;
               /* currentLocation.getAddress(latittude,longitude);*/
                nearbyPlaceListener.generateNearbyPlaceList(.5,100,latittude,longitude);
            }
        });
       /* SearchAutoCompleteAPI searchAutoCompleteAPI=new SearchAutoCompleteAPI(this, new SearchAutoCompleteListener() {
            @Override
            public void OnPlaceListReceived(ArrayList<Place> places) {
                //Toast.makeText(MainActivity.this, places.get(0).getAddress(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(String message) {

            }
        });
        searchAutoCompleteAPI.generatelist("Barikoi");*/
        /*GeoCodeAPI geoCodeAPI=new GeoCodeAPI(this, new PlaceGeoCodeListener() {
            @Override
            public void GeoCodePlace(Place place) {
                tvplace.setText(place.getAddress());
            }

            @Override
            public void OnFailure(String Message) {

            }
        });*/

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
