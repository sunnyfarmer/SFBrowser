package sf.browser.ui.activities;

import sf.browser.R;
import sf.browser.R.drawable;
import sf.browser.R.layout;
import sf.browser.R.string;
import sf.browser.controllers.Controller;
import sf.browser.utils.Constants;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

/**
 * Combined bookmarks and history activity.
 * @author user
 *
 */
public class BookmarksHistoryActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_SHOW_FULL_SCREEN, false)) {
	    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    }
	    
	    if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_GENERAL_HIDE_TITLE_BARS, true)) {
	    	requestWindowFeature(Window.FEATURE_NO_TITLE);
	    }
	    
	    this.setContentView(R.layout.bookmarks_history_activity);
	    
	    this.setTitle(R.string.BookmarksListActivity_Title);
	    
	    Resources res = this.getResources();
	    TabHost tabHost = getTabHost();
	    TabHost.TabSpec spec;
	    Intent intent;
	    
	    //Bookmarks
	    intent = new Intent().setClass(this, BookmarksListActivity.class);
	    
	    spec = tabHost.newTabSpec("bookmarks").setIndicator(res.getString(R.string.Main_MenuShowBookmarks),
	    		res.getDrawable(R.drawable.ic_tab_bookmarks))
	    		.setContent(intent);
	    tabHost.addTab(spec);
	    
	    //History
	    intent = new Intent().setClass(this, HistoryListActivity.class);
	    
	    spec = tabHost.newTabSpec("history").setIndicator(res.getString(R.string.Main_MenuShowHistory),
	    		res.getDrawable(R.drawable.ic_tab_history))
	    		.setContent(intent);
	    tabHost.addTab(spec);
	    
	    if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.PREFERENCES_USE_WEAVE, false)) {
	    	//Weave bookmarks
	    	intent = new Intent().setClass(this, WeaveBookmarksListActivity.class);
	    	
	    	spec = tabHost.newTabSpec("weave").setIndicator(res.getString(R.string.WeaveBookmarksListActivity_Title),
	    			res.getDrawable(R.drawable.ic_tab_weave))
	    			.setContent(intent);
	    	tabHost.addTab(spec);
	    }
	    
	    tabHost.setCurrentTab(0);
	    
	    tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("bookmarks")) {
					setTitle(R.string.BookmarksListActivity_Title);
				} else if (tabId.equals("history")) {
					setTitle(R.string.HistoryListActivity_Title);
				} else if (tabId.equals("weave")) {
					setTitle(R.string.WeaveBookmarksListActivity_Title);
				} else {
					setTitle(R.string.app_name);
				}
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
