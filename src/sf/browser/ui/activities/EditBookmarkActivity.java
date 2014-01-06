package sf.browser.ui.activities;

import sf.browser.R;
import sf.browser.providers.BookmarksProviderWrapper;
import sf.browser.utils.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Add / Edit bookmark activity.
 * @author user
 *
 */
public class EditBookmarkActivity extends Activity {

	private EditText mTitleEditText;
	private EditText mUrlEditText;

	private Button mOkButton;
	private Button mCancelButton;
	
	private long mRowId = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    Window w = this.getWindow();
	    w.requestFeature(Window.FEATURE_LEFT_ICON);
	    
	    setContentView(R.layout.edit_bookmark_activity);
	    w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_input_add);
	    
	    mTitleEditText = (EditText) this.findViewById(R.id.EditBookmarkActivity_TitleValue);
	    mUrlEditText = (EditText) this.findViewById(R.id.EditBookmarkActivity_UrlValue);
	    
	    mOkButton = (Button) this.findViewById(R.id.EditBookmarkActivity_BtnOk);
	    mCancelButton = (Button) this.findViewById(R.id.EditBookmarkActivity_BtnCancel);
	    
	    this.mOkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setAsBookmark();
				setResult(RESULT_OK);
				finish();
			}
		});
	    
	    this.mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	    
	    Bundle extras = this.getIntent().getExtras();
	    if (extras != null) {
	    	String title = extras.getString(Constants.EXTRA_ID_BOOKMARK_TITLE);
	    	if (title != null && title.length()>0) {
	    		mTitleEditText.setText(title);
	    	}
	    	
	    	String url = extras.getString(Constants.EXTRA_ID_BOOKMARK_URL);
	    	if (url != null && url.length()>0) {
	    		mUrlEditText.setText(url);
	    	} else {
	    		mUrlEditText.setHint("http://");
	    	}
	    	
	    	this.mRowId = extras.getLong(Constants.EXTRA_ID_BOOKMARK_ID);
	    }
	    
	    if (mRowId == -1) {
	    	setTitle(R.string.EditBookmarkActivity_TitleAdd);
	    }
	}
	
	/**
	 * Set the current title and url values as a bookmark, e.g. adding a record if necessary or set only the bookmark flag.
	 */
	private void setAsBookmark() {
		BookmarksProviderWrapper.setAsBookmark(getContentResolver(), mRowId, mTitleEditText.getText().toString(), mUrlEditText.getText().toString(), true);
	}
}
