package sf.browser.ui.activities;

import java.util.List;

import org.greendroid.QuickActionGrid;

import sf.browser.R;
import sf.browser.events.IDownloadEventsListener;
import sf.browser.ui.components.CustomWebView;
import sf.browser.ui.runnables.HideToolbarsRunnable;
import sf.browser.utils.Constants;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements IToolbarsContainer, OnTouchListener, IDownloadEventsListener{

	public static MainActivity INSTANCE = null;

	private static final int FLIP_PIXEL_THRESHOLD = 200;
	private static final int FLIP_TIME_THRESHOLD = 400;

	private static final int MENU_ADD_BOOKMARK = Menu.FIRST;
	private static final int MENU_SHOW_BOOKMARKS = Menu.FIRST + 1;
	private static final int MENU_DOWNLOADS = Menu.FIRST + 2;
	private static final int MENU_PREFERENCES = Menu.FIRST + 3;
	private static final int MENU_EXIT = Menu.FIRST + 4;

	private static final int CONTEXT_MENU_OPEN = Menu.FIRST + 10;
	private static final int CONTEXT_MENU_OPEN_IN_NEW_TAB = Menu.FIRST + 11;
	private static final int CONTEXT_MENU_DOWNLOAD = Menu.FIRST + 12;
	private static final int CONTEXT_MENU_COPY = Menu.FIRST + 13;
	private static final int CONTEXT_MENU_SEND_MAIL = Menu.FIRST + 14;
	private static final int CONTEXT_MENU_SHARE = Menu.FIRST + 15;

	private static final int OPEN_BOOKMARKS_HISTORY_ACTIVITY = 0;
	private static final int OPEN_DOWNLOAD_ACTIVITY = 1;
	private static final int OPEN_FILE_CHOOSER_ACTIVITY = 2;

	protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = 
			new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);

	protected LayoutInflater mInflater = null;

	private LinearLayout mTopBar;
	private LinearLayout mBottomBar;

	private LinearLayout mFindBar;

	private ImageButton mFindPreviousButton;
	private ImageButton mFindNextButton;
	private ImageButton mFindCloseButton;

	private EditText mFindText;

	private ImageView mPreviousTabView;
	private ImageView mNextTabView;

	private ImageButton mTooldButton;
	private AutoCompleteTextView mUrlEditText;
	private ImageButton mGoButton;
	private ProgressBar mProgressBar;

	private ImageView mBubbleRightView;
	private ImageView mBubbleLeftView;

	private CustomWebView mCurrentWebView;
	private List<CustomWebView> mWebViews;

	private ImageButton mPreviousButton;
	private ImageButton mNextButton;

	private ImageButton mNewTabButton;
	private ImageButton mRemoveTabButton;

	private ImageButton mQuickButton;

	private Drawable mCircularPregress;

	private boolean mUrlBarVisible;
	private boolean mToolsActionGridVisible = false;
	private boolean mFindDialogVisible = false;

	private TextWatcher mUrlTextWatch;

	private HideToolbarsRunnable mHideToolbarsRunnable;

	private ViewFlipper mViewFlipper;

	private GestureDetector mGestureDetector;

	private SwitchTabsMethod mSwitchTabsMethod = SwitchTabsMethod.BOTH;

	private QuickActionGrid mToolsActionGrid;

	private ValueCallback<Uri> mUploadMessage;

	private OnSharedPreferenceChangeListener mPreferenceChangeListener;

	private View mCustomView;
	private Bitmap mDefaultVideoPoster = null;
	private View mVideoProgressView = null;

	private FrameLayout mFullscreenContainer;

	private WebChromeClient.CustomViewCallback mCustomViewCallback;

	private enum SwitchTabsMethod {
		BUTTONS,
		FLING,
		BOTH
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		INSTANCE = this;

		Constants.initializeConstantsFromResources(this);

		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDownloadEvent(String event, Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void hideToolbars() {
		// TODO Auto-generated method stub
		
	}

}
