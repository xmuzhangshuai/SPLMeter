package com.splmeter.ui;

import com.smallrhino.splmeter.R;
import com.splmeter.customewidget.MyAlertDialog;

import android.app.Dialog;
import android.app.DialogFragment;
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
		myAlertDialog.setTitle("以下内容已复制到剪贴板");
		myAlertDialog.setMessage("“我正在参与公众噪声监测项目，目前身边噪声值为45.2dBA，非常安静，快来和我一起参加吧。下载地址http://www.citi-sense.cn/download/” /n赶快去分享吧~！");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
				ClipboardManager clipboardManager = (ClipboardManager) mainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
				clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "我正在参与公众噪声监测项目，目前身边噪声值为45.2dBA，非常安静，快来和我一起参加吧。下载地址http://www.citi-sense.cn/download/"));
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();
			}
		};
		myAlertDialog.setPositiveButton("确定", comfirm);
		myAlertDialog.setNegativeButton("取消", cancle);
		myAlertDialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share_scientist:
			this.dismiss();
			shwoTtip();
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
