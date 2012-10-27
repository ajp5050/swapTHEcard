package sgcs.cardbox;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class View_Create extends FrameLayout{
	Context act_create2;
	final int card_width = 1063, card_height = 591;
	int changed_width = 1063, changed_height = 591;
	int view_width, view_height;
	
	public ArrayList<Widget_Create_Base> objlist;
	public int selected_object_index = -1;
	
	public View_Create(Context context) {
		super(context);
		act_create2 = context;
		init();
	}
	
	public View_Create(Context context, AttributeSet attrs) {
		super(context, attrs);
		act_create2 = context;
		init();
	}
	
	public View_Create(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		act_create2 = context;
		init();
	}
	
	public class CanvasView extends View{
		public CanvasView(Context context) {
			super(context);
			setOnClickListener(new View.OnClickListener(){
				public void onClick(View v) {
					if(selected_object_index != -1)
						objlist.get(selected_object_index).disable_edit();
					selected_object_index = -1;
				}
			});
		}
		
		public void onDraw(Canvas canvas){
	        view_width = this.getWidth();
			view_height = this.getHeight();
		}
	}
	
	private void init(){
		CanvasView cv = new CanvasView(act_create2);
		addView(cv);
		
		objlist = new ArrayList<Widget_Create_Base>();
		objlist.clear();
	}
	
	public void setFormat(int format){
		switch(format){
        case 0: //empty
        	break;
        case 1: //design 1
        	break;
        case 2: //design 2
        	break;
        default:
        	break;
        }
	}
	
	public void createTextField(String lable, int x, int y){
		Widget_Create_Text widget = new Widget_Create_Text(act_create2);
		widget.setParentView(this);
		widget.setLocation(x, y);
		widget.setLable(lable);
		this.addView(widget);
		
		objlist.add(widget);
	}
	
	public void createImageField(String src, int x, int y){
		Widget_Create_Image widget = new Widget_Create_Image(act_create2);
		widget.setParentView(this);
		widget.setLocation(x, y);
		widget.setImageSrc(src);
		this.addView(widget);
		
		objlist.add(widget);
	}
}