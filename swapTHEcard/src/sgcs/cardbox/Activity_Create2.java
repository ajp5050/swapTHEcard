package sgcs.cardbox;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_Create2 extends Activity {

	View_Create view_create;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_create2);
        
        view_create = (View_Create)findViewById(R.id.view_create);
        Intent intent = getIntent();
        view_create.setFormat(intent.getIntExtra("format", 0));
        
        Button btnText = (Button)findViewById(R.id.create_text);
        Button btnImage = (Button)findViewById(R.id.create_image);
        btnText.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				view_create.createTextField("name", 50, 50);
			}
		});
        btnImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				view_create.createImageField("src", 50, 50);
			}
		});
	}
}