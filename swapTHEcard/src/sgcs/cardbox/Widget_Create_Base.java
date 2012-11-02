package sgcs.cardbox;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class Widget_Create_Base extends FrameLayout{
	protected Context act_create2;
	protected View_Create view_create;
	protected FrameLayout.LayoutParams params;
	
	protected int curr_x=0, curr_y=0;
	
	public Widget_Create_Base(Context context) {
		super(context);
		act_create2 = context;
		init();
	}

	public Widget_Create_Base(Context context, AttributeSet attrs) {
		super(context, attrs);
		act_create2 = context;
		init();
	}

	public Widget_Create_Base(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		act_create2 = context;
		init();
	}

	public void init() {

	}
	
	public void enable_edit(){
		
	}
	
	public void disable_edit(){
		
	}

	public void setParentView(View_Create view_Create) {
		view_create = view_Create;
	}
	
	public void setLocation(int x, int y){
		curr_x = x;
		curr_y = y;
		params.setMargins(curr_x, curr_y, 0, 0);
		setLayoutParams(params);
	}
	
	public void selected(){
		if(view_create.selected_object_index == view_create.objlist.indexOf(this))
			return;
		if(view_create.selected_object_index != -1)
			view_create.objlist.get(view_create.selected_object_index).disable_edit();
		view_create.selected_object_index = view_create.objlist.indexOf(this);
	}
	
	public void delete(){
		if(view_create.selected_object_index != -1)
			view_create.objlist.get(view_create.selected_object_index).disable_edit();
		view_create.selected_object_index = -1;
		
		view_create.objlist.remove(this);
		view_create.removeView(this);
	}
}
