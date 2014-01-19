package sf.browser.utils.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.WindowManager;

public class ActivityUtils {

	/**
	 * Get the size of the Display.
	 * @param context
	 * @return Point
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static Point getScreenSize(Context context) {
		Point p = new Point();
		WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		if (VERSION.SDK_INT >= 13) {
			windowManager.getDefaultDisplay().getSize(p);
		} else {
			p.x = windowManager.getDefaultDisplay().getWidth();
			p.y = windowManager.getDefaultDisplay().getHeight();
		}
		return p;
	}
}
