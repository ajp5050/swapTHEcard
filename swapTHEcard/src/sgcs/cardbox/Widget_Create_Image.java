package sgcs.cardbox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Widget_Create_Image extends Widget_Create_Base {
	private ImageView imageview;
	private float temp_x, temp_y;
	private int move_count;
	
	public Widget_Create_Image(Context context) {
		super(context);
	}

	public Widget_Create_Image(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Widget_Create_Image(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init() {
		//initialize
		params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);

		//create imageview
		imageview = new ImageView(act_create2);
		imageview.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					curr_x += (int)(event.getX()-temp_x);
					curr_y += (int)(event.getY()-temp_y);
					setLocation(curr_x, curr_y);
				}
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					move_count++;
					if(move_count > 5){
						setLocation(curr_x + (int)(event.getX()-temp_x), curr_y + (int)(event.getY()-temp_y));
						move_count = 0;
					}
				}
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					move_count = 0;
					temp_x = event.getX();
					temp_y = event.getY();
				}
				return true;
			}
		});

        //locate these widgets
        imageview.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(imageview);
	}
	
	public void enable_edit(){
		
	}
	
	public void disable_edit(){
		
	}
}
