package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.activitymanager.TheActivityManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.RegularUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ViewUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.ClearEditText;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.LoginInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.LoginResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import cn.jpush.android.api.JPushInterface;

/**
 * 登陆
 * Created by hujie on 16/8/9.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.phoneET)
    ClearEditText mPhoneET;
    @BindView(R.id.passET)
    EditText mPassET;
    @BindView(R.id.eyeIV)
    ImageView mEyeIV;

    ProgressHudUtil progressHudUtil;

    private boolean eyeOpen = false;


    @OnClick(R.id.eyeView)
    public void onEyeClick() {
        if (eyeOpen) {
            mEyeIV.setImageResource(R.drawable.j_pos_icon_dl_eye1);
            /* 设定EditText的内容为可见的 */
            mPassET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mPassET.setSelection(mPassET.length());
            eyeOpen = false;
        } else {
            eyeOpen = true;
            mEyeIV.setImageResource(R.drawable.j_pos_icon_dl_eye2);
            /* 设定EditText的内容为隐藏的 */
            mPassET.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mPassET.setSelection(mPassET.length());
        }

    }

    private boolean checkNull() {
        if (ViewUtil.isTextNull(mPhoneET)) {
            ToastUtil.makeToast("手机号不能为空");
            return true;
        } else if (!RegularUtil.isPhoneNum(ViewUtil.getText(true, mPhoneET))) {
            ToastUtil.makeToast("请输入正确的手机号");
            return true;
        }
        if (ViewUtil.isTextNull(mPassET)) {
            ToastUtil.makeToast("密码不能为空");
            return true;
        }

        return false;
    }

    @OnClick(R.id.loginBtn)
    public void onLoginClick() {
        if (!checkNull()) {
            Api.api().logon(bindUntilEvent(ActivityEvent.DESTROY), ViewUtil.getText(true, mPhoneET), ViewUtil.getText(true, mPassET), new SimpleRequestListener<LoginResultWrap>() {

                @Override
                public void onSuccess(LoginResultWrap loginResultWrap) {
                    LoginInfo loginInfo = new LoginInfo();
                    loginInfo.setPhoneNum(ViewUtil.getText(true, mPhoneET));
                    loginInfo.setPassWord(ViewUtil.getText(true, mPassET));
                    loginInfo.setToken(loginResultWrap.getToken());
                    UserInfo u = loginResultWrap.getUser();
                    String rid = JPushInterface.getRegistrationID(getApplicationContext());
                    if (!rid.isEmpty()) {
                        if (u.getPushId() != null && u.getPushId().equals(rid)) {
                          /*  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/
                        } else {
                            u.setPushId(rid);
                            HttpPushId("pushId", u.getUserPersonUuid(), rid);
                        }

                    } else {
//                        Toast.makeText(getApplicationContext(), "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
                    }
                    UserManager.getInstance().saveLoginResult(loginInfo, u);
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(ApiError error) {

                    super.onError(error);

                }

                @Override
                public void onStart() {
                    progressHudUtil.showProgressHud("登录中...", false);
                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }
            });
         /*   Api.api().logon_(bindUntilEvent(ActivityEvent.DESTROY), ViewUtil.getText(true, mPhoneET), ViewUtil.getText(true, mPassET), new SimpleRequestListener<UserInfo>() {

                @Override
                public void onSuccess(UserInfo u) {
                    LoginInfo loginInfo = new LoginInfo();
                    loginInfo.setPhoneNum(ViewUtil.getText(true, mPhoneET));
                    loginInfo.setPassWord(ViewUtil.getText(true, mPassET));
                    loginInfo.setToken(u.getTokenId());

//                    UserInfo u = loginResultWrap.getUser();


                    String rid = JPushInterface.getRegistrationID(getApplicationContext());
                    if (!rid.isEmpty()) {
                        if (u.getPushId() != null && u.getPushId().equals(rid)) {
//                            HttpFindA(token, userInfoModel, Constants.PushId_INFO_HTTP+"?action=pushId"+"&psnId="+userInfoModel.getUserPersonUuid()+"&pushId="+rid);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            u.setPushId(rid);
                            HttpPushId("pushId", u.getUserPersonUuid(), rid);
                        }
                        UserManager.getInstance().saveLoginResult(loginInfo, u);


                    } else {
                        Toast.makeText(getApplicationContext(), "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
                    }

                  *//*  Intent intent = new Intent(mActivity, MainActivity.class);
                    startActivity(intent);
                    finish();*//*
                }

                @Override
                public void onError(ApiError error) {

                    super.onError(error);

                }

                @Override
                public void onStart() {
                    progressHudUtil.showProgressHud("登录中...", false);
                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }
            });
        */
        }




    }


    @OnClick(R.id.forgetPassTV)
    public void onForgetPassClick() {
        startActivity(new Intent(this, FindPasswordActivity.class));
    }

    @Override
    public int layoutId() {
        return R.layout.a_login_layout;
    }

    @Override
    public void init() {
        //设置无标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkReLogin();

        progressHudUtil = new ProgressHudUtil(this);
        LoginInfo loginInfo = UserManager.getInstance().getLoginInfo();
        if (loginInfo != null) {
            mPhoneET.setText(loginInfo.getPhoneNum());
            mPassET.setText(loginInfo.getPassWord());
        }
        mPhoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    mPassET.setText("");
                }else {

                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (progressHudUtil != null) {
            progressHudUtil.destory();
        }
        super.onDestroy();
    }


    // 检查是否需要重新登录
    private void checkReLogin() {
        
        Intent intent = getIntent();
        boolean reLogin = intent.getBooleanExtra("reLogin", false);
        if (reLogin) {
            TheActivityManager.getInstance().toInstanceOf(this.getClass());
            UserManager.getInstance().onLoginOut();
            ToastUtil.makeToast("登录过期,请你重新登录");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkReLogin();
    }

    @Override
    public boolean supportActionbar() {
        return false;
    }

    @Override
    public int getThemeId() {
        return R.style.TranlucentStatusTheme;
    }

    private void HttpPushId(String pushId, String userPersonUuid, String rid) {
        Api.api().pushId(pushId, userPersonUuid, rid, new SimpleRequestListener() {

            @Override
            public void onSuccess(Object o) {
//                ToastUtil.makeToast("pushId更新成功！");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(ApiError error) {

                super.onError(error);

            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud("提交中...", false);
            }

            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }
        });
    }
    private int mSecretNumber = 0;
    private static final long MIN_CLICK_INTERVAL = 600;//间隔时间0.6秒内
    private long mLastClickTime;
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        if (keyCode == KeyEvent.KEYCODE_HOME) {//后门
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - mLastClickTime;
            mLastClickTime = currentClickTime;

            if (elapsedTime < MIN_CLICK_INTERVAL&&elapsedTime>100) {
                ++mSecretNumber;
                if (6 == mSecretNumber) {//点击次数等于6次后做后门操作
                    TheActivityManager.getInstance().finishAll();
                    UserManager.getInstance().onLoginOut();
                }
            } else {
                mSecretNumber = 0;
            }
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
