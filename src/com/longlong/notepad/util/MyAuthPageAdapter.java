package com.longlong.notepad.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;

import com.longlong.notepad.R;

/**
 * 
 * @author ChengLong_Lu
 *
 */
public class MyAuthPageAdapter extends AuthorizeAdapter {
	public void onCreate() {
		disablePopUpAnimation();
		hideShareSDKLogo();
		View rv = (View) getBodyView().getParent();
		Context context = rv.getContext();
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.sign_enter);
		anim.setDuration(500);
		rv.setAnimation(anim);
	}
}
