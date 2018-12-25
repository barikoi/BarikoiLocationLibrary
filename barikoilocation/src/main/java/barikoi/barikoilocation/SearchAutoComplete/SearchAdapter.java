package barikoi.barikoilocation.SearchAutoComplete;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {

    private ArrayList<Place> places;
    ///private Context context;
    private OnPlaceItemSelectListener opsl;
    //private ArrayList<Place> placeListFiltered;

    public SearchAdapter(ArrayList<Place> places, OnPlaceItemSelectListener opsl){
        this.places=places;
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

        holder.placeView.setText(holder.mItem.getAddress());
        holder.areatag.setText(holder.mItem.getArea());
        holder.codeView.setText(holder.mItem.getCode());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opsl.onPlaceSelected( holder.mItem,position);
            }
        });

    }

    public int findposition(Place p){
        int position=0;
        for (int i=0; i<getItemCount();i++){
            if(places.get(i).getCode().equals(p.getCode())){
                position=i;
            }
        }
        return  position;
    }
    public int findposition(String s){
        int position=0;
        for (int i=0; i<getItemCount();i++){
            if(places.get(i).getCode().equals(s)){
                position=i;
            }
        }

        return  position;
    }

    public int findqueryposition(String query){
        int position=0;
        for (int i=0; i<getItemCount();i++){
            if(places.get(i).getAddress().matches(query)){
                position=i;
            }
        }
        return  position;
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
                ArrayList<Place> filteredList = new ArrayList<Place>();
                if (charString.isEmpty()) {
                    filteredList = places;
                } else {

                    for (Place row : places) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAddress().toLowerCase().contains(charString.toLowerCase()) || row.getSubType().contains(charString) || row.getCode().contains(charString)) {
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

    public void removeAt(int position) {
        places.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, places.size());
    }

    public ArrayList<Place> getList() {
        return places;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public final View mView;
        public final TextView codeView;
        public final TextView placeView;
        public final TextView areatag;
        public Place mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            codeView =  mView.findViewById(R.id.textViewCode);
            placeView =  mView.findViewById(R.id.textView_placename);
            areatag=  mView.findViewById(R.id.textViewAreaTag);
        }
    }

    public interface OnPlaceItemSelectListener{

        void onPlaceSelected(Place mItem, int position);
    }
}

