package sgcs.cardbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Widget_Create_Image extends Widget_Create_Base {
	private ImageView imageview;
	
	private float temp_x, temp_y;
	private int move_count;
	private boolean is_moving;
	private AlertDialog.Builder menubuilder;
	
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

		menubuilder = new AlertDialog.Builder(act_create2);
		menubuilder.setTitle("Menu");
		menubuilder.setItems(new String[] {"Select Image", "Delete"}, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case 0:
					;
				case 1:
					delete();
				default:
					;
				}
			}
		});
		menubuilder.create();
		
		//create imageview
		imageview = new ImageView(act_create2);
		imageview.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					is_moving = false;
					move_count = 0;
					temp_x = event.getX();
					temp_y = event.getY();
					selected();
				}
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					move_count++;
					if(move_count > 5){
						is_moving = true;
						setLocation(curr_x + (int)(event.getX()-temp_x), curr_y + (int)(event.getY()-temp_y));
						move_count = 0;
					}
				}
				if(event.getAction() == MotionEvent.ACTION_UP){
					if(is_moving == true){
						curr_x += (int)(event.getX()-temp_x);
						curr_y += (int)(event.getY()-temp_y);
						setLocation(curr_x, curr_y);
					}
				}
				return false;
			}
		});
		imageview.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				boolean consumed = false;
				if(is_moving == false){
					menubuilder.show();
					consumed = true;
				}
				return consumed;
			}
		});
		
        //locate these widgets
        imageview.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(imageview);
	}

	public void setImageSrc(String src) {

	}
}
