package sgcs.cardbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_Create1  extends Activity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create1);
        
      //Button
        Button btnEmpty = (Button)findViewById(R.id.design_empty);
        Button btnBase1 = (Button)findViewById(R.id.design_base1);
        Button btnBase2 = (Button)findViewById(R.id.design_base2);
        btnEmpty.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Activity_Create2.class);
				intent.putExtra("format", 0);
				startActivity(intent);
			}
		});
        btnBase1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Activity_Create2.class);
				intent.putExtra("format", 1);
				startActivity(intent);
			}
		});
        btnBase2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Activity_Create2.class);
				intent.putExtra("format", 2);
				startActivity(intent);
			}
		});
    }
}
