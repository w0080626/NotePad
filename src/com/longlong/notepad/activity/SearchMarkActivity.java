package com.longlong.notepad.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.longlong.notepad.R;
import com.longlong.notepad.adapter.MarksAdapter;
import com.longlong.notepad.entity.Content;
import com.longlong.notepad.util.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class SearchMarkActivity extends Activity implements OnItemClickListener {
	private SearchView sv_searchView;
	private SwipeMenuListView lv_searchMarks;
	private List<Content> list = new ArrayList<Content>();
	private MarksAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchmark);
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		sv_searchView = (SearchView) findViewById(R.id.sv_searchzhandian);
		sv_searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String param) {

				getData(param);
				return false;
			}
		});
		lv_searchMarks = (SwipeMenuListView) findViewById(R.id.lv_searchmarks);
		lv_searchMarks.setOnItemClickListener(this);
	}

	private void getData(String param) {
		final BmobQuery<Content> query = new BmobQuery<Content>();
		query.addWhereEqualTo("user_id", Utils.getUserid());
		query.addWhereContains("Content", param);
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
		query.findObjects(this, new FindListener<Content>() {
			@Override
			public void onSuccess(List<Content> object) {
				list = object;
				setAdapter();
			}

			@SuppressLint("ShowToast") @Override
			public void onError(int code, String msg) {
				Toast.makeText(getApplicationContext(),
						"查询失败  " + code + "   " + msg, 2000).show();
			}
		});

	}

	/**
	 * 
	 */
	private void setAdapter() {
		myAdapter = new MarksAdapter(list, getApplicationContext());
		lv_searchMarks.setAdapter(myAdapter);

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
		lv_searchMarks.setMenuCreator(creator);

		// step 2. listener item click event
		lv_searchMarks
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						@SuppressWarnings("unused")
						Content item = list.get(position);
						switch (index) {
						case 0:
							// delete(item);
							list.remove(position);
							myAdapter.notifyDataSetChanged();
							break;
						case 1:
							// delete

							break;
						}
						return false;
					}
				});

		// set SwipeListener
		lv_searchMarks.setOnSwipeListener(new OnSwipeListener() {

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
		lv_searchMarks
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						return false;
					}
				});

	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	/**
	 * 点击ListView中项跳转编辑页面
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
		Intent intent = new Intent(SearchMarkActivity.this,
				ContextActivity.class);
		Content item = list.get(index);
		Bundle bundle = new Bundle();
		bundle.putSerializable("item", item);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 返回到显示全部页面
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(SearchMarkActivity.this, MarkActivity.class);
		startActivity(intent);
		finish();
	}

}
