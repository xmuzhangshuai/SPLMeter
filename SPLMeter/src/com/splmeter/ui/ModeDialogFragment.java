package com.splmeter.ui;

import java.util.ArrayList;
import java.util.List;

import com.splmeter.dbservice.AsmtValueDbService;
import com.splmeter.dbservice.ModeDbService;
import com.splmeter.entities.Mode;
import com.splmeter.utils.CommonTools;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import cn.citisense.splmeter.R;

/**
 * @description:所在地对话框
 * @company: smallrhino
 * @author：张帅
 * @date 2016年4月20日 上午9:55:15
 */
public class ModeDialogFragment extends DialogFragment implements OnClickListener {
	private View rootView;

	private MainActivity mainActivity;
	private Button lastBtn;
	private Button nextBtn;

	private ListView modeListView;
	private ModeDbService modeDbService;
	private AsmtValueDbService asmtValueDbService;
	private List<String> modeNameList;
	private List<Mode> modeList;

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
		modeDbService = ModeDbService.getInstance(getActivity());
		asmtValueDbService = AsmtValueDbService.getInstance(getActivity());
		modeNameList = new ArrayList<>();
		modeList = new ArrayList<>();
		modeList = modeDbService.getModeList();
		if (CommonTools.isZh(getActivity())) {
			for (Mode m : modeList) {
				modeNameList.add(m.getMode_name_cn());
			}
		} else {
			for (Mode m : modeList) {
				modeNameList.add(m.getMode_name_en());
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dialog_mode, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		findViewById();
		initView();
		this.setCancelable(false);
		return rootView;
	}

	private void findViewById() {
		modeListView = (ListView) rootView.findViewById(R.id.modeListview);
		lastBtn = (Button) rootView.findViewById(R.id.last_btn);
		nextBtn = (Button) rootView.findViewById(R.id.next_btn);
	}

	private void initView() {
		modeListView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_single_choice, modeNameList));
		lastBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		nextBtn.setEnabled(false);
		modeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				nextBtn.setEnabled(true);
				MainActivity.asmtValue.setMode(modeList.get(position).getMode_code());
			}
		});
	}

	private void saveData() {

		asmtValueDbService.asmtValueDao.update(MainActivity.asmtValue);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.last_btn:
			this.dismiss();
			mainActivity.stopRecord();
			mainActivity.saveData();
			break;
		case R.id.next_btn:
			saveData();
			this.dismiss();
			mainActivity.next_last(2);
			break;
		default:
			break;
		}
	}

}
