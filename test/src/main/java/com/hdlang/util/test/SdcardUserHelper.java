package com.hdlang.util.test;

import android.content.Context;
import android.database.SQLException;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/12.
 */

public class SdcardUserHelper {

    private String mUserKey = "ms87u5";

    private static SdcardUserHelper sInstance;
    private Context context;

    public static SdcardUserHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SdcardUserHelper(context);
        }
        return sInstance;
    }

    public SdcardUserHelper(Context context){
        this.context = context;
    }

    public void changeAutoLoginState(String userId, boolean autoLogin) {
        NutDB.getInstance(context).updateUserWithAutoLogin(userId, "1", autoLogin);
    }

    public synchronized SdcardUserEntity getSdcardUserEntity(String userId) {
        NutDB nutDB = NutDB.getInstance(context);
        SdcardUserEntity entity = nutDB.getUserById(userId);
        return entity;
    }

    /**
     * function: 获取用户列表, 根据更新时间排序
     *
     * @return
     * @ author:linjunying 2013-12-12 上午11:50:38
     */
    public synchronized ArrayList<SdcardUserEntity> getUserListOrderForUpdateTime(String channel, String userId, boolean autoLogin) {
        if (TextUtils.isEmpty(channel)) {
            channel = "-1";
        }
        NutDB nutDB = NutDB.getInstance(context);
        try {
            return nutDB.getUsersOrderByUpdatetime(channel, userId, autoLogin);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 更新游戏账号
     *
     * @param userId
     * @param curAccountId
     * @param nextAccountId
     */
    public synchronized void updateUserWithAccountId(String userId, String curAccountId, String nextAccountId) {
        NutDB nutDB = NutDB.getInstance(context);
        try {
            nutDB.updateUserWithAccountId(userId, curAccountId, nextAccountId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新游戏账号
     *
     * @param userId
     * @param nextAccountId
     */
    public synchronized void updateUserWithAccountId(String userId, String nextAccountId) {
        NutDB nutDB = NutDB.getInstance(context);
        try {
            nutDB.updateUserWithNextAccountId(userId, nextAccountId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * function: 获取用户列表, 根据更新时间排序
     *
     * @return
     * @ author:linjunying 2013-12-12 上午11:50:38
     */
    public synchronized ArrayList<SdcardUserEntity> getUserListOrderForUpdateTime(String channel) {
        if (TextUtils.isEmpty(channel)) {
            channel = "-1";
        }
        NutDB nutDB = NutDB.getInstance(context);
        try {
            return nutDB.getUsersOrderByUpdatetime(channel);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public synchronized ArrayList<SdcardUserEntity> getUserListForStorage(String channel) {
        NutDB nutDB = NutDB.getInstance(context);
        return nutDB.getUsers(channel);
    }

    public synchronized void deleteSdcardUser(SdcardUserEntity sdcardUserEntity) {
        NutDB nutDB = NutDB.getInstance(context);
        nutDB.deleteUser(sdcardUserEntity);
    }

    /**
     * 保存登录账号
     *
     * @param sdcardUserEntity
     */
    public synchronized void saveUserListForStorage(SdcardUserEntity sdcardUserEntity) {
        NutDB nutDB = NutDB.getInstance(context);
        sdcardUserEntity.setCompanyId("2");
        nutDB.addOrUpdateUser(sdcardUserEntity);

    }

    /**
     * 替换登录账号
     *
     * @param
     */
    public synchronized void updateUserListForStorage(String account) {

        NutDB nutDB = NutDB.getInstance(context);
        nutDB.updateUserById(account);

    }


}
