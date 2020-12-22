package com.seanyj.androidlib.utils;

import android.content.Context;

public class SizeUtil
{
	public static int dip2px(Context context, float dip)
	{
		return (int) (0.5F + dip
				* context.getResources().getDisplayMetrics().density);
	}

	public static float getDensity(Context context)
	{
		return context.getResources().getDisplayMetrics().density;
	}

	public static int getScreenHeight(Context context)
	{
		return (int) (0.5F + context.getResources().getDisplayMetrics().heightPixels);
	}

	public static int getScreenWidth(Context context)
	{
		return (int) (0.5F + context.getResources().getDisplayMetrics().widthPixels);
	}

	public static int px2dip(Context context, float px)
	{
		return (int) (0.5F + px
				/ context.getResources().getDisplayMetrics().density);
	}
}
