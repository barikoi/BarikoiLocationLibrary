package barikoi.barikoilocation.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import barikoi.barikoilocation.ReverseGeoAPI;
import barikoi.barikoilocation.ReverseGeoAPIListener;
import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.SearchAutoComplete.BarikoiSearchAutocomplete;
import barikoi.barikoilocation.SearchAutoComplete.SearchAutoCompleteAPI;
import barikoi.barikoilocation.SearchAutoComplete.SearchAutoCompleteListener;

public class MainActivity extends AppCompatActivity {
    EditText lat,lon;
    Button submit;
    TextView place;
    ReverseGeoAPI currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat=findViewById(R.id.Lat);
        lon=findViewById(R.id.Lon);
        submit=findViewById(R.id.submit);
        place=findViewById(R.id.place);
        currentLocation=new ReverseGeoAPI(this, new ReverseGeoAPIListener() {
            @Override
            public void reversedAddress(Place address) {
                place.setText(address.getAddress());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latittude=Double.parseDouble(lat.getText().toString());
                double longitude=Double.parseDouble(lon.getText().toString());
                currentLocation.getAddress(latittude,longitude);
            }
        });
        SearchAutoCompleteAPI searchAutoCompleteAPI=new SearchAutoCompleteAPI(this, new SearchAutoCompleteListener() {
            @Override
            public void OnPlaceListRecieved(ArrayList<Place> places) {

            }
        });
        searchAutoCompleteAPI.generatelist("Barikoi");
    }
}
