package com.splmeter.utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.splmeter.dbservice.ModeDbService;
import com.splmeter.dbservice.SoundSourceDbService;
import com.splmeter.entities.Mode;
import com.splmeter.entities.SoundSource;

import android.content.Context;

/**
 * @description:网络操作的通用方法
 * @company: smallrhino
 * @author：张帅
 * @date 2015年11月25日 下午5:37:42
 */
public class ServerUtils {
	private Context context;

	public ServerUtils(Context context) {
		this.context = context;
	}

	/**
	 * 从网络获取数据并更新
	 */
	public void initData() {
		getMode();
		getSoundSource();
		uploadUserState();
	}

	/**
	 * 提交用户状态
	 */
	private void uploadUserState() {
		RequestParams params = new RequestParams();
		params.put("IMEI", CommonTools.getIMEI(context));
		params.put("reportTime", DateTimeTools.getCurrentDateTimeForString());

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				LogTool.i(statusCode + "===" + response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("uploadUserState服务器错误" + errorResponse);

			}
		};
		AsyncHttpClientTool.post("?m=Home&a=ReportUserState", params, responseHandler);
	}

	/**
	 * 获取声源
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
		AsyncHttpClientTool.post("?m=Home&a=GetSoundSource", params, responseHandler);
	}

	/**
	 * 获取所在地
	 */
	public void getMode() {

		RequestParams params = new RequestParams();
		final ModeDbService modeDbService = ModeDbService.getInstance(context);
		Mode m1 = new Mode(1, "I", "室内", "indoor");
		Mode m2 = new Mode(2, "R", "居住用地", "indoor");
		Mode m3 = new Mode(3, "A", "公共管理与服务用地", "indoor");
		Mode m4 = new Mode(4, "B", "商业服务业设施用地", "indoor");
		Mode m5 = new Mode(5, "M", "工业用地", "indoor");
		Mode m6 = new Mode(6, "W", "物流仓储用地", "indoor");
		Mode m7 = new Mode(7, "S", "道路与交通设施用地", "indoor");
		Mode m8 = new Mode(8, "U", "公共设施用地", "indoor");
		Mode m9 = new Mode(9, "G", "绿地与广场用地", "indoor");
		modeDbService.addMode(m1);
		modeDbService.addMode(m2);
		modeDbService.addMode(m3);
		modeDbService.addMode(m4);
		modeDbService.addMode(m5);
		modeDbService.addMode(m6);
		modeDbService.addMode(m7);
		modeDbService.addMode(m8);
		modeDbService.addMode(m9);

		//		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
		//
		//			@Override
		//			public void onSuccess(int statusCode, Header[] headers, String response) {
		//				// TODO Auto-generated method stub
		//				LogTool.i(statusCode + "===" + response);
		//				try {
		//					JSONObject j1 = new JSONObject(response);
		//					String data = j1.getString("data");
		//					JSONArray jsonArray = new JSONArray(data);
		//					for (int i = 0; i < jsonArray.length(); i++) {
		//						JSONObject j2 = (JSONObject) jsonArray.get(i);
		//						SoundSource s = new SoundSource(j2.getInt("ssi_id"), j2.getString("ssi_code"), j2.getString("ssi_type"), j2.getString("ssi_item_cn"),
		//								j2.getString("ssi_item_en"));
		//						soundSourceDbService.addSoundSource(s);
		//					}
		//
		//				} catch (JSONException e) {
		//					e.printStackTrace();
		//				}
		//			}
		//
		//			@Override
		//			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
		//				// TODO Auto-generated method stub
		//				LogTool.e("getSoundSource服务器错误" + errorResponse);
		//
		//			}
		//		};
		//		AsyncHttpClientTool.post("?m=Home&a=GetSoundSource", params, responseHandler);
	}
}
