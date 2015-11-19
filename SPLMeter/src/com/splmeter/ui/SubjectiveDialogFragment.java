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
import android.widget.Button;
import android.widget.SeekBar;
import u.aly.v;

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
	private SeekBar seekBarSoundsize;
	private SeekBar seekBarComfortlevel;
	private SeekBar seekBarHarmony;

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
		rootView = inflater.inflate(R.layout.fragment_dialog_subjective, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		findViewById();
		initView();
		return rootView;
	}

	private void findViewById() {
		lastBtn = (Button) rootView.findViewById(R.id.last_btn);
		nextBtn = (Button) rootView.findViewById(R.id.next_btn);
		seekBarSoundsize = (SeekBar) rootView.findViewById(R.id.seekBar_soundsize);
		seekBarComfortlevel = (SeekBar) rootView.findViewById(R.id.seekBar_comfortlevel);
		seekBarHarmony = (SeekBar) rootView.findViewById(R.id.seekBar_harmony);
	}

	private void initView() {
		lastBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.last_btn:
			this.dismiss();
			mainActivity.next_last(1);
			break;
		case R.id.next_btn:
			this.dismiss();
			mainActivity.next_last(3);
			break;
		default:
			break;
		}
	}
}
