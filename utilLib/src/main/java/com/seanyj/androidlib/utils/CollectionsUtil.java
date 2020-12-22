package com.seanyj.androidlib.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionsUtil
{
	public static final void sortForIndex(List<String> list)
	{
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs)
			{
				String lhsStartLetter = WordsUtil.getStartLetter(lhs);
				String rhsStartLetter = WordsUtil.getStartLetter(rhs);

				if (lhsStartLetter.equals(rhsStartLetter)) {
					return lhs.compareTo(rhs);
				}

				if ("#".equals(lhsStartLetter)) {
					return 1;
				}

				if ("#".equals(rhsStartLetter)) {
					return -1;
				}

				return lhsStartLetter.compareTo(rhsStartLetter);
			}
		});
	}

}
