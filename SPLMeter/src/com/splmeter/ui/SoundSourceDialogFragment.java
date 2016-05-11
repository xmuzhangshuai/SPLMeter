package com.splmeter.ui;

import java.util.ArrayList;
import java.util.List;

import com.splmeter.dbservice.AsmtValueDbService;
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
import cn.citisense.splmeter.R;

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
	private AsmtValueDbService asmtValueDbService;
	private StringBuffer soundSourceSelected;//选中的数据

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainActivity = (MainActivity) getActivity();
		soundSourceSelected = new StringBuffer();
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, 0);
		soundSourceDbService = SoundSourceDbService.getInstance(getActivity());
		asmtValueDbService = AsmtValueDbService.getInstance(getActivity());
		soundSourcesList1 = soundSourceDbService.getSoundSourceType01();
		soundSourcesList2 = soundSourceDbService.getSoundSourceType02();
		soundSourcesList3 = soundSourceDbService.getSoundSourceType03();
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
		this.setCancelable(false);
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

	private void saveData() {
		long[] a = getListSelectededItemIds(listview1);
		for (int i = 0; i < a.length; i++) {
			soundSourceSelected.append(soundSourcesList1.get((int) a[i]).getSsi_code() + ",");
		}
		long[] b = getListSelectededItemIds(listview2);
		for (int i = 0; i < b.length; i++) {
			soundSourceSelected.append(soundSourcesList2.get((int) b[i]).getSsi_code() + ",");
		}
		long[] c = getListSelectededItemIds(listview3);
		for (int i = 0; i < c.length; i++) {
			soundSourceSelected.append(soundSourcesList3.get((int) c[i]).getSsi_code() + ",");
		}
		if (soundSourceSelected.length() > 1) {
			soundSourceSelected.deleteCharAt(soundSourceSelected.length() - 1);
		}

		MainActivity.asmtValue.setSource(soundSourceSelected.toString());

		asmtValueDbService.asmtValueDao.update(MainActivity.asmtValue);
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
			saveData();
			this.dismiss();
			mainActivity.next_last(3);
			break;
		default:
			break;
		}
	}

	// 避免使用getCheckItemIds()方法
	public long[] getListSelectededItemIds(ListView listView) {

		long[] ids = new long[listView.getCount()];//getCount()即获取到ListView所包含的item总个数
		//定义用户选中Item的总个数
		int checkedTotal = 0;
		for (int i = 0; i < listView.getCount(); i++) {
			//如果这个Item是被选中的
			if (listView.isItemChecked(i)) {
				ids[checkedTotal++] = i;
			}
		}

		if (checkedTotal < listView.getCount()) {
			//定义选中的Item的ID数组
			final long[] selectedIds = new long[checkedTotal];
			//数组复制 ids
			System.arraycopy(ids, 0, selectedIds, 0, checkedTotal);
			return selectedIds;
		} else {
			//用户将所有的Item都选了
			return ids;
		}
	}

}
