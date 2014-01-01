package sf.browser.ui.runnables;

import sf.browser.providers.BookmarksProviderWrapper;
import sf.browser.utils.Constants;
import android.content.Context;
import android.preference.PreferenceManager;

public class HistoryUpdater implements Runnable {
	
	private Context mContext;
	private String mTitle;
	private String mUrl;
	private String mOriginalUrl;

	public HistoryUpdater(Context context, String title, String url, String originalUrl) {
		mContext = context;
		mTitle = title;
		mUrl = url;
		mOriginalUrl = originalUrl;
		
		if (mUrl.startsWith(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT)) {
			mUrl = mUrl.substring(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT.length());
		}
	}
	
	@Override
	public void run() {
		BookmarksProviderWrapper.updateHistory(mContext.getContentResolver(), mTitle, mUrl, mOriginalUrl);
		BookmarksProviderWrapper.truncateHistory(mContext.getContentResolver(), 
				PreferenceManager.getDefaultSharedPreferences(mContext).getString(Constants.PREFERENCES_BROWSER_HISTORY_SIZE, "90"));
	}

}
