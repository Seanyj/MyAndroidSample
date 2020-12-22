package com.seanyj.androidlib.utils;

import android.text.TextUtils;

import com.hyphenate.util.HanziToPinyin;
import com.hyphenate.util.HanziToPinyin.Token;

import java.util.ArrayList;

public class WordsUtil
{
	public static String getStartLetter(String name)
	{
		final String DefaultLetter = "#";
		String letter = DefaultLetter;

		if (TextUtils.isEmpty(name)) {
			return DefaultLetter;
		}

		char char0 = name.toLowerCase().charAt(0);
		if (Character.isDigit(char0)) {
			return DefaultLetter;
		}

		ArrayList<Token> l = HanziToPinyin.getInstance().get(
				name.substring(0, 1));
		if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
			Token token = l.get(0);
			letter = token.target.substring(0, 1);
			char c = letter.toLowerCase().charAt(0);
			if (c < 'a' || c > 'z') {
				return DefaultLetter;
			}
			return letter;
		}

		return DefaultLetter;
	}
}
