package com.rainbow.smartring.core.database;

import android.content.Context;

import com.rainbow.smartring.entity.DaoMaster;
import com.rainbow.smartring.entity.DaoSession;
import com.rainbow.smartring.entity.HrData;
import com.rainbow.smartring.entity.HrDataDao;
import com.rainbow.smartring.entity.O2Data;
import com.rainbow.smartring.entity.O2DataDao;
import com.rainbow.smartring.entity.TData;
import com.rainbow.smartring.entity.TDataDao;
import com.rainbow.smartring.entity.UserProfile;
import com.rainbow.smartring.entity.UserProfileDao;
import com.rainbow.smartring.core.log.Logger;

import org.greenrobot.greendao.database.Database;

import java.util.Date;
import java.util.List;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";

    private DaoSession mDaoSession = null;
    private UserProfileDao mUserProfileDao = null;
    private TDataDao mTDataDao = null;
    private HrDataDao mHrDataDao = null;
    private O2DataDao mO2DataDao = null;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        return Holder.INSTANCE;
    }

    public DatabaseManager init(Context context) {
        initDao(context);
        return this;
    }

    private void initDao(Context context) {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, "body-profile.db");
        final Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        mUserProfileDao = mDaoSession.getUserProfileDao();
        mTDataDao = mDaoSession.getTDataDao();
        mHrDataDao = mDaoSession.getHrDataDao();
        mO2DataDao = mDaoSession.getO2DataDao();
    }

    public final UserProfileDao getUserProfileDao() {
        return mUserProfileDao;
    }

    public final TDataDao getTDataDao() {
        return mTDataDao;
    }

    public final HrDataDao getHrDataDao() {
        return mHrDataDao;
    }

    public final O2DataDao getO2DataDao() {
        return mO2DataDao;
    }

    private static final class Holder {
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

    private void generateUserProfile(String mac){
        // 首次插入可能会出现这种情况
        Logger.e("未找到指定数据表，故生成一个新的数据表");
        try {
            UserProfile userProfile = new UserProfile();
            userProfile.setSid(mac);
            DatabaseManager.getInstance().getUserProfileDao().insert(userProfile);
        } catch (Exception e) {
            Logger.d(TAG, e.toString());
        }
    }

    public final void insertT(Date date, String mac, double t) {
        UserProfile profile = getUserProfileDao().queryBuilder()
                .where(UserProfileDao.Properties.Sid.eq(mac))
                .unique();
        if (profile == null) {
            generateUserProfile(mac);
            // 重新获取profile
            profile = getUserProfileDao().queryBuilder()
                    .where(UserProfileDao.Properties.Sid.eq(mac))
                    .unique();
        }
        if (profile == null) {
            throw new RuntimeException("奇怪的异常出现了");
        }
        List<TData> tDataList = profile.getTDataList();
        TData newTData = new TData();
        newTData.setTemperature(t);
        newTData.setDate(date);
        newTData.setUid(profile.getId());

        mTDataDao.insert(newTData);
        tDataList.add(newTData);
    }

    public final void insertHr(Date date, String mac, int hr) {
        UserProfile profile = getUserProfileDao().queryBuilder()
                .where(UserProfileDao.Properties.Sid.eq(mac))
                .unique();
        if (profile == null) {
            generateUserProfile(mac);
            // 重新获取profile
            profile = getUserProfileDao().queryBuilder()
                    .where(UserProfileDao.Properties.Sid.eq(mac))
                    .unique();
        }
        if (profile == null) {
            throw new RuntimeException("奇怪的异常出现了");
        }
        List<HrData> hrDataList = profile.getHrDataList();
        HrData newHrData = new HrData();
        newHrData.setHr(hr);
        newHrData.setDate(date);
        newHrData.setUid(profile.getId());

        mHrDataDao.insert(newHrData);
        hrDataList.add(newHrData);
    }

    public final void insertO2(Date date, String mac, int o2) {
        UserProfile profile = getUserProfileDao().queryBuilder()
                .where(UserProfileDao.Properties.Sid.eq(mac))
                .unique();
        if (profile == null) {
            generateUserProfile(mac);
            // 重新获取profile
            profile = getUserProfileDao().queryBuilder()
                    .where(UserProfileDao.Properties.Sid.eq(mac))
                    .unique();
        }
        if (profile == null) {
            throw new RuntimeException("奇怪的异常出现了");
        }
        List<O2Data> o2DataList = profile.getO2DataList();
        O2Data newO2Data = new O2Data();
        newO2Data.setO2(o2);
        newO2Data.setDate(date);
        newO2Data.setUid(profile.getId());

        mO2DataDao.insert(newO2Data);
        o2DataList.add(newO2Data);
    }

    public void deleteAll() {
        mTDataDao.deleteAll();
        mHrDataDao.deleteAll();
        mO2DataDao.deleteAll();
        mUserProfileDao.deleteAll();
    }
}
