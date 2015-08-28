package com.longlong.notepad.activity;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

import com.longlong.notepad.R;
import com.longlong.notepad.entity.Content;
import com.longlong.notepad.util.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContextActivity extends BaseActivity implements OnClickListener {
	private ImageView iv_goBack;
	private TextView tv_goBack;
	private TextView tv_time;
	private TextView tv_finish;
	private EditText et_context;
	private Content content;
	private ImageView iv_add;
	private ImageView iv_share;
	private ImageView iv_delete;
	private RelativeLayout rl;
	private LinearLayout ll_dibu;
	private String[] areas = new String[] { "新浪微博分享", "QQ空间分享" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.context);
		Intent intent = this.getIntent();
		content = (Content) intent.getSerializableExtra("item");
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		ll_dibu = (LinearLayout) findViewById(R.id.ll_dibu);
		iv_add = (ImageView) findViewById(R.id.iv_add_context);
		iv_share = (ImageView) findViewById(R.id.iv_share_context);
		iv_delete = (ImageView) findViewById(R.id.iv_delete_context);
		iv_goBack = (ImageView) findViewById(R.id.iv_goback_context);
		tv_goBack = (TextView) findViewById(R.id.tv_goback_context);
		tv_time = (TextView) findViewById(R.id.tv_time_context);
		tv_finish = (TextView) findViewById(R.id.tv_finish_context);
		et_context = (EditText) findViewById(R.id.et_context_context);
		et_context.setCursorVisible(false);
		rl = (RelativeLayout) findViewById(R.id.context);
		rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				et_context.setCursorVisible(true);
				tv_finish.setVisibility(View.VISIBLE);
				if (ll_dibu.getVisibility() == View.VISIBLE) {
					ll_dibu.setVisibility(View.GONE);
				}else{
					ll_dibu.setVisibility(View.VISIBLE);
				}

			}
		});
		tv_time.setText(content.getCreatedAt());
		et_context.setText(content.getContent());
		// 设置焦点移动到文本最后
		CharSequence text = et_context.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}

		iv_add.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
		iv_share.setOnClickListener(this);
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
		case R.id.iv_goback_context:
			onBackPressed();
			break;
		case R.id.tv_goback_context:
			onBackPressed();
			break;
		case R.id.iv_add_context:
			Intent intent = new Intent(ContextActivity.this, EditActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_delete_context:
			content.delete(getApplicationContext(), new DeleteListener() {
				@Override
				public void onSuccess() {
					Intent intent = new Intent(ContextActivity.this,
							MarkActivity.class);
					startActivity(intent);
					finish();
				}

				@SuppressLint("ShowToast")
				@Override
				public void onFailure(int code, String msg) {
					Toast.makeText(getApplicationContext(),
							"删除失败了  " + code + "   " + msg, 2000).show();
				}
			});
			break;
		case R.id.iv_share_context:
			/**
			 * 分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享
			 * 分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享
			 * 分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享
			 * 分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享
			 * 分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享
			 * 分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享分享
			 */
			break;
		case R.id.tv_finish_context:
			String userid = Utils.getUserid();
			String context = et_context.getText().toString();
			if (context == null || context == "") {
				Toast.makeText(getApplicationContext(), "请写下点什么", 2000).show();

			} else {
				// 获取print方法，传入参数并执行
				content.setUser_id(userid);
				content.setContent(context);
				Toast.makeText(getApplicationContext(), content.getContent(),
						2000).show();
				content.update(this, content.getObjectId(),
						new UpdateListener() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "更新成功",
										2000).show();
								tv_finish.setVisibility(View.GONE);
								InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								inputMethodManager.hideSoftInputFromWindow(
										ContextActivity.this.getCurrentFocus()
												.getWindowToken(),
										InputMethodManager.HIDE_NOT_ALWAYS);
								et_context.setCursorVisible(false);
							}

							@Override
							public void onFailure(int code, String msg) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(),
										"更新失败    " + code + "  " + msg, 2000)
										.show();
								tv_finish.setVisibility(View.GONE);
							}
						});
			}
			break;
		default:
			break;
		}
	}

	class RadioOnClick implements DialogInterface.OnClickListener {
		private int index;

		public RadioOnClick(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void onClick(DialogInterface dialog, int whichButton) {
			setIndex(whichButton);
			Toast.makeText(ContextActivity.this,
					"您已经选择了： " + index + ":" + areas[index], Toast.LENGTH_LONG)
					.show();
			dialog.dismiss();
		}
	}

}
