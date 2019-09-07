package com.hdlang.util.test;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.hlibrary.util.Logger;

import java.util.ArrayList;

public class NutDB extends SQLiteOpenHelper {
    private final static String DB_NAME = "nut.db";
    private final static String TB_NAME = "tb_user";
    private final static String KEY = "ms87u5";
    private final static int VERSION = 2;
    private static NutDB instance;

    private NutDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static NutDB getInstance(Context context) {
        if (instance == null) {
            synchronized (NutDB.class) {
                if (instance == null) {
                    instance = new NutDB(context);
                }
            }
        }
        return instance;
    }


    private String encodeUserId(String userid) {
        return userid;
    }

    private String decodeUserId(String userid) {
        return userid;
    }

    private String encodeCompanyId(String companyId) {
        return companyId;
    }

    private String decodeCompanyId(String companyId) {


        return companyId;
    }

    private String encodeCommon(String companyId) {
        return companyId;
    }

    private String decodeCommon(String companyId, String defaultText) {
        if (!TextUtils.isEmpty(companyId)) {
           return companyId;
        }

        return defaultText;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL("create table IF NOT EXISTS " +
                    TB_NAME +
                    "(_id varchar(255) primary key," +
                    "user varchar(255)," +
                    "passwd varchar(255)," +
                    "isAutoLogin int," +
                    "updatetime long, companyId varchar(255) default '" +
                    encodeCompanyId("-1") + "', curAccountId varchar(255) default '" +
                    encodeCommon("") + "', nextAccountId varchar(255) default '" +
                    encodeCommon("") + "', validity long default " +
                    0 + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SdcardUserEntity getUserById(String id) {
        SdcardUserEntity entity = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select _id,user,passwd,isAutoLogin,updatetime,companyId,curAccountId,nextAccountId,validity from " +
                    TB_NAME + " where _id = '" +
                    encodeUserId(id) + "'", null);
            if (cursor.moveToNext()) {
                entity = new SdcardUserEntity();
                entity.setUserId(decodeUserId(cursor.getString(0)));
                String user = cursor.getString(1);
                if (!TextUtils.isEmpty(user)) {
                    entity.setUser(user);
                }
                String password = cursor.getString(2);
                if (!TextUtils.isEmpty(password)) {
                    entity.setPasswd(password);
                }
                entity.setAutoLogin(cursor.getInt(3) == 1);
                entity.setUpdatetime(cursor.getLong(4));
                entity.setCompanyId(decodeCompanyId(cursor.getString(5)));
                entity.setCurAccountId(decodeCommon(cursor.getString(6), ""));
                entity.setNextAccountId(decodeCommon(cursor.getString(7), ""));
                entity.setValidity(cursor.getLong(8));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return entity;
    }

    public ArrayList<SdcardUserEntity> getUsers(String companyId) {
        ArrayList<SdcardUserEntity> sdcardUserEntities = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select _id,user,passwd,isAutoLogin,updatetime,companyId,curAccountId,nextAccountId,validity from " +
                    TB_NAME + " where companyId in ('" + encodeCompanyId("-1") + "','"
                    + encodeCompanyId(companyId) + "')", null);
            while (cursor.moveToNext()) {
                SdcardUserEntity entity = new SdcardUserEntity();
                entity.setUserId(decodeUserId(cursor.getString(0)));
                String user = cursor.getString(1);
                if (!TextUtils.isEmpty(user)) {
                    entity.setUser(user);
                }
                String password = cursor.getString(2);
                if (!TextUtils.isEmpty(password)) {
                    entity.setPasswd(password);
                }
                entity.setAutoLogin(cursor.getInt(3) == 1);
                entity.setUpdatetime(cursor.getLong(4));
                entity.setCompanyId(decodeCompanyId(cursor.getString(5)));
                entity.setCurAccountId(decodeCommon(cursor.getString(6), ""));
                entity.setNextAccountId(decodeCommon(cursor.getString(7), ""));
                entity.setValidity(cursor.getLong(8));
                sdcardUserEntities.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return sdcardUserEntities;
    }

    public synchronized ArrayList<SdcardUserEntity> getUsersOrderByUpdatetime(String companyId, String userId, boolean autoLogin) {
        ArrayList<SdcardUserEntity> sdcardUserEntities = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        try {
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("select _id,user,passwd,isAutoLogin,updatetime,companyId,curAccountId,nextAccountId,validity from ");
            sqlBuffer.append(TB_NAME);
            sqlBuffer.append(" where companyId in ('");
            sqlBuffer.append(encodeCompanyId("-1"));
            sqlBuffer.append("','");
            sqlBuffer.append(encodeCompanyId(companyId));
            sqlBuffer.append("')");
            sqlBuffer.append(" and ");
            sqlBuffer.append("isAutoLogin = ");
            sqlBuffer.append(autoLogin ? 1 : 0);
            sqlBuffer.append(" and _id = '");
            sqlBuffer.append(encodeUserId(userId));
            sqlBuffer.append("' order by updatetime desc");
            final String sql = sqlBuffer.toString();
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                SdcardUserEntity entity = new SdcardUserEntity();
                entity.setUserId(decodeUserId(cursor.getString(0)));
                String user = cursor.getString(1);
                if (!TextUtils.isEmpty(user)) {
                    entity.setUser(user);
                }
                String password = cursor.getString(2);
                if (!TextUtils.isEmpty(password)) {
                    entity.setPasswd(password);
                }
                entity.setAutoLogin(cursor.getInt(3) == 1);
                entity.setUpdatetime(cursor.getLong(4));
                entity.setCompanyId(decodeCompanyId(cursor.getString(5)));
                entity.setCurAccountId(decodeCommon(cursor.getString(6), ""));
                entity.setNextAccountId(decodeCommon(cursor.getString(7), ""));
                entity.setValidity(cursor.getLong(8));
                sdcardUserEntities.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return sdcardUserEntities;
    }

    public synchronized ArrayList<SdcardUserEntity> getUsersOrderByUpdatetime(String companyId) {
        ArrayList<SdcardUserEntity> sdcardUserEntities = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select _id,user,passwd,isAutoLogin,updatetime,companyId,curAccountId,nextAccountId,validity from " +
                    TB_NAME + " where companyId in ('" + encodeCompanyId("-1") + "','" +
                    encodeCompanyId(companyId) + "') order by updatetime desc", null);
            while (cursor.moveToNext()) {
                SdcardUserEntity entity = new SdcardUserEntity();
                entity.setUserId(decodeUserId(cursor.getString(0)));
                String user = cursor.getString(1);
                if (!TextUtils.isEmpty(user)) {
                    entity.setUser(user);
                }
                String password = cursor.getString(2);
                if (!TextUtils.isEmpty(password)) {
                    entity.setPasswd(password);
                }
                entity.setAutoLogin(cursor.getInt(3) == 1);
                entity.setUpdatetime(cursor.getLong(4));
                entity.setCompanyId(decodeCompanyId(cursor.getString(5)));
                entity.setCurAccountId(decodeCommon(cursor.getString(6), ""));
                entity.setNextAccountId(decodeCommon(cursor.getString(7), ""));
                entity.setValidity(cursor.getLong(8));
                sdcardUserEntities.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return sdcardUserEntities;
    }

    public void addUser(SdcardUserEntity entity, SQLiteDatabase db) throws SQLException {
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append("INSERT INTO " +
                TB_NAME + "(_id,user,passwd,isAutoLogin,updatetime,companyId,curAccountId,nextAccountId,validity) VALUES (");
        sqlBuilder.append("'").append(encodeUserId(entity.getUserId())).append("',");
        if (TextUtils.isEmpty(entity.getUser())) {
            sqlBuilder.append("'',");
        } else {
            sqlBuilder.append("'").append(entity.getUser()).append("',");
        }
        if (TextUtils.isEmpty(entity.getPasswd())) {
            sqlBuilder.append("'',");
        } else {
            sqlBuilder.append("'").append(entity.getPasswd()).append("',");
        }
        sqlBuilder.append(entity.isAutoLogin() ? 1 : 0).append(",")
                .append(entity.getUpdatetime()).append(",")
                .append("'").append(encodeCompanyId(entity.getCompanyId())).append("',");
        if (!TextUtils.isEmpty(entity.getCurAccountId())) {
            sqlBuilder.append("'").append(encodeCommon(entity.getCurAccountId())).append("',");
        } else {
            sqlBuilder.append("'").append(encodeCommon("")).append("',");
        }
        if (!TextUtils.isEmpty(entity.getNextAccountId())) {
            sqlBuilder.append("'").append(encodeCommon(entity.getNextAccountId())).append("',");
        } else {
            sqlBuilder.append("'").append(encodeCommon("")).append("',");
        }
        if (entity.getValidity() > 0) {
            sqlBuilder.append(entity.getValidity());
        }else {
            sqlBuilder.append("0");
        }
        sqlBuilder.append(")");
        Logger.Companion.getInstance().defaultTagI("sql = ",sqlBuilder.toString());
        db.execSQL(sqlBuilder.toString());

    }

    public void addUser(SdcardUserEntity entity) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            addUser(entity, db);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    private boolean haveUserId(String userId, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select _id from " +
                TB_NAME + " where _id = '" +
                encodeUserId(userId) + "'", null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public void addOrUpdateUser(SdcardUserEntity entity) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            if (haveUserId(entity.getUserId(), db)) {
                updateUser(entity, db);
            } else {
                addUser(entity, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.endTransaction();
        db.close();
    }

    private void updateUser(SdcardUserEntity entity, SQLiteDatabase db) throws SQLException {
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append("UPDATE ").append(TB_NAME).append(" SET ");
        sqlBuilder.append("passwd = '").append(entity.getPasswd()).append("'");
        sqlBuilder.append(",updatetime='").append(entity.getUpdatetime()).append("'");
        sqlBuilder.append(",isAutoLogin=").append(entity.isAutoLogin() ? 1 : 0);
        sqlBuilder.append(",companyId='").append(encodeCompanyId(entity.getCompanyId())).append("'");
        sqlBuilder.append(" WHERE _id = '").append(encodeUserId(entity.getUserId())).append("'");
        db.execSQL(sqlBuilder.toString());
    }

    public void updateUserById(String user) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append("UPDATE ").append(TB_NAME).append(" SET ");
        sqlBuilder.append(" user='").append(user).append("'");
        try {
            db.execSQL(sqlBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void updateUser(SdcardUserEntity entity) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            updateUser(entity, db);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void updateUserWithAccountId(String userId, String curAccountId, String nextAccountId) throws SQLException {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append("UPDATE ").append(TB_NAME).append(" SET curAccountId = '").append(encodeCommon(curAccountId));
        sqlBuilder.append("' , nextAccountId = '").append(encodeCompanyId(nextAccountId)).append("'");
        sqlBuilder.append(" WHERE _id = '").append(encodeUserId(userId)).append("'");
        final String sql = sqlBuilder.toString();
        db.execSQL(sql);
        db.close();
    }

    public void updateUserWithNextAccountId(String userId, String nextAccountId) throws SQLException {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append("UPDATE ").append(TB_NAME).append(" SET ");
        sqlBuilder.append("nextAccountId = '").append(encodeCompanyId(nextAccountId)).append("'");
        sqlBuilder.append(" WHERE _id = '").append(encodeUserId(userId)).append("'");
        final String sql = sqlBuilder.toString();
        db.execSQL(sql);
        db.close();
    }

    public void updateUserWithAutoLogin(String userId, String companyId, boolean autoLogin) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append("UPDATE ").append(TB_NAME).append(" SET isAutoLogin = ").append(autoLogin ? 1 : 0);
        sqlBuilder.append(" , companyId = '").append(encodeCompanyId(companyId)).append("'");
        sqlBuilder.append(" WHERE _id = '").append(encodeUserId(userId)).append("'");
        final String sql = sqlBuilder.toString();
        db.execSQL(sql);
        db.close();
    }

    public void updateUserWithPassword(String userId, String password) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append("UPDATE ").append(TB_NAME).append(" SET passwd = '").append(password);
        sqlBuilder.append("' WHERE _id = '").append(encodeUserId(userId)).append("'");
        db.execSQL(sqlBuilder.toString());
        db.close();
    }

    public synchronized void deleteUser(SdcardUserEntity entity) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            StringBuffer sqlBuilder = new StringBuffer();
            sqlBuilder.append("DELETE FROM ").append(TB_NAME).append(" WHERE _id = '").append(encodeUserId(entity.getUserId())).append("'");
            db.execSQL(sqlBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " +
                    TB_NAME + " ADD curAccountId varchar(255) default '" + encodeCompanyId("") + "'");
            db.execSQL("ALTER TABLE " +
                    TB_NAME + " ADD nextAccountId varchar(255) default '" + encodeCompanyId("") + "'");
            db.execSQL("ALTER TABLE " +
                    TB_NAME + " ADD validity long default 0");
        }
    }
}
