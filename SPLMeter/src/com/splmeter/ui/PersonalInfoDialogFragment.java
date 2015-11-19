package com.splmeter.ui;

import com.smallrhino.splmeter.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainActivity = (MainActivity) getActivity();
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
		return rootView;
	}

	private void findViewById() {
		lastBtn = (Button) rootView.findViewById(R.id.last_btn);
		nextBtn = (Button) rootView.findViewById(R.id.next_btn);
		ageSpinner = (Spinner) rootView.findViewById(R.id.ageSpinner);
	}

	private void initView() {
		lastBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

		ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.ageGroup, android.R.layout.simple_spinner_item);
		ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ageSpinner.setAdapter(ageAdapter);
		ageSpinner.setSelection(2, true);
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
			this.dismiss();
			break;
		default:
			break;
		}
	}
}
