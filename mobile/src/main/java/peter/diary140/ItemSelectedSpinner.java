package peter.diary140;

import android.view.View;
import android.widget.AdapterView;

public class ItemSelectedSpinner implements AdapterView.OnItemSelectedListener {
    int posg;
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        posg=pos;
    }
    public String getselected() {
        switch (posg) {
            case(0) :
                return "5";
            case (1) :
                return "4";
            case (2) :
                return "3";
            case (3):
                return "2";
            case (4) :
                return "1";
        }
        return "5";
    }
    public void onNothingSelected(AdapterView parent) {
        // Do nothing.
    }
}
