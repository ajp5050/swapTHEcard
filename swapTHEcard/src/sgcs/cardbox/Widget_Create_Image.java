package sgcs.cardbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Widget_Create_Image extends Widget_Create_Base {
	private ImageView imageview;
	private float base_width, base_height;
	private float scale_x = 1.0f, scale_y = 1.0f;
	private boolean is_measured = false;
	
	private float temp_x, temp_y;
	private int move_count;
	private float baseDist;
	private boolean is_moving;
	private boolean is_scaling;
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
		menubuilder.setItems(new String[] {"Select Image", "Set Detail Scale","Delete"}, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case 0:
					break;
				case 1:
					break;
				case 2:
					delete();
					break;
				default:
					break;
				}
			}
		});
		menubuilder.create();
		
		//create imageview
		imageview = new ImageView(act_create2);
		imageview.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(is_measured == false){
					base_width = imageview.getWidth();
					base_height = imageview.getHeight();
					is_measured = true;
				}
				
				if(event.getPointerCount() == 1){
					if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
						is_moving = false;
						is_scaling = false;
						move_count = 0;
						temp_x = event.getX();
						temp_y = event.getY();
						selected();
					}
					if(event.getActionMasked() == MotionEvent.ACTION_MOVE){
						if(is_scaling == true)
							return true;
						move_count++;
						if(move_count > 5){
							is_moving = true;
							setLocation(curr_x + (int)(event.getX()-temp_x), curr_y + (int)(event.getY()-temp_y));
							move_count = 0;
						}
					}
					if(event.getActionMasked() == MotionEvent.ACTION_UP){
						if(is_scaling == true)
							return true;
						if(is_moving == true){
							curr_x += (int)(event.getX()-temp_x);
							curr_y += (int)(event.getY()-temp_y);
							setLocation(curr_x, curr_y);
						}
					}
					return false;
				}
				else if(event.getPointerCount() == 2){
					is_scaling = true;
					if(event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
						baseDist = getDistance(event);
					}
					else if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
						float ratio = getDistance(event)/baseDist;
						params.width = (int)(base_width*scale_x*ratio);
						params.height = (int)(base_height*scale_y*ratio);
						params.setMargins(curr_x + (int)(base_width*scale_x*(1.0f-ratio)/2.0f), curr_y + (int)(base_height*scale_y*(1.0f-ratio)/2.0f), 0, 0);
						setLayoutParams(params);
						scale_x = scale_x*ratio;
						scale_y = scale_y*ratio;
						curr_x += (int)(base_width*scale_x*(1.0f-ratio)/4.0f);
						curr_y += (int)(base_height*scale_y*(1.0f-ratio)/4.0f);
					}
					else{
						move_count++;
						if(move_count > 5){
							float ratio = getDistance(event)/baseDist;
							params.width = (int)(base_width*scale_x*ratio);
							params.height = (int)(base_height*scale_y*ratio);
							params.setMargins(curr_x + (int)(base_width*scale_x*(1.0f-ratio)/2.0f), curr_y + (int)(base_height*scale_y*(1.0f-ratio)/2.0f), 0, 0);
							setLayoutParams(params);
						}
					}
					return true;
				}
				else{
					return false;
				}
			}
		});
		imageview.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				boolean consumed = false;
				if(is_moving == false && is_scaling == false){
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
		imageview.setImageResource(R.drawable.h1_naver);
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		setLayoutParams(params);
		
		is_measured = false;
		scale_x = 1.0f;
		scale_y = 1.0f;
	}
	
	private float getDistance(MotionEvent event){
		float dx = event.getX(0) - event.getX(1);
		float dy = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(dx*dx + dy*dy);
	}
}