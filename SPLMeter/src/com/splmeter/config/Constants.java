package com.splmeter.config;

import android.media.AudioFormat;

public class Constants {

	// 是否处于开发模式
	public static final boolean DEVELOPER_MODE = true;
	//域名或者是IP
	//	public static String AppliactionServerDomain = "http://soundscape.citi-sense.cn/";
	public static String AppliactionServerDomain = "http://www.szlcgkj.com/Noise/";

	//下载地址
	public static String DownLoadPath = "http://www.citi-sense.cn/download/";

	public static class RecordValue {
		public static final int CHANNELCONFIGURATION = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		public static final int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;
		//采样率
		public static final int FREQUENCY = 44100;
		public static final int BLOCKSIZE = 2048;
		//校准函数中的参数值
		public static final double CALIBRATE_A = 1.26067;
		public static final double CALIBRATE_B = 82.00148;
	}
}
