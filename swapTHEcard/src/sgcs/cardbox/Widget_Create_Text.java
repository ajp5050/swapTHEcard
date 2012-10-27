package sgcs.cardbox;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
	private boolean is_moving;
	private AlertDialog.Builder menubuilder;
	
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
		
		menubuilder = new AlertDialog.Builder(act_create2);
		menubuilder.setTitle("Menu");
		menubuilder.setItems(new String[] {"Select Lable", "Edit Font", "Delete"}, new DialogInterface.OnClickListener() {
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
		
		text = "Empty";

		//create textview
		textview = new TextView(act_create2);
        textview.setText(text);
		textview.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				boolean consumed = false;
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
						consumed = true;
					}
				}
				return consumed;
			}
		});
		textview.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				boolean consumed = false;
				if(is_moving == false){
					menubuilder.show();
					consumed = true;
				}
				return consumed;
			}
		});
		textview.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				enable_edit();
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
