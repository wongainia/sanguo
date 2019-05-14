package com.vikings.sanguo.ui.map.core;

import android.graphics.Point;

public interface Projection {

	/**
     * Create a new GeoPoint from pixel coordinates relative to the top-left of
     * the MapView that provided this PixelConverter.
     */
    GeoPoint fromPixels(int x, int y);

    /**
     * Converts a distance in meters (along the equator) to one in (horizontal)
     * pixels at the current zoomlevel.
     */
    float metersToEquatorPixels(float meters);

    /**
     * Converts the given GeoPoint to onscreen pixel coordinates, relative to
     * the top-left of the MapView that provided this Projection.
     */
    Point toPixels(GeoPoint in, Point out);
	
}
