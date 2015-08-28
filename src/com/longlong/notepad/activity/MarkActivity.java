package com.longlong.notepad.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.longlong.notepad.R;
import com.longlong.notepad.adapter.MarksAdapter;
import com.longlong.notepad.entity.Content;
import com.longlong.notepad.util.SignupPage;
import com.longlong.notepad.util.Utils;
import com.mob.tools.FakeActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MarkActivity extends Activity implements OnItemClickListener {
	boolean isExit;
	private ImageView iv_search;
	public static MarkActivity closeit = null;
	private ImageView iv_setting;
	private SwipeMenuListView lv_marks;
	private ImageView iv_add;
	private List<Content> list = new ArrayList<Content>();
	public static boolean isActive = false;
	private MarksAdapter myAdapter;

	@Override
	protected void onStart() {
		super.onStart();
		isActive = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		closeit = this;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listmark);
		Bmob.initialize(this, "3cfa2bb0a456f31cb2907e8cf683dc60");
		init();
		initData();
	}

	/**
	 * 获取当前被点击的按钮，如果被点击的是返回键调用退出方法
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 退出方法 弹出提示框 将Hanlder延时两秒执行 如果在两秒钟再次被点击返回按钮，退出程序 否者isExit 将被重新设置为false
	 */
	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}

	};

	/**
	 * 初始化控件
	 */
	@SuppressLint("HandlerLeak")
	private void init() {
		iv_search = (ImageView) findViewById(R.id.iv_search);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);

		// 设置按钮点击监听
		iv_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isActive) {
					Intent intent = new Intent(MarkActivity.this,
							SignupPage.class);
					startActivityForResult(intent, 11);
					overridePendingTransition(R.anim.anim_enter,
							R.anim.anim_exit);
				} else {
					Toast.makeText(MarkActivity.this, " ", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		iv_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MarkActivity.this,
						SearchMarkActivity.class);
				startActivity(intent);
				finish();
			}
		});
		lv_marks = (SwipeMenuListView) findViewById(R.id.lv_marks);
		lv_marks.setOnItemClickListener(this);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		iv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MarkActivity.this,
						EditActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化数据
	 * 
	 * @throws Exception
	 */
	private void initData() {
		final BmobQuery<Content> query = new BmobQuery<Content>();
		query.addWhereEqualTo("user_id", Utils.getUserid());
		query.count(this, Content.class, new CountListener() {
			@Override
			public void onSuccess(int count) {
				query.setLimit(count);
			}

			@Override
			public void onFailure(int code, String msg) {
				query.setLimit(50);
			}
		});

		query.order("createdAt");

		boolean isCache = query.hasCachedResult(getApplicationContext(),
				Content.class);
		if (isCache) {
			query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK); // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
		} else {
			query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK); // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
		}

		query.findObjects(this, new FindListener<Content>() {
			@Override
			public void onSuccess(List<Content> object) {
				list = object;
				setAdapter();
			}

			@SuppressLint("ShowToast")
			@Override
			public void onError(int code, String msg) {
				Toast.makeText(getApplicationContext(),
						"查询失败  " + code + "   " + msg, 2000).show();
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void setAdapter() {
		myAdapter = new MarksAdapter(list, getApplicationContext());
		lv_marks.setAdapter(myAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.RED));
				// set item width
				openItem.setWidth(dp2px(50));
				// set item title
				openItem.setTitle("删除");
				// set item title fontsize
				openItem.setTitleSize(13);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

			}
		};
		// set creator
		lv_marks.setMenuCreator(creator);

		// step 2. listener item click event
		lv_marks.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu,
					int index) {
				Content item = list.get(position);
				switch (index) {
				case 0:

					item.delete(getApplicationContext(), new DeleteListener() {

						@Override
						public void onSuccess() {
							list.remove(position);
							myAdapter.notifyDataSetChanged();
						}

						@SuppressLint("ShowToast")
						@Override
						public void onFailure(int code, String msg) {
							Toast.makeText(getApplicationContext(),
									"删除失败了  " + code + "   " + msg, 2000)
									.show();
						}
					});
					break;
				case 1:
					// delete

					break;
				}
				return false;
			}
		});

		// set SwipeListener
		lv_marks.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
		// listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		lv_marks.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				return false;
			}
		});

	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
		Intent intent = new Intent(MarkActivity.this, ContextActivity.class);
		Content item = list.get(index);
		Bundle bundle = new Bundle();
		bundle.putSerializable("item", item);
		intent.putExtras(bundle);
		startActivity(intent);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
