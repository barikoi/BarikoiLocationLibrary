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
    Button submit;
    TextView tvplace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BarikoiAPI.getINSTANCE(this,"MTExMTpKQkZZMzNIQk45");
        submit=findViewById(R.id.submit);
        tvplace=findViewById(R.id.place);
        ReverseGeoAPI.builder(this)
                .SetLatLng(23.83723803415923,90.36668110638857)
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
        GeoCodeAPI.builder(this)
                .nameOrCode("bkoi2017")
                .build()
                .generatelist(new PlaceGeoCodeListener() {
            @Override
            public void geoCodePlace(Place place) {
                Log.d("geoCodePlace",""+place.getAddress());
            }

            @Override
            public void onFailure(String Message) {
            }
        });
        NearbyPlaceAPI.builder(this)
                .setDistance(.5)
                .setLimit(10)
                .setLatLng(23.83723803415923,90.36668110638857)
                .build()
                .generateNearbyPlaceList(new NearbyPlaceListener() {
            @Override
            public void OnPlaceListReceived(ArrayList<Place> places) {
                Log.d("NearbyAPILIST",""+places.size());
            }

            @Override
            public void OnFailure(String message) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
       /* SearchAutoCompleteAPI searchAutoCompleteAPI=new SearchAutoCompleteAPI(this, new SearchAutoCompleteListener() {
            @Override
            public void OnPlaceListReceived(ArrayList<Place> places) {
                //Toast.makeText(MainActivity.this, places.get(0).getAddress(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {

            }
        });
        searchAutoCompleteAPI.generatelist("Barikoi");*/
        /*GeoCodeAPI geoCodeAPI=new GeoCodeAPI(this, new PlaceGeoCodeListener() {
            @Override
            public void geoCodePlace(Place place) {
                tvplace.setText(place.getAddress());
            }

            @Override
            public void onFailure(String Message) {

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
