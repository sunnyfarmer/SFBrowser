package sf.browser.model.adapters;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import sf.browser.model.items.DownloadItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadListAdapter extends BaseAdapter {
	private Context mContext;
	private List<DownloadItem> mDownloads;

	private Map<DownloadItem, TextView> mTitleMap;
	private Map<DownloadItem, ProgressBar> mBarMap;
	private Map<DownloadItem, ImageButton> mButtonMap;
	
	public DownloadListAdapter(Context context, List<DownloadItem> downloads) {
		this.mContext = context;
		this.mDownloads = downloads;
		this.mTitleMap = new Hashtable<DownloadItem, TextView>();
		this.mBarMap = new Hashtable<DownloadItem, ProgressBar>();
		this.mButtonMap = new Hashtable<DownloadItem, ImageButton>();
	}
	
	/**
	 * Get a map of download item related to the UI text component representing the download title.
	 * @return
	 */
	public Map<DownloadItem, TextView> getmTitleMap() {
		return mTitleMap;
	}

	/**
	 * Get a map of download item related to the UI progress bar component.
	 * @return
	 */
	public Map<DownloadItem, ProgressBar> getmBarMap() {
		return mBarMap;
	}

	/**
	 * Get a map of download item related to the UI cancel button component.
	 * @return
	 */
	public Map<DownloadItem, ImageButton> getmButtonMap() {
		return mButtonMap;
	}

	@Override
	public int getCount() {
		return this.mDownloads.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mDownloads.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
