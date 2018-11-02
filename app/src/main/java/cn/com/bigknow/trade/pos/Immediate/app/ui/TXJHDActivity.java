package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.adapter.TXJHDAdapter;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryDsMain;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.IdResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by hujie on 16/10/20.
 *
 *
 * 进货菜品列表  填写进货单
 */

public class TXJHDActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SimpleSwipeRefreshLayout refreshLayout;

    TXJHDAdapter mAdapter;

    @AutoBundleField
    TXJHDWrapper wrapper;

    @AutoBundleField(required = false)
    boolean isEdit = false;

    boolean isAdd = false;
    FoodInfo foodInfoADD;

    @BindView(R.id.cpTV)
    TextView mCpTV;
    @BindView(R.id.tvZ)
    TextView tvZ;
    @BindView(R.id.kindsTV)
    TextView mKindsTV;
    @BindView(R.id.hjTV)
    TextView mHjTV;

    @BindView(R.id.kindsTV_)
    TextView mKindsTV_;
    @BindView(R.id.hcTV)
    TextView mHcTV;

    @BindView(R.id.hjLL)
    LinearLayout mHjLL;
    @BindView(R.id.addTV)
    TextView mAddTV;
    @BindView(R.id.submitTV)
    TextView mSubmitTV;
    String Where = null;

    DecimalFormat df=new   java.text.DecimalFormat("#.##");

    @Override
    public void onBackPressed() {

        if (Where!=null && Where.equals("PUSH")){
            startActivity(new Intent(TXJHDActivity.this,MyMsgTypeActivity.class));
            finish();
        }
        if (!isEdit) {
            showExitJHDialog(0);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean showBackNavication() {

        return super.showBackNavication();


    }

    @Override
    public void init() {
        Where = getIntent().getStringExtra("WHERE");
        if (Where!=null && Where.equals("PUSH")){
            isEdit = true;
            wrapper = (TXJHDWrapper) getIntent().getSerializableExtra("wrapper");
        }
        mAdapter = new TXJHDAdapter();
        refreshLayout.setAdapter(mAdapter);
        refreshLayout.setRefreshEnable(false);

        if (isEdit) {
//            mAddTV.setVisibility(View.GONE);
//            mSubmitTV.setText("保存");
        }

        setData();
    }

    @Override
    protected boolean autoBindBundle() {
        return true;
    }

    private void setData() {
        if (wrapper.getChePai() != null) {
            mCpTV.setText(wrapper.getChePai().getChePai());
        }

        mAdapter.setNewData(wrapper.getFoodInfos());
        if (wrapper.getTotal()!=null && wrapper.getTotal().toString().trim().length()>0) {
            tvZ.setText(df.format(Float.valueOf(wrapper.getTotal())) + "吨");
        }else {
            tvZ.setText("0" + "吨");
        }
        if (wrapper.getFoodInfos().size() > 0) {
            mHjLL.setVisibility(View.VISIBLE);
            mKindsTV.setText("共" + wrapper.getFoodInfos().size() + "种");
            mKindsTV_.setText("共" + wrapper.getFoodInfos().size() + "种");


            float total = 0;
            for (FoodInfo foodInfo : wrapper.getFoodInfos()) {
                total += foodInfo.getQty();
            }
            BigDecimal numHj = new BigDecimal(total);
            mHjTV.setText(df.format(numHj) + "");
            if (wrapper.getTotal()!=null && wrapper.getTotal().toString().trim().length()>0) {
                float zTotal = Float.valueOf(wrapper.getTotal())*2000;
                BigDecimal numZp = new BigDecimal(zTotal);
                mHcTV.setText(df.format(numZp.subtract(numHj)));
            }else {
                BigDecimal numP = new BigDecimal(0-total);
                mHcTV.setText(df.format(numP));
            }

        } else {
            mHjLL.setVisibility(View.GONE);
        }
    }

    //更改车牌
    @OnClick(R.id.ggcpBtn)
    public void ggcpBtn() {
        startActivity(TXJHDCPActivityAutoBundle.createIntentBuilder().wrapper(wrapper).build(this));
    }

    @Override
    public boolean autoBindEvent() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SimpleEvent event) {
        if (event.type == SimpleEventType.ON_CHANGE_CHEPAI_BACK) {
            wrapper = (TXJHDWrapper) event.objectEvent;
        } else if (event.type == SimpleEventType.ON_JH_BACK) {
            FoodInfo foodInfoADD = (FoodInfo) event.objectEvent;
            foodInfoADD.setItemId(foodInfoADD.getId());
            wrapper.getFoodInfos().add(foodInfoADD);
        } else if (event.type == SimpleEventType.ON_EDIT_FOOD_ZL_CLICK) {
            FoodInfo foodInfo = (FoodInfo) event.objectEvent;
            showDialog(foodInfo);
        } else if (event.type == SimpleEventType.ON_EDIT_FOOD_CDGYS_CLICK) {
            selectFoodInfo = (FoodInfo) event.objectEvent;
//            wrapper.getFoodInfos().set((FoodInfo) event.objectEvent);
            startActivity(XZCDGYSActivityAutoBundle.createIntentBuilder(selectFoodInfo).isEdit(true).build(this));
        } else if (event.type == SimpleEventType.ON_JH_CDGYS_EDIT_BACK) {
            FoodInfo foodInfo = (FoodInfo) event.objectEvent;
            if (selectFoodInfo != null) {
                /*for (FoodInfo info: wrapper.getFoodInfos()){
                    if (info.getItemId().equals(foodInfo.getItemId())){
                        info = foodInfo;
                    }
                }*/
                int i = mAdapter.getData().indexOf(selectFoodInfo);
                wrapper.getFoodInfos().set(i,foodInfo);
                /*mAdapter.remove(i);
                mAdapter.addData(i, foodInfo);*/

            }
        } else if (event.type == SimpleEventType.ON_JH_FOOD_DELETE_CLICK) {

                int i =(int)event.objectEvent;
                wrapper.getFoodInfos().remove(i);


        }else if (event.type ==SimpleEventType.ON_JH_ADD_BACK){
//           foodInfoADD = (FoodInfo) event.objectEvent;
//            foodInfoADD.setItemId(foodInfoADD.getId());
//            wrapper.getFoodInfos().add((FoodInfo) event.objectEvent);
            //进货单---新增菜品
        }
        setData();
    }

    private FoodInfo selectFoodInfo;

    private void showDialog(FoodInfo foodInfo) {
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

        nameTV.setText(foodInfo.getAlias());

        UserManager.getInstance().loadFoodHeadImage(this, headIV, foodInfo.getImgId());

        cdTV.setText(foodInfo.getArea());
        gysTV.setText(foodInfo.getVendor());

        AlertDialogUtil.showNumKeyboardDialog(mActivity, view1, view -> {
            if (view.getId() == R.id.sureBtn) {
                if (numkeyboardView.getNumValue() == 0) {
                    ToastUtil.makeToast("请输入重量");
                } else {
                    if(numkeyboardView.getNumValue()<1)
                    {
                        ToastUtil.makeToast("重量不能低于1");
                        return;
                    }
                    if (showJ.getVisibility() == View.VISIBLE) {  //斤
                        foodInfo.setQty(numkeyboardView.getNumValue());
                    }else { //吨
                        foodInfo.setQty(numkeyboardView.getNumValue()*2000);
                    }
                    AlertDialogUtil.dissMiss();
                    mAdapter.notifyDataSetChanged();
                    setData();
                }
            }
        });
    }


    @OnClick(R.id.addTV)
    public void onAddClick() {

        Intent intent = new Intent(this, JHChooseFoodActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.submitTV)
    public void onSubmitClick() {

        if (wrapper.getChePai() == null) {
//            ToastUtil.makeToast("请填写车牌");
//            return;
        }
        if (wrapper.getFoodInfos().size() == 0) {
            ToastUtil.makeToast("请添加菜品");
            return;
        }
       /* if (wrapper.getFileId() == null) {
            ToastUtil.makeToast("请上传过磅单据");
            return;
        }*/
        requstSave();
    }

    private void requstSave() {
        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        List<RequestWrap> requestWraps = new ArrayList<>();

        RequestWrap<FoodEntryDsMain> requestWrap = new RequestWrap<>();
        requestWrap.name = "dsMain";
        List<FoodEntryDsMain> list = new ArrayList<>();

        FoodEntryDsMain dsMain = new FoodEntryDsMain();


        if (isEdit) {
            dsMain.id = wrapper.getId();
            dsMain.transType = "C";
            dsMain.state = wrapper.getState();
            if (wrapper.getChePai()!=null) {
                dsMain.searchNo = wrapper.getChePai().getChePai();
            }
            dsMain.kinds = wrapper.getFoodInfos().size() + "";
            dsMain.itemQty = mHjTV.getText().toString();
            dsMain.totalQty = wrapper.getTotal();
            dsMain.fileId = wrapper.getFileId();
            dsMain.updateDate = wrapper.getUpdateDate();
        } else {
            dsMain.transType = "C";
            if (wrapper.getChePai()!=null) {
                dsMain.searchNo = wrapper.getChePai().getChePai();
            }
            dsMain.kinds = wrapper.getFoodInfos().size() + "";
            dsMain.itemQty = mHjTV.getText().toString();
            dsMain.totalQty = wrapper.getTotal();
            dsMain.fileId = wrapper.getFileId();

        }


        list.add(dsMain);

        requestWrap.data = list;
        requestWraps.add(requestWrap);

        RequestWrap<FoodEntryInfo> requestWrap1 = new RequestWrap<FoodEntryInfo>();
        requestWrap1.name = "dsDet";


        List list1 = new ArrayList<>();

        for (FoodInfo foodInfo : wrapper.getFoodInfos()) {
            if (foodInfo.getUnitId() == null) {
                foodInfo.setUnitId(foodInfo.getPurUnitId());
            }

//                foodInfo.setItemId(foodInfo.getId());
        }
        list1.addAll(wrapper.getFoodInfos());
        requestWrap1.data = list1;
        requestWraps.add(requestWrap1);

        simpleRequestWrap.dataset = requestWraps;


//        String json = JSON.toJSONString(simpleRequestWrap);

        Api.api().submitFoodEntry(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener<IdResultWrap>() {
            @Override
            public void onStart() {
                super.onStart();
                progressHudUtil.showProgressHud("正在提交数据，请稍后", false);
            }

            @Override
            public void onSuccess(IdResultWrap o) {
                ToastUtil.makeToast("操作成功");
                EventMamager.getInstance().postEvent(SimpleEventType.ON_JH_REFRESH);
                EventMamager.getInstance().postEvent(SimpleEventType.ON_CLEANJ_KC_BACK);// 更新卖菜列表
                finish();
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
                progressHudUtil.dismissProgressHud();
            }

            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }
        });
    }

    Display display;
    static AlertDialog dialog;

    public void dissclose() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
    //弹出提示框 退出
    private void showExitJHDialog(int type) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_wxit_jh, null);

        View cd_back = (View) view1.findViewById(R.id.cd_layout_quxiao);
        View cd_sure = (View) view1.findViewById(R.id.cd_layout_sure);
        TextView tvNews = (TextView) view1.findViewById(R.id.tvNews);
        if (type ==4){
            tvNews.setText("确定要回到主页？");
        }
        cd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();
            }
        });
        cd_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();
                finish();
            }
        });
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new AlertDialog.Builder(mActivity)
                .create();
        dialog.show();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        WindowManager windowManager = mActivity.getWindowManager();
        display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth())*3/4; //设置宽度
//        lp.height = (int) (display.getHeight())*1/3;
//                (int) (DensityUtil.dip2px(mActivity, 550));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view1);

    }
    private long firstTime = 0;

    private long jianClickTime;
    private long jiaClickTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            if (!isEdit) {
                showExitJHDialog(0);
            }

        }
        else  if (keyCode == KeyEvent.KEYCODE_HOME) {
            Message message = Message.obtain();
//            message.obj = data;


            if (!isEdit) {   // 进货
//                message.what = 0;
                showExitJHDialog(0);

            }else {
//                message.what = 1;
                finish();
            }
//            handler.sendMessage(message);
            return true;
//            showExitJHDialog(4);
        }else if(keyCode == KeyEvent.KEYCODE_MENU) {//MENU键
            //监控/拦截菜单键
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN ||keyCode==KeyEvent.KEYCODE_VOLUME_UP) {//音量减小增大


            if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    jianClickTime=System.currentTimeMillis();
                }
            }
            if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    jiaClickTime=System.currentTimeMillis();
                }
            }




            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 100) {                                         //如果两次按键时间间隔大于0.1秒，则不操作
                firstTime = secondTime;//更新firstTime
            } else {

                if(jianClickTime>=jiaClickTime){
                    if(jianClickTime-jiaClickTime<100){
                        //两次按键小于0.1秒时，打开设置界面
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                }else{
                    if(jiaClickTime-jianClickTime<100){
                        //两次按键小于0.1秒时，打开设置界面
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                }


            }

        }
        return super.onKeyDown(keyCode, event);

    }
    private Handler handler = new Handler() {

        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {
//           int m = (int)msg.obj;
            if(msg.what == 0){ //jh
                showExitJHDialog(0);
            }else if (msg.what == 1){
                finish();
            }
        }
    };

    @Override
    public int layoutId() {
        return R.layout.txjhd_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        enableHomeKeyDispatched(false);
        super.onCreate(savedInstanceState);

//        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

    }
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d("mActivity_",mActivity.getClass().getSimpleName());
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) { // 监听home键
                LogUtil.d("mActivity_",mActivity.getClass().getSimpleName());
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (!isEdit) {   // 进货
                    showExitJHDialog(0);

                }else {
                    finish();
                }
                // 表示按了home键,程序到了后台

            }
        }
    };
    private void enableHomeKeyDispatched(boolean enable) {
        final Window window = getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        if (enable) {
            lp.flags |= 0x80000000;//WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED;
        } else {
            lp.flags &= ~0x80000000;//WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED;
        }
        window.setAttributes(lp);
    }
}
