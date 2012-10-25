package sgcs.cardbox;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Activity_Main extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Button
        Button btnCreate = (Button)findViewById(R.id.createCard);
        Button btnRecognize = (Button)findViewById(R.id.recognizeCard);
        Button btnManage = (Button)findViewById(R.id.manageCard);
        Button btnTransmit = (Button)findViewById(R.id.transmitCard);
        btnCreate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Activity_Create1.class);
				startActivity(intent);
			}
		});
        btnRecognize.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Activity_Recognize1.class);
				startActivity(intent);
			}
		});
        btnManage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Activity_Manage1.class);
				startActivity(intent);
			}
		});
        btnTransmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Activity_Transmit1.class);
				startActivity(intent);
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
