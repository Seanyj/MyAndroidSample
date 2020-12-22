package com.seanyj.androidlib.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.seanyj.androidlib.R;
import com.seanyj.androidlib.map.MapDrawSearchLayout.DrawSearchCallback;

public class MapActivity extends Activity implements OnClickListener
{
	public static final double DEFAULT_LATITUDE = 31.3;
	public static final double DEFAULT_LONGITUDE = 120.6;
	public static final String COMMUNITY_NAME = "name";
	public static final String AVAILABLE_CNT = "count";
	public static final String LOCATION = "location";

	private MapView mMapView = null;
	private MapDrawSearchLayout mSearchLayout;
	private Context mContext;
	private MapDrawSearchLayout.DrawSearchCallback mDrawSearchCallback;
	private Button mBtnDraw;
	private Button mBtnLocate;
	private DrawStatus mDrawStatus = DrawStatus.NORMAL;
	private List<BitmapDescriptor> mDescriptors = new ArrayList<BitmapDescriptor>();
	private List<Map<String, Object>> mData = new ArrayList<>();
	private List<Map<String, Object>> mScreenedData = new ArrayList<Map<String, Object>>();

	private enum DrawStatus {
		NORMAL, PREPARE_DRAW, END_DRAW;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		mContext = this;

		initData();

		findviews();
		MapUtil.initMap(mMapView, null, new LatLng(DEFAULT_LATITUDE,
				DEFAULT_LONGITUDE));
		show(false);

		setListener();
	}

	private void initData()
	{
		Map<String, Object> map;

		map = new HashMap<String, Object>();
		map.put(COMMUNITY_NAME, "记得深刻理解花园");
		map.put(AVAILABLE_CNT, 8);
		LatLng latLng = new LatLng(DEFAULT_LATITUDE + 0.1,
				DEFAULT_LONGITUDE - 0.1);
		map.put(LOCATION, latLng);
		mData.add(map);

		map = new HashMap<String, Object>();
		map.put(COMMUNITY_NAME, "哈哈花园");
		map.put(AVAILABLE_CNT, 4);
		latLng = new LatLng(DEFAULT_LATITUDE - 0.1, DEFAULT_LONGITUDE + 0.1);
		map.put(LOCATION, latLng);
		mData.add(map);
	}

	private void findviews()
	{
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBtnDraw = (Button) findViewById(R.id.btn_draw);
		mBtnLocate = (Button) findViewById(R.id.btn_locate);
		mSearchLayout = new MapDrawSearchLayout(mContext,
				findViewById(R.id.search_layout));
	}

	private void setListener()
	{
		mDrawSearchCallback = new DrawSearchCallback() {
			@Override
			public void onDrawEnd(double topLeftLng, double topLeftLat,
					double bottomRightLng, double bottomRightLat)
			{
				screenAndShow(topLeftLng, topLeftLat, bottomRightLng,
						bottomRightLat);
				mDrawStatus = DrawStatus.END_DRAW;
				mBtnDraw.setText(R.string.map_clear_draw);
			}
		};

		mSearchLayout.setDrawSearchCallback(mDrawSearchCallback);
		mBtnDraw.setOnClickListener(this);
		mBtnLocate.setOnClickListener(this);
	}

	protected void drawMarkers()
	{
		if (mScreenedData == null || mScreenedData.size() == 0) {
			return;
		}

		for (int i = 0; i < mScreenedData.size(); i++) {
			Map<String, Object> item = mScreenedData.get(i);
			String name = (String) item.get(COMMUNITY_NAME);
			int cnt = (int) item.get(AVAILABLE_CNT);
			LatLng latLng = (LatLng) item.get(LOCATION);

			MapUtil.AddMarker(mContext, mMapView.getMap(), mDescriptors, name,
					latLng, cnt);
		}
	}

	private void clear()
	{
		if (mDescriptors != null) {
			for (BitmapDescriptor bDescriptor : mDescriptors) {
				bDescriptor.recycle();
			}
			mDescriptors.clear();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		clear();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public void onClick(View v)
	{
//		switch (v.getId()) {
//		case R.id.btn_draw:
//			if (mDrawStatus == DrawStatus.NORMAL) {
//				// not at drawing
//				mBtnDraw.setText(R.string.map_cancel_draw);
//				clear();
//				mSearchLayout.startDraw();
//				mDrawStatus = DrawStatus.PREPARE_DRAW;
//			} else if (mDrawStatus == DrawStatus.PREPARE_DRAW) {
//				// prepare but not begin drawing
//				mBtnDraw.setText(R.string.map_draw_search);
//				mSearchLayout.endDraw(false);
//				mDrawStatus = DrawStatus.NORMAL;
//				show(false);
//			} else {
//				// at the end of drawing
//				mBtnDraw.setText(R.string.map_draw_search);
//				mDrawStatus = DrawStatus.NORMAL;
//				mSearchLayout.clear();
//				clear();
//				show(false);
//			}
//			break;
//		case R.id.btn_locate:
//			break;
//		default:
//			break;
//		}
	}

	private void screenAndShow(double topLeftLng, double topLeftLat,
			double bottomRightLng, double bottomRightLat)
	{
		List<LatLng> latLngs = getLatLngsFromWeb(topLeftLat, topLeftLng,
				bottomRightLat, bottomRightLng);
		int[] screened = mSearchLayout.screeningPoints(latLngs);
		screenedData(screened);
		show(true);
	}

	private List<LatLng> getLatLngsFromWeb(double topLeftLat,
			double topLeftLng, double bottomRightLat, double bottomRightLng)
	{
		return getLatLngsFromData(mData);
	}

	private void show(boolean isScreen)
	{
		if (!isScreen) {
			mScreenedData = mData;
		}

		drawMarkers();
	}

	private void screenedData(int[] screened)
	{
		mScreenedData = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < mData.size(); i++) {
			if (screened[i] == -1) {
				mScreenedData.add(mData.get(i));
			}
		}
	}

	private List<LatLng> getLatLngsFromData(List<Map<String, Object>> data)
	{
		List<LatLng> latLngs = new ArrayList<LatLng>();

		for (int i = 0; i < data.size(); i++) {
			LatLng latLng = (LatLng) data.get(i).get(LOCATION);
			latLngs.add(latLng);
		}

		return latLngs;
	}
}
