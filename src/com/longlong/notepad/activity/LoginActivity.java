package com.longlong.notepad.activity;

/**
 * 
 * @author ChengLong_Lu
 *
 */
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;

import com.longlong.notepad.util.ThirdPartyLogin;
import com.longlong.notepad.util.UserInfo;
import com.longlong.notepad.util.OnLoginListener;

import android.app.Activity;
import android.os.Bundle;

public class LoginActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showDemo();
		finish();
	}

	private void showDemo() {
		ThirdPartyLogin tpl = new ThirdPartyLogin();
		tpl.setOnLoginListener(new OnLoginListener() {
			public boolean onSignin(String platform, HashMap<String, Object> res) {
				// 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
				// 此处全部给回需要注册
				return true;
			}
		});
		tpl.show(this);
	}

}
