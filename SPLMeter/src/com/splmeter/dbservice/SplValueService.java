package com.splmeter.dbservice;

import com.splmeter.base.BaseApplication;
import com.splmeter.dao.DaoSession;
import com.splmeter.dao.SPLValueDao;

import android.content.Context;

public class SplValueService {
	private static final String TAG = SplValueService.class.getSimpleName();
	private static SplValueService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public SPLValueDao splValueDao;

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
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.splValueDao = instance.mDaoSession.getSPLValueDao();
		}
		return instance;
	}
}
