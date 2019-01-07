package barikoi.barikoilocation.SearchAutoComplete;

import android.app.Activity;
import android.content.Context;
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

import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.R;

/**
 * This is the autocomplete search ui for developers
 * this ui can be used as a view component in any activity
 */
public class BarikoiSearchAutocomplete extends Fragment{
    private GetSelectedPlaceListener getSelectedPlaceListener;
    Place place;
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
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.getSelectedPlaceListener =(GetSelectedPlaceListener)context;
    }

    public interface GetSelectedPlaceListener{
        void getSelectedPlaceListener(Place place);
        default void getError(String error){};
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                place= (Place) data.getSerializableExtra("place_selected");
                barikoiEditText.setText(place.toString());
                this.getSelectedPlaceListener.getSelectedPlaceListener(this.place);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                if(data!=null){
                    String error=data.getStringExtra("error");
                    Log.d("BarikoiErrorFragment",error);
                    this.getSelectedPlaceListener.getError(error);
                }
                else{
                    this.getSelectedPlaceListener.getError("Nothing Selected");
                }
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
