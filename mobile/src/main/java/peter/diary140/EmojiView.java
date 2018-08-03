package peter.diary140;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by peterwarrington on 05/11/2017.
 */

public class EmojiView extends ConstraintLayout{
    public EmojiView(Context context) {
        super(context);
        init();
    }

    public EmojiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmojiView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
        private void init() {
                inflate(getContext(),R.layout.emojiview, this);
        }
}
