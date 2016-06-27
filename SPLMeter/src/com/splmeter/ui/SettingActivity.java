package com.splmeter.ui;

import com.splmeter.base.BaseActivity;
import com.splmeter.base.BaseApplication;
import com.splmeter.utils.SharePreferenceUtil;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.citisense.splmeter.R;

/**
 * @description:设置页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 上午9:24:06
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	private Button cancelBtn;
	private Button confirmBtn;
	private View helpView;
	private View aboutView;
	private SharePreferenceUtil sharePreferenceUtil;
	private CheckBox shareCheckBox;
	private EditText clbEditText;

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
		helpView = findViewById(R.id.help);
		aboutView = findViewById(R.id.about);
		shareCheckBox = (CheckBox) findViewById(R.id.share_checkbox);
		clbEditText = (EditText) findViewById(R.id.clbTxt);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		helpView.setOnClickListener(this);
		aboutView.setOnClickListener(this);

		shareCheckBox.setChecked(sharePreferenceUtil.getAutoShare());
		clbEditText.setText("" + sharePreferenceUtil.getCalibration());

		clbEditText.addTextChangedListener(new TextWatcher() {
			private boolean isChanged = false;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (isChanged) {// ----->如果字符未改变则返回      
					return;
				}
				String str = s.toString();

				isChanged = true;
				String cuttedStr = str;
				boolean flag = false;
				/* 删除字符串中的dot */
				for (int i = str.length() - 1; i >= 0; i--) {
					char c = str.charAt(i);
					if ('.' == c && i == str.length() - 3) {
						cuttedStr = str.substring(0, i + 2);
						if (cuttedStr.endsWith(".")) {
							cuttedStr = cuttedStr.substring(0, i + 1);
						}
						flag = true;
						break;
					}
				}
				if (flag) {
					clbEditText.setText(cuttedStr);
				}
				isChanged = false;
			}
		});
	}

	private void validateSetting() {
		sharePreferenceUtil.setAutoShare(shareCheckBox.isChecked());
		try {
			sharePreferenceUtil.setCalibration(Float.parseFloat(clbEditText.getText().toString()));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 提示对话框
	 */
	//	private void showAboutDialog() {
	//		final MyAlertDialog myAlertDialog = new MyAlertDialog(this);
	//		myAlertDialog.setTitle(this.getResources().getString(R.string.about));
	//		myAlertDialog.setMessage(getVersion());
	//		View.OnClickListener comfirm = new OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				myAlertDialog.dismiss();
	//			}
	//		};
	//		View.OnClickListener cancle = new OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				// TODO Auto-generated method stub
	//				myAlertDialog.dismiss();
	//			}
	//		};
	//		myAlertDialog.setPositiveButton(this.getResources().getString(R.string.confirm), comfirm);
	//		myAlertDialog.setNegativeButton(this.getResources().getString(R.string.cancel), cancle);
	//		myAlertDialog.show();
	//	}

	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			String version = info.versionName;
			return this.getString(R.string.version_name) + version;
		} catch (Exception e) {
			e.printStackTrace();
			return this.getString(R.string.can_not_find_version_name);
		}
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
			startActivity(new Intent(SettingActivity.this, WebActivity.class).putExtra("url", "http://www.citi-sense.cn/pss.html").putExtra("title", ""));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.help:
			startActivity(new Intent(SettingActivity.this, WebActivity.class).putExtra("url", "http://www.citi-sense.cn/splmeter_help.html").putExtra("title", ""));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		default:
			break;
		}
	}
}
