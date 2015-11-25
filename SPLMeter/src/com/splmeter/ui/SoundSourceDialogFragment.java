package com.splmeter.ui;

import java.util.ArrayList;
import java.util.List;

import com.smallrhino.splmeter.R;
import com.splmeter.dbservice.SoundSourceDbService;
import com.splmeter.entities.SoundSource;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * @description:声源辨析对话框
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 下午9:15:58
 */
public class SoundSourceDialogFragment extends DialogFragment implements OnClickListener {
	private View rootView;

	private MainActivity mainActivity;
	private Button lastBtn;
	private Button nextBtn;
	private ListView listview1, listview2, listview3;
	private List<SoundSource> soundSourcesList1, soundSourcesList2, soundSourcesList3;
	private List<String> soundSourceNameList1, soundSourceNameList2, soundSourceNameList3;

	private SoundSourceDbService soundSourceDbService;

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
		soundSourceDbService = SoundSourceDbService.getInstance(getActivity());
		soundSourcesList1 = soundSourceDbService.getSoundSourceType00();
		soundSourcesList2 = soundSourceDbService.getSoundSourceType01();
		soundSourcesList3 = soundSourceDbService.getSoundSourceType02();
		soundSourceNameList1 = new ArrayList<>();
		soundSourceNameList2 = new ArrayList<>();
		soundSourceNameList3 = new ArrayList<>();
		if (CommonTools.isZh(getActivity())) {
			for (SoundSource s : soundSourcesList1) {
				soundSourceNameList1.add(s.getSsi_item_cn());
			}
			for (SoundSource s : soundSourcesList2) {
				soundSourceNameList2.add(s.getSsi_item_cn());
			}
			for (SoundSource s : soundSourcesList3) {
				soundSourceNameList3.add(s.getSsi_item_cn());
			}
		} else {
			for (SoundSource s : soundSourcesList1) {
				soundSourceNameList1.add(s.getSsi_item_en());
			}
			for (SoundSource s : soundSourcesList2) {
				soundSourceNameList2.add(s.getSsi_item_en());
			}
			for (SoundSource s : soundSourcesList3) {
				soundSourceNameList3.add(s.getSsi_item_en());
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dialog_soundsource, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		findViewById();
		initView();
		return rootView;
	}

	private void findViewById() {
		listview1 = (ListView) rootView.findViewById(R.id.listView1);
		listview2 = (ListView) rootView.findViewById(R.id.listView2);
		listview3 = (ListView) rootView.findViewById(R.id.listView3);
		lastBtn = (Button) rootView.findViewById(R.id.last_btn);
		nextBtn = (Button) rootView.findViewById(R.id.next_btn);
	}

	private void initView() {
		listview1.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_multiple_choice, soundSourceNameList1));
		listview2.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_multiple_choice, soundSourceNameList2));
		listview3.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_multiple_choice, soundSourceNameList3));
		lastBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.last_btn:
			this.dismiss();
			break;
		case R.id.next_btn:
			this.dismiss();
			mainActivity.next_last(2);
			break;
		default:
			break;
		}
	}
}
