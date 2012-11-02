package sgcs.cardbox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.scrollview.api.FullScrollView;

public class ScrollView_Create extends FullScrollView {
	
	public ScrollView_Create(Context context) {
		super(context);
	}
	
	public ScrollView_Create(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollView_Create(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	return false;
    }
}
