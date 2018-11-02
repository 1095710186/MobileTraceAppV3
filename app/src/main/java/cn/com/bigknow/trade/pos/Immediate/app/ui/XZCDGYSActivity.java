package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.CDGYSAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;

/**
 * Created by hujie on 16/10/15.
 *
 * 选择产地供应商
 */

public class XZCDGYSActivity extends BaseActivity {


    @BindView(R.id.refreshLayout)
    SimpleSwipeRefreshLayout refreshLayout;

    CDGYSAdapter mAdapter;
    @AutoBundleField
    FoodInfo foodInfo;

    @AutoBundleField(required = false)
    boolean isEdit = false;


    @Override
    protected boolean autoBindBundle() {
        return true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {
         if (event.type ==SimpleEventType.ON_JH_ADD_){
            foodInfo = (FoodInfo)event.objectEvent;
             refreshLayout.startRefresh();
         }
    }

    @OnClick(R.id.a_foodentry_tv_xz)
    public void onXzOnClick(){ //新增
//        Intent intent = new Intent(this, FoodInfoUpdateActivity.class);
//        intent.putExtra("foodInfo",foodInfo);
//        intent.putExtra("type",SimpleEventType.ON_JH_ADD_BACK);
//        startActivity(intent);
        startActivity(FoodInfoUpdateActivityAutoBundle.createIntentBuilder(foodInfo).build(mActivity));
    }
    @Override
    public void init() {

        if (isEdit) {
            setTitle("修改产地供应商");
        }

        mAdapter = new CDGYSAdapter();
        addHeadView();
        refreshLayout.setAdapter(mAdapter);

        if (foodInfo != null && foodInfo.getVendorId() != null) {
            mAdapter.setSelectAreaBeanId(foodInfo.getVendorId());
        }

        refreshLayout.setOnRefreshListener(new SimpleSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        refreshLayout.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (isEdit) {

                    foodInfo.setVendorId(((VendorAreaBean) baseQuickAdapter.getItem(i)).getId());
                    foodInfo.setVendor(((VendorAreaBean) baseQuickAdapter.getItem(i)).getVendor());
                    foodInfo.setArea(((VendorAreaBean) baseQuickAdapter.getItem(i)).getProdArea());

                    EventMamager.getInstance().postObjEvent(SimpleEventType.ON_JH_CDGYS_EDIT_BACK, foodInfo);
                    finish();
                } else {

                    showDialog((VendorAreaBean) baseQuickAdapter.getItem(i));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.startRefresh();
    }

    private void showDialog(VendorAreaBean bean) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dd_weight_num_keyboard_layout, null);
        TextView nameTV = (TextView) view1.findViewById(R.id.nameTV);
        TextView cdTV = (TextView) view1.findViewById(R.id.gysTV);
        TextView gysTV = (TextView) view1.findViewById(R.id.cdTV);
        TextView showTV = (TextView) view1.findViewById(R.id.showTV);
        ImageView headIV = (ImageView) view1.findViewById(R.id.headIV);
        TextView showJ = (TextView) view1.findViewById(R.id.showJ);
        TextView showT = (TextView) view1.findViewById(R.id.showT);
        NumKeyboardView numkeyboardView = (NumKeyboardView) view1.findViewById(R.id.numkeyboardView);
        showJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showT.setVisibility(View.VISIBLE);
                showJ.setVisibility(View.GONE);
                if (numkeyboardView.getNumValue()>0){
//                    showTV.setText("");
                    numkeyboardView.onKeyBoardClick.onCleanAll();
                }
            }
        });
        showT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJ.setVisibility(View.VISIBLE);
                showT.setVisibility(View.GONE);
                if (numkeyboardView.getNumValue()>0){
//                    showTV.setText("");
                    numkeyboardView.onKeyBoardClick.onCleanAll();//onClickClear();

                }
            }
        });
        numkeyboardView.setShowTextView(showTV);

        nameTV.setText(foodInfo.getAlias());//getItemName());

        UserManager.getInstance().loadFoodHeadImage(this, headIV, foodInfo.getImgId());

        cdTV.setText(bean.getProdArea());
        gysTV.setText(bean.getVendor());


        AlertDialogUtil.showNumKeyboardDialog(mActivity, view1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sureBtn) {
                    if (numkeyboardView.getNumValue() == 0) {
                        ToastUtil.makeToast("请输入重量");
                    } else {
                        if (numkeyboardView.getNumValue() < 1) {
                            ToastUtil.makeToast("重量不能低于1");
                            return;
                        }

                        EntityManager.saveFoodSortCount(foodInfo.getId());
                        if (showJ.getVisibility() == View.VISIBLE) {
                            foodInfo.setQty(numkeyboardView.getNumValue());
                        }else {
                            foodInfo.setQty(numkeyboardView.getNumValue()*2000);
                        }
                        foodInfo.setVendorId(bean.getId());
                        foodInfo.setVendor(bean.getVendor());
                        foodInfo.setArea(bean.getProdArea());
                        AlertDialogUtil.dissMiss();

                        EventMamager.getInstance().postObjEvent(SimpleEventType.ON_JH_BACK, foodInfo);

                        EventMamager.getInstance().postObjEvent(SimpleEventType.ON_JH_ADD_BACK, foodInfo);
                        finish();
                    }

                }
            }
        });
    }


    private void getData() {
        Api.api().getVendorList(bindUntilEvent(ActivityEvent.DESTROY), isEdit ? foodInfo.getItemId() : foodInfo.getId(), foodInfo.getMerchantId(), new SimpleRequestListener<BaseEntity<List<VendorAreaBean>>>() {
            @Override
            public void onSuccess(BaseEntity<List<VendorAreaBean>> o) {
                if (o != null && o.getData().size() > 0) {
//                    refreshLayout.setRefreshEnable(false);
                    mAdapter.setNewData(o.getData());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFinish() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void addHeadView() {
        View view = LayoutInflater.from(this).inflate(R.layout.xzcdgys_head_layout, null);
        TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        ImageView headIV = (ImageView) view.findViewById(R.id.headIV);

        nameTV.setText(foodInfo.getAlias());//getItemName());

        UserManager.getInstance().loadFoodHeadImage(this, headIV, foodInfo.getImgId());

        mAdapter.addHeaderView(view);

    }

    @Override
    public int layoutId() {
        return R.layout.a_cd_gys_layout;
    }
}
