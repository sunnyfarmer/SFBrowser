package org.greendroid;

import java.util.ArrayList;
import java.util.List;

import sf.browser.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public abstract class QuickActionWidget extends PopupWindow{

	private static final int MEASURE_AND_LAYOUT_DONE = 1 << 1;

	private final int[] mLocation = new int[2];
	private final Rect mRect = new Rect();

	private int mPrivateFlags;

	private Context mContext;

	private boolean mDismissOnClick;
	private int mArrowOffsetY;

	private int mPopupY;
	private boolean mIsOnTop;

	private int mScreenHeight;
	private int mScreenWidth;
	private boolean mIsDirty;

	private OnQuickActionClickListener mOnQuickActionClickListener;
	private ArrayList<QuickAction> mQuickActions = new ArrayList<QuickAction>();

	/**
	 * Interface that may be used to listen to clicks on quick actions
	 * @author user
	 *
	 */
	public static interface OnQuickActionClickListener {
		/**
		 * Clients may implement this method to notify of a click on a
		 * particular quick action
		 * 
		 * @param widget
		 * @param position
		 */
		void onQuickActionClicked(QuickActionWidget widget, int position);
	}

	public QuickActionWidget(Context context) {
		super(context);

		this.mContext = context;

		this.initializeDefault();

		this.setFocusable(true);
		this.setTouchable(true);
		this.setOutsideTouchable(true);
		this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		final WindowManager windowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
		this.mScreenWidth = windowManager.getDefaultDisplay().getWidth();
		this.mScreenHeight = windowManager.getDefaultDisplay().getHeight();
	}

	public void setContentView(int layoutId) {
		this.setContentView(LayoutInflater.from(this.mContext).inflate(layoutId, null));
	}

	private void initializeDefault() {
		this.mDismissOnClick = true;
		this.mArrowOffsetY = mContext.getResources().getDimensionPixelSize(R.dimen.gd_arrow_offset);
	}

	public int getArrowOffsetY() {
		return this.mArrowOffsetY;
	}
	public void setArrowOffsetY(int offsetY) {
		this.mArrowOffsetY = offsetY;
	}

	protected int getScreenWidth() {
		return this.mScreenWidth;
	}
	protected int getScreenHeight() {
		return this.mScreenHeight;
	}

	public void setDismissOnClick(boolean dismissOnClick) {
		this.mDismissOnClick = dismissOnClick;
	}
	public boolean getDismissOnclick() {
		return this.mDismissOnClick;
	}

	public void setOnQuickActionClickListener(OnQuickActionClickListener listener) {
		this.mOnQuickActionClickListener = listener;
	}
	public void addQuickAction(QuickAction action) {
		if (action != null) {
			this.mQuickActions.add(action);
			this.mIsDirty = true;
		}
	}
	public void clearAllQuickActions() {
		if (!this.mQuickActions.isEmpty()) {
			this.mQuickActions.clear();
			this.mIsDirty = true;
		}
	}

	/**
	 * call that method to display the {@link QuickActionWidget} anchored to the
	 * given view.
	 * 
	 * @param anchor The view the {@link QuickActionWidget} will be anchored to.
	 */
	public void show(View anchor) {
		final View contenView = getContentView();

		if (contenView == null) {
			throw new IllegalStateException("You need to set the content view using the setContentView method");
		}

		// Replace the background of the popup with a cleared background
		this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		final int[] loc = mLocation;
		anchor.getLocationOnScreen(loc);
		this.mRect.set(loc[0], loc[1], loc[0]+anchor.getWidth(), loc[1]+anchor.getHeight());

		if (this.mIsDirty) {
			clearAllQuickActions();
			populateQuickActions(this.mQuickActions);
		}

		onMeasureAndLayout(mRect, contenView);

		if ((this.mPrivateFlags & MEASURE_AND_LAYOUT_DONE) != MEASURE_AND_LAYOUT_DONE) {
			throw new IllegalStateException("onMeasureAndLayout() did not set the widget specification by calling"
					+ " setWidgetSpecs()");
		}

		this.showArrow();
		prepareAnimationStyle();
		showAtLocation(anchor, Gravity.NO_GRAVITY, 0, mPopupY);
	}

	protected void clearQuickActions() {
		if (!this.mQuickActions.isEmpty()) {
			onClearQuickActions();
		}
	}
	protected void onClearQuickActions() {
	}

	protected abstract void populateQuickActions(List<QuickAction> quickActions);

	protected abstract void onMeasureAndLayout(Rect anchorRect, View contentView);

	protected void setWidgetSpecs(int popupY, boolean isOnTop) {
		this.mPopupY = popupY;
		this.mIsOnTop = isOnTop;

		this.mPrivateFlags |= MEASURE_AND_LAYOUT_DONE;
	}

	private void showArrow() {
		final View contentView = getContentView();
		final int arrowId = this.mIsOnTop ? R.id.gdi_arrow_down : R.id.gdi_arrow_up;
		final View arrow = contentView.findViewById(arrowId);
		final View arrowUp = contentView.findViewById(R.id.gdi_arrow_up);
		final View arrowDown = contentView.findViewById(R.id.gdi_arrow_down);

		if (arrowId == R.id.gdi_arrow_up) {
			arrowUp.setVisibility(View.VISIBLE);
			arrowDown.setVisibility(View.INVISIBLE);
		} else if (arrowId == R.id.gdi_arrow_down) {
			arrowUp.setVisibility(View.INVISIBLE);
			arrowDown.setVisibility(View.VISIBLE);
		}

		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) arrow.getLayoutParams();
		param.leftMargin = mRect.centerX() - (arrow.getMeasuredWidth()) / 2;
	}

	private void prepareAnimationStyle() {
		final int screenWidth = mScreenWidth;
		final boolean onTop = this.mIsOnTop;
		final int arrowPointX = this.mRect.centerX();

		if (arrowPointX <= screenWidth / 4) {
			this.setAnimationStyle(onTop ? R.style.GreenDroid_Animation_Popup_Left
					: R.style.GreenDroid_Animation_PopDown_Left);
		} else if (arrowPointX >= 3*screenWidth / 4) {
			this.setAnimationStyle(onTop ? R.style.GreenDroid_Animation_Popup_Right
					: R.style.GreenDroid_Animation_PopDown_Right);
		} else {
			this.setAnimationStyle(onTop ? R.style.GreenDroid_Animation_Popup_Center
					: R.style.GreenDroid_Animation_PopDown_Center);
		}
	}

	protected Context getContext() {
		return this.mContext;
	}

	protected OnQuickActionClickListener getOnQuickActionClickListener() {
		return this.mOnQuickActionClickListener;
	}
}



















