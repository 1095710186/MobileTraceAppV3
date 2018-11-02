package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by hujie on 16/8/9.
 */
public class LoginInfo implements Serializable {


    private String phoneNum;
    private String passWord;
    private String token;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
