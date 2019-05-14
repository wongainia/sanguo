package com.vikings.sanguo.ui.map.core;

import android.graphics.Canvas;

public interface Overlay {

	public void draw(Canvas canvas, MapView mapView);

	public boolean onTap(GeoPoint arg0, MapView mapView);

}
