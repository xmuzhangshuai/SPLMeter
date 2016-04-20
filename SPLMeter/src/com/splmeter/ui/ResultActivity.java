package com.splmeter.ui;

import com.splmeter.base.BaseActivity;
import com.splmeter.base.BaseApplication;
import com.splmeter.utils.SharePreferenceUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.citisense.splmeter.R;

/**
 * @description:结果页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 上午9:24:06
 */
public class ResultActivity extends BaseActivity implements OnClickListener {
	private Button cancelBtn;
	private Button confirmBtn;
	private TextView queitRateTextView;
	private TextView countTextView;

	private SharePreferenceUtil sharePreferenceUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		queitRateTextView = (TextView) findViewById(R.id.queitRateTextView);
		countTextView = (TextView) findViewById(R.id.countTextView);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);

	}

	private void validateSetting() {
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

		default:
			break;
		}
	}
}
