package com.splmeter.ui;

import java.util.Locale;

import com.smallrhino.splmeter.R;
import com.splmeter.base.BaseActivity;
import com.splmeter.base.BaseApplication;
import com.splmeter.utils.SharePreferenceUtil;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * @description:设置页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 上午9:24:06
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	private Button cancelBtn;
	private Button confirmBtn;
	private Spinner modeSpinner;
	private Spinner langSpinner;
	private View helpView;
	private View checkUpdateView;
	private View aboutView;
	private SharePreferenceUtil sharePreferenceUtil;
	private int in_out_door;
	private String language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		modeSpinner = (Spinner) findViewById(R.id.modeSpinner);
		langSpinner = (Spinner) findViewById(R.id.langSpinner);
		helpView = findViewById(R.id.help);
		checkUpdateView = findViewById(R.id.check_update);
		aboutView = findViewById(R.id.about);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		helpView.setOnClickListener(this);
		checkUpdateView.setOnClickListener(this);
		aboutView.setOnClickListener(this);
		in_out_door = sharePreferenceUtil.getInOutDoor();
		language = sharePreferenceUtil.getLanguage();

		/************设置户内户外**********/
		ArrayAdapter<CharSequence> modeAadapter = ArrayAdapter.createFromResource(this, R.array.modeGroup, android.R.layout.simple_spinner_item);
		modeAadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		modeSpinner.setAdapter(modeAadapter);
		modeSpinner.setSelection(sharePreferenceUtil.getInOutDoor(), true);
		modeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				in_out_door = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		/********设置语言**********/
		ArrayAdapter<CharSequence> langAadapter = ArrayAdapter.createFromResource(this, R.array.langGroup, android.R.layout.simple_spinner_item);
		langAadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		langSpinner.setAdapter(langAadapter);
		String sta = getResources().getConfiguration().locale.getCountry();
		if (sta.equals("zh")) {
			langSpinner.setSelection(0, true);
		} else {
			langSpinner.setSelection(1, true);
		}

	}

	/** 
	 * 刷新语言 
	 */
	public void updateActivity(String sta) {

		// 本地语言设置  
		Locale myLocale = new Locale(sta);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		this.recreate();
		//		startActivity((new Intent(this, TabActivity.class)));
		//		this.finish();
	}

	private void validateSetting() {
		sharePreferenceUtil.setInOutDoor(in_out_door);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancel_btn:
			finish();
			break;
		case R.id.confirm_btn:
			validateSetting();
			finish();
			break;
		case R.id.about:
			Intent intent = new Intent(SettingActivity.this, WebActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;

		default:
			break;
		}
	}
}
