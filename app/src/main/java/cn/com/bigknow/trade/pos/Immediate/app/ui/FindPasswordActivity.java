package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.RegularUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ViewUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.ClearEditText;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * 忘记密码
 * Created by hujie on 2016/8/12 11:24
 */
public class FindPasswordActivity extends BaseActivity {
    @BindView(R.id.edPhone)
    ClearEditText mEdPhone;
    @BindView(R.id.edCode)
    EditText mEdCode;
    @BindView(R.id.edNewPassword)
    EditText mEdNewPassword;
    @BindView(R.id.eyeIV)
    ImageView mEyeIV;
    @BindView(R.id.tvCodeStatus)
    TextView mTvCodeStatus;
    @BindView(R.id.btnGetCode)
    Button mBtnGetCode;
    @BindView(R.id.edIDCode)
    ClearEditText mEdIDCode;

    private TimeCount time;
    private ProgressHudUtil progressHudUtil;

    @OnClick(R.id.btnGetCode)
    public void onGetCodeClick() {
        if (!checkPhone()) {//手机号输入正确
            Api.api().getSmsCode(mEdPhone.getText().toString(), new SimpleRequestListener() {
                @Override
                public void onSuccess(Object o) {
                    mTvCodeStatus.setVisibility(View.VISIBLE);
                    mBtnGetCode.setSelected(true);
                    mBtnGetCode.setClickable(false);//防止重复点击
                    mEdPhone.setEnabled(false);
                    mTvCodeStatus.setText("已向您的手机号" + getHidePhone(mEdPhone.getText().toString()) + "发送验证码，请注意查收。");
                    time.start();
                }

                @Override
                public void onError(ApiError error) {
                    super.onError(error);
                }

                @Override
                public void onStart() {
                    progressHudUtil.showProgressHud("正在发送...", false);
                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }
            });

        }
    }

    @OnClick(R.id.btnOk)
    public void onOkClick() {
        if (!checkPhoneAll()) {//提交
            Api.api().resetPassword(mEdPhone.getText().toString(), mEdIDCode.getText().toString(), mEdCode.getText().toString(), mEdNewPassword.getText().toString(), mEdNewPassword.getText().toString(), new SimpleRequestListener() {
                @Override
                public void onSuccess(Object o) {
                    ToastUtil.makeToast("密码重置成功！");
                    FindPasswordActivity.this.finish();
                }

                @Override
                public void onError(ApiError error) {
                    super.onError(error);
                }

                @Override
                public void onStart() {
                    progressHudUtil.showProgressHud("正在提交...", false);
                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }
            });

        }
    }

    private boolean eyeOpen = false;

    @OnClick(R.id.eyeView)
    public void onEyeClick() {
        mEyeIV.setImageResource(eyeOpen ? R.drawable.j_pos_icon_dl_eye1 : R.drawable.j_pos_icon_dl_eye2);
        mEdNewPassword.setTransformationMethod(eyeOpen ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        mEdNewPassword.setSelection(mEdNewPassword.length());
        eyeOpen = !eyeOpen;
    }

    private boolean checkPhone() {
        if (ViewUtil.isTextNull(mEdPhone)) {
            ToastUtil.makeToast("手机号不能为空");
            return true;
        } else if (!RegularUtil.isPhoneNum(ViewUtil.getText(true, mEdPhone))) {
            ToastUtil.makeToast("请输入正确的手机号");
            return true;
        }
        if (ViewUtil.isTextNull(mEdIDCode)) {
            ToastUtil.makeToast("身份证后六位不能为空");
            return true;
        }
        return false;
    }

    private boolean checkPhoneAll() {
        if (ViewUtil.isTextNull(mEdPhone)) {
            ToastUtil.makeToast("手机号不能为空");
            return true;
        } else if (!RegularUtil.isPhoneNum(ViewUtil.getText(true, mEdPhone))) {
            ToastUtil.makeToast("请输入正确的手机号");
            return true;
        }
        if (ViewUtil.isTextNull(mEdIDCode)) {
            ToastUtil.makeToast("身份证后六位不能为空");
            return true;
        }
        if (ViewUtil.isTextNull(mEdCode)) {
            ToastUtil.makeToast("验证码不能为空");
            return true;
        }
        if (ViewUtil.isTextNull(mEdNewPassword)) {
            ToastUtil.makeToast("新密码不能为空");
            return true;
        }

        return false;
    }

    @Override
    public void init() {
        time = new TimeCount(90000, 1000);
        progressHudUtil = new ProgressHudUtil(this);
    }

    @Override
    public int layoutId() {
        return R.layout.a_findpassword_layout;
    }

    private String getHidePhone(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7, 11);
    }

    @Override
    protected void onDestroy() {
        if (progressHudUtil != null) {
            progressHudUtil.destory();
        }
        super.onDestroy();
        time.cancel();//销毁计时器
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mBtnGetCode.setText("获取验证码");
            mBtnGetCode.setClickable(true);
            mBtnGetCode.setSelected(false);
            mTvCodeStatus.setVisibility(View.INVISIBLE);
            mEdPhone.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mBtnGetCode.setText(millisUntilFinished / 1000 + "秒后重发");
        }
    }
}
