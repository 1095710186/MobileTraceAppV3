package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.ImageUtils;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.PhotoUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UUIDUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.activitymanager.TheActivityManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FileImgWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by laixy on 2016/10/11.
 * 个人信息
 */


public class MyselfInfoActivity extends BaseActivity {
    @BindView(R.id.ivAvator)
    RoundedImageView ivAvator;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvUserType)
    TextView tvUserType;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvWenhua)
    TextView tvWenhua;
    @BindView(R.id.tvIDNo)
    TextView tvIDNo;
    @BindView(R.id.tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.tvShichang)
    TextView tvShichang;
    @BindView(R.id.tvShopNo)
    TextView tvShopNo;
    @BindView(R.id.tvBindPhone)
    TextView tvBindPhone;


    public AlertDialog.Builder builder;
    public AlertDialog dialog;


    private String uploadSession = UUIDUtil.generateFormattedGUID();
    private String userId = UserManager.getInstance().getUserInfo().getUserId();
    private String orgId = UserManager.getInstance().getUserInfo().getOrgId();

    @OnClick(R.id.btnGotoShopInfo)
    public void gotoShopInfo() {
        Intent intent = new Intent(this, MerchantInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ivAvator)
    public void uodateAcator() {
        PhotoUtil.open(this, true, 1);
    }

    @OnClick(R.id.exitBtn)
    public void exitLogin() {
        dialog.show();
    }

    @Override
    public void init() {
        setTitle("我的资料");
        Api.api().getUserInfo(bindUntilEvent(ActivityEvent.DESTROY), UserManager.getInstance().getUserInfo().getUserId(), new SimpleRequestListener<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                UserInfo userInfo1 = UserManager.getInstance().getUserInfo();
//                mTvName.setText(userInfo.getUserName());
                tvUserType.setText(userInfo1.getOrgName());
                tvShopName.setText(userInfo1.getAdminOrgName());
                tvName.setText(userInfo.getPsnName());
              /*  tvUserType.setText(userInfo.getOrgType());
                tvShopName.setText(userInfo.getOrgName());*/
                tvSex.setText(userInfo.getSexText());
                tvWenhua.setText(userInfo.getEduName());
                tvIDNo.setText(userInfo.getIdNo());
                tvBirthday.setText(userInfo.getBirthDate());
                tvShichang.setText(userInfo.getOrgName());
                tvShopNo.setText(userInfo.getMerchantNo());
                tvBindPhone.setText(userInfo.getMobile());
            }

            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onStart() {
                progressHudUtil.showProgressHud(false);
            }
        });
        createDialog(this);
    }

    public void createDialog(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要退出登录？");
        builder.setPositiveButton("确定", (dialog, id) -> {
            Api.api().exitLogin(bindUntilEvent(ActivityEvent.DESTROY), new SimpleRequestListener() {
                @Override
                public void onError(ApiError error) {
                    super.onError(error);
                    TheActivityManager.getInstance().toInstanceOf(this.getClass());
                    UserManager.getInstance().onLoginOut();
                    Toast.makeText(MyselfInfoActivity.this, "退出成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MyselfInfoActivity.this, LoginActivity.class);

                    startActivity(intent);
                    finish();
                }

                @Override
                public void onSuccess(Object o) {
                    BaseEntity entity = (BaseEntity) o;
                    LogUtil.v("exitLogin", o.toString());
                    if (entity.getSuccess() == 1) {
                        TheActivityManager.getInstance().toInstanceOf(this.getClass());
                        UserManager.getInstance().onLoginOut();
                        Toast.makeText(MyselfInfoActivity.this, "退出成功", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MyselfInfoActivity.this, LoginActivity.class);
//
                        startActivity(intent);
                        finish();

                    }
                }
            });
        });
        builder.setNegativeButton("取消", (dialog, id) -> {
            dialog.dismiss();
        });
        dialog = builder.create();
    }

    @Override
    public int layoutId() {
        return R.layout.a_myself_layout;
    }


    @Override
    protected void onResume() {
        super.onResume();
        UserManager.getInstance().loadHeadImage(this, ivAvator);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理图片选择器的返回值
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get Image Path List
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (pathList != null && pathList.size() > 0) {
                String filePah = pathList.get(0);
                if (new File(filePah).exists()) {
                    FileImgWrapper fileImgWrapper = new FileImgWrapper();
                    fileImgWrapper.setMasterId(UserManager.getInstance().getUserInfo().getUserPersonUuid());
                    fileImgWrapper.setFilePath(filePah);
                    //如果mImgWrapperInfos或者mOKImgWrapperInfos为空那么有可能就是新增菜品或者就是菜品修改没有图片
//                    if (mImgWrapperInfos == null) mImgWrapperInfos = new ArrayList<>();
                    //从图片选择器选择一个图片后要回到当前界面，封装一个Wrapper，加入到列表，供后面点击保存按钮时去用
                    fileImgWrapper.setMasterType("psnPhoto");
                    fileImgWrapper.setPosition(1);
//                    mImgWrapperInfos.add(fileImgWrapper);
                    uploadHead(fileImgWrapper);
//                    setImageViews();
                }
            } else {
                ToastUtil.makeToast("图片不存在");
            }
        }
    }

    /**
     * 上传图片接口
     *
     * @param fileImgWrappers
     */
    public void uploadHead(FileImgWrapper fileImgWrappers) {

        File file = new File(fileImgWrappers.filePath);
        if (file.exists()) {
            byte[] fileByte = null;
            Bitmap bitmap = ImageUtils.getScaleBitmap(fileImgWrappers.filePath);
            if (bitmap == null) {
                ToastUtil.makeToast("图片不存在");
                return;
            }

            progressHudUtil.showProgressHud();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100表示不压缩，将不压缩的数据存放到baos中
            int per = 100;
            int size = baos.toByteArray().length / 1024;

            while (size > 100 ) { // 循环判断如果压缩后图片是否大于400kb,大于继续压缩
                if (per > 10) {
                    per -= 10;// 每次都减少10
                } else {
                    break;
                }
                baos.reset();// 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, per, baos);// 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
                size = baos.toByteArray().length / 1024;
            }
            try {
                baos.flush();
                baos.close();
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                    System.gc();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileByte = baos.toByteArray();
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), fileByte);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("file", file.getName(), photoRequestBody);
            Api.api().uploadHead(bindUntilEvent(ActivityEvent.DESTROY), ModelConfig.UPLOAD, photo, uploadSession, fileImgWrappers.getMasterId(), fileImgWrappers.masterType, userId, orgId, new SimpleRequestListener() {
                @Override
                public void onSuccess(Object o) {

                    fileImgWrappers.setFileId(((BaseEntity) o).fileid);
                    if (((BaseEntity) o).getSuccess() == 1) {
                        ToastUtil.makeToast("头像" + "上传成功");
                        UserInfo u = UserManager.getInstance().getUserInfo();
                        u.setHeadImg(((BaseEntity) o).fileid);
//                        UserManager.getInstance().updateUserInfo(loginInfo, u);
                        ivAvator.setImageBitmap(null);
//                        ivAvator.setImageURI( Uri.parse((String) fileImgWrappers.filePath));
                        UserManager.getInstance().loadHeadImage(mActivity, ivAvator);
                    } else {
                        ToastUtil.makeToast("头像" + "上传失败");
                    }

                }

                @Override
                public void onError(ApiError error) {
                    super.onError(error);
                    ToastUtil.makeToast("头像" + "上传失败");

                }

                @Override
                public void onStart() {
                    progressHudUtil.showProgressHud("头像上传中...", false);
                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }
            });
        }

    }

}
