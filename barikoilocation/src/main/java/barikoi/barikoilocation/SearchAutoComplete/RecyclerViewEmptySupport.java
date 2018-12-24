package barikoi.barikoilocation.SearchAutoComplete;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewEmptySupport extends RecyclerView {
    private View emptyView,nonetview;

    /*private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && emptyView != null &&query!=null) {
                if(adapter.getItemCount() == 0 && query.getText().toString().length()>2) {
                    emptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.GONE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                }
            }

        }
    };*/

    public void emptyshow(boolean empty){
        if(empty) {
            if(emptyView!=null){
                emptyView.setVisibility(View.VISIBLE);
            }
            if(nonetview!=null) {
                nonetview.setVisibility(GONE);
            }
            RecyclerViewEmptySupport.this.setVisibility(View.GONE);
        }
        else {
            if(emptyView!=null){
                emptyView.setVisibility(View.GONE);
            }
            if(nonetview!=null) {
                nonetview.setVisibility(GONE);
            }
            RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
        }
    }

    public void nonetshow(boolean nonet){
        if(nonet){
            emptyView.setVisibility(View.GONE);
            nonetview.setVisibility(VISIBLE);
            RecyclerViewEmptySupport.this.setVisibility(GONE);
        }
        else {
            emptyView.setVisibility(View.GONE);
            nonetview.setVisibility(GONE);
            RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
        }
    }
    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setNonetview(View nonetview){
        this.nonetview=nonetview;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

}
