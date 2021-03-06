package com.splmeter.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.splmeter.analysis.DrawProcess;
import com.splmeter.analysis.FFTSplCal;
import com.splmeter.analysis.SPLBo;
import com.splmeter.base.BaseActivity;
import com.splmeter.base.BaseApplication;
import com.splmeter.config.Constants;
import com.splmeter.config.Constants.RecordValue;
import com.splmeter.dbservice.AsmtValueDbService;
import com.splmeter.dbservice.SplValueService;
import com.splmeter.entities.AsmtValue;
import com.splmeter.entities.SPLValue;
import com.splmeter.jsonobject.JsonAsmtValue;
import com.splmeter.utils.AsyncHttpClientTool;
import com.splmeter.utils.CommonTools;
import com.splmeter.utils.DateTimeTools;
import com.splmeter.utils.FastJsonTool;
import com.splmeter.utils.LocationTool;
import com.splmeter.utils.LogTool;
import com.splmeter.utils.ServerUtils;
import com.splmeter.utils.SharePreferenceUtil;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import cn.citisense.splmeter.R;

/**
 * @description:主页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 上午9:23:27
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	private Button settingBtn;
	private Button startBtn;
	private Button resultBtn;
	private TextView currentValue;
	private FFTSplCal fftCal;
	private RecordAudio recordTask;
	private ImageView seekBarLevelDrawable;
	private ImageView seekBarLevelThumb;
	private TextView levelTextView;
	private TextView fsLabel;
	private TextView doorLabel;
	private TextView tips;
	private TextView resultValueLabel;
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

	private int saveFlag = 0;// 为1是开始保存数据
	private int onFlag = 0;//0为停止监控，1为正在监控
	public static int currentPage = 1;//是否为当前页面
	public static int startOrEva = 0;//0为开始，1为评价
	AudioManager mAudioManager;
	boolean isExit;

	SurfaceView sfv; // 绘图所用
	DrawProcess drawProcess;// 处理
	private Date lastTime;
	private LocationTool locationTool;
	private float mLpa, mF;
	private List<Float> splList;

	private AsmtValueDbService asmtValueDbService;
	private SplValueService splValueService;
	public static AsmtValue asmtValue;
	public SPLValue splValue;

	private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
		String SYSTEM_REASON = "reason";
		String SYSTEM_HOME_KEY = "homekey";
		String SYSTEM_HOME_KEY_LONG = "recentapps";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_REASON);
				if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
					currentPage = 0;
				} else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
					//表示长按home键,显示最近使用的程序列表  
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();

		// 从网络获取数据并存储到本地
		new ServerUtils(MainActivity.this).initData();
		asmtValueDbService = AsmtValueDbService.getInstance(MainActivity.this);
		uploadData();//上传数据

		splList = new ArrayList<>();
		locationTool = new LocationTool(MainActivity.this);

		splValueService = SplValueService.getInstance(MainActivity.this);

		registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

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
		resultBtn = (Button) findViewById(R.id.result_btn);
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
		resultValueLabel = (TextView) findViewById(R.id.result_value_label);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		settingBtn.setOnClickListener(this);
		startBtn.setOnClickListener(this);
		resultBtn.setOnClickListener(this);

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
		getTips();
		lastTime = DateTimeTools.getCurrentDate();
	}

	public void refresh() {
		// 室内室外
		if (sharePreferenceUtil.getInOutDoor() == 0) {
			doorLabel.setText(getResources().getString(R.string.outdoor));
		} else {
			doorLabel.setText(getResources().getString(R.string.indoor));
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
			try {
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				android.os.Process.killProcess(android.os.Process.myPid());
			} catch (Exception e) {
				// TODO: handle exception
			}
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
	 * 显示所在地对话框
	 */
	void showModeDialog() {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("mode_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		ModeDialogFragment newFragment = new ModeDialogFragment();
		newFragment.show(ft, "mode_dialog");
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
			showModeDialog();
			break;
		case 2:
			showSoundSourceDialog();
			break;
		case 3:
			showSubjectiveDialog();
			break;
		case 4:
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
				//				saveFlag = 1;
				next_last(1);
			} else {// 开始

				asmtValue = new AsmtValue();
				asmtValue.setImei(CommonTools.getIMEI(MainActivity.this));
				asmtValue.setModeltype(CommonTools.getPhoneType());
				asmtValue.setCalb(sharePreferenceUtil.getCalibration());
				asmtValue.setPost(0);

				asmtValue.setId(asmtValueDbService.insert(asmtValue));

				saveFlag = 1;
				onFlag = 1;
				startOrEva = 1;
				resultValueLabel.setText("");
				mLpa = 0;
				mF = 0;
				splList.clear();
				startBtn.setBackgroundResource(R.drawable.sel_btn_checked);
				startBtn.setText(R.string.evaluate);
				recordTask = new RecordAudio();
				recordTask.execute();
			}

			break;
		case R.id.result_btn:
			Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
			startActivity(resultIntent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;

		default:
			break;
		}
	}

	/**
	 * 停止记录数据
	 */
	public void stopRecord() {
		saveFlag = 0;
		onFlag = 0;
	}

	/**
	 * 开始记录数据
	 */
	public void saveSPLValue(float maxLpa, float mainFrequence, float spl) {
		if (DateTimeTools.getIntervalForSecond(lastTime) < 1) {
		} else {
			if (spl > 0) {
				splValue = new SPLValue();
				splValue.setAsmt_id(asmtValue.getId());

				// 记录时间
				lastTime = DateTimeTools.getCurrentDate();
				splValue.setTime(lastTime);

				// 记录耳机状态
				splValue.setEarphone(CommonTools.getEarPhone(this) ? 1 : 0);

				// 记录位置
				splValue.setLng((float) locationTool.getLongitude());
				splValue.setLat((float) locationTool.getLatitude());
				splValue.setAlt((float) locationTool.getAltitude());
				splValue.setAcc((float) locationTool.getAccuracy());

				// 记录声压级
				splList.add(spl);
				splValue.setSpl(spl);

				//记录mainF、maxLpa
				splValue.setMainF(mainFrequence);
				splValue.setMaxLpa(maxLpa);
				if (maxLpa > this.mLpa) {
					this.mLpa = maxLpa;
					this.mF = mainFrequence;
				}
				asmtValue.setMF(mF);
				asmtValue.setMLpa(mLpa);
				asmtValue.setLstTime(DateTimeTools.DateToString(lastTime));
				asmtValue.setUtc(DateTimeTools.getUTCTime(lastTime));
				String timeZoneString = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
				asmtValue.setTimeZone(timeZoneString.substring(3, timeZoneString.length()));
				asmtValueDbService.update(asmtValue);

				splValueService.insert(splValue);
			}
		}
	}

	public void saveData() {
		startOrEva = 0;
		startBtn.setBackgroundResource(R.drawable.sel_btn);
		startBtn.setText(R.string.on);

		Collections.sort(splList);
		Collections.reverse(splList);
		int length = splList.size();
		if (length > 0) {
			float L10 = splList.get(length / 10);
			float L50 = splList.get(length / 2);
			float L90 = splList.get(length * 9 / 10);
			float Laeq = (float) Math.round((L50 + (L10 - L90) * (L10 - L90) / 60.0) * 10) / 10;
			currentValue.setText("" + Laeq);//修改主界面值
			resultValueLabel.setText(getResources().getString(R.string.result_value));

			currentLevel = (int) ((Laeq - seekBarLevelMinValue) / 5);// 当前层级
			if (currentLevel > 4) {
				currentLevel = 4;
			} else if (currentLevel < 0) {
				currentLevel = 0;
			}
			levelTextView.setText(levels[currentLevel]);

			float ratio = (Laeq - seekBarLevelMinValue) / seekBarLevelBlock;
			if (ratio < 0) {
				ratio = 0;
			} else if (ratio > 1) {
				ratio = 1;
			}

			// 初始化
			seekBarLevelThumb.setX(seekBarLevelThumbIntial + ratio * seekBarLevelDrawableWidth);

			MainActivity.asmtValue.setL10(L10);
			MainActivity.asmtValue.setL50(L50);
			MainActivity.asmtValue.setL90(L90);
			MainActivity.asmtValue.setLaeq(Laeq);
			asmtValueDbService.update(asmtValue);

			uploadData();
		}
	}

	/**
	 * 上传数据
	 */
	public void uploadData() {
		if (sharePreferenceUtil.getAutoShare()) {
			final List<AsmtValue> asmtValueList = asmtValueDbService.asmtValueDao.loadAll();
			if (asmtValueList != null && asmtValueList.size() > 0) {
				for (AsmtValue asmtValue : asmtValueList) {
					if (asmtValue.getPost() == 0) {
						if (asmtValue.getL10() != null && asmtValue.getL10() > 0) {

							JsonAsmtValue jsonAsmtValue = new JsonAsmtValue(asmtValue);

							RequestParams params = new RequestParams();
							params.put("data", FastJsonTool.createJsonString(jsonAsmtValue));
							LogTool.e(FastJsonTool.createJsonString(jsonAsmtValue));

							TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {

								@Override
								public void onSuccess(int statusCode, Header[] headers, String response) {
									// TODO Auto-generated method stub
									LogTool.i(statusCode + "uploadData===" + response);
									JSONObject j1;
									try {
										j1 = new JSONObject(response);
										String data = j1.getString("data");
										if (data != null && data.equals("success")) {
											for (AsmtValue asmtValue2 : asmtValueList) {
												asmtValue2.setPost(1);
												asmtValueDbService.update(asmtValue2);
											}
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

								@Override
								public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
									// TODO Auto-generated method stub
									LogTool.e("上传数据服务器错误" + errorResponse);

								}
							};
							AsyncHttpClientTool.post("?m=Home&a=ReportResultValue", params, responseHandler);
						}
					}
				}
			}

		}
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
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RecordValue.FREQUENCY, RecordValue.CHANNELCONFIGURATION,
						RecordValue.AUDIOENCODING, bufferSize);
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
				if (onFlag == 1 && calibrateSPLValue > 0) {
					currentValue.setText("" + calibrateSPLValue);
				}

				double maxSPL = splBo.getMaxSPL();
				double maxFrequency = splBo.getMaxFrequency();
				// 主频以及对应的HZ
				fsLabel.setText(MainActivity.this.getResources().getString(R.string.basic_frequency) + ":" + fftCal.getMaxSudBA(maxSPL)
						+ MainActivity.this.getResources().getString(R.string.dBCaption) + "(" + fftCal.getMaxSudHz(maxFrequency)
						+ MainActivity.this.getResources().getString(R.string.hz) + ")");

				// 记录数据
				if (saveFlag == 1) {
					saveSPLValue(fftCal.getMaxSudBADouble(maxSPL), fftCal.getMaxSudHzDouble(maxFrequency), calibrateSPLValue);
				}

				if (onFlag == 1) {
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
				currentLevel = (int) ((calibrateSPLValue - seekBarLevelMinValue) / 5);// 当前层级
			}
		}
	}

	/**
	 * 首页的提示
	 */
	public void getTips() {
		RequestParams params = new RequestParams();
		try {
			params.put("version_code", new ServerUtils(MainActivity.this).getVersionName());
			//			params.put("version_name", new ServerUtils(MainActivity.this).getVersionName());
			LogTool.e("版本号：" + new ServerUtils(MainActivity.this).getVersionCode() + "名字：" + new ServerUtils(MainActivity.this).getVersionName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				LogTool.i(statusCode + "===" + response);
				try {
					JSONObject j1 = new JSONObject(response);
					String data = j1.getString("data");
					JSONObject j2 = new JSONObject(data);
					String t_tiptxt_en = j2.getString("t_tiptxt_en");
					String t_tiptxt_cn = j2.getString("t_tiptxt_cn");
					sharePreferenceUtil.setMainLabelTextCN(t_tiptxt_cn);
					sharePreferenceUtil.setMainLabelTextEN(t_tiptxt_en);
					tips.setText(CommonTools.isZh(MainActivity.this) ? sharePreferenceUtil.getMainLabelTextCN()
							: sharePreferenceUtil.getMainLabelTextEN());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("getTips服务器错误" + errorResponse);

			}
		};
		AsyncHttpClientTool.post("?m=Home&a=GetTips", params, responseHandler);
	}
}
