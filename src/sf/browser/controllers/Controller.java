package sf.browser.controllers;

import java.util.ArrayList;
import java.util.List;

import sf.browser.model.items.DownloadItem;
import sf.browser.ui.components.CustomWebView;

import android.content.Context;
import android.content.SharedPreferences;

public final class Controller {

	private SharedPreferences mPreferences;

	private List<CustomWebView> mWebViewList;
	private List<DownloadItem> mDownloadList;
	private List<String> mAdBlockWhiteList = null;
	private List<String> mMobileViewUrlList = null;

	/**
	 * Holder for singleton implementation.
	 * @author user
	 *
	 */
	private static final class ControllerHolder {
		private static final Controller INSTANCE = new Controller();
		private ControllerHolder() {}
	}

	/**
	 * Get the unique instance of the Controller.
	 * @return
	 */
	public static Controller getInstance() {
		return ControllerHolder.INSTANCE;
	}

	/**
	 * Private Constructor.
	 */
	private Controller() {
		mDownloadList = new ArrayList<DownloadItem>();
	}

	/**
	 * Get the list of current WebViews.
	 * @return The list of current WebViews.
	 */
	public List<CustomWebView> getWebViewList() {
		return mWebViewList;
	}

	/**
	 * Set the list of current WebViews.
	 * @param list
	 */
	public void setWebViewList(List<CustomWebView> list) {
		this.mWebViewList = list;
	}

	/**
	 * Get a SharedPreferences instance.
	 * @return
	 */
	public SharedPreferences getPreferences() {
		return this.mPreferences;
	}

	/**
	 * Set the SharedPreferences instance.
	 * @param preferences
	 */
	public void setPreferences(SharedPreferences preferences) {
		this.mPreferences = preferences;
	}

	/**
	 * Get the current download list
	 * @return
	 */
	public List<DownloadItem> getDownloadList() {
		return this.mDownloadList;
	}

	/**
	 * Add an item to the download list.
	 * @param item
	 */
	public void addToDownload(DownloadItem item) {
		this.mDownloadList.add(item);
	}

	public synchronized void clearCompletedDownloads() {
		List<DownloadItem> newList = new ArrayList<DownloadItem>();

		for (DownloadItem item : this.mDownloadList) {
			if (!item.isFinished()) {
				newList.add(item);
			}
		}

		this.mDownloadList.clear();
		this.mDownloadList = newList;
	}

	public List<String> getAdBlockWhiteList(Context context) {
		if (mAdBlockWhiteList == null) {
			DbAdapter db = new DbAdapter(context);
			db.open();
			mAdBlockWhiteList = db.getWhiteList();
			db.close();
		}
		return mAdBlockWhiteList;
	}

	/**
	 * Reset the AdBlock white list, so that it will be reloaded.
	 */
	public void resetAdBlockWhiteList() {
		this.mAdBlockWhiteList = null;
	}

	/**
	 * Get the list of mobile view urls.
	 * @param context
	 * @return A list of String url.
	 */
	public List<String> getMobileViewUrlList(Context context) {
		if (this.mMobileViewUrlList == null) {
			DbAdapter db = new DbAdapter(context);
			db.opent();
			mMobileViewUrlList = db.getMobileViewUrlList();
			db.close();
		}
		return mMobileViewUrlList;
	}

	/**
	 * Reset the mobile view url list, so that it will be reloaded.
	 */
	public void resetMobileViewUrlList() {
		mMobileViewUrlList = null;
	}
}
