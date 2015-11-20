package com.splmeter.ui;

import com.smallrhino.splmeter.R;
import com.splmeter.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

/**
 * @description:各种html页面
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月20日 下午3:02:26
 */
public class WebActivity extends BaseActivity {

	private WebView webView;
	private Button backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.webView);
		backBtn = (Button) findViewById(R.id.goback_btn);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		webView.loadUrl("http://www.smallrhino.net");
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
