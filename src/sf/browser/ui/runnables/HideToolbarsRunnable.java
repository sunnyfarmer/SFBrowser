package sf.browser.ui.runnables;

import android.os.Handler;
import android.util.Log;
import sf.browser.ui.activities.IToolbarsContainer;

/**
 * A runnable to hide tool bars after the given delay
 * @author user
 *
 */
public class HideToolbarsRunnable implements Runnable {
	private static final String TAG = "HideToolbarsRunnable";

	private IToolbarsContainer mParent;
	private boolean mDisabled;
	private int mDelay;

	/**
	 * Constructor
	 * @param parent The parent tool bar container
	 * @param delay The delay before hiding, in milliseconds
	 */
	public HideToolbarsRunnable(IToolbarsContainer parent, int delay) {
		this.mParent = parent;
		this.mDisabled = false;
		this.mDelay = delay;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ((mParent!=null) && (!mDisabled)) {
				mParent.hideToolbars();
			}
		}
	};

	/**
	 * Disable this runnable
	 */
	public void setDisabled() {
		this.mDisabled = true;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(this.mDelay);
			mHandler.sendEmptyMessage(0);
		} catch (InterruptedException e) {
			Log.w(TAG, "Expection in thread: " + e.getMessage());
			mHandler.sendEmptyMessage(0);
		}
	}

}
