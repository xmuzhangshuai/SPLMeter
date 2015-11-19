package com.splmeter.ui;

import com.smallrhino.splmeter.R;
import com.splmeter.base.BaseActivity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		settingBtn.setOnClickListener(this);
		startBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
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
			startBtn.setBackgroundResource(R.drawable.sel_btn_checked);
			startBtn.setText(R.string.evaluate);
			showSoundSourceDialog();
			break;
		case R.id.share_btn:

			break;

		default:
			break;
		}
	}

}
