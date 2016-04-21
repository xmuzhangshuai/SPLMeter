package com.splmeter.ui;

import java.util.ArrayList;
import java.util.List;

import com.splmeter.base.BaseActivity;
import com.splmeter.base.BaseApplication;
import com.splmeter.dbservice.AsmtValueDbService;
import com.splmeter.entities.AsmtValue;
import com.splmeter.utils.DateTimeTools;
import com.splmeter.utils.SharePreferenceUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.citisense.splmeter.R;

/**
 * @description:结果页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月19日 上午9:24:06
 */
public class ResultActivity extends BaseActivity implements OnClickListener {
	private Button cancelBtn;
	private Button confirmBtn;
	private TextView queitRateTextView;
	private TextView countTextView;

	private SharePreferenceUtil sharePreferenceUtil;
	private ListView resultListView;
	private List<AsmtValue> asmtValueList;
	private AsmtValueDbService asmtValueDbService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		asmtValueDbService = AsmtValueDbService.getInstance(ResultActivity.this);
		asmtValueList = new ArrayList<>();
		asmtValueList = asmtValueDbService.asmtValueDao.loadAll();
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		queitRateTextView = (TextView) findViewById(R.id.queitRateTextView);
		countTextView = (TextView) findViewById(R.id.countTextView);
		resultListView = (ListView) findViewById(R.id.resultlist);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);

		resultListView.setAdapter(new ResultlAdapter());
	}

	private void validateSetting() {
	}

	/**
	 * 提示对话框
	 */
	//	private void showAboutDialog() {
	//		final MyAlertDialog myAlertDialog = new MyAlertDialog(this);
	//		myAlertDialog.setTitle(this.getResources().getString(R.string.about));
	//		myAlertDialog.setMessage(getVersion());
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
	//		myAlertDialog.setPositiveButton(this.getResources().getString(R.string.confirm), comfirm);
	//		myAlertDialog.setNegativeButton(this.getResources().getString(R.string.cancel), cancle);
	//		myAlertDialog.show();
	//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancel_btn:
			finish();
			break;
		case R.id.confirm_btn:
			validateSetting();
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * @description:列表适配器
	 * @company: smallrhino
	 * @author：张帅
	 * @date 2016年4月21日 上午11:14:50
	 */
	class ResultlAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView laeqTextView;
			public TextView quietTextView;
			public TextView timeTextView;
			public TextView placeTextView;
			public TextView soundsourceTextView;
			public TextView evaluateTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return asmtValueList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return asmtValueList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			final AsmtValue asmtValue = asmtValueList.get(position);
			if (asmtValue == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(ResultActivity.this).inflate(R.layout.result_list_item, null);
				holder = new ViewHolder();
				holder.laeqTextView = (TextView) view.findViewById(R.id.laeqTextView);
				holder.quietTextView = (TextView) view.findViewById(R.id.quietTextView);
				holder.timeTextView = (TextView) view.findViewById(R.id.timeTextView);
				holder.placeTextView = (TextView) view.findViewById(R.id.placeTextView);
				holder.soundsourceTextView = (TextView) view.findViewById(R.id.soundsourceTextView);
				holder.evaluateTextView = (TextView) view.findViewById(R.id.evaluateTextView);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			if (asmtValue.getLaeq() != null) {
				holder.laeqTextView.setText(asmtValue.getLaeq().toString());
				holder.quietTextView.setText(asmtValue.getLaeq().toString());
				holder.soundsourceTextView.setText(asmtValue.getLaeq().toString());
				holder.evaluateTextView.setText(asmtValue.getLaeq().toString());
			}
			holder.timeTextView.setText(DateTimeTools.DateToString(asmtValue.getSplValueList().get(0).getTime()));
			holder.placeTextView.setText("记录条数：	" + asmtValue.getSplValueList().size());

			return view;
		}
	}
}
