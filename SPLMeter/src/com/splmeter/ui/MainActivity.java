package com.splmeter.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.loopj.android.http.RequestParams;
import com.smallrhino.splmeter.R;
import com.splmeter.analysis.AudioProcess;
import com.splmeter.analysis.FFTSplCal;
import com.splmeter.analysis.SPLBo;
import com.splmeter.base.AppManager;
import com.splmeter.base.BaseActivity;
import com.splmeter.base.BaseApplication;
import com.splmeter.config.Constants;
import com.splmeter.config.Constants.RecordValue;
import com.splmeter.utils.CommonTools;
import com.splmeter.utils.ServerUtils;
import com.splmeter.utils.SharePreferenceUtil;
import com.umeng.update.UmengUpdateAgent;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @description:主页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 上午9:23:27
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	private Button settingBtn;
	private Button startBtn;
	private Button shareBtn;
	private int flag = 0;
	private TextView currentValue;
	private FFTSplCal fftCal;
	private RecordAudio recordTask;
	private ImageView seekBarLevelDrawable;
	private ImageView seekBarLevelThumb;
	private TextView levelTextView;
	private TextView fsLabel;
	private TextView doorLabel;
	private TextView tips;
	private float seekBarLevelDrawableWidth;
	private float seekBarLevelMinValue = 45;//噪音范围最小值
	private float seekBarLevelBlock = 25;
	private float seekBarLevelThumbIntial;
	private int currentLevel = 0;
	private String[] levels;
	private SharePreferenceUtil sharePreferenceUtil;

	private LinearLayout abscissaLayout;//横坐标
	private LinearLayout ordinateLayout;//纵坐标
	//	private String[] abscissaArray = new String[] { "20", "50", "100", "200", "500", "1K", "5K", "10K", "20K" };
	private List<String> abscissaArray = new ArrayList<>();
	private String[] ordinateArray = new String[] { "100", "80", "60", "40", "20", "0" };

	private List<Map<String, Float>> basicFrequencyList;//频谱图内容
	public static RequestParams resultParams;//最终上传的结果
	public static int shareFlag = 0;//0为未测试，1为测试过，2为已经分享成功
	AudioManager mAudioManager;
	boolean isExit;

	SurfaceView sfv; //绘图所用
	AudioProcess audioProcess = new AudioProcess();//处理
	static final int yMax = 50;//Y轴缩小比例最大值  
	static final int yMin = 1;//Y轴缩小比例最小值  
	private int uploadMaxsize = 100;//上传主频对的最大数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//从网络获取数据并存储到本地
		new ServerUtils(MainActivity.this).initData();
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
		basicFrequencyList = new ArrayList<>();

		//友盟更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);

		resultParams = new RequestParams();

		findViewById();
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//室内室外
		if (sharePreferenceUtil.getInOutDoor() == 0) {
			doorLabel.setText(getResources().getString(R.string.outdoor));
		} else {
			doorLabel.setText(getResources().getString(R.string.indoor));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		settingBtn = (Button) findViewById(R.id.setting_btn);
		startBtn = (Button) findViewById(R.id.start_btn);
		shareBtn = (Button) findViewById(R.id.share_btn);
		currentValue = (TextView) findViewById(R.id.current_value);
		seekBarLevelDrawable = (ImageView) findViewById(R.id.seekbar_level_drawable);
		seekBarLevelThumb = (ImageView) findViewById(R.id.seekbar_level_thumb);
		levelTextView = (TextView) findViewById(R.id.status);
		fsLabel = (TextView) findViewById(R.id.fs_label);
		abscissaLayout = (LinearLayout) findViewById(R.id.abscissa);
		ordinateLayout = (LinearLayout) findViewById(R.id.ordinate);
		doorLabel = (TextView) findViewById(R.id.in_out_door);
		tips = (TextView) findViewById(R.id.participants);
		sfv = (SurfaceView) this.findViewById(R.id.SurfaceView);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		settingBtn.setOnClickListener(this);
		startBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);

		//初始化显示
		audioProcess.initDraw(yMax / 2, sfv.getHeight(), this, Constants.RecordValue.FREQUENCY);

		ViewTreeObserver vto = seekBarLevelDrawable.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				seekBarLevelDrawable.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				seekBarLevelDrawableWidth = seekBarLevelDrawable.getWidth();
				seekBarLevelThumbIntial = seekBarLevelThumb.getX() - seekBarLevelThumb.getWidth() / 2;
			}
		});

		/************控制系统音量大小，防止出现噪音*************/
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 2, 0);
		mAudioManager.setSpeakerphoneOn(false);

		levels = getResources().getStringArray(R.array.levelGroup);

		//将频谱View添加到布局

		//横坐标和纵坐标
		initCoordinate();

		tips.setText(CommonTools.isZh(this) ? sharePreferenceUtil.getMainLabelTextCN() : sharePreferenceUtil.getMainLabelTextEN());
	}

	/**
	 * 按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			CommonTools.showShortToast(getBaseContext(), "再按一次退出程序");
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			// close();
			AppManager.getInstance().AppExit(getApplicationContext());
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}
	};

	/**
	 * 横坐标和纵坐标
	 */
	private void initCoordinate() {
		abscissaArray.add("0");
		for (int index = 64; index <= 512; index = index + 64) {
			String str = String.valueOf(Constants.RecordValue.FREQUENCY / 1024 * index);
			abscissaArray.add(str);
		}
		for (String abs : abscissaArray) {
			TextView textView = new TextView(this);
			textView.setTextColor(Color.argb(255, 7, 251, 251));
			textView.setTextSize(10);
			textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
			textView.setText(abs);
			abscissaLayout.addView(textView);
		}
		for (String odr : ordinateArray) {
			TextView textView = new TextView(this);
			textView.setText(odr);
			textView.setTextColor(Color.argb(255, 7, 251, 251));
			textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
			textView.setTextSize(10);
			ordinateLayout.addView(textView);
		}
	}

	/**
	 * 显示声源辨析对话框
	 */
	void showSoundSourceDialog() {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("sound_source_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		SoundSourceDialogFragment newFragment = new SoundSourceDialogFragment();
		newFragment.show(ft, "sound_source_dialog");
	}

	/**
	 * 显示主观评价对话框
	 */
	void showSubjectiveDialog() {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("subjective_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		SubjectiveDialogFragment newFragment = new SubjectiveDialogFragment();
		newFragment.show(ft, "subjective_dialog");
	}

	/**
	* 显示个人信息对话框
	*/
	void showPersonalInfoDialog() {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("personal_info_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		PersonalInfoDialogFragment newFragment = new PersonalInfoDialogFragment();
		newFragment.show(ft, "personal_info_dialog");
	}

	/**
	* 显示分享对话框
	*/
	void showShareDialog() {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("share_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		ShareDialogFragment newFragment = new ShareDialogFragment();
		newFragment.show(ft, "share_dialog");
	}

	/**
	 * 获取实时值
	 * @return
	 */
	public float getCurrentValue() {
		float a = 0;
		try {
			a = Float.parseFloat(currentValue.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return a;
	}

	/**
	 * 下一步或上一步
	 */
	public void next_last(int flag) {
		switch (flag) {
		case 1:
			showSoundSourceDialog();
			break;
		case 2:
			showSubjectiveDialog();
			break;
		case 3:
			showPersonalInfoDialog();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setting_btn:
			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.start_btn:
			flag++;
			if (flag % 2 == 0) {//评价
				startBtn.setBackgroundResource(R.drawable.sel_btn);
				startBtn.setText(R.string.on);
				next_last(1);
				try {
					JSONArray jsonArray = new JSONArray(basicFrequencyList);
					MainActivity.resultParams.put("mainEmaxLpaPair", jsonArray.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {//开始
				basicFrequencyList.clear();
				startBtn.setBackgroundResource(R.drawable.sel_btn_checked);
				startBtn.setText(R.string.evaluate);
				recordTask = new RecordAudio();
				recordTask.execute();
			}

			break;
		case R.id.share_btn:
			showShareDialog();
			break;

		default:
			break;
		}
	}

	/**
	 * RecordAudio继承异步任务类，实现获取声音得到缓存声频
	 * @author lzjing
	 *
	 */
	private class RecordAudio extends AsyncTask<Void, float[], Void> {
		Map<String, Float> map;

		@Override
		protected Void doInBackground(Void... params) {
			try {
				float[] transform;
				int bufferSize = AudioRecord.getMinBufferSize(RecordValue.FREQUENCY, RecordValue.CHANNELCONFIGURATION, RecordValue.AUDIOENCODING);
				/*
				 * 关于采样的注释： audioSource音频源，此参数的值为MIC
				 * sampleRateInHz采样率，此处根据需求改为44100 channelConfig声道设置 audioFormat
				 * 编码制式和采样大小 bufferSizeInBytes 采集数据需要的缓冲区的大小
				 */
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RecordValue.FREQUENCY, RecordValue.CHANNELCONFIGURATION, RecordValue.AUDIOENCODING,
						bufferSize);

				audioProcess.baseLine = sfv.getHeight() - 15;
				audioProcess.frequence = Constants.RecordValue.FREQUENCY;
				audioProcess.start(audioRecord, bufferSize, sfv);

				//新建一个数组用于缓存声音
				short[] buffer = new short[RecordValue.BLOCKSIZE];
				//				float[] toTransform = new float[RecordValue.BLOCKSIZE];
				fftCal = new FFTSplCal();
				while ((flag % 2) != 0) {
					//将声音信息读取到缓存中
					int bufferReadResult = audioRecord.read(buffer, 0, RecordValue.BLOCKSIZE);

					fftCal.transBuffer(bufferReadResult, buffer);
					transform = fftCal.toTransform;
					publishProgress(transform);
				}
				//停止并且释放声音设备
				audioRecord.stop();
				audioRecord.release();
			} catch (Throwable t) {
				Log.e("AudioRecord", "Recording Failed" + t.toString());
			}
			return null;
		}

		protected void onProgressUpdate(float[]... transform) {
			SPLBo splBo = new SPLBo();
			splBo = fftCal.getSPL();
			double splValue = splBo.getSPLValue();
			float calibrateSPLValue = fftCal.getDoubleCalibrateSPL(splValue, sharePreferenceUtil.getCalibration());
			currentValue.setText("" + calibrateSPLValue);

			double maxSPL = splBo.getMaxSPL();
			double maxFrequency = splBo.getMaxFrequency();
			//主频以及对应的HZ
			fsLabel.setText(MainActivity.this.getResources().getString(R.string.basic_frequency) + "：" + fftCal.getMaxSudBA(maxSPL)
					+ MainActivity.this.getResources().getString(R.string.dBCaption) + "（" + fftCal.getMaxSudHz(maxFrequency)
					+ MainActivity.this.getResources().getString(R.string.hz) + "）");
			//记录主频
			map = new HashMap<>();
			map.put("maxLpa", fftCal.getDoubleMaxSudBA(maxSPL));
			map.put("mainE", fftCal.getDoubleMaxSudHz(maxFrequency));
			if (basicFrequencyList.size() < uploadMaxsize) {
				basicFrequencyList.add(map);
			} else {
				basicFrequencyList.remove(0);
				basicFrequencyList.add(map);
			}

			currentLevel = (int) ((calibrateSPLValue - seekBarLevelMinValue) / 5);//当前层级
			if (currentLevel > 4) {
				currentLevel = 4;
			} else if (currentLevel < 0) {
				currentLevel = 0;
			}
			levelTextView.setText(levels[currentLevel]);

			float ratio = (calibrateSPLValue - seekBarLevelMinValue) / seekBarLevelBlock;
			if (ratio < 0) {
				ratio = 0;
			} else if (ratio > 1) {
				ratio = 1;
			}

			//初始化  
			seekBarLevelThumb.setX(seekBarLevelThumbIntial + ratio * seekBarLevelDrawableWidth);
		}
	}

}
