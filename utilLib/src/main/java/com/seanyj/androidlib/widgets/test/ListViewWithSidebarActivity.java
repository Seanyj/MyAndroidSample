package com.seanyj.androidlib.widgets.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seanyj.androidlib.R;
import com.seanyj.androidlib.utils.CollectionsUtil;
import com.seanyj.androidlib.widgets.ListViewWithSidebar;

public class ListViewWithSidebarActivity extends Activity
{
	private ListViewWithSidebar listViewWithSidebar;
	private ListView listView;
	private EditText query;
	private ImageButton clearSearch;
	private List<String> mData;
	private String mSelName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_with_sidebar);
		initView();
		setUpView();
	}

	protected void initView()
	{
		listViewWithSidebar = (ListViewWithSidebar) findViewById(R.id.layout_listview_with_sidebar);
		listView = listViewWithSidebar.getListView();

		// 搜索框
		query = (EditText) findViewById(R.id.query);
		clearSearch = (ImageButton) findViewById(R.id.search_clear);

		TextView headerView = new TextView(this);
		headerView.setText("hello owrldl");
		listView.addHeaderView(headerView);
		registerForContextMenu(listView);
	}

	protected void setUpView()
	{
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				String s = (String) listView.getItemAtPosition(position);
				Toast.makeText(ListViewWithSidebarActivity.this, s,
						Toast.LENGTH_SHORT).show();
			}
		});

		getData();
		sortData();
		listViewWithSidebar.init(mData);

		query.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				listViewWithSidebar.filter(s);
				if (s.length() > 0) {
					clearSearch.setVisibility(View.VISIBLE);
				} else {
					clearSearch.setVisibility(View.INVISIBLE);

				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
			}

			public void afterTextChanged(Editable s)
			{
			}
		});

		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				query.getText().clear();
			}
		});
	}

	private void sortData()
	{
		CollectionsUtil.sortForIndex(mData);
	}

	private void getData()
	{
		mData = new ArrayList<String>();
		mData.add("Jack");
		mData.add("Jane");
		mData.add("Andy");
		mData.add("Lucy");
		mData.add("123");
		mData.add("45");
		mData.add("$#%");
		mData.add("34ds@#");
		mData.add("王晓敏");
		mData.add("周扒皮");
		mData.add("张学友");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		mSelName = (String) listView
				.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
		getMenuInflater().inflate(R.menu.item_manipulate, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.delete) {
			deleteItem();
			return true;
		} else if (item.getItemId() == R.id.show) {
			showItem();
			return true;
		}

		return super.onContextItemSelected(item);
	}

	private void deleteItem()
	{
	}

	private void showItem()
	{
		Toast.makeText(this, mSelName, Toast.LENGTH_SHORT).show();
	}
}
