package com.seanyj.androidlib;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity
{
	public static final String PREFIX = "com.example.myadlib.prefix";
	public static final String TITLE = "title";
	public static final String INTENT = "intent";
	private static final String SAMPLE_ACTION = "com.example.myadlib.sample";

	private String mPrefix;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mPrefix = getIntent().getStringExtra(PREFIX);
		if (mPrefix == null) {
			mPrefix = "";
		} else {
			String title;
			if (!mPrefix.contains("/")) {
				title = mPrefix;
			} else {
				title = mPrefix.substring(mPrefix.lastIndexOf("/") + 1);
			}
			setTitle(title);
		}

		setListAdapter(new SimpleAdapter(this, getData(),
				android.R.layout.simple_list_item_1, new String[] { TITLE },
				new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
	}

	private List<Map<String, Object>> getData()
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Boolean> entries = new HashMap<String, Boolean>();
		String[] prefixPath = mPrefix.equals("") ? null : mPrefix.split("/");
		String prefixWithSlash = mPrefix;
		if (!prefixWithSlash.equals("")) {
			prefixWithSlash += "/";
		}

		Intent intent = new Intent();
		intent.setAction(SAMPLE_ACTION);
		intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
		PackageManager pm = getPackageManager();
		List<ResolveInfo> resoleInfoList = pm.queryIntentActivities(intent, 0);

		if (resoleInfoList == null) {
			return list;
		}

		for (int i = 0; i < resoleInfoList.size(); i++) {
			ResolveInfo resolveInfo = resoleInfoList.get(i);
			CharSequence lableSeq = resolveInfo.loadLabel(pm);
			String label = lableSeq != null ? lableSeq.toString()
					: resolveInfo.activityInfo.name;

			if (prefixWithSlash.equals("") || label.startsWith(prefixWithSlash)) {
				String[] labelPath = label.split("/");
				String nextTitle = prefixPath == null ? labelPath[0]
						: labelPath[prefixPath.length];

				if ((prefixPath == null ? 0 : prefixPath.length) == labelPath.length - 1) {
					addItem(list,
							nextTitle,
							activityIntent(
									resolveInfo.activityInfo.applicationInfo.packageName,
									resolveInfo.activityInfo.name));
				} else {
					if (!entries.containsKey(nextTitle)) {
						Intent browseIntet = browseIntent(mPrefix.equals("") ? nextTitle
								: mPrefix + "/" + nextTitle);
						entries.put(nextTitle, true);
						addItem(list, nextTitle, browseIntet);
					}
				}
			}
		}

		Collections.sort(list, new DisplayNameComparator());

		return list;
	}

	private class DisplayNameComparator implements
			Comparator<Map<String, Object>>
	{
		private final Collator collator = Collator.getInstance();

		@Override
		public int compare(Map<String, Object> lhs, Map<String, Object> rhs)
		{
			return collator.compare(lhs.get(TITLE), rhs.get(TITLE));
		}
	}

	private Intent browseIntent(String nextTitle)
	{
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		intent.putExtra(PREFIX, nextTitle);

		return intent;
	}

	private Intent activityIntent(String packageName, String name)
	{
		Intent intent = new Intent();
		intent.setClassName(packageName, name);

		return intent;
	}

	private void addItem(List<Map<String, Object>> list, String nextTitle,
			Intent intent)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TITLE, nextTitle);
		map.put(INTENT, intent);
		list.add(map);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		@SuppressWarnings("unchecked")
		Map<String, Object> item = (Map<String, Object>) l
				.getItemAtPosition(position);
		Intent i = (Intent) item.get(INTENT);

		startActivity(i);
	}
}
