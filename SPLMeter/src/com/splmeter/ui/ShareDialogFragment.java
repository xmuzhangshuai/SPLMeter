package com.splmeter.ui;

import org.apache.http.Header;

import com.loopj.android.http.TextHttpResponseHandler;
import com.smallrhino.splmeter.R;
import com.splmeter.base.BaseApplication;
import com.splmeter.config.Constants;
import com.splmeter.customewidget.MyAlertDialog;
import com.splmeter.utils.AsyncHttpClientTool;
import com.splmeter.utils.CommonTools;
import com.splmeter.utils.LocationTool;
import com.splmeter.utils.LogTool;
import com.splmeter.utils.NetworkUtils;
import com.splmeter.utils.SharePreferenceUtil;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * @description:分享窗口
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月20日 上午11:07:52
 */
public class ShareDialogFragment extends DialogFragment implements OnClickListener {

	private View rootView;

	private MainActivity mainActivity;
	private TextView shareScientist;
	private TextView shareSina;
	private TextView shareTXweibo;
	private TextView shareWechat;
	private TextView shareTwitter;
	private TextView shareFacebook;
	LocationTool locationTool;
	private SharePreferenceUtil sharePreferenceUtil;

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
		locationTool = new LocationTool(getActivity());
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dialog_share, container, false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		findViewById();
		initView();
		return rootView;
	}

	private void findViewById() {
		shareScientist = (TextView) rootView.findViewById(R.id.share_scientist);
		shareSina = (TextView) rootView.findViewById(R.id.share_sina);
		shareTXweibo = (TextView) rootView.findViewById(R.id.share_tx_weibo);
		shareWechat = (TextView) rootView.findViewById(R.id.share_wechat);
		shareTwitter = (TextView) rootView.findViewById(R.id.share_twitter);
		shareFacebook = (TextView) rootView.findViewById(R.id.share_facebook);
	}

	private void initView() {
		shareScientist.setOnClickListener(this);
		shareSina.setOnClickListener(this);
		shareTXweibo.setOnClickListener(this);
		shareWechat.setOnClickListener(this);
		shareTwitter.setOnClickListener(this);
		shareFacebook.setOnClickListener(this);
	}

	/**
	 * 提示对话框
	 */
	private void shwoTtip() {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
		myAlertDialog.setTitle(mainActivity.getResources().getString(R.string.already_copy));
		myAlertDialog.setMessage(mainActivity.getResources().getString(R.string.share_detail1) + " " + mainActivity.getCurrentValue() + "dBA，"
				+ CommonTools.getLeverBySPL(mainActivity.getCurrentValue(), mainActivity) + "，" + mainActivity.getResources().getString(R.string.share_detail2)
				+ Constants.DownLoadPath + "”");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
				ClipboardManager clipboardManager = (ClipboardManager) mainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
				clipboardManager.setPrimaryClip(ClipData.newPlainText(null,
						mainActivity.getResources().getString(R.string.share_detail1) + " " + mainActivity.getCurrentValue() + "dBA，"
								+ CommonTools.getLeverBySPL(mainActivity.getCurrentValue(), mainActivity) + "，" + mainActivity.getResources().getString(R.string.share_detail2)
								+ Constants.DownLoadPath + "”"));
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton(mainActivity.getResources().getString(R.string.confirm), comfirm);
		myAlertDialog.setNegativeButton(mainActivity.getResources().getString(R.string.cancel), cancle);
		myAlertDialog.show();
	}

	/**
	 * 提示对话框
	 */
	private void showSuccessDialog() {
		final MyAlertDialog myAlertDialog = new MyAlertDialog(mainActivity);
		myAlertDialog.setTitle(mainActivity.getResources().getString(R.string.infoTitle));
		myAlertDialog.setMessage(mainActivity.getResources().getString(R.string.share_success));
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton(mainActivity.getResources().getString(R.string.confirm), comfirm);
		myAlertDialog.setNegativeButton(mainActivity.getResources().getString(R.string.cancel), cancle);
		myAlertDialog.show();
	}

	private void saveData() {
		MainActivity.resultParams.put("gender", sharePreferenceUtil.getGender());
		MainActivity.resultParams.put("age", sharePreferenceUtil.getAgeGroup() + 1);
		mainActivity.saveData();
		MainActivity.resultParams.put("IMEI", CommonTools.getIMEI(mainActivity));
		MainActivity.resultParams.put("modelType", CommonTools.getPhoneType());
		
		MainActivity.shareFlag = 2;
		MainActivity.startOrEva = 0;
		mainActivity.refresh();
	}

	/**
	 * 上传数据
	 */
	private void uploadData() {
		if (NetworkUtils.isNetworkAvailable(getActivity())) {
			final ProgressDialog dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getActivity().getResources().getString(R.string.shareing));

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog.show();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					// TODO Auto-generated method stub
					LogTool.i(statusCode + "===" + response);
					showSuccessDialog();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("ReportSPLValue服务器错误" + errorResponse);
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
				}
			};
			AsyncHttpClientTool.post("?m=Home&a=ReportSPLValue", MainActivity.resultParams, responseHandler);
		} else {
			CommonTools.showShortToast(getActivity(), CommonTools.isZh(getActivity()) ? "分享失败！请保障网络畅通或稍后再试" : "Failed! Make sure Internet connected or try again later");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share_scientist:
			this.dismiss();
			if (MainActivity.shareFlag == 0) {
				LogTool.e("必须先测试！");
			} else if (MainActivity.shareFlag == 1) {
				saveData();
				uploadData();
			} else if (MainActivity.shareFlag == 2) {
				LogTool.e("已经分享过！");
			}
			break;
		case R.id.share_sina:
			this.dismiss();
			shwoTtip();
			break;
		case R.id.share_tx_weibo:
			this.dismiss();
			shwoTtip();
			break;
		case R.id.share_wechat:
			this.dismiss();
			shwoTtip();
			break;
		case R.id.share_twitter:
			this.dismiss();
			shwoTtip();
			break;
		case R.id.share_facebook:
			this.dismiss();
			shwoTtip();
			break;
		default:
			break;
		}
	}
}
