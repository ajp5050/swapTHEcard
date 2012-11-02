package sgcs.cardbox;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class View_Create extends FrameLayout{
	//size width = 1063, height = 591 with dip = 300
	Context act_create2;
	
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
	
	private void init(){
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
		addView(widget);
		
		objlist.add(widget);
	}
	
	public void createImageField(String src, int x, int y){
		Widget_Create_Image widget = new Widget_Create_Image(act_create2);
		widget.setParentView(this);
		widget.setLocation(x, y);
		widget.setImageSrc(src);
		addView(widget);
		
		objlist.add(widget);
	}
}