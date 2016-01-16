package com.splmeter.utils;

import java.math.BigDecimal;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * @description:位置工具类，获取经纬度和海拔
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月28日 上午10:26:19
 */
public class LocationTool {
	private Context context;
	LocationManager locationManager;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private double altitude = 0.0;
	private double accuracy = 0.0;

	public LocationTool(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double getAltitude() {
		return altitude;
	}

	LocationListener locationListener = new LocationListener() {

		// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		// Provider被enable时触发此函数，比如GPS被打开
		@Override
		public void onProviderEnabled(String provider) {

		}

		// Provider被disable时触发此函数，比如GPS被关闭 
		@Override
		public void onProviderDisabled(String provider) {

		}

		//当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发 
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				latitude = location.getLatitude(); //经度   
				longitude = location.getLongitude(); //纬度
				altitude = location.getAltitude();//海拔
				BigDecimal b = new BigDecimal(altitude);
				altitude = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				accuracy = location.getAccuracy();
			}
		}
	};

	public void stopWatching() {
		if (locationListener != null && locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}

	}

	private void init() {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location;
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
		} else {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
		}
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			altitude = location.getAltitude();
			BigDecimal b = new BigDecimal(altitude);
			altitude = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			accuracy = location.getAccuracy();
		}
	}
}
