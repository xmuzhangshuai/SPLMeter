package com.splmeter.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.splmeter.base.BaseActivity;
import com.splmeter.dbservice.AsmtValueDbService;
import com.splmeter.dbservice.ModeDbService;
import com.splmeter.dbservice.SoundSourceDbService;
import com.splmeter.entities.AsmtValue;
import com.splmeter.utils.CommonTools;
import com.splmeter.utils.DateTimeTools;
import com.splmeter.utils.LogTool;

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
	private String[] levels;
	private ListView resultListView;
	private List<AsmtValue> asmtValueList;
	private AsmtValueDbService asmtValueDbService;
	private ModeDbService modeDbService;
	private SoundSourceDbService soundSourceDbService;
	private ResultlAdapter resultAdapter;
	private String[] soundLevles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		asmtValueDbService = AsmtValueDbService.getInstance(ResultActivity.this);
		modeDbService = ModeDbService.getInstance(ResultActivity.this);
		soundSourceDbService = SoundSourceDbService.getInstance(ResultActivity.this);
		asmtValueList = new ArrayList<>();
		soundLevles = getResources().getStringArray(R.array.sound_levelGroup);
		asmtValueList = asmtValueDbService.asmtValueDao.loadAll();
		Collections.reverse(asmtValueList);
		levels = getResources().getStringArray(R.array.levelGroup);
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
		resultAdapter = new ResultlAdapter();
		resultListView.setAdapter(resultAdapter);

		if (CommonTools.isZh(ResultActivity.this)) {
			countTextView.setText("共" + asmtValueList.size() + "条记录");
		} else {
			countTextView.setText(asmtValueList.size() + "  records");
		}
		int count = 0;
		for (AsmtValue as : asmtValueList) {
			if (as.getLaeq() != null && as.getLaeq() < 55) {
				count++;
			}
		}
		if (asmtValueList.size() > 0) {
			float value = (float) (Math.round((float) count * 100 / asmtValueList.size() * 10) / 10.0);
			queitRateTextView.setText(getResources().getString(R.string.queitRate) + " " + value + "%");
		} else {
			queitRateTextView.setText(getResources().getString(R.string.queitRate) + " " + 0 + "%");
		}

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
			asmtValueDbService.asmtValueDao.deleteAll();
			asmtValueList.clear();
			resultAdapter.notifyDataSetChanged();
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
			try {

				if (asmtValue.getLaeq() != null) {
					holder.laeqTextView.setText("" + asmtValue.getLaeq() + "dBA");
					float laeq = asmtValue.getLaeq().floatValue();
					if (laeq < 50) {
						holder.quietTextView.setText(levels[0]);
					} else if (laeq < 55) {
						holder.quietTextView.setText(levels[1]);
					} else if (laeq < 60) {
						holder.quietTextView.setText(levels[2]);
					} else if (laeq < 65) {
						holder.quietTextView.setText(levels[3]);
					} else {
						holder.quietTextView.setText(levels[4]);
					}
				} else {
					LogTool.e("AsmtValue.getLaeq()为null");
				}

				//时间
				holder.timeTextView.setText(DateTimeTools.DateToString(asmtValue.getSplValueList().get(0).getTime()));

				//位置
				if (asmtValue.getMode() != null) {
					if (CommonTools.isZh(ResultActivity.this)) {
						holder.placeTextView
								.setText(getResources().getString(R.string.place) + modeDbService.getModeCNNameByCode(asmtValue.getMode()));
					} else {
						holder.placeTextView
								.setText(getResources().getString(R.string.place) + modeDbService.getModeENNameByCode(asmtValue.getMode()));
					}

				} else {
					holder.placeTextView.setText(getResources().getString(R.string.place) + getResources().getString(R.string.unknown));
				}

				//声源
				if (asmtValue.getSource() != null && asmtValue.getSource().length() > 0) {

					String[] sources = asmtValue.getSource().split(",");
					String s = "";

					for (int i = 0; i < sources.length; i++) {
						if (i != sources.length - 1) {
							if (CommonTools.isZh(ResultActivity.this)) {
								s = s + soundSourceDbService.getSourceCNNameByCode(sources[i]) + "，";
							} else {
								s = s + soundSourceDbService.getSourceENNameByCode(sources[i]) + ",";
							}
						} else {
							if (CommonTools.isZh(ResultActivity.this)) {
								s = s + soundSourceDbService.getSourceCNNameByCode(sources[i]);
							} else {
								s = s + soundSourceDbService.getSourceENNameByCode(sources[i]);
							}
						}
					}
					holder.soundsourceTextView.setText(getResources().getString(R.string.soundesource) + s);
				} else {
					holder.soundsourceTextView.setText(getResources().getString(R.string.soundesource) + getResources().getString(R.string.unknown));
				}

				//评价
				if (asmtValue.getAsmt() != null) {
					String[] asmts = asmtValue.getAsmt().split(",");
					String a = "";
					a = getResources().getString(R.string.sound_level2) + soundLevles[Integer.parseInt(asmts[0]) + 2] + ", "
							+ getResources().getString(R.string.acoustic_comfort_level) + soundLevles[Integer.parseInt(asmts[1]) + 2] + ", "
							+ getResources().getString(R.string.coordinated_level) + soundLevles[Integer.parseInt(asmts[2]) + 2];
					holder.evaluateTextView.setText(getResources().getString(R.string.evaluate) + "：" + a);
				} else {
					holder.evaluateTextView.setText(getResources().getString(R.string.evaluate) + "：" + getResources().getString(R.string.unknown));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return view;
		}
	}
}
