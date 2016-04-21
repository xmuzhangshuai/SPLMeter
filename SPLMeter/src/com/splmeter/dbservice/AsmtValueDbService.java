package com.splmeter.dbservice;

import com.splmeter.base.BaseApplication;
import com.splmeter.dao.AsmtValueDao;
import com.splmeter.dao.DaoSession;

import android.content.Context;

public class AsmtValueDbService {
	private static final String TAG = AsmtValueDbService.class.getSimpleName();
	private static AsmtValueDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public AsmtValueDao asmtValueDao;

	public AsmtValueDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static AsmtValueDbService getInstance(Context context) {
		if (instance == null) {
			instance = new AsmtValueDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.asmtValueDao = instance.mDaoSession.getAsmtValueDao();
		}
		return instance;
	}

}
