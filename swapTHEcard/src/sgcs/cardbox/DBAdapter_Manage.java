package sgcs.cardbox;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter_Manage {
	
	public static final String KEY_NAME = "name";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_COMPANY = "company";
	public static final String KEY_POSITION = "position";
	
	public static final int FIND_BY_NAME = 0;
	public static final int FIND_BY_PHONE = 1;
	
	private static final String TAG = "dbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb; // 데이터베이스를 저장
	
	private static final String DATABASE_CREATE =
		"create table carddata (_id integer primary key autoincrement,"+
		"name text not null, " + "company text not null, " + "position text not null, " + "phone text not null);";
	
	private static final String DATABASE_NAME = "carddatum.db";
	private static final String DATABASE_TABLE = "carddata";
	private static final int DATABASE_VERSION = 1;
	
	private final Context mCtx;
	
	private class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public void onCreate(SQLiteDatabase db){
			db.execSQL(DATABASE_CREATE);
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			Log.w(TAG, "Upgrading db from version" + oldVersion + " to" +
					newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS carddata");
			onCreate(db);
		}
		
	}
	
	public DBAdapter_Manage(Context ctx){
		this.mCtx = ctx;
	}
	
	public DBAdapter_Manage open() throws SQLException{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		mDbHelper.close();
	}
	
	// 레코드 생성
	public long createBook(String name, String company, String position, String phone){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_COMPANY, company);
		initialValues.put(KEY_POSITION, position);
		initialValues.put(KEY_PHONE, phone);
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	// 레코드 제거
	public boolean deleteBook(long rowID){
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	// 모든 레코드 반환
	public Cursor fetchAllBooks(String SortKey){
		return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_COMPANY, KEY_POSITION, KEY_PHONE}, null, null, null, null, SortKey+" ASC");
	}
	
	// 특정 레코드 반환
	public Cursor fetchBook(long rowID) throws SQLException{
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_COMPANY, KEY_POSITION, KEY_PHONE}, KEY_ROWID + "=" + rowID, null, null, null, null, null);
		if(mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
		
	// 레코드 수정
	public boolean updateBook(long rowID, String name, String company, String position, String phone){
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_COMPANY, company);
		args.put(KEY_POSITION, position);
		args.put(KEY_PHONE, phone);
		
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor searchBook(String content){
		String selection = KEY_NAME + " LIKE ? OR " + KEY_PHONE + " LIKE ?";
		String selectionArgs[] = {"%" + content + "%", "%" + content + "%"};
		
		Cursor mCursor = 
				mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_COMPANY, KEY_POSITION, KEY_PHONE}, selection, selectionArgs, null, null, KEY_NAME + " ASC");
		if(mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
}
