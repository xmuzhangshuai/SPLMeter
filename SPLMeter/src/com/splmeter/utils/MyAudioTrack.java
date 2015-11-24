package com.splmeter.utils;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * @description:播放音频
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月23日 下午11:23:11
 */
public class MyAudioTrack {
	int mFrequency; // 采样率
	int mChannel; // 声道
	int mSampBit; // 采样精度
	AudioTrack mAudioTrack;

	public MyAudioTrack(int frequency, int channel, int sampbit) {
		mFrequency = frequency;
		mChannel = channel;
		mSampBit = sampbit;
	}

	public void init() {
		if (mAudioTrack != null) {
			release();
		}
		// 获得构建对象的最小缓冲区大小
		int minBufSize = AudioTrack.getMinBufferSize(mFrequency, mChannel, mSampBit);
		mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mFrequency, mChannel, mSampBit, minBufSize, AudioTrack.MODE_STREAM);
		mAudioTrack.play();
	}

	public void release() {
		if (mAudioTrack != null) {
			mAudioTrack.stop();
			mAudioTrack.release();
		}

	}

	public int getAudioSessionId() {
		return mAudioTrack.getAudioSessionId();
	}

	public void playAudioTrack(short[] data, int offset, int length) {
		if (data == null || data.length == 0) {
			return;
		}
		try {
			mAudioTrack.write(data, offset, length);
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("MyAudioTrack", "catch exception...");
		}
	}

	public int getPrimePlaySize() {
		int minBufSize = AudioTrack.getMinBufferSize(mFrequency, mChannel, mSampBit);
		return minBufSize * 2;
	}
}