package sf.browser.model.adapters;

import sf.browser.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.provider.Browser;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

/**
 * Cursor adapter for bookmarks.
 * @author user
 *
 */
public class BookmarksCursorAdapter extends SimpleCursorAdapter {

	private int mFaviconSize;
	
	public BookmarksCursorAdapter(Context context, int layout, Cursor c, String[] from , int[] to, int faviconSize) {
		super(context, layout, c, from, to);
		this.mFaviconSize = faviconSize;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View superView = super.getView(position, convertView, parent);
		
		ImageView thumbnailView = (ImageView) superView.findViewById(R.id.BookmarkRow_Thumbnail);
		
		byte[] favicon = this.getCursor().getBlob(getCursor().getColumnIndex(Browser.BookmarkColumns.FAVICON));
		if (favicon != null) {
			BitmapDrawable icon = new BitmapDrawable(BitmapFactory.decodeByteArray(favicon, 0, favicon.length));
			
			Bitmap bm = Bitmap.createBitmap(mFaviconSize, mFaviconSize, Bitmap.Config.ARGB_4444);
			Canvas canvas = new Canvas(bm);
			
			icon.setBounds(0, 0, mFaviconSize, mFaviconSize);
			icon.draw(canvas);
			
			thumbnailView.setImageBitmap(bm);
		} else {
			thumbnailView.setImageResource(R.drawable.fav_icn_unknown);
		}
		
		return superView;
	}
}
