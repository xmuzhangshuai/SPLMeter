package com.splmeter.base;

import java.util.LinkedHashMap;
import java.util.Map;

import com.splmeter.utils.SharePreferenceUtil;
import com.splmeter.utils.UserPreference;

import android.app.Application;
import android.media.MediaPlayer;

/**   
 *    
 * 项目名称：lanquan   
 * 类名称：BaseApplication   
 * 类描述：   将取得DaoMaster对象的方法放到Application层这样避免多次创建生成Session对象。
 * 创建人：张帅     
 * 创建时间：2013-12-20 下午9:10:55   
 * 修改人：张帅     
 * 修改时间：2013-12-20 下午9:10:55   
 * 修改备注：   
 * @version    
 *    
 */
public class BaseApplication extends Application {
	private static BaseApplication myApplication;
	// private static DaoMaster daoMaster;
	// private static DaoSession daoSession;
	private UserPreference userPreference;
	private SharePreferenceUtil sharePreferenceUtil;

	public synchronized static BaseApplication getInstance() {
		return myApplication;
	}

	// @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		if (myApplication == null)
			myApplication = this;

		initData();
	}

	private void initData() {
		userPreference = new UserPreference(this);
		sharePreferenceUtil = new SharePreferenceUtil(this);
	}

	public synchronized SharePreferenceUtil getsharePreferenceUtil() {
		if (sharePreferenceUtil == null)
			sharePreferenceUtil = new SharePreferenceUtil(this);
		return sharePreferenceUtil;
	}

	public synchronized UserPreference getUserPreference() {
		if (userPreference == null)
			userPreference = new UserPreference(this);
		return userPreference;
	}

	/** 
	 * 取得DaoMaster 
	 *  
	 * @param context 
	 * @return 
	 */
	//	public static DaoMaster getDaoMaster(Context context) {
	//		if (daoMaster == null) {
	//			OpenHelper openHelper = new DaoMaster.DevOpenHelper(context, "quanzi.db", null);
	//			daoMaster = new DaoMaster(openHelper.getWritableDatabase());
	//		}
	//		return daoMaster;
	//	}

	/**
	 * 取得DaoSession 
	 * @param context
	 * @return
	 */
	//	public static DaoSession getDaoSession(Context context) {
	//		if (daoSession == null) {
	//			if (daoMaster == null) {
	//				daoMaster = getDaoMaster(context);
	//			}
	//			daoSession = daoMaster.newSession();
	//		}
	//		return daoSession;
	//	}

	//	public static void initImageLoader(Context context) {
	//		// This configuration tuning is custom. You can tune every option, you
	//		// may tune some of them,
	//		// or you can create default configuration by
	//		// ImageLoaderConfiguration.createDefault(this);
	//		// method.
	//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
	//				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
	//				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
	//				.build();
	//		// Initialize ImageLoader with configuration.
	//		ImageLoader.getInstance().init(config);
	//	}

}
