package com.splmeter.dbservice;

import com.splmeter.base.BaseApplication;
import com.splmeter.dao.DaoSession;
import com.splmeter.dao.SPLValueDao;
import com.splmeter.entities.SPLValue;
import com.splmeter.utils.SharePreferenceUtil;

import android.content.Context;

public class SplValueService {
	private static final String TAG = SplValueService.class.getSimpleName();
	private static SplValueService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public SPLValueDao splValueDao;
	private static SharePreferenceUtil sharePreferenceUtil;

	public SplValueService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static SplValueService getInstance(Context context) {
		if (instance == null) {
			instance = new SplValueService();
			sharePreferenceUtil = BaseApplication.getInstance().getsharePreferenceUtil();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.splValueDao = instance.mDaoSession.getSPLValueDao();
		}
		return instance;
	}

	public long insert(SPLValue splValue) {
		if (sharePreferenceUtil.getAutoShare()) {
			return splValueDao.insert(splValue);
		}
		return 0;
	}

	public void update(SPLValue splValue) {
		if (sharePreferenceUtil.getAutoShare()) {
			splValueDao.update(splValue);
		}
	}
}
