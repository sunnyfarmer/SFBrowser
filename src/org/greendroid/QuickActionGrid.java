package org.greendroid;

import java.util.List;

import sf.browser.R;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class QuickActionGrid extends QuickActionWidget{
	private GridView mGridView;

	public QuickActionGrid(Context context) {
		super(context);

		setContentView(R.layout.gd_quick_action_grid);

		final View v = getContentView();
		this.mGridView = (GridView) v.findViewById(R.id.gdi_grid);
	}

	@Override
	protected void populateQuickActions(final List<QuickAction> quickActions) {
		this.mGridView.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return quickActions.size();
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				TextView textView = (TextView) view;

				if (view == null) {
					final LayoutInflater inflater = LayoutInflater.from(getContext());
					textView = (TextView) inflater.inflate(R.layout.gd_quick_action_grid_item, mGridView, false);
				}

				QuickAction quickAction = quickActions.get(position);
				textView.setText(quickAction.mTitle);
				textView.setCompoundDrawablesWithIntrinsicBounds(null, quickAction.mDrawable, null, null);

				return textView;
			}
			
		});
	}

	@Override
	protected void onMeasureAndLayout(Rect anchorRect, View contentView) {
		contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		contentView.measure(MeasureSpec.makeMeasureSpec(getScreenWidth(), MeasureSpec.EXACTLY), LayoutParams.WRAP_CONTENT);

		int rootHeight = contentView.getMeasuredHeight();

		int offsetY = getArrowOffsetY();
		int dyTop = anchorRect.top;
		int dyBottom = getScreenHeight() - anchorRect.bottom;

		boolean onTop = (dyTop > dyBottom);
		int popupY = (onTop) ? anchorRect.top-rootHeight+offsetY : anchorRect.bottom-offsetY;

		setWidgetSpecs(popupY, onTop);
	}

	private OnItemClickListener mInternalItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			getOnQuickActionClickListener().onQuickActionClicked(QuickActionGrid.this, position);
			if (getDismissOnclick()) {
				dismiss();
			}
		}
	};
}