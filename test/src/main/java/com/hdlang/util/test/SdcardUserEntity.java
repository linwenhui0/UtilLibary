package com.hdlang.util.test;

import com.alibaba.fastjson.JSON;

/**
 * sdcard保存用户信息
 * Created by cxb on 2016/12/12.
 */

public class SdcardUserEntity {

    private String userId;
    private String user;
    private String passwd = "";
    private boolean isAutoLogin;
    private long updatetime;
    private String companyId = "0";
    private String curAccountId = "";
    private String nextAccountId = "";
    private long validity;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public boolean isAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        isAutoLogin = autoLogin;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCurAccountId(String curAccountId) {
        this.curAccountId = curAccountId;
    }

    public String getCurAccountId() {
        return curAccountId;
    }

    public String getNextAccountId() {
        return nextAccountId;
    }

    public void setNextAccountId(String nextAccountId) {
        this.nextAccountId = nextAccountId;
    }

    public long getValidity() {
        return validity;
    }

    public void setValidity(long validity) {
        if (validity < 0) {
            this.validity = 0;
        } else {
            this.validity = validity;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
