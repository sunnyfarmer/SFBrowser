package sf.browser.ui.activities;

import sf.browser.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * About dialog activity.
 * @author user
 *
 */
public class AboutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Window w = getWindow();
	    w.requestFeature(Window.FEATURE_LEFT_ICON);
	    
	    this.setContentView(R.layout.about_activity);
	    
	    w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_info);
	    
	    TextView versionText = (TextView) this.findViewById(R.id.AboutActivity_VersionText);
	    versionText.setText(this.getString(R.string.AboutActivity_VersionText) + " " + getVersion());
	    
	    TextView licenseText = (TextView) this.findViewById(R.id.AboutActivity_LicenseText);
	    licenseText.setText(this.getString(R.string.AboutActivity_LicenseText) + " " + this.getString(R.string.AboutActivity_LicenseTextValue));

	    TextView urlText = (TextView) this.findViewById(R.id.AboutActivity_UrlText);
	    urlText.setText(this.getString(R.string.AboutActivity_UrlTextValue));
	    
	    Button closeBtn = (Button) this.findViewById(R.id.AboutActivity_CloseBtn);
	    closeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * Get the current package version.
	 * @return The current version.
	 */
	private String getVersion() {
		String result = "";
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			
			result = String.format("%s (%s)", info.versionName, info.versionCode);
		} catch (NameNotFoundException e) {
			Log.w(AboutActivity.class.toString(), "Unable to get application version: " + e.getMessage());
			result = "Unable to get application version.";
		}
		
		return result;
	}
}