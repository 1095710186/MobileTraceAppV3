package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.ClearEditText;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by laixiaoyang on 2016/
 * 修改密码
 */


public class ChangePasswordActivity extends BaseActivity {

    /**
     * 旧密码
     */
    @BindView(R.id.activity_changepwd_edtYpwd)
    ClearEditText edtYpwd;

    /**
     * 控制旧密码显示于与否
     */
    @BindView(R.id.activity_changepwd_eyeIV)
    ImageView imvYeyeIv;


    /**
     * 新密码
     */
    @BindView(R.id.activity_changepwd_edtNewPassword)
    EditText edtNewPwd;
    /**
     * 控制xin密码显示于与否
     */
    @BindView(R.id.activity_changepwd_neweyeIV)
    ImageView imvNeweyeIv;
    /**
     * 修改
     */
    @BindView(R.id.activity_changepwd_btnSmit)
    Button btnSubmit;

    private ProgressHudUtil progressHudUtil;
    private boolean eyeOpenY = false;

    @OnClick(R.id.activity_changepwd_eyeIV)
    public void onYEyeClick() {
        imvYeyeIv.setImageResource(eyeOpenY ? R.drawable.j_pos_icon_dl_eye1 : R.drawable.j_pos_icon_dl_eye2);
        edtYpwd.setTransformationMethod(eyeOpenY ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        edtYpwd.setSelection(edtYpwd.length());
        eyeOpenY = !eyeOpenY;
    }
    private boolean eyeOpenNew = false;

    @OnClick(R.id.activity_changepwd_eyeIV)
    public void onNewEyeClick() {
        imvNeweyeIv.setImageResource(eyeOpenNew ? R.drawable.j_pos_icon_dl_eye1 : R.drawable.j_pos_icon_dl_eye2);
        edtNewPwd.setTransformationMethod(eyeOpenNew ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        edtNewPwd.setSelection(edtNewPwd.length());
        eyeOpenNew = !eyeOpenNew;
    }
    @OnClick(R.id.activity_changepwd_btnSmit)
    public void btnSumbitChange(){ //修改
        String userId = UserManager.getInstance().getUserInfo().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            requestPwd(userId);
        }
    }




    @Override
    public void init() {
        progressHudUtil = new ProgressHudUtil(this);


    }

    @Override
    public int layoutId() {
        return R.layout.a_changepassword_layout;
    }

    public void requestPwd(String userId) {
        Api.api().changePassword(userId, edtYpwd.getText().toString(), edtNewPwd.getText().toString(), edtNewPwd.getText().toString(), new SimpleRequestListener() {
            @Override
            public void onSuccess(Object o) {
                ToastUtil.makeToast("密码修改成功！");
                ChangePasswordActivity.this.finish();
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
