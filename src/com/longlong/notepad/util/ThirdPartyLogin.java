package com.longlong.notepad.util;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.renren.Renren;

import com.longlong.notepad.R;
import com.longlong.notepad.activity.MarkActivity;
import com.mob.tools.FakeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.os.Handler.Callback;
import android.os.Message;

/**
 * 
 * @author ChengLong_Lu
 * 
 */
public class ThirdPartyLogin extends FakeActivity implements OnClickListener,
		Callback, PlatformActionListener {
	private long exitTime = 0;
	private Handler handler;
	private OnLoginListener signupListener;
	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;
	private static final int MSG_AUTH_COMPLETE = 4;

	/** 设置授权回调，用于判断是否进入注册 */
	public void setOnLoginListener(OnLoginListener l) {
		this.signupListener = l;
	}

	@Override
	public void onCreate() {
		// 初始化ui
		handler = new Handler(this);
		activity.setContentView(R.layout.login);
		(activity.findViewById(R.id.weibo_login)).setOnClickListener(this);
		(activity.findViewById(R.id.qq_login)).setOnClickListener(this);
		(activity.findViewById(R.id.renren_login)).setOnClickListener(this);
		(activity.findViewById(R.id.baidu_login)).setOnClickListener(this);
		Platform plat1 = ShareSDK.getPlatform(SinaWeibo.NAME);
		Platform plat2 = ShareSDK.getPlatform(QQ.NAME);
		Platform plat3 = ShareSDK.getPlatform(Renren.NAME);
		if (plat1.isValid()) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { plat1.getName(), null };
			handler.sendMessage(msg);
		} else if (plat2.isValid()) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { plat2.getName(), null };
			handler.sendMessage(msg);
		} else if (plat3.isValid()) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { plat3.getName(), null };
			handler.sendMessage(msg);
		} else {
			Animation anim = AnimationUtils.loadAnimation(activity,
					R.anim.login_button_enter);
			activity.findViewById(R.id.login_button).setAnimation(anim);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.weibo_login: {
			// 新浪微博
			Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
			authorize(sina);
		}
			break;
		case R.id.qq_login: {
			// QQ
			Platform qzone = ShareSDK.getPlatform(QQ.NAME);
			authorize(qzone);
		}
			break;
		case R.id.renren_login: {
			Platform renren = ShareSDK.getPlatform(Renren.NAME);
			authorize(renren);
		}
			break;
		case R.id.baidu_login: {
			Toast.makeText(activity, "百度登录暂不支持...", Toast.LENGTH_SHORT).show();
		}
			break;
		}
	}

	private void authorize(Platform plat) {

		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	@Override
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { platform.getName(), res };
			handler.sendMessage(msg);
		}
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_AUTH_CANCEL: {
			// 取消授权
			Toast.makeText(activity, "授权操作已取消", Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_AUTH_ERROR: {
			// 授权失败
			Toast.makeText(activity, "授权操作遇到错误", Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_AUTH_COMPLETE: {
			// 授权成功
			Toast.makeText(activity, "授权成功，正在跳转登录操作…", Toast.LENGTH_SHORT)
					.show();
			Object[] objs = (Object[]) msg.obj;
			String platform = (String) objs[0];
			HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
			if (signupListener != null
					&& signupListener.onSignin(platform, res)) {
				Intent intent = new Intent(activity, MarkActivity.class);
				startActivity(intent);
				this.finish();
			}
		}
			break;
		}
		return false;
	}

	private void initSDK(Context context) {
		ShareSDK.initSDK(context);
	}

	public void show(Context context) {
		initSDK(context);
		super.show(context, null);
	}

	/**
	 * 获取当前被点击的按钮，如果被点击的是返回键调用退出方法
	 */
	@Override
	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		if (MarkActivity.isActive) { // 如果MarkActivity运行 就关闭它
			MarkActivity.closeit.finish();
		}
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyEvent(keyCode, event);
	}

	/**
	 * 退出方法 弹出提示框 将Hanlder延时两秒执行 如果在两秒钟再次被点击返回按钮，退出程序 否者isExit 将被重新设置为false
	 */
}
