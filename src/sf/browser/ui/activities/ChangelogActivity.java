package sf.browser.ui.activities;

import sf.browser.R;
import sf.browser.utils.ApplicationUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Changelog dialog activity.
 * @author user
 *
 */
public class ChangelogActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Window w = getWindow();
	    w.requestFeature(Window.FEATURE_LEFT_ICON);
	    
	    this.setContentView(R.layout.changelog_activity);
	    
	    w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_info);

	    TextView changelogText = (TextView) findViewById(R.id.ChangelogContent);
	    changelogText.setText(ApplicationUtils.getChangelogString(this));
	    
	    Button closeBtn = (Button) this.findViewById(R.id.ChangelogActivity_CloseBtn);
	    closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
