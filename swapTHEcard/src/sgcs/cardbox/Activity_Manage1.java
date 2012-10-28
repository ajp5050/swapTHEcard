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
	
	// spinner에서 선택된 index와 종류
	protected int mPos;
    protected String mSelection;
    
    public ListView MyList;
	public MyListAdapter MyAdapter;
    
	// Spinner
    protected Spinner SortSpinner;
    protected ArrayAdapter<CharSequence> SortSelectList; //문자열 어댑터 선언
    
    //search
    public EditText SearchText;
    
    // DB상의 저장 순서
    public static final int DB_INDEX = 0;
    public static final int DB_NAME = 1;
    public static final int DB_COMPANY = 2;
    public static final int DB_POSITION = 3;
    public static final int DB_PHONE = 4;

    
    // 자료를 저장하는 형태
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

        //xml에 선언한 스피너를 id값으로 불러옴
        SortSpinner = (Spinner) findViewById (R.id.manage_SortKind);
        
        //스피너를 선택 시 보여질 팝업의 제목을 지정
        SortSpinner.setPrompt("정렬 방식");
        
        //어댑터 객체를 생성하고 보여질 아이템 리소스와 문자열 지정
        SortSelectList = ArrayAdapter.createFromResource(this, R.array.manage_SortingBy, android.R.layout.simple_spinner_item);
        
        //스피너에 adapter 연결
        SortSpinner.setAdapter(SortSelectList);
        
        SortSelectList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(this, SortSelectList);
        SortSpinner.setOnItemSelectedListener(spinnerListener);
  		
        /* DB delete (only using debug)
        this.deleteDatabase("carddatum.db");
        */
        
        // database를 불러온다
        dbdata = new DBAdapter_Manage(this);
        dbdata.open();

        
        /* DB add data (only using debug)*/
        /*
        dbdata.createBook("한지민", "삼성전자", "대리","01012344323");
        dbdata.createBook("김지수", "LG전자", "부장", "024568877");
        dbdata.createBook("강수민", "서강대학교", "교수", "01083746372");
        dbdata.createBook("Christina", "삼성전자", "대리", "01011542211");
        dbdata.createBook("Angela", "LG전자", "신입", "0310000012");
        dbdata.createBook("Brian", "두산정보통신", "사장", "01045624154");
        */
        
        Cursor cursor;
        arItem = new ArrayList<MyItem>();
        MyItem mi;
        
        arItem.clear();
        
        // 사람들 정보를 불러와 이름별로 sorting해서 listing
        cursor = dbdata.fetchAllBooks(mSelection);
//        startManagingCursor(cursor);
        
        if(cursor.getCount() > 0){
        	while(cursor.moveToNext()){
        		String smallData;
        		if(mPos == 1){	// 이름별 sorting
        			smallData = cursor.getString(DB_COMPANY) + ", " + cursor.getString(DB_POSITION);
        		}
        		else if(mPos == 2){	// 회사별 sorting
        			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_POSITION);
        		}
        		else if(mPos == 3){	// 직위별 sorting
        			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_COMPANY);
        		}
        		else{
        			smallData = null;
        		}
        		mi = new MyItem(R.drawable.ic_menu_search, cursor.getString(mPos), cursor.getInt(DB_INDEX), smallData);	// 이름, index
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
	
	// 이름과 명함 preview를 보여주기 위한 form, sorting 시에 사람에 대한 정보가 나타나는 기본 형식이다.
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
	
	
	// spinner에서 선택한 값을 기준으로 정렬
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
            		if(mPos == 1){	// 이름별 sorting
            			smallData = cursor.getString(DB_COMPANY) + ", " + cursor.getString(DB_POSITION);
            		}
            		else if(mPos == 2){	// 회사별 sorting
            			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_POSITION);
            		}
            		else if(mPos == 3){	// 직위별 sorting
            			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_COMPANY);
            		}
            		else{
            			smallData = null;
            		}
            		MyItem mi = new MyItem(R.drawable.ic_menu_search, cursor.getString(mPos), cursor.getInt(DB_INDEX), smallData);	// 이름, index
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
        		if(mPos == 1){	// 이름별 sorting
        			smallData = cursor.getString(DB_COMPANY) + ", " + cursor.getString(DB_POSITION);
        		}
        		else if(mPos == 2){	// 회사별 sorting
        			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_POSITION);
        		}
        		else if(mPos == 3){	// 직위별 sorting
        			smallData = cursor.getString(DB_NAME) + ", " + cursor.getString(DB_COMPANY);
        		}
        		else{
        			smallData = null;
        		}
        		MyItem mi = new MyItem(R.drawable.ic_menu_search, cursor.getString(mPos), cursor.getInt(DB_INDEX), smallData);	// 현재 spinner에서 선택되어 있는 방식대로
        		arItem.add(mi);
        	}
        }
        else{
        	Toast.makeText(this, "검색 결과를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
	}
}

