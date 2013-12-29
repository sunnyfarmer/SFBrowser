package sf.browser.ui.activities;

import java.util.ArrayList;
import java.util.List;

import org.greendroid.QuickAction;
import org.greendroid.QuickActionGrid;
import org.greendroid.QuickActionWidget;
import org.greendroid.QuickActionWidget.OnQuickActionClickListener;

import sf.browser.R;
import sf.browser.controllers.Controller;
import sf.browser.events.EventController;
import sf.browser.events.IDownloadEventsListener;
import sf.browser.model.adapters.UrlSuggestionCursorAdapter;
import sf.browser.providers.BookmarksProviderWrapper;
import sf.browser.providers.BookmarksProviderWrapper.BookmarksSource;
import sf.browser.ui.components.CustomWebView;
import sf.browser.ui.components.CustomWebViewClient;
import sf.browser.ui.runnables.FaviconUpdaterRunnable;
import sf.browser.ui.runnables.HideToolbarsRunnable;
import sf.browser.utils.ApplicationUtils;
import sf.browser.utils.Constants;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebIconDatabase;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
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

	private ImageButton mToolsButton;
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
		
		Controller.getInstance().setPreferences(PreferenceManager.getDefaultSharedPreferences(this));
		
		if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_SHOW_FULL_SCREEN, false)) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_GENERAL_HIDE_TITLE_BARS, true)) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		
		this.setProgressBarVisibility(true);

		setContentView(R.layout.activity_main);

		mCircularPregress = getResources().getDrawable(R.drawable.spinner);
		
		EventController.getInstance().addDownloadListener(this);
		
		mHideToolbarsRunnable = null;
		
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		buildComponents();
		
		mViewFlipper.removeAllViews();
		
		updateSwitchTabsMethod();
		updateBookmarksDatabaseSource();
		
		registerPreferenceChangeListener();
		
		Intent i = getIntent();
		if (i.getData() != null) {
			//App first launch from another app.
			addTab(false);
			navigateToUrl(i.getDataString());
		} else {
			//Normal start.
			int currentVersionCode = ApplicationUtils.getApplicationVersionCode(this);
			int savedVersionCode = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constants.PREFERENCES_LAST_VERSION_CODE, -1);
			
			//if currentVersionCode and savedVersionCode are different, the application has been updated.
			if (currentVersionCode != savedVersionCode) {
				//Save the current version code.
				Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
				editor.putInt(Constants.PREFERENCES_LAST_VERSION_CODE, currentVersionCode);
				editor.commit();
				
				//Display changelog dialog.
				Intent changelogIntent = new Intent(this, ChangelogActivity.class);
				startActivity(changelogIntent);
			}
			
			boolean lastPageRestored = false;
			if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.PREFERENCES_BROWSER_RESTORE_LAST_PAGE, false)) {
				if (savedInstanceState != null) {
					String savedUrl = savedInstanceState.getString(Constants.EXTRA_SAVED_URL);
					if (savedUrl != null) {
						addTab(false);
						navigateToUrl(savedUrl);
						lastPageRestored = true;
					}
				}
			}
			
			if (!lastPageRestored) {
				addTab(true);
			}
		}

		initializeWebIconDatabase();
		
		startToolbarsHideRunnable();
	}

	private void initializeWebIconDatabase() {
		final WebIconDatabase db = WebIconDatabase.getInstance();
		db.open(getDir("icons", 0).getPath());
	}

	@Override
	protected void onDestroy() {
		WebIconDatabase.getInstance().close();
		
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.PREFERENCES_PRIVACY_CLEAR_CACHE_ON_EXIT, false)) {
			mCurrentWebView.clearCache(true);
		}
		
		EventController.getInstance().removeDownloadListener(this);
		
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(mPreferenceChangeListener);
		
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(Constants.EXTRA_SAVED_URL, mCurrentWebView.getUrl());
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * Handle a url request from external apps.
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getData() != null) {
			addTab(false);
			navigateToUrl(intent.getDataString());
		}
		
		setIntent(intent);
		
		super.onNewIntent(intent);
	}
	
	/**
	 * Restart the application.
	 */
	public void restartApplication() {
		PendingIntent intent = PendingIntent.getActivity(this.getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis()+2000, intent);
		System.exit(2);
	}

	/**
	 * Create main UI.
	 */
	private void buildComponents() {
		mToolsActionGrid = new QuickActionGrid(this);
		mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_home, R.string.QuickAction_Home));
		mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_share, R.string.QuickAction_Share));
		mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_find, R.string.QuickAction_Find));
		mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_select, R.string.QuickAction_SelectText));
		mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_mobile_view, R.string.QuickAction_MobileView));
		
		mToolsActionGrid.setOnQuickActionClickListener(new OnQuickActionClickListener() {
			
			@Override
			public void onQuickActionClicked(QuickActionWidget widget, int position) {
				switch(position) {
				case 0:
					navigateToHome();
					break;
				case 1:
					ApplicationUtils.sharePage(MainActivity.this, mCurrentWebView.getTitle(), mCurrentWebView.getUrl());
					break;
				case 2:
					// Somewhat dirty hack: when the find dialog was shown from a QuickAction,
					// the soft keyboard did not show... Hack is to wait a little before showing
					// the file dialog through a thread.
					startShowFindDialogRunnable();
					break;
				case 3:
					swithToSelectAndCopyTextMode();
				case 4:
					String currentUrl = mUrlEditText.getText().toString();
					
					//Do not reload mobile view if already on it.
					if (!currentUrl.startsWith(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT)) {
						String url = String.format(Constants.URL_GOOGLE_MOBILE_VIEW, mUrlEditText.getText().toString());
						navigateToUrl(url);
					}
					break;
				}
			}
		});
		
		mToolsActionGrid.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mToolsActionGridVisible = false;
				startToolbarsHideRunnable();
			}
		});
		
		mGestureDetector = new GestureDetector(this, new GestureListener());

		mUrlBarVisible = true;
		
		mWebViews = new ArrayList<CustomWebView>();
		Controller.getInstance().setWebViewList(mWebViews);
		
		mBubbleRightView = (ImageView) findViewById(R.id.BubbleRightView);
		mBubbleRightView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setToolbarsVisibility(true);
			}
		});
		mBubbleLeftView.setVisibility(View.GONE);

		mViewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);
		
		mTopBar = (LinearLayout) this.findViewById(R.id.BarLayout);
		mTopBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Dummy event to steel it from the WebView, in case of clicking between the buttons.
			}
		});
	
		mBottomBar = (LinearLayout) findViewById(R.id.BottomBarLayout);
		mBottomBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Dummy event to steel it from WebView, in case of clicking between the buttons.
			}
		});
		
		mFindBar = (LinearLayout) findViewById(R.id.findControls);
		mFindBar.setVisibility(View.GONE);
		
		mPreviousTabView = (ImageView) findViewById(R.id.PreviousTabView);
		mPreviousTabView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPreviousTab(true);
			}
		});
		mPreviousTabView.setVisibility(View.GONE);
		
		mNextTabView = (ImageView) findViewById(R.id.NextTabView);
		mNextTabView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showNextTab(true);
			}
		});
		mNextTabView.setVisibility(View.GONE);
		
		String[] from = new String[] {UrlSuggestionCursorAdapter.URL_SUGGESTION_TITLE, UrlSuggestionCursorAdapter.URL_SUGGESTION_URL};
		int[] to = new int[] {R.id.AutocompleteTitle, R.id.AutocompleteUrl};
		
		UrlSuggestionCursorAdapter adapter = new UrlSuggestionCursorAdapter(this, R.layout.url_autocomplete_line, null, from, to);
		
		adapter.setCursorToStringConverter(new CursorToStringConverter() {
			@Override
			public CharSequence convertToString(Cursor cursor) {
				String aColumnString = cursor.getString(cursor.getColumnIndex(UrlSuggestionCursorAdapter.URL_SUGGESTION_URL));
				return aColumnString;
			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			@Override
			public Cursor runQuery(CharSequence constraint) {
				if (constraint!=null && constraint.length()>0) {
					return BookmarksProviderWrapper.getUrlSuggestions(getContentResolver(), constraint.toString(),
							PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(Constants.PREFERENCES_USE_WEAVE, false));.
				} else {
					return BookmarksProviderWrapper.getUrlSuggestions(getContentResolver(), null,
							PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(Constants.PREFERENCES_USE_WEAVE, false));
				}
			}
		});
		
		mUrlEditText = (AutoCompleteTextView) findViewById(R.id.UrlText);
		mUrlEditText.setThreshold(1);
		mUrlEditText.setAdapter(adapter);
		
		mUrlEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					navigateToUrl();
					return true;
				}
				return false;
			}
		});
		
		mUrlTextWatch = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				updateGoButton();
			}
		};
		
		mUrlEditText.addTextChangedListener(mUrlTextWatch);
		
		mUrlEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mUrlEditText.setSelection(0, mUrlEditText.getText().length());
				}
			}
		});
		
		mUrlEditText.setCompoundDrawablePadding(5);
		
		mGoButton = (ImageButton) findViewById(R.id.GoBtn);
		mGoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentWebView.isLoading()) {
					mCurrentWebView.stopLoading();
				} else if (!mCurrentWebView.isSameUrl(mUrlEditText.getText().toString())) {
					navigateToUrl();
				} else {
					mCurrentWebView.reload();
				}
			}
		});

		mToolsButton = (ImageButton) findViewById(R.id.ToolsBtn);
		mToolsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mToolsActionGridVisible = true;
				mToolsActionGrid.show(v);
			}
		});

		mProgressBar = (ProgressBar) findViewById(R.id.WebViewProgress);
		mProgressBar.setMax(100);
		
		mPreviousButton = (ImageButton) findViewById(R.id.PreviousBtn);
		mNextButton = (ImageButton) findViewById(R.id.NextBtn);
		
		mPreviousButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				navigatePrevious();
			}
		});
		
		mNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				navigateNext();
			}
		});
		
		mNewTabButton = (ImageButton) findViewById(R.id.NewTabBtn);
		mNewTabButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addTab(true);
			}
		});
		
		mRemoveTabButton = (ImageButton) findViewById(R.id.RemoveTabBtn);
		mRemoveTabButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mViewFlipper.getChildCount() == 1 && !mCurrentWebView.getUrl().equals(Constants.URL_ABOUT_START)) {
					navigateToHome();
					updateUI();
					updatePreviousNextTabViewsVisibility();
				} else {
					removeCurrentTab();
				}
			}
		});
		
		mQuickButton = (ImageButton) findViewById(R.id.QuickBtn);
		mQuickButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onQuickButton();
			}
		});
		
		mFindPreviousButton = (ImageButton) findViewById(R.id.find_previous);
		mFindPreviousButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentWebView.findNext(false);
				hideKeyboardFromFindDialog();
			}
		});
		
		mFindNextButton = (ImageButton) findViewById(R.id.find_next);
		mFindNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentWebView.findNext(true);
				hideKeyboardFromFindDialog();
			}
		});
		
		mFindCloseButton = (ImageButton) findViewById(R.id.find_close);
		mFindCloseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeFindDialog();
			}
		});
		
		mFindText = (EditText) findViewById(R.id.find_value);
		mFindText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				doFind();
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void registerPreferenceChangeListener() {
		mPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if (key.equals(Constants.PREFERENCES_BOOKMARKS_DATABASE)) {
					updateBookmarksDatabaseSource();
				}
			}
		};
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);
	}
	
	/**
	 * Apply preferences to the current UI objects.
	 */
	public void applyPreferences() {
		//To update to Bubble positon.
		setToolbarsVisibility(false);
		
		updateSwitchTabsMethod();
		
		for (CustomWebView view : mWebViews) {
			view.initializeOptions();
		}
	}

	private void updateSwitchTabsMethod() {
		String method = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREFERENCES_GENERAL_SWITCH_TABS_METHOD, "buttons");
		
		if (method.equals("buttons")) {
			mSwitchTabsMethod = SwitchTabsMethod.BUTTONS;
		} else if (method.equals("fling")) {
			mSwitchTabsMethod = SwitchTabsMethod.FLING;
		} else if (method.equals("both")) {
			mSwitchTabsMethod = SwitchTabsMethod.BOTH;
		} else {
			mSwitchTabsMethod = SwitchTabsMethod.BUTTONS;
		}
	}

	private void updateBookmarksDatabaseSource() {
		String source = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREFERENCES_BOOKMARKS_DATABASE, "STOCK");
		
		if (source.equals("STOCK")) {
			BookmarksProviderWrapper.setBookmarksSource(BookmarksSource.STOCK);
		} else if (source.equals("INTERNAL")) {
			BookmarksProviderWrapper.setBookmarksSource(BookmarksSource.INTERNAL);
		}
	}

	private void setStatusBarVisibility(boolean visible) {
		int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * Initialize a newly created WebView.
	 */
	private void initializeCurrentWebView() {
		mCurrentWebView.setWebViewClient(new CustomWebViewClient(this));
		mCurrentWebView.setOnTouchListener(this);
		
		mCurrentWebView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				HitTestResult result = ((WebView) v).getHitTestResult();
				
				int resultType = result.getType();
				if ((resultType == HitTestResult.ANCHOR_TYPE) ||
						(resultType == HitTestResult.IMAGE_ANCHOR_TYPE) ||
						(resultType == HitTestResult.SRC_ANCHOR_TYPE) ||
						(resultType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {
					Intent i = new Intent();
					i.putExtra(Constants.EXTRA_ID_URL, result.getExtra());
					
					MenuItem item = menu.add(0, CONTEXT_MENU_OPEN, 0, R.string.Main_MenuOpen);
					item.setIntent(i);
					
					item = menu.add(0, CONTEXT_MENU_OPEN_IN_NEW_TAB, 0, R.string.Main_MenuOpenNewTab);
					item.setIntent(i);
					
					item = menu.add(0, CONTEXT_MENU_COPY, 0, R.string.Main_MenuCopyLinkUrl);
					item.setIntent(i);
					
					item = menu.add(0, CONTEXT_MENU_DOWNLOAD, 0, R.string.Main_MenuDownload);
					item.setIntent(i);
					
					item = menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.Main_MenuShareLinkUrl);
					item.setIntent(i);
					
					menu.setHeaderTitle(result.getExtra());
				} else if (resultType == HitTestResult.IMAGE_TYPE) {
					Intent i = new Intent();
					i.putExtra(Constants.EXTRA_ID_URL, result.getExtra());
					
					MenuItem item = menu.add(0, CONTEXT_MENU_OPEN, 0, R.string.Main_MenuViewImage);
					item.setIntent(i);
					
					item = menu.add(0, CONTEXT_MENU_COPY, 0, R.string.Main_MenuCopyImageUrl);
					item.setIntent(i);
					
					item = menu.add(0, CONTEXT_MENU_DOWNLOAD, 0, R.string.Main_MenuDownloadImage);
					item.setIntent(i);
					
					item = menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.Main_MenuShareImageUrl);
					item.setIntent(i);
					
					menu.setHeaderTitle(result.getExtra());
				} else if (resultType == HitTestResult.EMAIL_TYPE) {
					Intent sendMail = new Intent(Intent.ACTION_VIEW, Uri.parse(WebView.SCHEME_MAILTO + result.getExtra()));
					
					MenuItem item = menu.add(0, CONTEXT_MENU_SEND_MAIL, 0, R.string.Main_MenuSendEmail);
					item.setIntent(sendMail);
					
					Intent i = new Intent();
					i.putExtra(Constants.EXTRA_ID_URL, result.getExtra());
					
					item = menu.add(0, CONTEXT_MENU_COPY, 0, R.string.Main_MenuCopyEmailUrl);
					item.setIntent(i);
					
					item = menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.Main_MenuShareEmailUrl);
					item.setIntent(i);
					
					menu.setHeaderTitle(result.getExtra());
				}
			}
		});
		
		mCurrentWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				doDownloadStart(url, userAgent, contentDisposition, mimetype, contentLength);
			}
		});
		
		final Activity activity = this;
		mCurrentWebView.setWebChromeClient(new WebChromeClient() {
			
			// This is an undocumented method, it _is_ used, whatever Eclipse may think
			// Used to show a file chooser dialog.
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				MainActivity.this.startActivityForResult(Intent.createChooser(i, MainActivity.this.getString(R.string.Main_FileChooserPrompt)), OPEN_FILE_CHOOSER_ACTIVITY);
			}
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				MainActivity.this.startActivityForResult(Intent.createChooser(i, MainActivity.this.getString(R.string.Main_FileChooserPrompt)), OPEN_FILE_CHOOSER_ACTIVITY);
			}
			
			@Override
			public Bitmap getDefaultVideoPoster() {
				if (mDefaultVideoPoster == null) {
					mDefaultVideoPoster = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.default_video_poster);
				}
				
				return mDefaultVideoPoster;
			}
			@Override
			public View getVideoLoadingProgressView() {
				if (mVideoProgressView == null) {
					LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
					mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
				}
				
				return mVideoProgressView;
			}
			
			@Override
			public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
				showCustomView(view, callback);
			}
			
			@Override
			public void onHideCustomView() {
				hideCustomView();
			}
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				((CustomWebView) view).setProgress(newProgress);
				mProgressBar.setProgress(mCurrentWebView.getProgress());
			}
			
			@Override
			public void onReceivedIcon(WebView view, Bitmap icon) {
				new Thread(new FaviconUpdaterRunnable(MainActivity.this, view.getUrl(), view.getOriginalUrl(), icon)).start();
				updateFavIcon();
				
				super.onReceivedIcon(view, icon);
			}
			
			@Override
			public boolean onCreateWindow(WebView view, final boolean dialog, final boolean userGesture, final Message resultMsg) {
				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
				
				addTab(false, mViewFlipper.getDisplayedChild());
				
				transport.setWebView(mCurrentWebView);
				resultMsg.sendToTarget();
				
				return true;
			}
			
			@Override
			public void onReceivedTitle(WebView view, String title) {
				setTitle(String.format(getResources().getString(R.string.ApplicationNameUrl), title));
				
				startHistoryUpdaterRunnable(title, mCurrentWebView.getUrl(), mCurrentWebView.getOriginalUrl());
				
				super.onReceivedTitle(view, title);
			}
			
			@Override
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				new AlertDialog.Builder(activity)
					.setTitle(R.string.Commons_JavaScriptDialog)
					.setMessage(message)
					.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							result.confirm();
						}
					})
					.setCancelable(false)
					.create()
					.show();
				return true;
			}
			
			@Override
			public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
				new AlertDialog.Builder(MainActivity.this)
					.setTitle(R.string.Commons_JavaScriptDialog)
					.setMessage(message)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							result.confirm();
						}
					})
					.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							result.cancel();
						}
					})
					.create()
					.show();
				
				return true;
			}
		});
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
