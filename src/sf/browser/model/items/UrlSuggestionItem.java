package sf.browser.model.items;

/**
 * Store a suggestion item.
 * @author user
 *
 */
public class UrlSuggestionItem {

	private static final float TITLE_COEFFICIENT = 2;
	private static final float URL_COEFFICIENT = 1;
	
	private static final float BOOKMARK_COEFFICIENT = 3;
	private static final float WEAVE_COEFFICIENT = 1;
	private static final float HISTORY_COEFFICIENT = 1;
	
	private String mPattern;
	private String mTitle;
	private String mUrl;
	private int mType;
	
	private float mNote;
	private boolean mNoteComputed = false;

	/**
	 * Constructor
	 * @param pattern
	 * @param title
	 * @param url
	 * @param type The item's type ( 1 -> history, 2 -> bookmark).
	 */
	public UrlSuggestionItem(String pattern, String title, String url, int type) {
		this.mPattern = pattern;
		this.mTitle = title;
		this.mUrl = url;
		this.mType = type;
	}

	public String getTitle() {
		return this.mTitle;
	}
	
	public String getUrl() {
		return mUrl;
	}
	
	public int getType() {
		return mType;
	}

	public float getNote() {
		if (!mNoteComputed) {
			computeNote();
			mNoteComputed = true;
		}
		return mNote;
	}

	/**
	 * Compute the note of the current item.
	 * The principle is to count the number of occurence of the pattern in the title and in the url, and to do a weighted sum.
	 * A match in title weight more than a match in url, and a match in bookmark weight more than a match in history.
	 */
	private void computeNote() {
		String pattern = mPattern.toLowerCase();
		
		// Count the number of match in a string, did not find a cleaner way.
		int titleMatchCount;
		String title = mTitle.toLowerCase();
		if (title.equals(pattern)) {
			titleMatchCount = 1;
		} else {
			titleMatchCount = title.split(pattern).length -1;
		}
		
		String url = mUrl.toLowerCase();
		int urlMatchCount = url.split("\\Q" + pattern + "\\E").length - 1;
		
		mNote = (titleMatchCount * TITLE_COEFFICIENT) + (urlMatchCount * URL_COEFFICIENT);
		
		switch(mType) {
		case 1:
			mNote *= HISTORY_COEFFICIENT;
			break;
		case 2:
			mNote *= BOOKMARK_COEFFICIENT;
			break;
		case 3:
			mNote *= WEAVE_COEFFICIENT;
			break;
		default:
			break;
		}
	}
}
