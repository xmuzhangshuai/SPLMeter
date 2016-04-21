package com.splmeter.dbservice;

import java.util.List;

import com.splmeter.base.BaseApplication;
import com.splmeter.dao.DaoSession;
import com.splmeter.dao.SoundSourceDao;
import com.splmeter.dao.SoundSourceDao.Properties;
import com.splmeter.entities.SoundSource;

import android.content.Context;

public class SoundSourceDbService {
	private static final String TAG = SoundSourceDbService.class.getSimpleName();
	private static SoundSourceDbService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	public SoundSourceDao soundSourceDao;

	public SoundSourceDbService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static SoundSourceDbService getInstance(Context context) {
		if (instance == null) {
			instance = new SoundSourceDbService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = BaseApplication.getDaoSession(context);
			instance.soundSourceDao = instance.mDaoSession.getSoundSourceDao();
		}
		return instance;
	}

	public List<SoundSource> getSoundSourceType01() {
		return soundSourceDao.queryBuilder().where(Properties.Ssi_type.eq("01")).list();
	}

	public List<SoundSource> getSoundSourceType02() {
		return soundSourceDao.queryBuilder().where(Properties.Ssi_type.eq("02")).list();
	}

	public List<SoundSource> getSoundSourceType03() {
		return soundSourceDao.queryBuilder().where(Properties.Ssi_type.eq("03")).list();
	}

	public void addSoundSource(SoundSource soundSource) {
		soundSourceDao.insertOrReplace(soundSource);
	}

	public String getSourceENNameByCode(String code) {
		return soundSourceDao.queryBuilder().where(Properties.Ssi_code.eq(code)).unique().getSsi_item_en();
	}

	public String getSourceCNNameByCode(String code) {
		return soundSourceDao.queryBuilder().where(Properties.Ssi_code.eq(code)).unique().getSsi_item_cn();
	}
}
