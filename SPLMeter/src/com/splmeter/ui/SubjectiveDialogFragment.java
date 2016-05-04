package com.splmeter.ui;

import com.splmeter.dbservice.AsmtValueDbService;

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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.citisense.splmeter.R;

/**
 * @description:主观评价对话框
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 下午9:16:11
 */
public class SubjectiveDialogFragment extends DialogFragment implements OnClickListener, OnSeekBarChangeListener {
	private View rootView;

	private MainActivity mainActivity;
	private Button lastBtn;
	private Button nextBtn;
	private SeekBar seekBarSoundsize;
	private SeekBar seekBarComfortlevel;
	private SeekBar seekBarHarmony;
	private TextView sound_levelText, comfort_levelText, coordinated_levelText;
	private String[] soundLevles, comfortLevels, coordiantedLevels;
	private AsmtValueDbService asmtValueDbService;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainActivity = (MainActivity) getActivity();
		soundLevles = mainActivity.getResources().getStringArray(R.array.sound_levelGroup);
		comfortLevels = mainActivity.getResources().getStringArray(R.array.acoustic_comfort_levelGroup);
		coordiantedLevels = mainActivity.getResources().getStringArray(R.array.coordinated_levelGroup);
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
		seekBarSoundsize = (SeekBar) rootView.findViewById(R.id.seekBar_soundsize);
		seekBarComfortlevel = (SeekBar) rootView.findViewById(R.id.seekBar_comfortlevel);
		seekBarHarmony = (SeekBar) rootView.findViewById(R.id.seekBar_harmony);
		sound_levelText = (TextView) rootView.findViewById(R.id.sound_level);
		comfort_levelText = (TextView) rootView.findViewById(R.id.comfort_level);
		coordinated_levelText = (TextView) rootView.findViewById(R.id.coordinated_level);
	}

	private void initView() {
		lastBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		seekBarSoundsize.setOnSeekBarChangeListener(this);
		seekBarComfortlevel.setOnSeekBarChangeListener(this);
		seekBarHarmony.setOnSeekBarChangeListener(this);

		sound_levelText.setText(mainActivity.getResources().getString(R.string.sound_level) + soundLevles[2]);
		comfort_levelText.setText(mainActivity.getResources().getString(R.string.acoustic_comfort_level) + comfortLevels[2]);
		coordinated_levelText.setText(mainActivity.getResources().getString(R.string.coordinated_level) + coordiantedLevels[2]);
	}

	private void saveResult() {
//		MainActivity.resultParams.put("asmt",
//				"" + (seekBarSoundsize.getProgress() / 10 - 2) + "," + (seekBarComfortlevel.getProgress() / 10 - 2) + "," + (seekBarHarmony.getProgress() / 10 - 2));
		MainActivity.asmtValue
				.setAsmt("" + (seekBarSoundsize.getProgress() / 10 - 2) + "," + (seekBarComfortlevel.getProgress() / 10 - 2) + "," + (seekBarHarmony.getProgress() / 10 - 2));
		asmtValueDbService.asmtValueDao.update(MainActivity.asmtValue);
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		int p = progress / 10;
		nextBtn.setEnabled(true);
		if (seekBar == seekBarSoundsize) {
			sound_levelText.setText(mainActivity.getResources().getString(R.string.sound_level) + soundLevles[p]);
		} else if (seekBar == seekBarComfortlevel) {
			comfort_levelText.setText(mainActivity.getResources().getString(R.string.acoustic_comfort_level) + comfortLevels[p]);
		} else if (seekBar == seekBarHarmony) {
			coordinated_levelText.setText(mainActivity.getResources().getString(R.string.coordinated_level) + coordiantedLevels[p]);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}
}
