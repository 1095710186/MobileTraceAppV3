package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.orange_box.storebox.StoreBox;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.app.di.DBManager;
import cn.com.bigknow.trade.pos.Immediate.app.store.UserInfoPreference;
import cn.com.bigknow.trade.pos.Immediate.app.ui.LoginActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MainActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.CommonUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.PreferenceManager;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.LoginInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by hujie on 16/6/19.
 *
 * 用户管理的类
 */
public class UserManager {

    private boolean hasLogin = false;
    private UserInfoPreference preference;
    private UserInfo userInfo;
    @Inject
    public Context context;
    private static UserManager userManager;
    private long lastLoginTime = 0;//上次登陆时间


    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    private UserManager() {
        BootstrapApp.get().appComponent().inject(this);
        createUserInfoPreference();
    }

    private void createUserInfoPreference() {
        if (preference == null) {
            preference = StoreBox.create(context, UserInfoPreference.class);
        }
    }

    public void saveLoginResult(LoginInfo loginInfo, UserInfo userInfo) {
        hasLogin = true;
        lastLoginTime = System.currentTimeMillis();
        saveLoginInfo(loginInfo);
        updateUserInfo(userInfo);
    }

    private void saveLoginInfo(LoginInfo loginInfo) {
        PreferenceManager.save(context, loginInfo);
    }


    public LoginInfo getLoginInfo() {
        return (LoginInfo) PreferenceManager.get(context, LoginInfo.class);
    }

    public String getToken() {

        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            return loginInfo.getToken();
        }
        return null;
    }

    public void updateUserInfo(UserInfo userInfo) {

        this.userInfo = userInfo;
        //更新
        if (preference != null) {
            preference.setUserInfo(userInfo);
        }
        EventBus.getDefault().post(userInfo);
    }


    public UserInfo getUserInfo() {
        if (userInfo != null) {
            return userInfo;
        } else if (preference != null) {
            userInfo = preference.getUserInfo();
            return userInfo;
        }
        return null;
    }


    /**
     * 只清除用户信息
     */
    private void clearUserInfo() {
        if (preference != null) {
            preference.removeUserInfo();
        }
    }

    public void destory() {

        DBManager.destory();

        hasLogin = false;
        lastLoginTime = 0;
        userManager = null;
        userInfo = null;
        preference = null;
    }

    /**
     * 异步登录下
     */
    public void asyncLogin() {

        if (needAsyncLogin()) {

            if (!CommonUtil.isNetWorkConnected(context)) {
                return;
            }
            Api.api().checkToken(new SimpleRequestListener<BaseEntity>() {
                @Override
                public void onSuccess(BaseEntity o) {
                    lastLoginTime = System.currentTimeMillis();
                    hasLogin = true;
                    String rid = JPushInterface.getRegistrationID(context);

                    UserInfo u = UserManager.getInstance().getUserInfo();

                    if (!rid.isEmpty()) {
                        if (u.getPushId() != null && UserManager.getInstance().getUserInfo().getPushId().equals(rid)) {

                        } else {
                            u.setPushId(rid);
                            HttpPushId("pushId", u.getUserPersonUuid(), rid);
                        }
                        UserManager.getInstance().updateUserInfo(u);

                    } else {
                        Toast.makeText(context, "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ApiError error) {
                    if (error.isApiError()) {
                        //登陆过期了
                        toLogin(context);
                    }
                }
            });

        }
    }  private void HttpPushId(String pushId, String userPersonUuid, String rid) {
        Api.api().pushId(pushId, userPersonUuid, rid, new SimpleRequestListener() {

            @Override
            public void onSuccess(Object o) {
//                ToastUtil.makeToast("pushId更新成功！");


            }

            @Override
            public void onError(ApiError error) {

                super.onError(error);

            }

        });
    }


    // 重新登录
    private static void toLogin(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("reLogin", true);
        context.startActivity(intent);
    }

    private boolean needAsyncLogin() {

        //24小时内token有效,这里每隔2个小时就登陆一次好了
        if (lastLoginTime != 0) {
            long space = System.currentTimeMillis() - lastLoginTime;
            if (space / 3600000 >= 2) {
                return true;
            }
        }
        return !hasLogin;
    }


    private void clearToken() {
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            loginInfo.setToken(null);
            saveLoginInfo(loginInfo);
        }
    }


    public void onLoginOut() {
        clearUserInfo();
        clearToken();
        destory();
    }

    public void loadHeadImage(Activity activity, ImageView headIV) {
        if (UserManager.getInstance().getUserInfo()!=null && UserManager.getInstance().getUserInfo().getHeadImg()!=null) {
            String IMG_URL = ModelConfig.BIGKNOW_FRAMEWORK_URL + "servlet/fileupload?oper_type=img&fileId=" + UserManager.getInstance().getUserInfo().getHeadImg();//"&random=" + System.currentTimeMillis();
            Glide.with(activity).load(IMG_URL).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(headIV);
        }else {
            headIV.setImageResource(R.drawable.w_pos_icon_tx);
        }
    }

    public void loadFoodHeadImage(Context activity, ImageView headIV, String id) {
        String IMG_URL = ModelConfig.MVP_URL + "api/appCommon/img.action";
        Glide.with(activity).load(IMG_URL + "?imgId=" + id).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(headIV);
    }


}
