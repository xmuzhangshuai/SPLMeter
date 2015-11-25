package com.splmeter.utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.splmeter.base.BaseApplication;
import com.splmeter.dbservice.SoundSourceDbService;
import com.splmeter.entities.SoundSource;

import android.content.Context;

/**
 * @description:网络操作的通用方法
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月25日 下午5:37:42
 */
public class ServerUtils {
	private SharePreferenceUtil sharePreferenceUtil;
	private Context context;

	public ServerUtils(Context context) {
		this.context = context;
		sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
	}

	/**
	 * 从网络获取数据并更新
	 */
	public void initData() {
		getTips();
		getSoundSource();
	}

	/**
	 * 首页的提示
	 */
	public void getTips() {
		RequestParams params = new RequestParams();

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				LogTool.i(statusCode + "===" + response);
				try {
					JSONObject j1 = new JSONObject(response);
					String data = j1.getString("data");
					JSONObject j2 = new JSONObject(data);
					String t_tiptxt_en = j2.getString("t_tiptxt_en");
					String t_tiptxt_cn = j2.getString("t_tiptxt_cn");
					sharePreferenceUtil.setMainLabelTextCN(t_tiptxt_cn);
					sharePreferenceUtil.setMainLabelTextEN(t_tiptxt_en);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("getTips服务器错误" + errorResponse);

			}
		};
		AsyncHttpClientTool.get("GetTips", params, responseHandler);
	}

	/**
	 * 首页的提示
	 */
	public void getSoundSource() {
		RequestParams params = new RequestParams();
		final SoundSourceDbService soundSourceDbService = SoundSourceDbService.getInstance(context);

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				LogTool.i(statusCode + "===" + response);
				try {
					JSONObject j1 = new JSONObject(response);
					String data = j1.getString("data");
					JSONArray jsonArray = new JSONArray(data);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject j2 = (JSONObject) jsonArray.get(i);
						SoundSource s = new SoundSource(j2.getInt("ssi_id"), j2.getString("ssi_code"), j2.getString("ssi_type"), j2.getString("ssi_item_cn"),
								j2.getString("ssi_item_en"));
						soundSourceDbService.addSoundSource(s);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("getSoundSource服务器错误" + errorResponse);

			}
		};
		AsyncHttpClientTool.get("GetSoundSource", params, responseHandler);
	}
}
