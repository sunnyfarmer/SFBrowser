package sf.browser.utils;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Holder for animation objects.
 * @author user
 *
 */
public class AnimationManager {

	private static final int BARS_ANIMATION_DURATION = 150;
	private static final int ANIMATION_DURATION = 350;
	
	private Animation mTopBarShowAnimation = null;
	private Animation mTopBarHideAnimation = null;
	private Animation mBottomBarShowAnimation = null;
	private Animation mBottomBarHideAnimation = null;
	
	private Animation mPreviousTabViewShowAnimation = null;
	private Animation mPreviousTabViewHideAnimation = null;
	private Animation mNextTabViewShowAnimation = null;
	private Animation mNextTabViewHideAnimation = null;
	
	private Animation mInFromRightAnimation;
	private Animation mOutToLeftAnimation;
	private Animation mInFromLeftAnimation;
	private Animation mOutToRightAnimation;
	
	private static class AnimationManagerHolder {
		private static final AnimationManager INSTANCE = new AnimationManager();
	}
	
	/**
	 * Get the unique instance of the Controller.
	 * @return
	 */
	public static AnimationManager getInstance() {
		return AnimationManagerHolder.INSTANCE;
	}

	private AnimationManager() {
		mTopBarShowAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mTopBarShowAnimation.setDuration(BARS_ANIMATION_DURATION);
		
		mTopBarHideAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		mTopBarHideAnimation.setDuration(BARS_ANIMATION_DURATION);

		mBottomBarShowAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mBottomBarShowAnimation.setDuration(BARS_ANIMATION_DURATION);
		
		mBottomBarHideAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f
				);
		mBottomBarHideAnimation.setDuration(BARS_ANIMATION_DURATION);
		
		mPreviousTabViewShowAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mPreviousTabViewShowAnimation.setDuration(BARS_ANIMATION_DURATION);
		
		mPreviousTabViewHideAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mPreviousTabViewHideAnimation.setDuration(BARS_ANIMATION_DURATION);
		
		mNextTabViewShowAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mNextTabViewShowAnimation.setDuration(BARS_ANIMATION_DURATION);
		
		mNextTabViewHideAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mNextTabViewHideAnimation.setDuration(BARS_ANIMATION_DURATION);
		
		mInFromRightAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mInFromRightAnimation.setDuration(ANIMATION_DURATION);
		mInFromRightAnimation.setInterpolator(new AccelerateInterpolator());
		
		mOutToLeftAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mOutToLeftAnimation.setDuration(ANIMATION_DURATION);
		mOutToLeftAnimation.setInterpolator(new AccelerateInterpolator());
		
		mInFromLeftAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mInFromLeftAnimation.setDuration(ANIMATION_DURATION);
		mInFromLeftAnimation.setInterpolator(new AccelerateInterpolator());
		
		mOutToRightAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mOutToRightAnimation.setDuration(ANIMATION_DURATION);
		mOutToRightAnimation.setInterpolator(new AccelerateInterpolator());
	}
	
	public Animation getTopBarShowAnimation() {
		return this.mTopBarShowAnimation;
	}
	
	public Animation getTopBarHideAnimation() {
		return this.mTopBarHideAnimation;
	}
	
	public Animation getBottomBarShowAnimation() {
		return this.mBottomBarShowAnimation;
	}
	
	public Animation getBottomBarHideAnimation() {
		return this.mBottomBarHideAnimation;
	}
	
	public Animation getPreviousTabViewShowAnimation() {
		return this.mPreviousTabViewShowAnimation;
	}
	
	public Animation getPreviousTabViewHideAnimation() {
		return this.mPreviousTabViewHideAnimation;
	}
	
	public Animation getNextTabViewShowAnimation() {
		return this.mNextTabViewShowAnimation;
	}
	
	public Animation getNextTabViewHideAnimation() {
		return this.mNextTabViewHideAnimation;
	}
	
	public Animation getInFromRightAnimation() {
		return this.mInFromRightAnimation;
	}
	
	public Animation getOutToLeftAnimation() {
		return this.mOutToLeftAnimation;
	}
	
	public Animation getInFromLeftAnimation() {
		return this.mInFromLeftAnimation;
	}
	
	public Animation getOutToRightAnimation() {
		return this.mOutToRightAnimation;
	}
}
