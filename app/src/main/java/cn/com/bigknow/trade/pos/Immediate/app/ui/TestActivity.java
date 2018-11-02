package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Intent;
import android.os.Bundle;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.RegularUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BillDetailbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ChePai;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import cn.jpush.android.api.JPushInterface;

public class TestActivity extends RxAppCompatActivity {

    ProgressHudUtil progressHudUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TextView tv = new TextView(this);
//        tv.setText("用户自定义打开的Activity");
        progressHudUtil = new ProgressHudUtil(this);
        progressHudUtil.showProgressHud("数据加载中...", false);
        Intent intent = getIntent();
        if (null != intent) {
	        Bundle bundle = getIntent().getExtras();
            if (bundle==null) {
                progressHudUtil.dismissProgressHud();
                finish();
                return;
            }else {
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE); //保存服务器推送下来的通知的标题。
                String content = bundle.getString(JPushInterface.EXTRA_ALERT); //保存服务器推送下来的通知内容。
                String jsonObject = bundle.getString(JPushInterface.EXTRA_EXTRA); //保存服务器推送下来的附加字段。这是个 JSON 字符串。对应 API 消息内容的 n_extras 字段
                try {
                    JSONObject object = new JSONObject(jsonObject);
                    String type = object.getString("type");
                    if (type == "ENTRY_PASS" || type.equals("ENTRY_PASS")) { //入场通过
                        getFoodEntry(object.getString("id"), 0);

                    } else if (type == "ENTRY_JEJECT" || type.equals("ENTRY_JEJECT")) { //入场驳回
                        getFoodEntry(object.getString("id"), 1);

                    } else if (type == "PAY_SUC" || type.equals("PAY_SUC")) { //订单
                        progressHudUtil.dismissProgressHud();
                        String orderId = object.getString("orderId");

                        getBill(orderId);
                    }else if (type == "EPAY_SUC" || type.equals("EPAY_SUC")){
                        progressHudUtil.dismissProgressHud();
                        String orderId = object.getString("orderId");

                        getBill(orderId);
                    }else  {
//                        ToastUtil.makeToast("系统消息");
                        startActivity(new Intent(TestActivity.this,MyMsgTypeActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    startActivity(new Intent(TestActivity.this,MyMsgTypeActivity.class));
                    finish();
                }
                progressHudUtil.dismissProgressHud();
            }
//            tv.setText("Title : " + title + "  " + "Content : " + content);
        }
//        addContentView(tv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }


    @Override
    protected void onDestroy() {
        if (progressHudUtil != null) {
            progressHudUtil.destory();
        }
        super.onDestroy();
    }

    /**
     * 获取申报信息
     */
    public void getFoodEntry(String id,int type) {
        Api.api().getFoodEntryInfo(bindUntilEvent(ActivityEvent.DESTROY), id, new SimpleRequestListener<List<FoodEntryInfo>>() {
            @Override
            public void onSuccess(List<FoodEntryInfo> foodEntryInfoList) {
                if (foodEntryInfoList !=null && foodEntryInfoList.size()>0) {
                    FoodEntryInfo info = foodEntryInfoList.get(0);
                    TXJHDWrapper wrapper = new TXJHDWrapper();
                    wrapper.setFoodInfos(info.getDetList());

                    wrapper.setTotal(info.getTotalQty());

                    String serachNo = info.getSearchNo();
                    if (serachNo != null) {
                        serachNo = serachNo.replaceAll("\\s*", "");
                        if (RegularUtil.isChePai(serachNo)) {
                            ChePai chePai = new ChePai();
                            chePai.setProvince(serachNo.substring(0, 1));
                            chePai.setCity(serachNo.substring(1, 2));
                            chePai.setNumber(serachNo.substring(2));
                            wrapper.setChePai(chePai);
                        }
                    }


                    wrapper.setId(info.getId());
                    wrapper.setUpdateDate(info.getUpdateDate());
                    wrapper.setFileId(info.getFileId());
                    wrapper.setState(info.getState());
                    Intent intent = new Intent();

//                    intent.putExtra("foodEntryInfo", foodEntryInfoListResultWrap.rows.get(0));

                    finish();
                    if (type == 1) { // 驳回
                        intent.setClass(getApplicationContext(), TXJHDActivity.class);

//                        startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mActivity));
                    }else if (type == 0){//通过
                        intent.setClass(getApplicationContext(), JhDetaiActivity.class);
//                        startActivity(JhDetaiActivityAutoBundle.createIntentBuilder(wrapper).build(mActivity));
                    }
                    intent.putExtra("wrapper",wrapper);
                    intent.putExtra("WHERE", "PUSH");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    ToastUtil.makeToast("没有详情信息");
                }
            }


            @Override
            public void onFinish() {
                progressHudUtil.dismissProgressHud();
            }
        });
    }

    /**
     * 订单信息
     * @param id
     */
    public void getBill(String id) {


        Api.api().getDzBillDetail(bindUntilEvent(ActivityEvent.DESTROY),  null,id, new SimpleRequestListener<BaseEntity<List<BillDetailbean>>>() {
            @Override
            public void onError(ApiError error) {
                super.onError(error);
                startActivity(new Intent(TestActivity.this,MyMsgTypeActivity.class));
                finish();
            }

            @Override
            public void onSuccess(BaseEntity<List<BillDetailbean>> o) {
                if (o.success==1 && !o.data.isEmpty()) {
                    startActivity( TjDzDetailActivityAutoBundle.createIntentBuilder(o.getData().get(0)).build(TestActivity.this));
                    finish();

                } else {
                    startActivity(new Intent(TestActivity.this,MyMsgTypeActivity.class));
                    finish();
//                    layoutAll.setVisibility(View.GONE);
//                    ToastUtil.makeToast("数据为空");

                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onStart() {
//                progressHudUtil.showProgressHud();
            }
        });
    }
}
