package sf.browser.ui.activities;

import java.util.List;

import sf.browser.model.DbAdapter;
import sf.browser.model.items.WeaveBookmarkItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WeaveBookmarksListActivity extends Activity {
	
	private static final int MENU_SYNC = Menu.FIRST;
	private static final int MENU_CLEAR = Menu.FIRST + 1;
	
	private static final int MENU_OPEN_IN_TAB = Menu.FIRST + 10;
	private static final int MENU_COPY_URL = Menu.FIRST + 11;
	private static final int MENU_SHARE = Menu.FIRST + 12;
	
	private static final String ROOT_FOLDER = "places";
	
	private LinearLayout mNavigationView;
	private TextView mNavigationText;
	private ImageButton mNavigationBack;
	private ListView mListView;
	
	private Button mSetupButton;
	private Button mSyncButton;
	
	private View mEmptyView;
	private View mEmptyFolderView;

	private List<WeaveBookmarkItem> mNavigationList;

	private ProgressDialog mProgressDialog;
	
	private DbAdapter mDbAdapter;
	private Cursor mCursor = null;
	
	private WeaveSyncTask mSyncTask;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	}

}
