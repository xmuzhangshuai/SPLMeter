package com.splmeter.config;

import android.media.AudioFormat;

public class Constants {

	// 是否处于开发模式
	public static final boolean DEVELOPER_MODE = true;
	// 域名或者是IP
	public static String AppliactionServerIP = "http://120.25.84.123/Noise/GetTips/";
	//
	public static String AppliactionServerDomain = "http://120.25.84.123/Noise/GetTips/";

	public static class WeChatConfig {
		public final static String API_KEY = "wx33f81b034c7f3e04";
		public final static String SECRIT_KEY = "b003237ea4d4d1df9089c97feef5ff76";
	}

	public static class RecordValue {
		public static final int CHANNELCONFIGURATION = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		public static final int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;
		//采样率
		public static final int FREQUENCY = 44100;
		public static final int BLOCKSIZE = 2048;
		//该值应该从配置文件中取
		public static final int CALIBRATEVALUE = 0;
	}
}
