package com.longlong.notepad.util;

import java.util.HashMap;

/**
 * 
 * @author ChengLong_Lu
 * 
 */
public interface OnLoginListener {

	/** 授权完成调用此接口，返回授权数据，如果需要注册，则返回true */
	public boolean onSignin(String platform, HashMap<String, Object> res);

}
