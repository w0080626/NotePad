package com.longlong.notepad.util;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

public class Utils {
	public static Platform getValidPlatform() {
		Platform plat1 = ShareSDK.getPlatform(SinaWeibo.NAME);
		Platform plat2 = ShareSDK.getPlatform(QQ.NAME);
		Platform plat3 = ShareSDK.getPlatform(Renren.NAME);
		if (plat1.isValid()) {
			return plat1;
		} else if (plat2.isValid()) {
			return plat2;
		} else if (plat3.isValid()) {
			return plat3;
		} else {
			return null;
		}
	}

	public static String getUserid() {
		Platform platform = Utils.getValidPlatform();
		if (platform != null) {
			PlatformDb db = platform.getDb();
			return db.getUserId();
		} else {
			return null;
		}

	}
}
