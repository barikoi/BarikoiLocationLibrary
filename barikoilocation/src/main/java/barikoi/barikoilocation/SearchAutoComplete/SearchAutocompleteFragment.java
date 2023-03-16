package barikoi.barikoilocation.SearchAutoComplete;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import barikoi.barikoilocation.PlaceModels.GeoCodePlace;
import barikoi.barikoilocation.PlaceModels.SearchAutoCompletePlace;
import barikoi.barikoilocation.R;

/**
 * This is the autocomplete search ui for developers
 * this ui can be used as a view component in any activity
 */
public class SearchAutocompleteFragment extends Fragment {
    private static final String TAG="SearchACFragment";
    private static final int requestCode=555;
    private PlaceSelectionListener placeSelectionListener;
    String city="";
    boolean bangla = false;
    EditText barikoiEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmentbarikoiautocomplete, container, false);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barikoiEditText=view.findViewById(R.id.barikoiEditText);
        ActivityResultLauncher<Intent> startSearch= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>(){

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == 555){
                            SearchAutoCompletePlace place= (SearchAutoCompletePlace) result.getData().getSerializableExtra("place_selected");
                            barikoiEditText.setText(place.toString());
                            try {
                                placeSelectionListener.onPlaceSelected(place);
                            }
                            catch (NullPointerException e){
                                Log.d(TAG,"PlaceSelectionListener not Implemented for search auto complete");
                            }
                        }
                        if (result.getResultCode() == Activity.RESULT_CANCELED) {
                            if(result.getData()!=null){
                                String error=result.getData().getStringExtra("error");
                                Log.d(TAG,error);
                                try {
                                    placeSelectionListener.onFailure(error);
                                }
                                catch (NullPointerException e){
                                    Log.d(TAG,"PlaceSelectionListener not Implemented for search auto complete");
                                }
                            }
                            else{
                                try {
                                    placeSelectionListener.onFailure("Nothing Selected");
                                }
                                catch (NullPointerException e){
                                    Log.d(TAG,"PlaceSelectionListener not Implemented for search auto complete");
                                }
                            }
                            //Write your code if there's no result
                        }
                    }
                });
        barikoiEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(getActivity(),SearchAutoCompleteActivity.class);
                if(city.length()>0) intent.putExtra("city", city);
                if(bangla) intent.putExtra("bangla", bangla);
//                startActivityForResult(intent,requestCode);
                startSearch.launch(intent);
                return true;
            }
        });
    }

    public void setCity(String city){
        this.city=city;
    }

    public void setBangla(boolean bangla){
        this.bangla=bangla;
    }
    public void setTextHint(String str){
        barikoiEditText=getView().findViewById(R.id.barikoiEditText);
        barikoiEditText.setHint(str);
    }
    public void setPlaceSelectionListener(PlaceSelectionListener placeSelectionListener){
      try{
          this.placeSelectionListener = placeSelectionListener;
      }
      catch (Exception e){}
    }
    public interface PlaceSelectionListener {
        void onPlaceSelected(SearchAutoCompletePlace place);
        void onFailure(String error);
    }


}
