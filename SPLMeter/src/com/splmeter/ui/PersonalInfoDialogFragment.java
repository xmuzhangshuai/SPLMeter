package com.splmeter.ui;

import org.apache.http.Header;

import com.loopj.android.http.TextHttpResponseHandler;
import com.smallrhino.splmeter.R;
import com.splmeter.base.BaseApplication;
import com.splmeter.customewidget.MyAlertDialog;
import com.splmeter.utils.AsyncHttpClientTool;
import com.splmeter.utils.CommonTools;
import com.splmeter.utils.DateTimeTools;
import com.splmeter.utils.LocationTool;
import com.splmeter.utils.LogTool;
import com.splmeter.utils.SharePreferenceUtil;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

/**
 * @description:个人信息对话框
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 下午9:33:57
 */
public class PersonalInfoDialogFragment extends DialogFragment implements OnClickListener {
	private View rootView;

	private MainActivity mainActivity;
	private Button lastBtn;
	private Button nextBtn;
	private Spinner ageSpinner;
	private RadioButton radioMale;
	private RadioButton radioFemale;
	private SharePreferenceUtil sharePreferenceUtil;
	LocationTool locationTool;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainActivity = (MainActivity) getActivity();
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
		locationTool = new LocationTool(mainActivity);
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dialog_personalinfo, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		findViewById();
		initView();
		this.setCancelable(false);
		return rootView;
	}

	private void findViewById() {
		lastBtn = (Button) rootView.findViewById(R.id.last_btn);
		nextBtn = (Button) rootView.findViewById(R.id.next_btn);
		ageSpinner = (Spinner) rootView.findViewById(R.id.ageSpinner);
		radioMale = (RadioButton) rootView.findViewById(R.id.radio_male);
		radioFemale = (RadioButton) rootView.findViewById(R.id.radio_female);
	}

	private void initView() {
		lastBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

		ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.ageGroup, android.R.layout.simple_spinner_item);
		ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ageSpinner.setAdapter(ageAdapter);
		ageSpinner.setSelection(sharePreferenceUtil.getAgeGroup(), true);

		if (sharePreferenceUtil.getGender() == 1) {
			radioMale.setChecked(true);
		} else {
			radioFemale.setChecked(true);
		}
	}

	private void saveData() {
		int gender = radioMale.isChecked() ? 1 : 0;
		int age = ageSpinner.getSelectedItemPosition() + 1;
		sharePreferenceUtil.setAgeGroup(age - 1);
		sharePreferenceUtil.setGender(gender);
		MainActivity.resultParams.put("gender", gender);
		MainActivity.resultParams.put("age", age);
	}

	/**
	 * 提示对话框
	 */
//	private void showSuccessDialog() {
//		final MyAlertDialog myAlertDialog = new MyAlertDialog(mainActivity);
//		myAlertDialog.setTitle(mainActivity.getResources().getString(R.string.infoTitle));
//		myAlertDialog.setMessage(mainActivity.getResources().getString(R.string.share_success));
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
//		myAlertDialog.setPositiveButton(mainActivity.getResources().getString(R.string.confirm), comfirm);
//		myAlertDialog.setNegativeButton(mainActivity.getResources().getString(R.string.cancel), cancle);
//		myAlertDialog.show();
//	}

	/**
	 * 上传数据
	 */
	private void uploadData() {
		if (sharePreferenceUtil.getAutoShare()) {
			MainActivity.resultParams.put("IMEI", CommonTools.getIMEI(mainActivity));
			MainActivity.resultParams.put("modelType", CommonTools.getPhoneType());
			mainActivity.saveData();
			LogTool.i("--------" + MainActivity.resultParams.toString());
			final ProgressDialog dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getActivity().getResources().getString(R.string.shareing));

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog.show();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					// TODO Auto-generated method stub
					LogTool.i(statusCode + "===" + response);
					MainActivity.shareFlag = 2;
//					showSuccessDialog();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("ReportSPLValue服务器错误" + errorResponse);
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
				}
			};
			AsyncHttpClientTool.post("ReportSPLValue", MainActivity.resultParams, responseHandler);
		} else {
			MainActivity.shareFlag = 1;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.last_btn:
			this.dismiss();
			mainActivity.next_last(2);
			break;
		case R.id.next_btn:
			mainActivity.stopSave();
			saveData();
			uploadData();
			this.dismiss();
			break;
		default:
			break;
		}
	}
}
