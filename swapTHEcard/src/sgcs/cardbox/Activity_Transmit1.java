package sgcs.cardbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_Transmit1 extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit1);
        
        //button
        Button btnBump = (Button)findViewById(R.id.transmitCard);
        btnBump.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), BumpTest.class);
				startActivity(intent);
			}
		});
	}

}
