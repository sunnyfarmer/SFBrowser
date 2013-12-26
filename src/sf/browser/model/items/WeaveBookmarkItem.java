package sf.browser.model.items;

public class WeaveBookmarkItem {

	private String mTitle;
	private String mUrl;
	private boolean mIsFolder;
	private String mWeaveId;
	
	public WeaveBookmarkItem(String title, String url, String weaveId, boolean isFolder) {
		this.mTitle = title;
		this.mUrl = url;
		this.mWeaveId = weaveId;
		this.mIsFolder = isFolder;
	}

	public String getmTitle() {
		return mTitle;
	}

	public String getmUrl() {
		return mUrl;
	}

	public boolean ismIsFolder() {
		return mIsFolder;
	}

	public String getmWeaveId() {
		return mWeaveId;
	}
}
