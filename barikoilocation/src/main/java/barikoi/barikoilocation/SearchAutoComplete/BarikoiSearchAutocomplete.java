package barikoi.barikoilocation.SearchAutoComplete;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class BarikoiSearchAutocomplete extends EditText{


    public BarikoiSearchAutocomplete(Context context) {
        super(context);
    }

    public BarikoiSearchAutocomplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarikoiSearchAutocomplete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent intent=new Intent(getContext(),SearchAutoCompleteActivity.class);
        getContext().startActivity(intent);
        return false;
    }
}
