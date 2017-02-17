package com.splmeter.ui;

import com.splmeter.dbservice.AsmtValueDbService;
import com.splmeter.utils.LogTool;

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
import android.widget.Spinner;
import android.widget.TextView;
import cn.citisense.splmeter.R;

/**
 * @description:主观评价对话框
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 下午9:16:11
 */
public class SubjectiveDialogFragment extends DialogFragment implements OnClickListener {
	private View rootView;

	private MainActivity mainActivity;
	private Button lastBtn;
	private Button nextBtn;
	private Spinner soundSizeSpinner;
	private Spinner comfortlevelSpinner;
	private Spinner harmonySpinner;
	private TextView sound_levelText, comfort_levelText, coordinated_levelText;
	private AsmtValueDbService asmtValueDbService;

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
		asmtValueDbService = AsmtValueDbService.getInstance(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dialog_subjective, container, false);
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
		soundSizeSpinner = (Spinner) rootView.findViewById(R.id.seekBar_soundsize);
		comfortlevelSpinner = (Spinner) rootView.findViewById(R.id.seekBar_comfortlevel);
		harmonySpinner = (Spinner) rootView.findViewById(R.id.seekBar_harmony);
		sound_levelText = (TextView) rootView.findViewById(R.id.sound_level);
		comfort_levelText = (TextView) rootView.findViewById(R.id.comfort_level);
		coordinated_levelText = (TextView) rootView.findViewById(R.id.coordinated_level);
	}

	private void initView() {
		lastBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

		ArrayAdapter<CharSequence> soundsizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sound_levelGroup,
				android.R.layout.simple_spinner_item);
		soundsizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		soundSizeSpinner.setAdapter(soundsizeAdapter);
		soundSizeSpinner.setSelection(0, true);

		ArrayAdapter<CharSequence> comfortAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.acoustic_comfort_levelGroup,
				android.R.layout.simple_spinner_item);
		comfortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		comfortlevelSpinner.setAdapter(comfortAdapter);
		comfortlevelSpinner.setSelection(0, true);

		ArrayAdapter<CharSequence> harmonyAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.coordinated_levelGroup,
				android.R.layout.simple_spinner_item);
		harmonyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		harmonySpinner.setAdapter(harmonyAdapter);
		harmonySpinner.setSelection(0, true);

		sound_levelText.setText(mainActivity.getResources().getString(R.string.sound_level));
		comfort_levelText.setText(mainActivity.getResources().getString(R.string.acoustic_comfort_level));
		coordinated_levelText.setText(mainActivity.getResources().getString(R.string.coordinated_level));
	}

	private void saveResult() {
		if (soundSizeSpinner.getSelectedItemPosition() == 0 && comfortlevelSpinner.getSelectedItemPosition() == 0
				&& harmonySpinner.getSelectedItemPosition() == 0) {
			MainActivity.asmtValue.setAsmt(null);
		} else {
			int s1 = soundSizeSpinner.getSelectedItemPosition() - 3;
			int s2 = comfortlevelSpinner.getSelectedItemPosition() - 3;
			int s3 = harmonySpinner.getSelectedItemPosition() - 3;
			String ss1 = "";
			String ss2 = "";
			String ss3 = "";
			if (s1 != -3) {
				ss1 = ss1 + s1;
			}
			if (s2 != -3) {
				ss2 = ss2 + s2;
			}
			if (s3 != -3) {
				ss3 = ss3 + s3;
			}
			MainActivity.asmtValue.setAsmt("" + ss1 + "," + ss2 + "," + ss3);
			asmtValueDbService.asmtValueDao.update(MainActivity.asmtValue);
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
			this.dismiss();
			saveResult();
			mainActivity.next_last(4);
			break;
		default:
			break;
		}
	}
}
