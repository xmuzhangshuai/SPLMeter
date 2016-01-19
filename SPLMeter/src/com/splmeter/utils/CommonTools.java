package com.splmeter.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.citisense.splmeter.R;

/**   
*    
* 项目名称：splmeter   
* 类名称：CommonTools   
* 类描述：   通用的工具类
* 创建人：张帅  
* 创建时间：2013-12-22 下午7:26:29   
* 修改人：张帅    
* 修改时间：2013-12-22 下午7:26:29   
* 修改备注：   
* @version    
*    
*/
public class CommonTools {

	/**
	* 判断程序是否在运行
	* @param context
	* @return
	*/
	//	public static boolean isAppRunning(Context context) {
	//		boolean isAppRunning = false;
	//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	//		List<RunningTaskInfo> list = am.getRunningTasks(100);
	//		for (RunningTaskInfo info : list) {
	//			if (info.topActivity.getPackageName().equals(Constants.PACKAGENAME)
	//					&& info.baseActivity.getPackageName().equals(Constants.PACKAGENAME)) {
	//				isAppRunning = true;
	//				//find it, break 
	//				break;
	//			}
	//		}
	//		return isAppRunning;
	//	}

	/**
	 * 根据声压值返回评级
	 * @param context
	 * @return
	 */
	public static String getLeverBySPL(float spl, Context context) {
		String level = "";
		String[] levels = context.getResources().getStringArray(R.array.levelGroup);
		if (spl < 50) {
			level = levels[0];
		} else if (spl < 55) {
			level = levels[1];
		} else if (spl < 60) {
			level = levels[2];
		} else if (spl < 65) {
			level = levels[3];
		} else {
			level = levels[4];
		}
		return level;
	}

	/**
	 * 判断系统语言是否为中文
	 * @param context
	 * @return
	 */
	public static boolean isZh(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getCountry();
		if (language.equals("CN"))
			return true;
		else
			return false;
	}

	/**
	 * 获取android当前可用内存大小
	 * @param context
	 * @return
	 */
	public static String getAvailMemory(Context context) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		//mi.availMem; 当前系统的可用内存 

		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化 
	}

	/**
	 * 获取总内存大小
	 * @return
	 */
	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件 
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小 

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				LogTool.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte 
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化 
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	/**
	 * 短暂显示Toast消息
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShortToast(Context context, String message) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(message);
		Toast toast = new Toast(context);
		toast.setDuration(2000);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 300);
		toast.setView(view);
		toast.show();
	}

	/**
	 * 获取IMEI
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return TelephonyMgr.getDeviceId();
	}

	/**
	 *获取手机型号
	 * @return
	 */
	public static String getPhoneType() {
		return android.os.Build.MODEL;
	}

	/**
	 *获取是否插入耳机状态
	 * @return
	 */
	public static boolean getEarPhone(Context context) {
		AudioManager localAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return localAudioManager.isWiredHeadsetOn();
	}

	/**
	 * 验证密码是否符合要求
	 * 以字母开头，长度在6~18之间，只能包含字符、数字和下划线
	 * @return
	 */
	public static boolean isPassValid(String pass) {
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{6,18}$");
		Matcher matcher = pattern.matcher(pass);
		return matcher.matches();
	}

}
