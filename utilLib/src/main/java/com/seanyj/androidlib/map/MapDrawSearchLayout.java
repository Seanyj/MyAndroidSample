package com.seanyj.androidlib.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.seanyj.androidlib.R;

public class MapDrawSearchLayout
{
	private BaiduMap baiduMap;
	public BitmapDescriptor bmDescriptor;
	private Context context;
	private Overlay drawOverlay;
	private DrawSearchCallback drawSearchCallback;
	private GroundOverlayOptions localGroundOverlayOptions;
	private Projection localProjection;
	private MapDrawSearchView mapDrawSearchView;

	private MapDrawSearchView.MapDrawSearchViewCallback mapDrawSearchViewCallback = new MapDrawSearchView.MapDrawSearchViewCallback() {
		public void onDrawEnd(Path paramPath)
		{
			MapDrawSearchLayout.this.endDraw(true);
		}
	};
	private MapView mapView;

	public MapDrawSearchLayout(Context paramContext, View paramView)
	{
		init(paramContext, paramView);
	}

	private void endDrawSearchView()
	{
		System.gc();
		LatLng localLatLng1 = this.localProjection
				.fromScreenLocation(new Point(0, 0));
		LatLng localLatLng2 = this.localProjection
				.fromScreenLocation(new Point(
						this.mapDrawSearchView.getWidth(),
						this.mapDrawSearchView.getHeight()));
		LatLngBounds localLatLngBounds = new LatLngBounds.Builder()
				.include(localLatLng2).include(localLatLng1).build();
		this.bmDescriptor = BitmapDescriptorFactory
				.fromBitmap(this.mapDrawSearchView.getPathBitmap());
		this.localGroundOverlayOptions = new GroundOverlayOptions()
				.positionFromBounds(localLatLngBounds).image(this.bmDescriptor)
				.transparency(0.8F);
		this.drawOverlay = this.baiduMap
				.addOverlay(this.localGroundOverlayOptions);
		if (this.drawSearchCallback == null)
			return;
		this.drawSearchCallback.onDrawEnd(localLatLng1.longitude,
				localLatLng1.latitude, localLatLng2.longitude,
				localLatLng2.latitude);
	}

	private void init(Context paramContext, View paramView)
	{
		this.context = paramContext;
		this.mapDrawSearchView = ((MapDrawSearchView) paramView
				.findViewById(R.id.map_draw_search_view));
		this.mapView = ((MapView) paramView.findViewById(R.id.bmapView));
		this.baiduMap = this.mapView.getMap();
		setListener();
	}

	private Point latlng2point(LatLng paramLatLng)
	{
		return this.localProjection.toScreenLocation(paramLatLng);
	}

	private void setListener()
	{
		this.mapDrawSearchView
				.setMapDrawSearchViewCallback(this.mapDrawSearchViewCallback);
	}

	private void startDrawSearchView()
	{
		clear();
		this.baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(15.0F));
		this.localProjection = this.baiduMap.getProjection();
	}

	public void clear()
	{
		this.mapDrawSearchView.clear();
		if (this.baiduMap != null)
			this.baiduMap.clear();
		if (this.bmDescriptor != null) {
			this.bmDescriptor.recycle();
			this.bmDescriptor = null;
		}
		if (this.drawOverlay != null) {
			this.drawOverlay.remove();
			this.drawOverlay = null;
		}
		if (this.localGroundOverlayOptions != null) {
//			this.localGroundOverlayOptions.image(BitmapDescriptorFactory
//					.fromResource(0));
			this.localGroundOverlayOptions = null;
		}
		if (this.mapDrawSearchView.getPathBitmap() != null) {
			this.mapDrawSearchView.getPathBitmap().recycle();
			this.mapDrawSearchView.setPathBitmap(null);
		}
		System.gc();
	}

	public void clearForDestroy()
	{
		this.mapDrawSearchView.clear();
		if (this.bmDescriptor != null) {
			this.bmDescriptor.recycle();
			this.bmDescriptor = null;
		}
		if (this.mapDrawSearchView.getPathBitmap() != null) {
			this.mapDrawSearchView.getPathBitmap().recycle();
			this.mapDrawSearchView.setPathBitmap(null);
		}
		System.gc();
	}

	public void endDraw(boolean paramBoolean)
	{
		if (paramBoolean)
			endDrawSearchView();
		this.mapDrawSearchView.setVisibility(View.GONE);
	}

	public BaiduMap getBaiduMap()
	{
		return this.baiduMap;
	}

	public Overlay getDrawOverlay()
	{
		return this.drawOverlay;
	}

	public DrawSearchCallback getDrawSearchCallback()
	{
		return this.drawSearchCallback;
	}

	public MapView getMapView()
	{
		return this.mapView;
	}

	public int[] screeningPoints(List<LatLng> list)
			throws IllegalArgumentException
	{
		if (list == null) {
			throw new IllegalArgumentException("parameter can not be null");
		}

		List<LatLng> outList = new ArrayList<LatLng>();
		int[] screened = new int[list.size()];
		Arrays.fill(screened, -1);

		for (int i = 0; i < list.size(); i++) {
			Point localPoint = latlng2point(list.get(i));
			if ((localPoint.x < 0)
					|| (localPoint.x >= this.mapDrawSearchView.getWidth())
					|| (localPoint.y < 0)
					|| (localPoint.y >= this.mapDrawSearchView.getHeight())) {
				outList.add(list.get(i));
				screened[i] = 1;
				continue;
			}

			if (!this.mapDrawSearchView.isPointInRange(localPoint)) {
				outList.add(list.get(i));
				screened[i] = 1;
			}
		}

		list.removeAll(outList);
		return screened;
	}

	public void setBaiduMap(BaiduMap paramBaiduMap)
	{
		this.baiduMap = paramBaiduMap;
	}

	public void setDrawOverlay(Overlay paramOverlay)
	{
		this.drawOverlay = paramOverlay;
	}

	public void setDrawSearchCallback(DrawSearchCallback paramDrawSearchCallback)
	{
		this.drawSearchCallback = paramDrawSearchCallback;
	}

	public void startDraw()
	{
		startDrawSearchView();
		this.mapDrawSearchView.setVisibility(View.VISIBLE);
	}

	public static abstract interface DrawSearchCallback
	{
		public abstract void onDrawEnd(double topLeftLng, double topLeftLat,
				double bottomRightLng, double bottomRightLat);
	}
}