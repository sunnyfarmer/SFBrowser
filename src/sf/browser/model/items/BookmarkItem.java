package sf.browser.model.items;

public class BookmarkItem {

	private String mTitle;
	private String mUrl;

	public BookmarkItem(String title, String url) {
		mTitle = title;
		mUrl = url;
	}

	public String getTitle() {
		return this.mTitle;
	}

	public String getUrl() {
		return this.mUrl;
	}
}
