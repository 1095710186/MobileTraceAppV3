package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 16/8/9.
 */
public class LoginResultWrap {


    private UserInfo user;
    private String token;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
