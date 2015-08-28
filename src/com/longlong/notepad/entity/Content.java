package com.longlong.notepad.entity;

import cn.bmob.v3.BmobObject;

public class Content extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Content() {
		this.setTableName("Content");
	}

	private String user_id;
	private String Content;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
