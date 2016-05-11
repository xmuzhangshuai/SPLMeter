package com.splmeter.ui;

import com.splmeter.base.BaseApplication;
import com.splmeter.dbservice.AsmtValueDbService;
import com.splmeter.utils.LocationTool;
import com.splmeter.utils.SharePreferenceUtil;

import android.app.Dialog;
import android.app.DialogFragment;
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
import cn.citisense.splmeter.R;

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
	private AsmtValueDbService asmtValueDbService;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainActivity = (MainActivity) getActivity();
		asmtValueDbService = AsmtValueDbService.getInstance(getActivity());
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

		ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.ageGroup,
				android.R.layout.simple_spinner_item);
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

		MainActivity.asmtValue.setAge(age);
		MainActivity.asmtValue.setGender(gender);
		asmtValueDbService.asmtValueDao.update(MainActivity.asmtValue);

		mainActivity.saveData();
		mainActivity.refresh();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.last_btn:
			this.dismiss();
			mainActivity.next_last(3);
			break;
		case R.id.next_btn:
			mainActivity.stopRecord();
			saveData();
			this.dismiss();
			break;
		default:
			break;
		}
	}
}
