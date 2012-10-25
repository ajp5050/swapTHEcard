package sgcs.cardbox;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Widget_Create_Text extends Widget_Create_Base {
	public String text;

	private TextView textview;
	private EditText edittext;
	private float temp_x, temp_y;
	private int move_count;
	
	
	public Widget_Create_Text(Context context) {
		super(context);
	}

	public Widget_Create_Text(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Widget_Create_Text(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init() {
		//initialize
		params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
		
		text = "Empty";

		//create textview
		textview = new TextView(act_create2);
        textview.setText(text);
		textview.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					curr_x += (int)(event.getX()-temp_x);
					curr_y += (int)(event.getY()-temp_y);
					setLocation(curr_x, curr_y);
					enable_edit();
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
					
					selected();
				}
				return true;
			}
		});
		
		//create editview
		edittext = new EditText(act_create2);
		edittext.setText(text);
        edittext.setVisibility(EditText.INVISIBLE);
        edittext.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {				
				disable_edit();
				return false;
			}
		});

        //locate these widgets
        textview.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        edittext.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(textview);
        addView(edittext);
	}
	
	public void enable_edit(){
		textview.setVisibility(TextView.INVISIBLE);
		edittext.setVisibility(EditText.VISIBLE);
		
		selected();
	}
	
	public void disable_edit(){
		text = edittext.getText().toString();
		textview.setText(text);
		
		textview.setVisibility(TextView.VISIBLE);
		edittext.setVisibility(EditText.INVISIBLE);
		
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		setLayoutParams(params);
	}
	
	public void setLable(String lable){
		if(lable.equals("name")){
			edittext.setInputType(InputType.TYPE_CLASS_TEXT);
		}
		else if(lable.equals("phone")){
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
		}
	}

}
