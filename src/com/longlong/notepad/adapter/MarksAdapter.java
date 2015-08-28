package com.longlong.notepad.adapter;

import java.util.List;
import java.util.Map;

import com.longlong.notepad.R;
import com.longlong.notepad.entity.Content;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MarksAdapter extends BaseAdapter {
	private List<Content> list;
	private Context context;

	public MarksAdapter(List<Content> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Content getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.list_item, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Content item = getItem(position);
		holder.iv_jiantou
				.setImageResource(R.drawable.arrow_right_622px_1190578_easyicon);
		String title = item.getContent();
		if (title.length() > 10) {
			title = title.substring(0, 10) + "...";
		} else {
		}
		holder.tv_title.setText(title);
		holder.tv_createdate.setText(item.getCreatedAt().substring(0, 10));
		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
		ImageView iv_jiantou;
		TextView tv_createdate;

		public ViewHolder(View view) {
			iv_jiantou = (ImageView) view.findViewById(R.id.iv_jiantou);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_createdate = (TextView) view.findViewById(R.id.tv_createdate);
			view.setTag(this);
		}
	}
}
