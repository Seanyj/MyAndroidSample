package com.seanyj.androidlib.widgets;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.seanyj.androidlib.R;
import com.seanyj.androidlib.widgets.test.adapters.IndexedListViewAdapter;

public class ListViewWithSidebar extends RelativeLayout
{
	protected Context context;
	protected ListView listView;
	protected IndexedListViewAdapter adapter;
	protected List<String> mList;
	protected IndexSidebar sidebar;

	protected int primaryColor;
	protected int primarySize;
	protected boolean showSiderBar;
	protected Drawable initialLetterBg;

	protected int initialLetterColor;

	public ListViewWithSidebar(Context context)
	{
		super(context);
		init(context, null);
	}

	public ListViewWithSidebar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context, attrs);
	}

	public ListViewWithSidebar(Context context, AttributeSet attrs, int defStyle)
	{
		this(context, attrs);
	}

	private void init(Context context, AttributeSet attrs)
	{
		this.context = context;
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.EaseContactList);
		primaryColor = ta.getColor(
				R.styleable.EaseContactList_ctsListPrimaryTextColor, 0);
		primarySize = ta.getDimensionPixelSize(
				R.styleable.EaseContactList_ctsListPrimaryTextSize, 0);
		showSiderBar = ta.getBoolean(
				R.styleable.EaseContactList_ctsListShowSiderBar, true);
		initialLetterBg = ta
				.getDrawable(R.styleable.EaseContactList_ctsListInitialLetterBg);
		initialLetterColor = ta.getColor(
				R.styleable.EaseContactList_ctsListInitialLetterColor, 0);
		ta.recycle();

		LayoutInflater.from(context).inflate(
				R.layout.layout_listview_with_sidebar, this);
		listView = (ListView) findViewById(R.id.listView);
		sidebar = (IndexSidebar) findViewById(R.id.sidebar);
	}

	/*
	 * init view
	 */
	public void init(List<String> list)
	{
		this.mList = list;
		adapter = new IndexedListViewAdapter(context, 0, new ArrayList<String>(
				mList));
		adapter.setPrimaryColor(primaryColor).setPrimarySize(primarySize)
				.setInitialLetterBg(initialLetterBg)
				.setInitialLetterColor(initialLetterColor);
		listView.setAdapter(adapter);

		sidebar.setListView(listView);
	}

	public void filter(CharSequence str)
	{
		adapter.getFilter().filter(str);
	}

	public ListView getListView()
	{
		return listView;
	}

}
