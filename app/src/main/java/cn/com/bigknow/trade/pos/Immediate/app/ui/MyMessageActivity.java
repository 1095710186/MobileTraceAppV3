package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tubb.smrv.SwipeMenuLayout;
import com.yatatsu.autobundle.AutoBundleField;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.di.DBManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.interface_.OnListItemClick;
import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.RegularUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.base.widget.SimpleSwipeRefreshLayout;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BillDetailbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Billbean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ChePai;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodEntryInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MessageInfoBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.MessageInfoBeanDao;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by laixy on 2016/10/11.
 * message
 */


public class MyMessageActivity extends BaseActivity {
    private ProgressHudUtil progressHudUtil;
    @BindView(R.id.ssrl)
    SimpleSwipeRefreshLayout ssrl;

    /*@AutoBundleField
    MessageInfoBean messageInfoBean;*/

    MyAdapter myAdapter;


    List<MessageInfoBean> list;



    public static AlertDialog.Builder builder;
    public static AlertDialog dialog;

    MessageInfoBean beanType;

    /*@Override
    protected boolean autoBindBundle() {
        return true;
    }*/
    @Override
    public void init() {
        beanType = (MessageInfoBean) getIntent().getSerializableExtra("bean");
        String msgType = beanType.getMsgType();
        if (msgType == "APP_PAY" || msgType.equals("APP_PAY")) { //支付类
            setTitle("交易消息");
        } else if (msgType == "APP_ENTRY" || msgType.equals("APP_ENTRY")) { //入场类
            setTitle("进货消息");
        } else  { //系统
            setTitle("系统消息");
        }
        progressHudUtil = new ProgressHudUtil(this);
        list = new ArrayList<>();
        myAdapter = new MyAdapter();
        ssrl.setAdapter(myAdapter);
        myAdapter.openLoadMore(10);
        ssrl.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getOutList(beanType));
//        List<MessageInfoBean> messageInfoBeen = getMessageInfo();
//        if (messageInfoBeen != null && !messageInfoBeen.isEmpty()) {
//            list = messageInfoBeen;
//            myAdapter.setNewData(list);
//            myAdapter.notifyDataSetChanged();
//            curPage++;
//        } else {
            ssrl.startRefresh();
//        }

        myAdapter.setOnLoadMoreListener((BaseQuickAdapter.RequestLoadMoreListener) () -> appendOutList(beanType));

        ssrl.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MessageInfoBean bean = myAdapter.getItem(i);
                String msgType = bean.getMsgType();
                String transType = bean.getTransType();
                if (msgType == "APP_PAY" || msgType.equals("APP_PAY")) { //支付类
                    if (transType == "PAY_SUC" || transType.equals("PAY_SUC")) { //支付成功

                    }
                    getBillDetail(null,bean.getCorrId());

                } else if (msgType == "APP_ENTRY" || msgType.equals("APP_ENTRY")) { //入场类
                    if (transType == "ENTRY_PASS" || transType.equals("ENTRY_PASS")) { //入场通过
                        getFoodEntry(bean.getCorrId(),0);
                    } else if (transType == "ENTRY_JEJECT" || transType.equals("ENTRY_JEJECT")) { //入场驳回
                        getFoodEntry(bean.getCorrId(),1);
                    }
                } else  { //系统
                    ToastUtil.makeToast("系统消息");
                }
            }
        });

    }

    /**
     * 存数据库
     *
     * @return
     */
    public void upDataMessage(List<MessageInfoBean> messageInfoBeen) {
        MessageInfoBeanDao messageInfoBeanDao = (MessageInfoBeanDao) DBManager.getInstance(this).getDao(MessageInfoBean.class);
        messageInfoBeanDao.deleteAll();
        messageInfoBeanDao.insertOrReplaceInTx(messageInfoBeen);
    }


    /**
     * 从数据库拿去数据
     *
     * @return
     */
    public List<MessageInfoBean> getMessageInfo() {
        MessageInfoBeanDao messageInfoBeanDao = (MessageInfoBeanDao) DBManager.getInstance(this).getDao(MessageInfoBean.class);
        List<MessageInfoBean> messageInfoBeens = messageInfoBeanDao.loadAll();
        return messageInfoBeens;
    }

    private int curPage = 1;

    public void getOutList(MessageInfoBean bean) {
        curPage = 1;
        myAdapter.openLoadMore(10);
        Api.api().getMessageList(bindUntilEvent(ActivityEvent.DESTROY),bean.getMsgType(), curPage, 10, new SimpleRequestListener<BaseEntity<List<MessageInfoBean>>>() {

            @Override
            public void onSuccess(BaseEntity<List<MessageInfoBean>> listBaseEntity) {

                list = listBaseEntity.data;
//                upDataMessage(list);
                myAdapter.setNewData(list);
//                myAdapter.notifyDataSetChanged();
                curPage++;

            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ssrl.stopRefresh();;
            }
        });
    }

    public void appendOutList(MessageInfoBean bean) {
        Api.api().getMessageList(bindUntilEvent(ActivityEvent.DESTROY),bean.getMsgType(),curPage, 10, new SimpleRequestListener<BaseEntity<List<MessageInfoBean>>>() {

            @Override
            public void onSuccess(BaseEntity<List<MessageInfoBean>> ListResultWrap) {

                ssrl.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ListResultWrap.data != null && !ListResultWrap.data.isEmpty()) {
                            list.addAll(ListResultWrap.data);
                            myAdapter.setNewData(list);
                            myAdapter.notifyDataSetChanged();
                            curPage++;
                        } else {
                            ToastUtil.makeToast("已经没有更多数据了！");
                        }
                    }
                });
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ssrl.stopRefresh();
            }
        });
    }



    public void onItemClick(int position) {
        MessageInfoBean messageInfoBean = list.get(position);
        String transType = messageInfoBean.getTransType();
        if (transType == "ENTRY_PASS" || transType.equals("ENTRY_PASS")) { //入场通过
            getFoodEntry(messageInfoBean.getCorrId(),0);

        } else if (transType == "ENTRY_JEJECT" || transType.equals("ENTRY_JEJECT")) { //入场驳回
            getFoodEntry(messageInfoBean.getCorrId(),1);
//            startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mContext))

        } else if (transType == "ORDER_NEW" || transType.equals("ORDER_NEW")) { //新增订单
            getBill(messageInfoBean.getCorrId());
        } else if (transType == "ORDER_CANCEL" || transType.equals("ORDER_CANCEL")) { //撤销订单
            getBill(messageInfoBean.getCorrId());
        }
//        ToastUtil.makeToast("点击了" + position+"-->"+transType);

    }

    //获取订单详情
    public void getBillDetail(String billNo, String id) {
        Api.api().getDzBillDetail(bindUntilEvent(ActivityEvent.DESTROY),  billNo,id, new SimpleRequestListener<BaseEntity<List<BillDetailbean>>>() {
            @Override
            public void onSuccess(BaseEntity<List<BillDetailbean>> o) {
                if (o.success==1 && !o.data.isEmpty()) {
                    startActivity( TjDzDetailActivityAutoBundle.createIntentBuilder(o.getData().get(0)).build(mActivity));

                } else {

//                    layoutAll.setVisibility(View.GONE);
                    ToastUtil.makeToast("查询数据为空");

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
    /**
     * 获取进货信息
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
                    if (type == 1) { // 驳回
                        startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).isEdit(true).build(mActivity));
                    }else if (type == 0){//通过
                        startActivity(JhDetaiActivityAutoBundle.createIntentBuilder(wrapper).build(mActivity));
                    }
                } else {

                    ToastUtil.makeToast("没有详情信息");
                }
            }


            @Override
            public void onFinish() {

            }
        });
    }

    /**
     *
     * @param id
     */
    public void getBill(String id) {


        Api.api().getBillDetail(bindUntilEvent(ActivityEvent.DESTROY), id, new SimpleRequestListener<BaseEntity<List<Billbean>>>() {
            @Override
            public void onSuccess(BaseEntity<List<Billbean>> o) {
                List<Billbean> billbeen = o.data;
                if (billbeen.size()>0) {

//                    startActivity( TjDzDetailActivityAutoBundle.createIntentBuilder( analyzeDzBillinfos.get(i).getBillNo()).build(mActivity));

                } else {
                    ToastUtil.makeToast("服务器返回数据为空");
                }

            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
//                error.errorMsg.equals("")

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public  class MyAdapter extends BaseQuickAdapter<MessageInfoBean> {


        public MyAdapter() {
            super(R.layout.w_mymessage_list_item, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, MessageInfoBean messageInfoBean) {
            baseViewHolder
                    .setText(R.id.tvName, messageInfoBean.getMsgTitle())
                    .setText(R.id.tvMessage, messageInfoBean.getContent())
                    .setText(R.id.tvTime, messageInfoBean.getSendTime())
                    ;

//            baseViewHolder.setOnClickListener(R.id.btDelete, (view) -> deteleMessage(baseViewHolder, messageInfoBean));
        }

        /**Main
         * 删除
         */
        public void deteleMessage(BaseViewHolder baseViewHolder, MessageInfoBean messageInfoBean) {
            ((SwipeMenuLayout) baseViewHolder.getConvertView()).smoothCloseMenu();
            createDialog(MyMessageActivity.this, messageInfoBean);
            dialog.show();
        }

        public void createDialog(Context context, MessageInfoBean messageInfoBean) {
            builder = new AlertDialog.Builder(context);
            builder.setMessage("确定要删除吗？");
            builder.setPositiveButton("确定", (dialog, id) -> {


                SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
                List<RequestWrap> requestWraps = new ArrayList<>();

                RequestWrap requestWrap = new RequestWrap<>();
                requestWrap.name = "dsMain";
                List<MessageInfoBean> list = new ArrayList<>();
                list.add(messageInfoBean);
                requestWrap.data = list;
                requestWraps.add(requestWrap);
                simpleRequestWrap.dataset = requestWraps;

                Api.api().getMessageDelete(bindUntilEvent(ActivityEvent.DESTROY), simpleRequestWrap, new SimpleRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        progressHudUtil.showProgressHud("正在删除，请稍后", false);
                    }

                    @Override
                    public void onSuccess(Object o) {
                        progressHudUtil.dismissProgressHud();
                        BaseEntity baseEntity = (BaseEntity) o;
                        if (baseEntity.getSuccess() == 1) {
                            ssrl.startRefresh();
//                            list.remove(messageInfoBean);
//                            myAdapter.notifyDataChangedAfterLoadMore(list, true);
                        }

                    }

                    @Override
                    public void onError(ApiError error) {
                        super.onError(error);
                        progressHudUtil.dismissProgressHud();
                    }
                });

            });
            builder.setNegativeButton("取消", (dialog, id) -> {
                dialog.dismiss();
            });
            dialog = builder.create();
        }
    }

    @Override
    public int layoutId() {
        return R.layout.a_mymessaget_layout;
    }




}
