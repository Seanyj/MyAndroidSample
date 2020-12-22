package com.seanyj.androidlib.map;

import java.util.List;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.seanyj.androidlib.utils.TextMarkerUtil;

public class MapUtil
{
	public static final float DEFUALT_LOCATE_SCALE = 15.0F;
	public static final float DEFUALT_SCALE = 14.8F;
	public static final double EXTEND_LATLNG = 0.02D;
	public static final boolean IS_OVERLOOKING = false;
	public static final boolean IS_SHOW_COMPASS = false;
	public static final boolean IS_SHOW_ROOM_CONTROLS = false;
	public static final float MAP_DRAW_SCALE = 15.0F;
	public static final float SHOW_NON_SCALE = 14.5F;

	public static Marker AddMarker(Context paramContext,
			BaiduMap paramBaiduMap, List<BitmapDescriptor> paramList,
			String paramString, LatLng paramLatLng, int paramInt)
	{
		BitmapDescriptor localBitmapDescriptor = BitmapDescriptorFactory
				.fromBitmap(TextMarkerUtil.getTextMarker(paramContext,
						formTitle(paramString, paramInt)));
		if (paramList != null)
			paramList.add(localBitmapDescriptor);
		return (Marker) paramBaiduMap.addOverlay(new MarkerOptions().icon(
				localBitmapDescriptor).position(paramLatLng));
	}

	private static String formTitle(String name, int cnt)
	{
		return name + ": " + cnt;
	}

	public static void initMap(MapView paramMapView,
			LocationClient paramLocationClient, LatLng latLng)
	{
		if (paramMapView == null) {
			return;
		}

		BaiduMap localBaiduMap;

		localBaiduMap = paramMapView.getMap();
		UiSettings localUiSettings = localBaiduMap.getUiSettings();
		localUiSettings.setCompassEnabled(false);
		localUiSettings.setOverlookingGesturesEnabled(false);
		paramMapView.showZoomControls(false);
		localBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14.8F));
		LatLng localLatLng = latLng;

		if (localLatLng != null) {
			localBaiduMap.setMapStatus(MapStatusUpdateFactory
					.newLatLng(localLatLng));
		}

		if (paramLocationClient != null) {
			localBaiduMap.setMyLocationEnabled(true);
			localBaiduMap
					.setMyLocationConfigeration(new MyLocationConfiguration(
							MyLocationConfiguration.LocationMode.NORMAL, false,
							null));
			LocationClientOption localLocationClientOption = new LocationClientOption();
			localLocationClientOption.setOpenGps(true);
			localLocationClientOption.setCoorType("bd09ll");
			localLocationClientOption.setScanSpan(1000);
			paramLocationClient.setLocOption(localLocationClientOption);
		}
	}

	public static void moveToLocated(MapView paramMapView, LatLng paramLatLng)
	{
		if ((paramMapView == null) || (paramLatLng == null))
			return;
		BaiduMap localBaiduMap = paramMapView.getMap();
		localBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.0F));
		localBaiduMap.animateMapStatus(MapStatusUpdateFactory
				.newLatLng(paramLatLng));
	}
}
