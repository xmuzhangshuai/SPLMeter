package com.splmeter.ui;

import com.smallrhino.splmeter.R;
import com.splmeter.base.BaseActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
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
	private ActionBar actionBar;
	private String url;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		if (TextUtils.isEmpty(url)) {
			finish();
			return;
		}

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
		actionBar = getActionBar();
		actionBar.setTitle(title);

		webView.loadUrl(url);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
