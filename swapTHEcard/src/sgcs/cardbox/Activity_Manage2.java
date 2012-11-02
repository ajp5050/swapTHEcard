package sgcs.cardbox;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Manage2 extends Activity {

	protected DBAdapter_Manage dbdata;
	private int DBindex;
	
	class UserData{
		UserData(String aName, String aCompany, String aPosition, String aPhone){
			name = aName;
			company = aCompany;
			position = aPosition;
			phone = aPhone;
		}
		String name;
		String company;
		String position;
		String phone;		
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_manage2);
	    
	    Intent intent = getIntent();
	    DBindex = intent.getIntExtra("userIndex", 0);
	    
	    dbdata = new DBAdapter_Manage(this);
        dbdata.open();
        
        Cursor cursor = dbdata.fetchBook(DBindex);
        startManagingCursor(cursor);
        
        UserData data = new UserData(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        
        TextView name = (TextView)findViewById(R.id.textViewName2);
        TextView company = (TextView)findViewById(R.id.textViewComp2);
        TextView position = (TextView)findViewById(R.id.textViewPos2);
        TextView phone = (TextView)findViewById(R.id.textViewPhone2);
        
        name.setText(data.name);
        company.setText(data.company);
        position.setText(data.position);
        phone.setText(data.phone);
        
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuItem item_delete, item_add, item_pos;
		item_delete = menu.add(1, 1, 1, "삭제");
		item_add = menu.add(1, 2, 2, "전화번호부 에 추가");
		item_pos = menu.add(1, 3, 3, "대표명함 지정");
		
		return true;		
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case 1:	// 삭제
	        boolean result;
	        result = dbdata.deleteBook(DBindex);
	        if(result == true){
	        	Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
	        	return true;
	        }
	        else{
	        	Toast.makeText(getApplicationContext(), "삭제가 실행되지 않았습니다.", Toast.LENGTH_SHORT).show();
	        	return false;
	        }
		case 2:	// 전화번호부에 추가
			
			return true;
		case 3:	// 대표명함 지정
			
			return true;
		
		}
		return false;
	}


}
