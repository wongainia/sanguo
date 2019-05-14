package com.vikings.sanguo.ui.map.core;

public class GeoPoint {

	private int latitudeE6;
	private int longitudeE6;

	public GeoPoint(int latitudeE6, int longitudeE6) {
		this.latitudeE6 = latitudeE6;
		this.longitudeE6 = longitudeE6;
	}

	GeoPoint(double latitude, double longitude) {
		this((int) (latitude * 1e6), (int) (longitude * 1e6));
	}

	public int getLatitudeE6() {
		return latitudeE6;
	}

	public int getLongitudeE6() {
		return longitudeE6;
	}

	public void setLatitudeE6(int latitudeE6) {
		this.latitudeE6 = latitudeE6;
	}
	
	public void setLongitudeE6(int longitudeE6) {
		this.longitudeE6 = longitudeE6;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + latitudeE6;
		result = prime * result + longitudeE6;
		return result;
	}

	@Override
	public String toString() {
		return "GeoPoint: Latitude: " + latitudeE6 + ", Longitude: "
				+ longitudeE6;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoPoint other = (GeoPoint) obj;
		if (latitudeE6 != other.latitudeE6)
			return false;
		if (longitudeE6 != other.longitudeE6)
			return false;
		return true;
	}

}
