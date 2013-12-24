package sf.browser.utils;

import java.util.Iterator;

import sf.browser.controllers.Controller;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Url management utils.
 * @author user
 *
 */
public class UrlUtils {

	/**
	 * Check if a string is an url.
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		return url.equals(Constants.URL_ABOUT_BLANK) ||
				url.equals(Constants.URL_ABOUT_START) ||
				url.contains(".");
	}

	/**
	 * Get the current search url.
	 * @param context
	 * @param searchTerms
	 * @return
	 */
	public static String getSearchurl(Context context, String searchTerms) {
		String currentSearchUrl = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.PREFERENCES_GENERAL_SEARCH_URL, Constants.URL_SEARCH_GOOGLE);
		return String.format(currentSearchUrl, searchTerms);
	}

	/**
	 * Check en url. Add http:// before if missing.
	 * @param url
	 * @return The modifed url if necessary.
	 */
	public static String checkUrl(String url) {
		if (url != null && url.length()>0) {
			if (!url.startsWith("http://") && 
					(!url.startsWith("https://")) &&
					(!url.startsWith("file://")) &&
					(!url.startsWith(Constants.URL_ABOUT_BLANK)) &&
					(!url.startsWith(Constants.URL_ABOUT_START))) {
				url = "http://"+url;
			}
		}
		return url;
	}

	/**
	 * Check if there is an item int the mobile view url list that match a given url.
	 * @param context
	 * @param url
	 * @return
	 */
	public static boolean checkInMobileViewUrlList(Context context, String url) {
		if (url != null) {
			boolean inList = false;
			Iterator<String> iter = Controller.getInstance().getMobileViewUrlList(context).iterator();
			while ( (iter.hasNext()) && (!inList)) {
				if (url.contains(iter.next())) {
					inList = true;
				}
			}
			return inList;
		} else {
			return false;
		}
	}
}
