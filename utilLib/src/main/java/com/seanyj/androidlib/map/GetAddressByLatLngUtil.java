package com.seanyj.androidlib.map;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class GetAddressByLatLngUtil
{
	public static void searchAddress(final Context context, Double lat,
			Double lng, final GetAddressListener getAddressListener)
	{
		if ((lat == null) || (lng == null))
			return;

		GeoCoder geoCoder = GeoCoder.newInstance();
		LatLng latLng = new LatLng(lat.doubleValue(), lng.doubleValue());

		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			public void onGetGeoCodeResult(GeoCodeResult geoCodeResult)
			{
			}

			public void onGetReverseGeoCodeResult(
					ReverseGeoCodeResult reverseGeoCodeResult)
			{
				if ((reverseGeoCodeResult == null)
						|| (reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR)) {
					Toast.makeText(context, "错误", Toast.LENGTH_SHORT).show();
					return;
				}
				getAddressListener.getResult(reverseGeoCodeResult.getAddress());
			}
		});

		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
	}

	public static abstract interface GetAddressListener
	{
		public abstract void getResult(String paramString);
	}
}
