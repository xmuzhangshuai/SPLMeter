package com.splmeter.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;

import com.loopj.android.http.RequestParams;
import com.smallrhino.splmeter.R;
import com.splmeter.analysis.DrawProcess;
import com.splmeter.analysis.FFTSplCal;
import com.splmeter.analysis.SPLBo;
import com.splmeter.base.AppManager;
import com.splmeter.base.BaseActivity;
import com.splmeter.base.BaseApplication;
import com.splmeter.config.Constants;
import com.splmeter.config.Constants.RecordValue;
import com.splmeter.utils.CommonTools;
import com.splmeter.utils.DateTimeTools;
import com.splmeter.utils.LocationTool;
import com.splmeter.utils.ServerUtils;
import com.splmeter.utils.SharePreferenceUtil;
import com.umeng.update.UmengUpdateAgent;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
	//	private int flag = 0;
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
	private float seekBarLevelMinValue = 45;// 噪音范围最小值
	private float seekBarLevelBlock = 25;
	private float seekBarLevelThumbIntial;
	private int currentLevel = 0;
	private String[] levels;
	private SharePreferenceUtil sharePreferenceUtil;

	private LinearLayout abscissaLayout;// 横坐标
	private LinearLayout ordinateLayout;// 纵坐标
	private List<String> abscissaArray = new ArrayList<>();
	private String[] ordinateArray = new String[] { "120", "100", "80", "60", "40", "20" };

	public static RequestParams resultParams;// 最终上传的结果
	public static int shareFlag = 0;// 0为未测试，1为测试过，2为已经分享成功
	private int saveFlag = 0;// 为1是开始保存数据
	private int onFlag = 0;//0为停止监控，1为正在监控
	public static int currentPage = 1;//是否为当前页面
	public static int startOrEva = 0;//0为开始，1为评价
	AudioManager mAudioManager;
	boolean isExit;

	SurfaceView sfv; // 绘图所用
	DrawProcess drawProcess;// 处理
	private int uploadMaxsize = 100;// 上传主频对的最大数
	private LocationTool locationTool;
	private float maxLpa, mainFrenquency;
	private List<String> timeList;
	private List<Integer> earPhoneList;
	private List<Float> latitudeList, longtitudeList, accuracyList, altitudeList, splList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 从网络获取数据并存储到本地
		new ServerUtils(MainActivity.this).initData();
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
		timeList = new ArrayList<>();
		latitudeList = new ArrayList<>();
		longtitudeList = new ArrayList<>();
		accuracyList = new ArrayList<>();
		altitudeList = new ArrayList<>();
		splList = new ArrayList<>();
		earPhoneList = new ArrayList<>();
		locationTool = new LocationTool(MainActivity.this);

		// 友盟更新
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
		refresh();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopRecord();
		super.onDestroy();
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

		// 初始化显示
		drawProcess = new DrawProcess(sfv);

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

		levels = getResources().getStringArray(R.array.levelGroup);

		// 横坐标和纵坐标
		initCoordinate();

		tips.setText(CommonTools.isZh(this) ? sharePreferenceUtil.getMainLabelTextCN() : sharePreferenceUtil.getMainLabelTextEN());
	}

	public void refresh() {
		// 室内室外
		if (sharePreferenceUtil.getInOutDoor() == 0) {
			doorLabel.setText(getResources().getString(R.string.outdoor));
		} else {
			doorLabel.setText(getResources().getString(R.string.indoor));
		}

		if (shareFlag == 0) {
			shareBtn.setEnabled(false);
		} else {
			shareBtn.setEnabled(true);
		}
		currentPage = 1;
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
			CommonTools.showShortToast(getBaseContext(), CommonTools.isZh(MainActivity.this) ? "再按一次退出程序" : "Click again to exit the program");
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
		Paint dashPaint;
		dashPaint = new Paint();
		dashPaint.setStyle(Paint.Style.STROKE);
		dashPaint.setColor(Color.GRAY);

		abscissaArray.add("0");
		for (int index = 64; index <= 512; index = index + 64) {
			int value = Constants.RecordValue.FREQUENCY / 1024 * index;
			String str = "" + value / 1000 + "K";
			abscissaArray.add(str);
		}
		for (int i = 0; i < abscissaArray.size(); i++) {
			TextView textView = new TextView(this);
			textView.setTextColor(Color.argb(255, 7, 251, 251));
			textView.setTextSize(10);
			if (i == abscissaArray.size() - 1) {
				textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			} else {
				textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
				textView.setScrollX(15);
			}

			textView.setText(abscissaArray.get(i));
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
			currentPage = 0;
			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.start_btn:
			if (startOrEva == 1) {// 评价
				saveFlag = 1;
				next_last(1);
			} else {// 开始
				saveFlag = 0;
				onFlag = 1;
				startOrEva = 1;
				shareFlag = 0;
				shareBtn.setEnabled(false);
				timeList.clear();
				maxLpa = 0;
				mainFrenquency = 0;
				longtitudeList.clear();
				latitudeList.clear();
				altitudeList.clear();
				accuracyList.clear();
				earPhoneList.clear();
				splList.clear();
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
	 * 停止记录数据
	 */
	public void stopSave() {
		saveFlag = 0;
		timeList.clear();
		earPhoneList.clear();
		latitudeList.clear();
		longtitudeList.clear();
		accuracyList.clear();
		altitudeList.clear();
		splList.clear();
	}

	public void stopRecord() {
		saveFlag = 0;
		onFlag = 0;
	}

	/**
	 * 开始记录数据
	 */
	public void startSave(float maxLpa, float mainFrequence, float spl) {
		if (maxLpa > this.maxLpa) {
			this.maxLpa = maxLpa;
			this.mainFrenquency = mainFrequence;
		}

		// 记录时间
		if (timeList.size() < uploadMaxsize) {
			timeList.add(DateTimeTools.getCurrentDateTimeForString());
		} else {
			timeList.remove(0);
			timeList.add(DateTimeTools.getCurrentDateTimeForString());
		}

		// 记录位置
		if (longtitudeList.size() < uploadMaxsize) {
			longtitudeList.add((float) locationTool.getLongitude());
			latitudeList.add((float) locationTool.getLongitude());
			altitudeList.add((float) locationTool.getLongitude());
			accuracyList.add((float) locationTool.getAccuracy());
		} else {
			longtitudeList.remove(0);
			latitudeList.remove(0);
			altitudeList.remove(0);
			accuracyList.remove(0);
			longtitudeList.add((float) locationTool.getLongitude());
			latitudeList.add((float) locationTool.getLatitude());
			altitudeList.add((float) locationTool.getAltitude());
			accuracyList.add((float) locationTool.getAccuracy());
		}

		// 记录耳机状态
		if (earPhoneList.size() < uploadMaxsize) {
			earPhoneList.add(CommonTools.getEarPhone(this) ? 1 : 0);
		} else {
			earPhoneList.remove(0);
			earPhoneList.add(CommonTools.getEarPhone(this) ? 1 : 0);
		}

		// 记录声压级
		if (splList.size() < uploadMaxsize) {
			splList.add(spl);
		} else {
			splList.remove(0);
			splList.add(spl);
		}
	}

	public void saveData() {
		startOrEva = 0;
		startBtn.setBackgroundResource(R.drawable.sel_btn);
		startBtn.setText(R.string.on);

		JSONArray timeArray = new JSONArray(timeList);
		resultParams.put("time", timeArray.toString());
		JSONArray longtitudeArray = new JSONArray(longtitudeList);
		resultParams.put("lng", longtitudeArray.toString());
		JSONArray latitudeArray = new JSONArray(latitudeList);
		resultParams.put("lat", latitudeArray.toString());
		JSONArray altitudeArray = new JSONArray(altitudeList);
		resultParams.put("alt", altitudeArray.toString());
		JSONArray accuracyArray = new JSONArray(accuracyList);
		resultParams.put("acc", accuracyArray.toString());
		JSONArray earPhoneArray = new JSONArray(earPhoneList);
		resultParams.put("earphone", earPhoneArray.toString());
		JSONArray splArray = new JSONArray(splList);
		resultParams.put("spl", splArray.toString());

		Collections.sort(splList);
		Collections.reverse(splList);
		int length = splList.size();
		if (length > 0) {
			float L10 = splList.get(length / 10);
			float L50 = splList.get(length / 2);
			float L90 = splList.get(length * 9 / 10);
			float Laeq = (float) Math.round((L50 + (L10 - L90) * (L10 - L90) / 60.0) * 10) / 10;
			currentValue.setText("" + Laeq);//修改主界面值

			resultParams.put("L10", L10);
			resultParams.put("L50", L50);
			resultParams.put("L90", L90);
			resultParams.put("Laeq", Laeq);
		}

		resultParams.put("mainF", mainFrenquency);
		resultParams.put("maxLpa", maxLpa);
	}

	/**
	 * RecordAudio继承异步任务类，实现获取声音得到缓存声频
	 * @author lzjing
	 *
	 */
	private class RecordAudio extends AsyncTask<Void, short[], Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				int bufferSize = AudioRecord.getMinBufferSize(RecordValue.FREQUENCY, RecordValue.CHANNELCONFIGURATION, RecordValue.AUDIOENCODING);
				/*
				 * 关于采样的注释： audioSource音频源，此参数的值为MIC
				 * sampleRateInHz采样率，此处根据需求改为44100 channelConfig声道设置 audioFormat
				 * 编码制式和采样大小 bufferSizeInBytes 采集数据需要的缓冲区的大小
				 */
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RecordValue.FREQUENCY, RecordValue.CHANNELCONFIGURATION, RecordValue.AUDIOENCODING,
						bufferSize);
				audioRecord.startRecording();
				drawProcess.baseLine = sfv.getHeight();
				drawProcess.sfvWidth = sfv.getWidth();
				drawProcess.sfvHeight = sfv.getHeight();

				// 新建一个数组用于缓存声音
				short[] buffer = new short[RecordValue.BLOCKSIZE];
				fftCal = new FFTSplCal();
				while (onFlag == 1) {
					// 将声音信息读取到缓存中
					int bufferReadResult = audioRecord.read(buffer, 0, RecordValue.BLOCKSIZE);
					fftCal.transBuffer(bufferReadResult, buffer);

					short[] a = fftCal.getFrequencyAndSPL();

					publishProgress(a);
				}
				if (audioRecord != null) {
					// 停止并且释放声音设备
					audioRecord.stop();
					audioRecord.release();
				}
			} catch (Throwable t) {
				Log.e("AudioRecord", "Recording Failed" + t.toString());
			}
			return null;
		}

		protected void onProgressUpdate(short[]... transform) {
			if (currentPage == 1) {
				drawProcess.draw(transform[0]);

				SPLBo splBo = new SPLBo();
				splBo = fftCal.getSPL();
				double splValue = splBo.getSPLValue();
				float calibrateSPLValue = fftCal.getCalibrateSPLDouble(splValue, sharePreferenceUtil.getCalibration());
				currentValue.setText("" + calibrateSPLValue);

				double maxSPL = splBo.getMaxSPL();
				double maxFrequency = splBo.getMaxFrequency();
				// 主频以及对应的HZ
				fsLabel.setText(MainActivity.this.getResources().getString(R.string.basic_frequency) + ":" + fftCal.getMaxSudBA(maxSPL)
						+ MainActivity.this.getResources().getString(R.string.dBCaption) + "(" + fftCal.getMaxSudHz(maxFrequency)
						+ MainActivity.this.getResources().getString(R.string.hz) + ")");

				// 记录数据
				if (saveFlag == 1) {
					startSave(fftCal.getMaxSudBADouble(maxSPL), fftCal.getMaxSudHzDouble(maxFrequency), calibrateSPLValue);
				}

				currentLevel = (int) ((calibrateSPLValue - seekBarLevelMinValue) / 5);// 当前层级
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

				// 初始化
				seekBarLevelThumb.setX(seekBarLevelThumbIntial + ratio * seekBarLevelDrawableWidth);
			}
		}
	}

}
