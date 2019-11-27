package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2019/11/18.
 */

public class UserBean {

    /**
     * Account : sample string 1
     * Password : sample string 2
     * IsRememberMe : true
     */

    private String Account;
    private String Password;
    private boolean IsRememberMe;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public boolean isIsRememberMe() {
        return IsRememberMe;
    }

    public void setIsRememberMe(boolean IsRememberMe) {
        this.IsRememberMe = IsRememberMe;
    }
}
