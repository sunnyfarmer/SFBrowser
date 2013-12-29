package sf.browser.ui.runnables;

import sf.browser.providers.BookmarksProviderWrapper;
import android.app.Activity;
import android.graphics.Bitmap;

/**
 * Runnable to update database favicon.
 * @author user
 *
 */
public class FaviconUpdaterRunnable implements Runnable {
	
	private Activity mActivity;
	private String mUrl;
	private String mOriginalUrl;
	private Bitmap mFavIcon;

	public FaviconUpdaterRunnable(Activity activity, String url, String originalUrl, Bitmap favicon) {
		this.mActivity = activity;
		this.mUrl = url;
		this.mOriginalUrl = originalUrl;
		this.mFavIcon = favicon;
	}
	
	@Override
	public void run() {
		BookmarksProviderWrapper.updateFavicon(mActivity, mUrl, mOriginalUrl, mFavIcon);
	}

}
