package sf.browser.ui.activities;

import sf.browser.events.IDownloadEventsListener;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;

public class DownloadsListActivity extends ListActivity implements IDownloadEventsListener{
	
	private static final int MENU_CLEAR_DOWNLOADS = Menu.FIRST;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	}

}
