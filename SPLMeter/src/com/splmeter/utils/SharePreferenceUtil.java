package com.splmeter.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 类名称：SharePreferenceUtil 
 * 类描述：SharedPreferences的一个工具类，调用setParam就能保存String,
 * Integer, Boolean, Float, Long类型的参�?同样调用getParam就能获取到保存在手机里面的数�?
 * 创建人： 张帅
 * 创建时间�?015�?�?3�?上午9:00:37
 * 
 */
public class SharePreferenceUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	public static final String USE_COUNT = "count";// 记录软件使用次数
	public static final String CONFIG = "config";//评价信息

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// 记录软件使用次数
	public int getUseCount() {
		return sp.getInt("count", 0);
	}

	public void setUseCount(int count) {
		editor.putInt("count", count);
		editor.commit();
	}

	// 性别
	public String getGender() {
		return sp.getString("gender", "");
	}

	public void setGender(String u_gender) {
		editor.putString("gender", u_gender);
		editor.commit();
	}

	// 年龄段
	public String getAgeGroup() {
		return sp.getString("ageGroup", "");
	}

	public void setAgeGroup(String ageGroup) {
		editor.putString("ageGroup", ageGroup);
		editor.commit();
	}

	// 室内，室外
	public String getInOutDoor() {
		return sp.getString("in_out_door", "");
	}

	public void setInOutDoor(String in_out_door) {
		editor.putString("in_out_door", in_out_door);
		editor.commit();
	}

	// 校准
	public int getCalibration() {
		return sp.getInt("Calibration", 0);
	}

	public void setCalibration(int calibration) {
		editor.putInt("Calibration", calibration);
		editor.commit();
	}

	// 是否分享给科学家
	public boolean getAutoShare() {
		return sp.getBoolean("autoshare", true);
	}

	public void setAutoShare(boolean isAutoShare) {
		editor.putBoolean("autoshare", isAutoShare);
		editor.commit();
	}
}
