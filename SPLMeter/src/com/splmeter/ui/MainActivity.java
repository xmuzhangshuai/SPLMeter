package com.splmeter.ui;

import com.smallrhino.splmeter.R;
import com.splmeter.analysis.FFTSplCal;
import com.splmeter.base.BaseActivity;
import com.splmeter.config.Constants.RecordValue;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
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
	private float seekBarLevelDrawableWidth;
	private float seekBarLevelMinValue = 45;//噪音范围最小值
	private float seekBarLevelMaxValue = 70;//噪音范围最大值
	private float seekBarLevelBlock = 25;
	private float seekBarLevelThumbIntial;
	private int currentLevel = 0;
	private String[] levels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById();
		initView();
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
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		settingBtn.setOnClickListener(this);
		startBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
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
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
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
			} else {//开始
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
		@Override
		protected Void doInBackground(Void... params) {
			try {
				float[] transform;
				int bufferSize = AudioRecord.getMinBufferSize(RecordValue.FREQUENCY, RecordValue.CHANNELCONFIGURATION, RecordValue.AUDIOENCODING);
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RecordValue.FREQUENCY, RecordValue.CHANNELCONFIGURATION, RecordValue.AUDIOENCODING,
						bufferSize);
				//启动声音
				audioRecord.startRecording();
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
				fftCal.getSPL();
				//停止并且释放声音设备
				audioRecord.stop();
				audioRecord.release();
			} catch (Throwable t) {
				Log.e("AudioRecord", "Recording Failed");
			}
			return null;
		}

		protected void onProgressUpdate(float[]... transform) {
			double splValue = fftCal.getSPL().getSPLValue();
			currentValue.setText(fftCal.getCalibrateSPL(splValue, RecordValue.CALIBRATEVALUE));

			float current = Float.parseFloat(currentValue.getText().toString());
			currentLevel = (int) ((current - seekBarLevelMinValue) / 5);//当前层级
			if (currentLevel > 4) {
				currentLevel = 4;
			} else if (currentLevel < 0) {
				currentLevel = 0;
			}
			levelTextView.setText(levels[currentLevel]);

			float ratio = (current - seekBarLevelMinValue) / seekBarLevelBlock;
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
