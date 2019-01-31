package barikoi.barikoilocation.SearchAutoComplete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import barikoi.barikoilocation.PlaceModels.SearchAutoCompletePlaceModel;
import barikoi.barikoilocation.R;

/**
 * This is the autocomplete search ui for developers
 * this ui can be used as a view component in any activity
 */
public class SearchAutocompleteFragment extends Fragment{
    private static final String TAG="1";
    private static final int requestCode=555;
    private PlaceSelectionListener placeSelectionListener;
    SearchAutoCompletePlaceModel place;
    EditText barikoiEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmentbarikoiautocomplete, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barikoiEditText=view.findViewById(R.id.barikoiEditText);
        barikoiEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barikoiEditText.setText(getContext().getText(R.string.address));
                Intent intent=new Intent(getActivity(),SearchAutoCompleteActivity.class);
                startActivityForResult(intent,requestCode);
            }
        });
    }

    public void setPlaceSelectionListener(PlaceSelectionListener placeSelectionListener){
      try{
          this.placeSelectionListener = placeSelectionListener;
      }
      catch (Exception e){}
    }
    public interface PlaceSelectionListener {
        void onPlaceSelected(SearchAutoCompletePlaceModel place);
        void onFailure(String error);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == this.requestCode) {
            if(resultCode == Activity.RESULT_OK){
                place= (SearchAutoCompletePlaceModel) data.getSerializableExtra("place_selected");
                barikoiEditText.setText(place.toString());
                try {
                    this.placeSelectionListener.onPlaceSelected(this.place);
                }
                catch (NullPointerException e){
                    Log.d(TAG,"PlaceSelectionListener not Implemented for search auto complete");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                if(data!=null){
                    String error=data.getStringExtra("error");
                    Log.d(TAG,error);
                    try {
                        this.placeSelectionListener.onFailure(error);
                    }
                    catch (NullPointerException e){
                        Log.d(TAG,"PlaceSelectionListener not Implemented for search auto complete");
                    }
                }
                else{
                    try {
                        this.placeSelectionListener.onFailure("Nothing Selected");
                    }
                    catch (NullPointerException e){
                        Log.d(TAG,"PlaceSelectionListener not Implemented for search auto complete");
                    }
                }
                //Write your code if there's no result
            }
        }
    }
}
