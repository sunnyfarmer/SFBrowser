package sf.browser.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sf.browser.ui.runnables.XmlHistoryBookmarksExporter;
import sf.browser.utils.ApplicationUtils;
import sf.browser.utils.DateUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Browser;
import android.util.Log;

public class DbAdapter {

	private static final String TAG = "DbAdapter";
	
	private static final String DATABASE_NAME = "SFBROWSER";
	private static final int DATABASE_VERSION = 6;

	/**
	 * AdBlock white list table.
	 */
	public static final String ADBLOCK_ROWID = "_id";
	public static final String ADBLOCK_URL = "url";

	private static final String ADBLOCK_WHITELIST_DATABASE_TABLE = "ADBLOCK_WHITELIST";

	private static final String ADBLOCK_WHITELIST_DATABASE_CREATE = "CREATE TABLE " + ADBLOCK_WHITELIST_DATABASE_TABLE + " (" +
			ADBLOCK_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			ADBLOCK_URL + " TEXT NOT NULL);";

	/**
	 * Mobile view url table.
	 */
	public static final String MOBILE_VIEW_URL_ROWID = "_id";
	public static final String MOBILE_VIEW_URL_URL = "url";
	
	private static final String MOBILE_VIEW_DATABASE_TABLE = "MOBILE_VIEW_URL";
	
	private static final String MOBILE_VIEW_DATABASE_CREATE = "CREATE TABLE " + MOBILE_VIEW_DATABASE_TABLE + " (" +
			MOBILE_VIEW_URL_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			MOBILE_VIEW_URL_URL + " TEXT NOT NULL);";

	protected boolean mAdBlockListNeedPopulate = false;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private final Context mContext;

	public DbAdapter(Context ctx) {
		this.mContext = ctx;
	}

	/**
	 * Open the database helper.
	 * @return The current database adapter.
	 */
	public DbAdapter open() {
		mDbHelper = new DatabaseHelper(this.mContext, this);
		mDb = mDbHelper.getWritableDatabase();
		
		if (this.mAdBlockListNeedPopulate) {
			populateDefaultWhiteList();
			this.mAdBlockListNeedPopulate = false;
		}
		
		return this;
	}

	/**
	 * Close the database helper.
	 */
	public void close() {
		this.mDbHelper.close();
	}

	public SQLiteDatabase getDatabase() {
		return this.mDb;
	}

	/*********************************************************************************************************************************************************
	 * Adblock white list.
	 */

	/**
	 * Get the white list url given its id.
	 * @param rowId
	 * @return The white list url.
	 */
	public String getWhiteListItemById(long rowId) {
		Cursor cursor = mDb.query(true, ADBLOCK_WHITELIST_DATABASE_TABLE, new String[] {ADBLOCK_ROWID,  ADBLOCK_URL},
				ADBLOCK_ROWID + "=" + rowId, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			String result;
			result = cursor.getString(cursor.getColumnIndex(ADBLOCK_URL));

			cursor.close();
			
			return result;
		} else {
			cursor.close();
			return null;
		}
	}

	/**
	 * Get the list of url presents in white list.
	 * @return The list of url presents in white list.
	 */
	public List<String> getWhiteList() {
		List<String> result = new ArrayList<String>();
		
		Cursor cursor = getWhiteListCursor();
		
		if (cursor.moveToFirst()) {
			do {
				result.add(cursor.getString(cursor.getColumnIndex(ADBLOCK_URL)));
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return result;
	}

	/**
	 * Get a cursor to the list of url presents in white list.
	 * @return A cursor to the list of url presents in white list.
	 */
	public Cursor getWhiteListCursor() {
		return this.mDb.query(ADBLOCK_WHITELIST_DATABASE_TABLE, new String[] {ADBLOCK_ROWID, ADBLOCK_URL}, null, null, null, null, null);
	}

	/**
	 * Insert an item in the white list.
	 * @param url The url to insert.
	 */
	public void insertInWhiteList(String url) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ADBLOCK_URL, url);

		mDb.insert(ADBLOCK_WHITELIST_DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete an item in white list given its id.
	 * @param id
	 */
	public void deleteFromWhiteList(long id) {
		this.mDb.execSQL("DELETE FROM " + ADBLOCK_WHITELIST_DATABASE_TABLE + " WHERE " + ADBLOCK_ROWID + " = " + id + ";");
	}

	/**
	 * Delete all records from the white list.
	 */
	public void clearWhiteList() {
		mDb.execSQL("DELETE FROM " + ADBLOCK_WHITELIST_DATABASE_TABLE + ";");
	}

    
    /*******************************************************************************************************************************************************    
     * Mobile view list.
     */

	/**
	 * Get an url from the mobile view list from its id.
	 * @param rowId
	 * @return The  url.
	 */
	public String getMobileViewUrlItemById(long rowId) {
		Cursor cursor = this.mDb.query(true, MOBILE_VIEW_DATABASE_TABLE, new String[] {MOBILE_VIEW_URL_ROWID, MOBILE_VIEW_URL_URL},
				MOBILE_VIEW_URL_ROWID + "=" + rowId, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			String result;
			result = cursor.getString(cursor.getColumnIndex(MOBILE_VIEW_URL_URL));
			
			cursor.close();
			
			return result;
		} else {
			cursor.close();
			return null;
		}
	}

	/**
	 * Get a list of all urls in mobile view list.
	 * @return
	 */
	public List<String> getMobileViewUrlList() {
		List<String> result = new ArrayList<String>();
		
		Cursor cursor = getMobileViewUrlCursor();
		
		if (cursor.moveToFirst()) {
			do {
				result.add(cursor.getString(cursor.getColumnIndex(MOBILE_VIEW_URL_URL)));
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return result;
	}

	/**
	 * Get a cursor to the mobile view url list.
	 * @return A Cursor to the mobile view list.
	 */
	public Cursor getMobileViewUrlCursor() {
		return mDb.query(MOBILE_VIEW_DATABASE_TABLE, new String[] {MOBILE_VIEW_URL_ROWID, MOBILE_VIEW_URL_URL}, null, null, null, null, null);
	}

	/**
	 * Insert an url in the mobile view url list.
	 * @param url
	 */
	public void insertInMobileViewUrlList(String url) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(MOBILE_VIEW_URL_URL, url);
		
		mDb.insert(MOBILE_VIEW_DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete an url from the mobile view url list.
	 * @param id The id of url to delete.
	 */
	public void deleteFromMobileViewUrlList(long id) {
		mDb.execSQL("DELETE FROM " + MOBILE_VIEW_DATABASE_TABLE + " WHERE " + MOBILE_VIEW_URL_ROWID + " = " + id + ";");
	}

	/**
	 * Clear the mobile view url list.
	 */
	public void clearMobileViewUrlList() {
		mDb.execSQL("DELETE FROM " + MOBILE_VIEW_DATABASE_TABLE + ";");
	}

	/**
	 * Populate the white with default values.
	 */
	private void populateDefaultWhiteList() {
		insertInWhiteList("google.com/reader");
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		private DbAdapter mParent;

		public DatabaseHelper(Context context, DbAdapter parent) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.mParent = parent;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(ADBLOCK_WHITELIST_DATABASE_CREATE);
			db.execSQL(MOBILE_VIEW_DATABASE_CREATE);
			mParent.mAdBlockListNeedPopulate = true;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Upgrading database.");

			switch(oldVersion) {
			case 1:
			case 2:
			case 3:
				db.execSQL(ADBLOCK_WHITELIST_DATABASE_CREATE);
				mParent.mAdBlockListNeedPopulate = true;
			case 4:
				db.execSQL(MOBILE_VIEW_DATABASE_CREATE);
			case 5:
				exportOldBookmarks(db);
				db.execSQL("DROP TABLE IF EXISTS BOOKMARKS;");
				db.execSQL("DROP TABLE IF EXISTS HISTORY;");
			default:
				break;
			}
		}

		/**
		 * Export bookmarks from the old database.
		 * Transform the query result into a MatrixCursor following
		 * the stock bookmarks databases, so it can be exported with
		 * the XmlHistoryBookmarksExporter without any change on it.
		 * @param db The database.
		 */
		private void exportOldBookmarks(SQLiteDatabase db) {
			Log.i(TAG, "Start export of old bookmarks.");
			try {
				if (ApplicationUtils.checkCardState(mParent.mContext, false)) {
					Log.i(TAG, "Export of old bookmarks: SDCard checked.");

					MatrixCursor cursor = null;
					
					Cursor c = db.query(
							"BOOKMARKS",
							new String[] {
								"_id",
								"title",
								"url",
								"creation_date",
								"count"								
							}, 
							null, null, null, null, null);
					
					if (c != null) {
						if (c.moveToFirst()) {
							cursor = new MatrixCursor(new String[] {
								Browser.BookmarkColumns.TITLE,
								Browser.BookmarkColumns.URL,
								Browser.BookmarkColumns.VISITS,
								Browser.BookmarkColumns.DATE,
								Browser.BookmarkColumns.CREATED,
								Browser.BookmarkColumns.BOOKMARK
							});

							int titleColumn = c.getColumnIndex("title");
							int urlColumn = c.getColumnIndex("url");
							int creationDateColumn = c.getColumnIndex("creation_date");
							int countColumn = c.getColumnIndex("count");

							while (!c.isAfterLast()) {
								Date date = DateUtils.convertFromDatabase(mParent.mContext, c.getString(creationDateColumn));

								Object[] data = new Object[6];
								data[0] = c.getString(titleColumn);
								data[1] = c.getString(urlColumn);
								data[2] = c.getInt(countColumn);
								data[3] = date.getTime();
								data[4] = date.getTime();
								data[5] = 1;

								cursor.addRow(data);
								
								c.moveToNext();
							}
							
							c.close();
						}
					}

					if (cursor != null) {
						Log.i(TAG, "Export of old bookmarks: Writing file.");
						new Thread(new XmlHistoryBookmarksExporter(null, "auto-export.xml", cursor, null)).start();
					}
				}
			} catch (Exception e) {
				Log.i(TAG, "Export of old bookmarks failed: " + e.getMessage());
			}

			Log.i(TAG, "End of export of old bookmarks.");
		}
	}
}
