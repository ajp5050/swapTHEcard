package sgcs.cardbox;

import java.util.ArrayList;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Activity_Manage1 extends Activity {

	private ArrayList<MyItem> arItem;
	protected DBAdapter_Manage dbdata;
	
	// spinner���� ���õ� index�� ����
	protected int mPos;
    protected String mSelection;
    
    public ListView MyList;
	public MyListAdapter MyAdapter;
    
	// Spinner
    protected Spinner SortSpinner;
    protected ArrayAdapter<CharSequence> SortSelectList; //���ڿ� ����� ����
    
    //search
    public EditText SearchText;
    
    // DB���� ���� ����
    public static final int DB_INDEX = 0;
    public static final int DB_NAME = 1;
    public static final int DB_COMPANY = 2;
    public static final int DB_POSITION = 3;
    public static final int DB_PHONE = 4;

    
    // �ڷḦ �����ϴ� ����
	class MyItem{
		MyItem(int aIcon, String aLD, int aDBindex, String aSD){
			Icon = aIcon;
			LargeData = aLD;
			DBindex = aDBindex;
			SmallData = aSD;
		}
		String LargeData;
		int Icon;
		int DBindex;
		String SmallData;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage1);
        
        mPos = DB_NAME;
        mSelection = "name";

        //xml�� ������ ���ǳʸ� id������ �ҷ���
        SortSpinner = (Spinner) findViewById (R.id.manage_SortKind);
        
        //���ǳʸ� ���� �� ������ �˾��� ������ ����
        SortSpinner.setPrompt("���� ���");
        
        //����� ��ü�� �����ϰ� ������ ������ ���ҽ��� ���ڿ� ����
        SortSelectList = ArrayAdapter.createFromResource(this, R.array.manage_SortingBy, android.R.layout.simple_spinner_item);
        
        //���ǳʿ� adapter ����
        SortSpinner.setAdapter(SortSelectList);
        
        SortSelectList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(this, SortSelectList);
        SortSpinner.setOnItemSelectedListener(spinnerListener);
  		
        /* DB delete (only using debug)
        this.deleteDatabase("carddatum.db");
        */
        
        // database�� �ҷ��´�
        dbdata = new DBAdapter_Manage(this);
        dbdata.open();

        
        /* DB add data (only using debug)*/
        /*
        dbdata.createBook("������", "�Ｚ����", "�븮","01012344323");
        dbdata.createBook("������", "LG����", "����", "024568877");
        dbdata.createBook("������", "�������б�", "����", "01083746372");
        dbdata.createBook("Christina", "�Ｚ����", "�븮", "01011542211");
        dbdata.createBook("Angela", "LG����", "����", "0310000012");
        dbdata.createBook("Brian", "�λ��������", "����", "01045624154");
        */
        
        Cursor cursor;
        arItem = new ArrayList<MyItem>();
        MyItem mi;
        
        arItem.clear();
        
        // ����� ������ �ҷ��� �̸����� sorting�ؼ� listing
        cursor = dbdata.fetchAllBooks(mSelection);
//        startManagingCursor(cursor);
        
        if(cursor.getCount() > 0){
        	while(cursor.moveToNext()){
        		String smallData;
        		if(mPos == 1){	// �̸��� sorting
        			smallData = cursor.getString(DB_COMPANY) + ", " + cursor.getString(DB_POSITION);
        		}
        		else if(mPos == 2){	// ȸ�纰 sorting
        			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_POSITION);
        		}
        		else if(mPos == 3){	// ������ sorting
        			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_COMPANY);
        		}
        		else{
        			smallData = null;
        		}
        		mi = new MyItem(R.drawable.ic_menu_search, cursor.getString(mPos), cursor.getInt(DB_INDEX), smallData);	// �̸�, index
        		arItem.add(mi);
        	}
        }
        
        cursor.close();

    	MyAdapter = new MyListAdapter(this, R.layout.list_item_view, arItem);
    	MyList = (ListView)findViewById(R.id.manage_listPerson);
    	MyList.setAdapter(MyAdapter);
    	MyList.setOnItemClickListener(ListItemClickListener);
        
        EditText SearchText = (EditText)findViewById(R.id.manage_SearchText);
        /*original source
        SearchText.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSearchRequested();
			}
		});
		*/
        /*adding search*/
        MyList.setTextFilterEnabled(true);
        SearchText.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				MyAdapter.setFilter(s.toString());
				MyAdapter.getFilter().filter("");
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				;
			}
			public void afterTextChanged(Editable s) {
				;
			}
        });
        /**/
	}
	
	AdapterView.OnItemClickListener ListItemClickListener = new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(getApplicationContext(), Activity_Manage2.class);
					intent.putExtra("userIndex", arItem.get(position).DBindex);
					startActivity(intent);
				}
			};
	
	// �̸��� ���� preview�� �����ֱ� ���� form, sorting �ÿ� ����� ���� ������ ��Ÿ���� �⺻ �����̴�.
	private class MyListAdapter extends ArrayAdapter<MyItem>{
		Context maincon;
		LayoutInflater Inflater;
		ArrayList<MyItem> arSrc;
		int layout;
		
		private String filter = "";
		
		public MyListAdapter(Context context, int alayout, ArrayList<MyItem> aarSrc){
			super(context, alayout, aarSrc);
			maincon = context;
			Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arSrc = aarSrc;
			layout = alayout;
		}

		public int getCount() {
			/*original source
			return arSrc.size();
			*/
			/*adding search*/
			int size = 0;
			int fullsize = arSrc.size();
			MyItem temp;
			if(!filter.equals("")){
				for(int i = 0; i < fullsize; i++){
					temp = arSrc.get(i);
					if(temp.LargeData.contains(filter)){
						size++;
					}
				}
			}
			else{
				size = fullsize;
			}
			return size;
			/**/
		}

		public MyItem getItem(int position) {
			/*original source
			return arSrc.get(position);
			*/
			/*adding search*/
			int itemindex = 0;
			int fullsize = arSrc.size();
			MyItem temp;
			if(!filter.equals("")){
				for(int i = 0; i < fullsize; i++){
					temp = arSrc.get(i);
					if(temp.LargeData.contains(filter)){
						if(position == itemindex)
							return temp;
						itemindex++;
					}
				}
			}
			else{
				return arSrc.get(position);
			}
			return null;
			/**/
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = Inflater.inflate(layout, parent, false);
			}
			ImageView img = (ImageView)convertView.findViewById(R.id.list_view_row_profile_thumbnail);
			img.setImageResource(getItem(position).Icon);
			
			TextView txt = (TextView)convertView.findViewById(R.id.list_view_row_user_screen_name);
			txt.setText(getItem(position).LargeData);
			
			TextView smallTxt = (TextView)convertView.findViewById(R.id.list_view_row_user_small);
			smallTxt.setText(getItem(position).SmallData);
			
			return convertView;
		}
		
		public void setFilter(String s) {
			filter = s;
		}
	}
	
	
	// spinner���� ������ ���� �������� ����
	public class myOnItemSelectedListener implements OnItemSelectedListener {
		
		ArrayAdapter mLocalAdapter;
        Activity mLocalContext;


		public myOnItemSelectedListener(Activity_Manage1 activity_Manage1, ArrayAdapter<CharSequence> sortSelectList) {
			this.mLocalContext = activity_Manage1;
	        this.mLocalAdapter = sortSelectList;
		}

		public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {
			mPos = pos + 1;
           
            arItem.clear();
            
            Cursor cursor;
            if(pos == 0){
            	mSelection = "name";
            }
            else if(pos == 1){
            	mSelection = "company";
            }
            else if(pos == 2){
            	mSelection = "position";
            }
            else{
            	
            }
            
            cursor = dbdata.fetchAllBooks(mSelection);            	
//            startManagingCursor(cursor);
            
            if(cursor.getCount() > 0){
            	while(cursor.moveToNext()){
            		String smallData;
            		if(mPos == 1){	// �̸��� sorting
            			smallData = cursor.getString(DB_COMPANY) + ", " + cursor.getString(DB_POSITION);
            		}
            		else if(mPos == 2){	// ȸ�纰 sorting
            			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_POSITION);
            		}
            		else if(mPos == 3){	// ������ sorting
            			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_COMPANY);
            		}
            		else{
            			smallData = null;
            		}
            		MyItem mi = new MyItem(R.drawable.ic_menu_search, cursor.getString(mPos), cursor.getInt(DB_INDEX), smallData);	// �̸�, index
            		arItem.add(mi);
            	}
            }
            
            cursor.close();

            MyList.setAdapter(MyAdapter);
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	
	// search
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      doSearchQuery(query);
	    }
	}
	
    
    public boolean onSearchRequested(){
    	return super.onSearchRequested();
    }
    
	private void doSearchQuery(String query) {
    	Cursor cursor = dbdata.searchBook(query);
//    	startManagingCursor(cursor);
        
        if(cursor.getCount() > 0){
        	arItem.clear();
        	while(cursor.moveToNext()){
        		String smallData;
        		if(mPos == 1){	// �̸��� sorting
        			smallData = cursor.getString(DB_COMPANY) + ", " + cursor.getString(DB_POSITION);
        		}
        		else if(mPos == 2){	// ȸ�纰 sorting
        			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_POSITION);
        		}
        		else if(mPos == 3){	// ������ sorting
        			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_COMPANY);
        		}
        		else{
        			smallData = null;
        		}
        		MyItem mi = new MyItem(R.drawable.ic_menu_search, cursor.getString(mPos), cursor.getInt(DB_INDEX), smallData);	// ���� spinner���� ���õǾ� �ִ� ��Ĵ��
        		arItem.add(mi);
        	}
        }
        else{
        	Toast.makeText(this, "�˻� ����� ã�� �� �����ϴ�.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
	}
}

