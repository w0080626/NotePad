package com.longlong.notepad.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import com.longlong.notepad.R;
import com.longlong.notepad.activity.LoginActivity;
import com.mob.tools.utils.UIHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 * 
 * @author ChengLong_Lu
 * 
 */
public class SignupPage extends Activity implements OnClickListener, Callback {

	/** 加载用户icon */
	private static final int LOAD_USER_ICON = 2;
	/** 图片名字 */
	private static final String PICTURE_NAME = "userIcon.jpg";

	private CircleImg ivUserIcon;
	private TextView tvUserName;
	private Platform platform;

	private String picturePath;
	private UserInfo userInfo = new UserInfo();

	/** 设置授权回调，用于判断是否进入注册 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tpl_page_signup);
		platform = Utils.getValidPlatform();
		tvUserName = (TextView) findViewById(R.id.tv_user_name);
		ivUserIcon = (CircleImg) findViewById(R.id.iv_user_icon);

		ivUserIcon.setOnClickListener(this);
		findViewById(R.id.ExitUser).setOnClickListener(this);
		findViewById(R.id.signup_back).setOnClickListener(this);
		findViewById(R.id.iv_user_icon).setOnClickListener(this);
		initData();
	}

	/** 初始化数据 */
	private void initData() {
		if (platform != null) {
			userInfo.setUserIcon(platform.getDb().getUserIcon());
			userInfo.setUserName(platform.getDb().getUserName());
		}

		tvUserName.setText(userInfo.getUserName());
		// 加载头像
		if (!TextUtils.isEmpty(userInfo.getUserIcon())) {
			loadIcon();
		}
		// 初始化照片保存地址
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String thumPicture = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/"
					+ this.getPackageName()
					+ "/download";
			File pictureParent = new File(thumPicture);
			File pictureFile = new File(pictureParent, PICTURE_NAME);

			if (!pictureParent.exists()) {
				pictureParent.mkdirs();
			}
			try {
				if (!pictureFile.exists()) {
					pictureFile.createNewFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			picturePath = pictureFile.getAbsolutePath();
		} else {
			Log.e("change user icon ==>>", "there is not sdcard!");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signup_back:
			this.finish();
			break;
		case R.id.ExitUser:
			// 取消授权
			new Thread(new Runnable() {

				@Override
				public void run() {
					ShareSDK.removeCookieOnAuthorize(true);
					platform.removeAccount();
				}
			}).start();
			Toast.makeText(this, "已退出授权", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.iv_user_icon:
			PicViewer pv = new PicViewer();
			pv.setImagePath(userInfo.getUserIcon());
			pv.show(this, null);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case LOAD_USER_ICON:
			ivUserIcon.setImageURI(Uri.parse(picturePath));
			break;
		default:
			break;
		}
		return false;
	}

	/**
	 * 加载头像
	 */
	public void loadIcon() {
		final String imageUrl = platform.getDb().getUserIcon();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL picUrl = new URL(imageUrl);
					Bitmap userIcon = BitmapFactory.decodeStream(picUrl
							.openStream());
					FileOutputStream b = null;
					try {
						b = new FileOutputStream(picturePath);
						userIcon.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							b.flush();
							b.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					userInfo.setUserIcon(picturePath);

					Message msg = new Message();
					msg.what = LOAD_USER_ICON;
					UIHandler.sendMessage(msg, SignupPage.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
