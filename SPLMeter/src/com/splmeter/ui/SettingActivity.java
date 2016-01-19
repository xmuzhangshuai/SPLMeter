package com.splmeter.ui;

import com.splmeter.base.BaseActivity;
import com.splmeter.base.BaseApplication;
import com.splmeter.utils.SharePreferenceUtil;
import com.splmeter.utils.ToastTool;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.citisense.splmeter.R;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
	private View helpView;
	//	private View checkUpdateView;
	private View aboutView;
	//	private TextView versionTextView;
	private SharePreferenceUtil sharePreferenceUtil;
	private int in_out_door;
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
		modeSpinner = (Spinner) findViewById(R.id.modeSpinner);
		helpView = findViewById(R.id.help);
		//		checkUpdateView = findViewById(R.id.check_update);
		aboutView = findViewById(R.id.about);
		//		versionTextView = (TextView) findViewById(R.id.version);
		shareCheckBox = (CheckBox) findViewById(R.id.share_checkbox);
		clbEditText = (EditText) findViewById(R.id.clbTxt);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		helpView.setOnClickListener(this);
		//		checkUpdateView.setOnClickListener(this);
		aboutView.setOnClickListener(this);
		//		versionTextView.setText(getVersion());
		in_out_door = sharePreferenceUtil.getInOutDoor();

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
		sharePreferenceUtil.setInOutDoor(in_out_door);
		sharePreferenceUtil.setAutoShare(shareCheckBox.isChecked());
		try {
			sharePreferenceUtil.setCalibration(Integer.parseInt(clbEditText.getText().toString()));
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
		case R.id.check_update:
			/**********友盟自动更新组件**************/
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
						break;
					case UpdateStatus.No: // has no update
						ToastTool.showShort(SettingActivity.this, getResources().getString(R.string.newest));
						break;
					case UpdateStatus.NoneWifi: // none wifi
						ToastTool.showShort(SettingActivity.this, getResources().getString(R.string.no_wifi));
						break;
					case UpdateStatus.Timeout: // time out
						ToastTool.showShort(SettingActivity.this, getResources().getString(R.string.timeout));
						break;
					}
				}
			});
			UmengUpdateAgent.forceUpdate(SettingActivity.this);
			break;
		default:
			break;
		}
	}
}
