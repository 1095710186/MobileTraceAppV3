package cn.com.bigknow.trade.pos.Immediate.app.store;


import net.orange_box.storebox.annotations.method.ClearMethod;
import net.orange_box.storebox.annotations.method.KeyByString;
import net.orange_box.storebox.annotations.method.RemoveMethod;
import net.orange_box.storebox.annotations.method.TypeAdapter;
import net.orange_box.storebox.annotations.type.FilePreferences;

import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;

/**
 * Created by hujie on 16/6/19.
 */
@FilePreferences("login_userinfo")
public interface UserInfoPreference {

    String keyUserInfo = "userInfo";

    @KeyByString(keyUserInfo)
    @TypeAdapter(UserInfoTypeAdapter.class)
    UserInfo getUserInfo();

    @KeyByString(keyUserInfo)
    @TypeAdapter(UserInfoTypeAdapter.class)
    void setUserInfo(UserInfo userInfo);


    @KeyByString(keyUserInfo)
    @RemoveMethod
    void removeUserInfo();


    @ClearMethod
    void clear();

}
