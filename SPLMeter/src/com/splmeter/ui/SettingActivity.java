package com.splmeter.ui;

import com.smallrhino.splmeter.R;
import com.smallrhino.splmeter.R.id;
import com.splmeter.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
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

		ArrayAdapter<CharSequence> modeAadapter = ArrayAdapter.createFromResource(this, R.array.modeGroup, android.R.layout.simple_spinner_item);
		modeAadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		modeSpinner.setAdapter(modeAadapter);

		ArrayAdapter<CharSequence> langAadapter = ArrayAdapter.createFromResource(this, R.array.langGroup, android.R.layout.simple_spinner_item);
		langAadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		langSpinner.setAdapter(langAadapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancel_btn:
			finish();
			break;
		case R.id.confirm_btn:
			finish();
			break;

		default:
			break;
		}
	}
}
