package com.splmeter.dbservice;

import java.util.List;

import com.splmeter.base.BaseApplication;
import com.splmeter.dao.DaoSession;
import com.splmeter.dao.ModeDao;
import com.splmeter.entities.Mode;

import android.content.Context;

public class ModeDbService {
	private static final String TAG = ModeDbService.class.getSimpleName();
	private static ModeDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public ModeDao modeDao;

	public ModeDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static ModeDbService getInstance(Context context) {
		if (instance == null) {
			instance = new ModeDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.modeDao = instance.mDaoSession.getModeDao();
		}
		return instance;
	}

	public List<Mode> getModeList() {
		return modeDao.queryBuilder().list();
	}

	public void addMode(Mode mode) {
		modeDao.insertOrReplace(mode);
	}
}
