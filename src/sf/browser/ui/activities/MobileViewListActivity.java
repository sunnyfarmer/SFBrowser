package sf.browser.ui.activities;

import sf.browser.model.DbAdapter;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SimpleCursorAdapter;

public class MobileViewListActivity extends ListActivity {

	private static final int MENU_ADD = Menu.FIRST;
	private static final int MENU_CLEAR = Menu.FIRST + 1;
	
	private static final int MENU_DELETE = Menu.FIRST + 10;
	
	private Cursor mCursor;
	private DbAdapter mDbAdapter;
	private SimpleCursorAdapter mCursorAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	}

}
