package sf.browser.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sf.browser.R;
import android.content.Context;
import android.util.Log;

/**
 * Utilities for date /time management.
 * @author user
 *
 */
public class DateUtils {

	/**
	 * Get the default date format.
	 * @param context
	 * @return
	 */
	private static String getDefaultFormat(Context context) {
		return context.getResources().getString(R.string.DATE_FORMAT_ISO8601);
	}

	/**
	 * Get a string representation of current date / time in a format suitable for file name.
	 * @return 
	 */
	public static String getNowForFileName() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		return sdf.format(c.getTime());
	}

	/**
	 * Parse a string representation of a date in default format to a Date object.
	 * @param context
	 * @param date
	 * @return
	 */
	public static Date convertFromDatabase(Context context, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(getDefaultFormat(context));
		
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			Log.w(DateUtils.class.toString(), "Error parsing date (" + date + "): " + e.getMessage());
			return new Date();
		}
	}
}
