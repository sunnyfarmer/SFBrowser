package org.greendroid;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

public class QuickAction {

	public Drawable mDrawable;
	public CharSequence mTitle;

	WeakReference<View> mView;

	public QuickAction(Drawable d, CharSequence title) {
		this.mDrawable = d;
		this.mTitle = title;
	}

	public QuickAction(Context ctx, int drawableId, CharSequence title) {
		this.mDrawable = ctx.getResources().getDrawable(drawableId);
		this.mTitle = title;
	}

	public QuickAction(Context ctx, Drawable d, int titleId) {
		mDrawable = d;
		mTitle = ctx.getResources().getString(titleId);
	}

	public QuickAction(Context ctx, int drawableId, int titleId) {
		mDrawable = ctx.getResources().getDrawable(drawableId);
		mTitle = ctx.getResources().getString(titleId);
	}
}
