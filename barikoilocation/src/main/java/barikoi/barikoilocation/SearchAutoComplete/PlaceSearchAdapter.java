package barikoi.barikoilocation.SearchAutoComplete;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import barikoi.barikoilocation.PlaceModels.SearchAutoCompletePlace;
import barikoi.barikoilocation.R;

/**
 * This class is used to hold the Place list of search autocomplete results
 */
public class PlaceSearchAdapter extends RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder> implements Filterable {

    private ArrayList<SearchAutoCompletePlace> places;
    ///private Context context;
    private OnPlaceItemSelectListener opsl;
    private boolean bangla=false;
    //private ArrayList<Place> placeListFiltered;

    public PlaceSearchAdapter(ArrayList<SearchAutoCompletePlace> places, boolean bangla, OnPlaceItemSelectListener opsl){
        this.places=places;
        this.bangla=bangla;
        //this.placeListFiltered=places;
        this.opsl=opsl;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = places.get(position);

        holder.placeView.setText(bangla?holder.mItem.getAddressBn():  holder.mItem.getAddress());
        holder.areatag.setText(bangla? holder.mItem.getAreaBn():holder.mItem.getArea());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opsl.onPlaceSelected( holder.mItem,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                ArrayList<SearchAutoCompletePlace> filteredList = new ArrayList<SearchAutoCompletePlace>();
                if (charString.isEmpty()) {
                    filteredList = places;
                } else {

                    for (SearchAutoCompletePlace row : places) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAddress().toLowerCase().contains(charString.toLowerCase())  || row.getCode().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                filterResults.count= filteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //placeListFiltered = (ArrayList<Place>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public ArrayList<SearchAutoCompletePlace> getList() {
        return places;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final View mView;
        public final TextView placeView;
        public final TextView areatag;
        public SearchAutoCompletePlace mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            placeView =  mView.findViewById(R.id.textView_placename);
            areatag=  mView.findViewById(R.id.textViewAreaTag);
        }
    }

    public interface OnPlaceItemSelectListener{

        void onPlaceSelected(SearchAutoCompletePlace mItem, int position);
    }
}

