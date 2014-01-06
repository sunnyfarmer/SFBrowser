package sf.browser.model.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HistoryItem {

	private long mId;
	private String mTitle;
	private String mUrl;
	private boolean mIsBookmark;
	private Bitmap mFavicon;

	public HistoryItem(long id, String title, String url, boolean isBookmark, byte[] faviconData) {
		this.mId = id;
		this.mTitle = title;
		this.mUrl = url;
		this.mIsBookmark = isBookmark;
		if (faviconData != null) {
			this.mFavicon = BitmapFactory.decodeByteArray(faviconData, 0, faviconData.length);
		} else {
			this.mFavicon = null;
		}
	}

	public long getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getUrl() {
		return mUrl;
	}

	public boolean isBookmark() {
		return mIsBookmark;
	}

	public Bitmap getFavicon() {
		return mFavicon;
	}

	
}
