package com.longlong.notepad.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.SaveListener;

import com.longlong.notepad.R;
import com.longlong.notepad.entity.Content;
import com.longlong.notepad.util.Utils;

public class EditActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_goBack;
	private TextView tv_goBack;
	private TextView tv_finish;
	private EditText et_context;
	private RelativeLayout edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit);
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		iv_goBack = (ImageView) findViewById(R.id.iv_goback);
		tv_goBack = (TextView) findViewById(R.id.tv_goback);
		tv_finish = (TextView) findViewById(R.id.tv_finish);
		et_context = (EditText) findViewById(R.id.et_context);
		et_context.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				tv_finish.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		edit = (RelativeLayout) findViewById(R.id.edit);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		iv_goBack.setOnClickListener(this);
		tv_goBack.setOnClickListener(this);
		tv_finish.setOnClickListener(this);
		et_context.setOnClickListener(this);
	}

	/**
	 * 重写点击事件
	 */
	@SuppressLint("ShowToast")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_goback:
			onBackPressed();
			break;
		case R.id.tv_goback:
			onBackPressed();
			break;
		case R.id.et_context:
			break;
		case R.id.tv_finish:
			final Content ce = new Content();
			String userid;

			userid = Utils.getUserid();

			if (userid == null) {
				Toast.makeText(this, "保存发生错误，请联系开发者...", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {
				Toast.makeText(this, userid, Toast.LENGTH_SHORT).show();
			}

			String context = et_context.getText().toString();
			if (context == null || context == "") {
				Toast.makeText(getApplicationContext(), "请写下点什么", 2000).show();

			} else {
				ce.setUser_id(userid);
				ce.setContent(context);
				Toast.makeText(getApplicationContext(), ce.getContent(), 2000)
						.show();
				ce.setTableName("Content");
				ce.save(getApplicationContext(), new SaveListener() {

					@Override
					public void onSuccess() {
						Toast.makeText(getApplicationContext(),
								"添加数据成功，返回objectId为：" + ce.getObjectId(), 2000)
								.show();
						Intent intent = new Intent(EditActivity.this,
								MarkActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onFailure(int code, String msg) {
						Toast.makeText(getApplicationContext(),
								"添加数据失败     " + code + "   " + msg, 2000)
								.show();
						Intent intent = new Intent(EditActivity.this,
								MarkActivity.class);
						startActivity(intent);
						finish();

					}
				});
			}
			break;
		default:
			break;
		}
	}

}
