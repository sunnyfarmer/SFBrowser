package sf.browser.model.items;

import java.io.File;
import java.util.Random;

import sf.browser.R;
import sf.browser.events.EventConstants;
import sf.browser.events.EventController;
import sf.browser.ui.activities.DownloadsListActivity;
import sf.browser.ui.runnables.DownloadRunnable;
import sf.browser.utils.IOUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class DownloadItem {

	private Context mContext;

	private String mUrl;
	private String mFileName;

	private int mProgress;

	private String mErrorMessage;

	private DownloadRunnable mRunnable;

	private boolean mIsFinished;
	private boolean mIsAborted;

	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private int mNotificationId;

	public DownloadItem(Context context, String url) {
		this.mContext = context;

		this.mUrl = url;
		this.mFileName = this.mUrl.substring(mUrl.lastIndexOf("/")+1);

		checkFileName();

		this.mProgress = 0;

		this.mRunnable = null;

		this.mErrorMessage = null;
		
		this.mIsFinished = false;
		this.mIsAborted = false;
		
		Random r = new Random();
		mNotificationId = r.nextInt();
		mNotification = null;
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * Gets the download url.
	 * @return
	 */
	public String getUrl() {
		return this.mUrl;
	}
	
	/**
	 * Gets the filename on disk.
	 * @return
	 */
	public String getFileName() {
		return mFileName;
	}

	public void updateFileName(String fileName) {
		mFileName = fileName;
		checkFileName();
	}

	public String getFilePath() {
		return IOUtils.getDownloadFolder().getAbsolutePath() + File.separator + mFileName;
	}

	/**
	 * Gets the download progress
	 * @return
	 */
	public int getProgress() {
		return this.mProgress;
	}

	public void setErrorMessage(String errorMessage) {
		this.mErrorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return mErrorMessage;
	}

	/**
	 * Trigger a start download event.
	 */
	public void onStart() {
		createNotification();
		
		EventController.getInstance().fireDownloadEvent(EventConstants.EVT_DOWNLOAD_ON_START, this);
	}

	/**
	 * Set this item is download finished state. Trigger a finished download event.
	 */
	public void onFinished() {
		this.mProgress = 100;
		this.mRunnable = null;
		
		this.mIsFinished = true;
		
		updateNotificationOnEnd();
		
		EventController.getInstance().fireDownloadEvent(EventConstants.EVT_DOWNLOAD_ON_FINISHED, this);
	}
	
	/**
	 * Set the current progress. Trigger a progress download event.
	 * @param progress The current progress.
	 */
	public void onProgress(int progress) {
		this.mProgress = progress;
		
		EventController.getInstance().fireDownloadEvent(EventConstants.EVT_DOWNLOAD_ON_PROGRESS, this);
	}
	
	/**
	 * Start the current download.
	 */
	public void startDownload() {
		if (mRunnable != null) {
			this.mRunnable.abort();
		}
		mRunnable = new DownloadRunnable(this);
		new Thread(mRunnable).start();
	}
	
	/**
	 * Abort the current download.
	 */
	public void abortDownload() {
		if (this.mRunnable != null) {
			this.mRunnable.abort();
		}
		this.mIsAborted = true;
	}

	public boolean isFinished() {
		return this.mIsFinished;
	}
	
	public boolean isAborted() {
		return this.mIsAborted;
	}

	/**
	 * Remove query parameters from file name
	 */
	private void checkFileName() {
		int queryParamStart = mFileName.indexOf("?");
		if (queryParamStart > 0) {
			mFileName = mFileName.substring(0, queryParamStart);
		}
	}

	/**
	 * Create the download notification.
	 */
	private void createNotification() {
		mNotification = new Notification(R.drawable.download_anim, mContext.getString(R.string.DownloadNotification_DownloadStart), System.currentTimeMillis());

		Intent notificationIntent = new Intent(this.mContext.getApplicationContext(), DownloadsListActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, notificationIntent, 0);

		mNotification.setLatestEventInfo(mContext.getApplicationContext(), mContext.getString(R.string.DownloadNotification_DownloadInProgress), mFileName, contentIntent);

		mNotificationManager.notify(mNotificationId, mNotification);
	}

	private void updateNotificationOnEnd() {
		if (mNotification != null) {
			this.mNotificationManager.cancel(mNotificationId);
		}

		String message;
		if (mIsAborted) {
			message = mContext.getString(R.string.DownloadNotification_DownloadCanceled);
		} else {
			message = mContext.getString(R.string.DownloadNotification_DownloadComplete);
		}
		
		mNotification = new Notification(R.drawable.stat_sys_download, mContext.getString(R.string.DownloadNotification_DownloadComplete), System.currentTimeMillis());
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent notificationIntent = new Intent(mContext.getApplicationContext(), DownloadsListActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, notificationIntent, 0);
		
		mNotification.setLatestEventInfo(mContext.getApplicationContext(), mFileName, message, contentIntent);
		
		mNotificationManager.notify(mNotificationId, mNotification);
	}
}
