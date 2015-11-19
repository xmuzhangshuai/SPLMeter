package com.splmeter.ui;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SoundSourceDialogFragment extends DialogFragment {
	private View rootView;

	private MainActivity mainActivity;
	private ListView listview1;
	private ListView listview2;
	private ListView listview3;
	private List<String> list1;
	private List<String> list2;
	private List<String> list3;

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
		list1 = new ArrayList<String>();
		list2 = new ArrayList<String>();
		list3 = new ArrayList<String>();
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
	}

	private void initView() {
		initData();

		listview1.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_multiple_choice, list1));
		listview2.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_multiple_choice, list2));
		listview3.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_multiple_choice, list3));
	}

	/**
	 * 初始化多选数据
	 */
	private void initData() {
		list1.add("风声");
		list1.add("水声");
		list1.add("鸟声");
		list1.add("虫鸣声");
		list1.add("动物声");
		list2.add("说话声");
		list2.add("嬉戏声");
		list3.add("交通声");
		list3.add("机器声");
		list3.add("音乐声");
		list3.add("广播声");
	}
}
