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

	public String getTitle() {
		return mTitle;
	}

	public String getUrl() {
		return mUrl;
	}

	public boolean isFolder() {
		return mIsFolder;
	}

	public String getWeaveId() {
		return mWeaveId;
	}
}
