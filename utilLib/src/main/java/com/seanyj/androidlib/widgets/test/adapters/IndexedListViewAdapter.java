package com.seanyj.androidlib.widgets.test.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.seanyj.androidlib.R;
import com.seanyj.androidlib.utils.WordsUtil;

public class IndexedListViewAdapter extends ArrayAdapter<String> implements
		SectionIndexer
{
	List<String> list;
	List<String> userList;
	List<String> copyUserList;
	private LayoutInflater layoutInflater;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
	private int res;
	private MyFilter myFilter;
	private boolean notiyfyByFilter;

	public IndexedListViewAdapter(Context context, int resource,
			List<String> objects)
	{
		super(context, resource, objects);

		this.res = resource;
		this.userList = objects;
		copyUserList = new ArrayList<>();
		copyUserList.addAll(objects);
		layoutInflater = LayoutInflater.from(context);
	}

	private static class ViewHolder
	{
		ImageView avatar;
		TextView nameView;
		TextView headerView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			if (res == 0)
				convertView = layoutInflater.inflate(R.layout.item_name_row,
						null);
			else
				convertView = layoutInflater.inflate(res, null);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.nameView = (TextView) convertView.findViewById(R.id.name);
			holder.headerView = (TextView) convertView
					.findViewById(R.id.header);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String username = getItem(position);
		String header = WordsUtil.getStartLetter(username);

		if (position == 0
				|| header != null
				&& !header.equals(WordsUtil
						.getStartLetter(getItem(position - 1)))) {
			if (TextUtils.isEmpty(header)) {
				holder.headerView.setVisibility(View.GONE);
			} else {
				holder.headerView.setVisibility(View.VISIBLE);
				holder.headerView.setText(header);
			}
		} else {
			holder.headerView.setVisibility(View.GONE);
		}
		holder.nameView.setText(username);
		holder.avatar.setImageResource(R.drawable.icn_default_avatar);

		if (primaryColor != 0)
			holder.nameView.setTextColor(primaryColor);
		if (primarySize != 0)
			holder.nameView
					.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
		if (initialLetterBg != null)
			holder.headerView.setBackgroundDrawable(initialLetterBg);
		if (initialLetterColor != 0)
			holder.headerView.setTextColor(initialLetterColor);

		return convertView;
	}

	@Override
	public int getPositionForSection(int section)
	{
		return positionOfSection.get(section);
	}

	@Override
	public int getSectionForPosition(int position)
	{
		return sectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections()
	{
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		list = new ArrayList<String>();

		list.add(getContext().getString(R.string.search_header));
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 0; i < count; i++) {
			String letter = WordsUtil.getStartLetter(getItem(i));
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i + 1);
			}
			sectionOfPosition.put(i + 1, section);
		}
		return list.toArray(new String[list.size()]);
	}

	@Override
	public Filter getFilter()
	{
		if (myFilter == null) {
			myFilter = new MyFilter(userList);
		}
		return myFilter;
	}

	protected class MyFilter extends Filter
	{
		List<String> mOriginalList = null;

		public MyFilter(List<String> myList)
		{
			this.mOriginalList = new ArrayList<String>(myList);
		}

		@Override
		protected synchronized FilterResults performFiltering(
				CharSequence prefix)
		{
			FilterResults results = new FilterResults();
			if (mOriginalList == null) {
				mOriginalList = new ArrayList<>();
			}

			if (prefix == null || prefix.length() == 0) {
				results.values = copyUserList;
				results.count = copyUserList.size();
			} else {
				String prefixString = prefix.toString();
				final int count = mOriginalList.size();
				final ArrayList<String> newValues = new ArrayList<>();
				for (int i = 0; i < count; i++) {
					String username = mOriginalList.get(i);

					if (username.startsWith(prefixString)) {
						newValues.add(username);
					} else {
						final String[] words = username.split(" ");
						final int wordCount = words.length;

						// Start at index 0, in case valueText starts with
						// space(s)
						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(username);
								break;
							}
						}
					}
				}
				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected synchronized void publishResults(CharSequence constraint,
				FilterResults results)
		{
			userList.clear();
			userList.addAll((List<String>) results.values);
			if (results.count > 0) {
				notiyfyByFilter = true;
				notifyDataSetChanged();
				notiyfyByFilter = false;
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

	@Override
	public void notifyDataSetChanged()
	{
		super.notifyDataSetChanged();
		if (!notiyfyByFilter) {
			copyUserList.clear();
			copyUserList.addAll(userList);
		}
	}

	protected int primaryColor;
	protected int primarySize;
	protected Drawable initialLetterBg;
	protected int initialLetterColor;

	public IndexedListViewAdapter setPrimaryColor(int primaryColor)
	{
		this.primaryColor = primaryColor;
		return this;
	}

	public IndexedListViewAdapter setPrimarySize(int primarySize)
	{
		this.primarySize = primarySize;
		return this;
	}

	public IndexedListViewAdapter setInitialLetterBg(Drawable initialLetterBg)
	{
		this.initialLetterBg = initialLetterBg;
		return this;
	}

	public IndexedListViewAdapter setInitialLetterColor(int initialLetterColor)
	{
		this.initialLetterColor = initialLetterColor;
		return this;
	}

}
