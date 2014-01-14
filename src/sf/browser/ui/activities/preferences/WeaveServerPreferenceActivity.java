package sf.browser.ui.activities.preferences;

import sf.browser.R;
import sf.browser.utils.Constants;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class WeaveServerPreferenceActivity extends BaseSpinnerCustomPreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}

	@Override
	protected int getSpinnerPromptId() {
		return R.string.WeaveServerPreferenceActivity_Prompt;
	}

	@Override
	protected int getSpinnerValuesArrayId() {
		return R.array.WeaveServerValues;
	}

	@Override
	protected void setSpinnerValueFromPreferences() {
		String currentServer = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREFERENCE_WEAVE_SERVER, Constants.WEAVE_DEFAULT_SERVER);
		
		if (currentServer.equals(Constants.WEAVE_DEFAULT_SERVER)) {
			mSpinner.setSelection(0);
			mCustomEditText.setEnabled(false);
			mCustomEditText.setText(Constants.WEAVE_DEFAULT_SERVER);
		} else {
			mSpinner.setSelection(1);
			mCustomEditText.setEnabled(true);
			mCustomEditText.setText(currentServer);
		}
	}

	@Override
	protected void onSpinnerItemSelected(int position) {
		switch (position) {
		case 0:
			mCustomEditText.setEnabled(false);
			mCustomEditText.setText(Constants.WEAVE_DEFAULT_SERVER);
			break;
		case 1:
			mCustomEditText.setEnabled(true);
			
			if (mCustomEditText.getText().toString().equals(Constants.WEAVE_DEFAULT_SERVER)) {
				mCustomEditText.setText(null);
			}
			break;
		default:
			mCustomEditText.setEnabled(false);
			mCustomEditText.setText(Constants.WEAVE_DEFAULT_SERVER);
			break;
		}
	}

	@Override
	protected void onOk() {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putString(Constants.PREFERENCE_WEAVE_SERVER, mCustomEditText.getText().toString());
		editor.commit();
	}

}
