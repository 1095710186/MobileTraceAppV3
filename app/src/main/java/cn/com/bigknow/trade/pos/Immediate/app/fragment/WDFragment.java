package cn.com.bigknow.trade.pos.Immediate.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle.android.FragmentEvent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.ui.AboutInfoActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.ChangePasswordActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.FoodEntryActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MyMsgTypeActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MyselfInfoActivity;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.UserInfo;

/**
 * Created by
 */

public class WDFragment extends BaseSupportFragment {

    @BindView(R.id.ivAvator)
    RoundedImageView ivAvator;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvUserType)
    TextView tvUserType;



//    public String IMG_URL = ModelConfig.BIGKNOW_FRAMEWORK_URL + "servlet/fileupload?oper_type=img&masterFileType=psnPhoto&masterId=" + UserManager.getInstance().getUserInfo().getUserPersonUuid();

    public static WDFragment newInstance() {
        Bundle args = new Bundle();
        WDFragment fragment = new WDFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        UserManager.getInstance().loadHeadImage(getActivity(),ivAvator);

    }



    public void initLazyView() {
        Api.api().getUserInfo(bindUntilEvent(FragmentEvent.DESTROY), UserManager.getInstance().getUserInfo().getUserId(), new SimpleRequestListener<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                UserInfo userInfo1 = UserManager.getInstance().getUserInfo();
//                mTvName.setText(userInfo.getUserName());
                tvUserType.setText(userInfo1.getOrgName());
                tvShopName.setText(userInfo1.getAdminOrgName());
                tvName.setText(userInfo.getPsnName());
              /*  tvUserType.setText(userInfo.getOrgType());
                tvShopName.setText(userInfo.getOrgName());*/

            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.f_wd_layout;
    }


    @OnClick(R.id.f_wd_tv_cpgl)
    public void onCpglOnClick() {//菜品管理
        Intent intent = new Intent(getActivity(), FoodEntryActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnGotoMyInfo)
    public void onWdzlOnClick() {//我的资料
        Intent intent = new Intent(getActivity(), MyselfInfoActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.f_wd_tv_wdxx)
    public void onWdmessagexxOnClick() {//我的消息
        Intent intent = new Intent(getActivity(), MyMsgTypeActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.f_wd_tv_xgmm)
    public void onXgmmOnClick() {//修改密码
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        getActivity().startActivity(intent);
    }
    @OnClick(R.id.f_wd_tv_gy)
    public void onGyOnClick() {//关于
        Intent intent = new Intent(getActivity(), AboutInfoActivity.class);
        getActivity().startActivity(intent);
    }
}
