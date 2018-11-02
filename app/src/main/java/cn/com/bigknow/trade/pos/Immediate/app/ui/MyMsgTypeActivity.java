package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tubb.smrv.SwipeMenuLayout;

import java.io.Serializable;
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
 * Created by laixy on 2016/11/17.
 * message  一级
 */


public class MyMsgTypeActivity extends BaseActivity {
    private ProgressHudUtil progressHudUtil;
    @BindView(R.id.ssrl)
    SimpleSwipeRefreshLayout ssrl;
    MyAdapter myAdapter;

    List<MessageInfoBean> list;



    public static AlertDialog.Builder builder;
    public static AlertDialog dialog;



    @Override
    public void init() {

        setTitle("消息");
        progressHudUtil = new ProgressHudUtil(this);
        list = new ArrayList<>();
        myAdapter = new MyAdapter();
        ssrl.setAdapter(myAdapter);

//        ssrl.setRefreshEnable(false);
        ssrl.setOnRefreshListener((SimpleSwipeRefreshLayout.OnRefreshListener) () -> getOutList());
//        List<MessageInfoBean> messageInfoBeen = getMessageInfo();
//        if (messageInfoBeen != null && !messageInfoBeen.isEmpty()) {
//            list = messageInfoBeen;
//            myAdapter.setNewData(list);
//            myAdapter.notifyDataSetChanged();
//        } else {
            ssrl.startRefresh();
//        }

//        myAdapter.setOnLoadMoreListener((BaseQuickAdapter.RequestLoadMoreListener) () -> appendOutList());
        ssrl.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MessageInfoBean bean = myAdapter.getItem(i);
                String msgType = bean.getMsgType();
                if (msgType == "APP_PAY" || msgType.equals("APP_PAY")) { //支付类

                } else if (msgType == "APP_ENTRY" || msgType.equals("APP_ENTRY")) { //入场类

                } else  { //系统

                }
                Intent intent = new Intent(mActivity, MyMessageActivity.class);
                intent.putExtra("bean", (Serializable) bean);
                startActivity(intent);
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


    public void getOutList() {
//        myAdapter.openLoadMore(20);
        Api.api().getMessageListType(bindUntilEvent(ActivityEvent.DESTROY),  new SimpleRequestListener<BaseEntity<List<MessageInfoBean>>>() {

            @Override
            public void onSuccess(BaseEntity<List<MessageInfoBean>> listBaseEntity) {

                list = listBaseEntity.data;
//                upDataMessage(list);
                if (list.size()>0) {
                    myAdapter.setNewData(list);
                }else {
                    ToastUtil.makeToast("还没有消息记录哟！");
                }

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

    /*public void appendOutList() {
        Api.api().getMessageList(bindUntilEvent(ActivityEvent.DESTROY), curPage, 20, new SimpleRequestListener<ListResultWrap<List<MessageInfoBean>>>() {

            @Override
            public void onSuccess(ListResultWrap<List<MessageInfoBean>> ListResultWrap) {

                ssrl.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ListResultWrap.rows != null && !ListResultWrap.rows.isEmpty()) {
                            list.addAll(ListResultWrap.rows);
                            myAdapter.setNewData(ListResultWrap.rows);
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

*/


    public  class MyAdapter extends BaseQuickAdapter<MessageInfoBean> {


        private OnListItemClick itemClick;

        public void setItemClick(OnListItemClick itemClick) {
            this.itemClick = itemClick;
        }

        public MyAdapter() {
            super(R.layout.w_message_swipmenu_item_layout, null);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, MessageInfoBean messageInfoBean) {
            baseViewHolder
                    .setText(R.id.tvName, messageInfoBean.getMsgTitle())
                    .setText(R.id.tvMessage, messageInfoBean.getContent())
                    .setText(R.id.tvTime, messageInfoBean.getSendTime())
                    .setVisible(R.id.btOpen, false)
                    .setVisible(R.id.btDelete,false);
            String msgType = messageInfoBean.getMsgType();
            if (msgType == "APP_PAY" || msgType.equals("APP_PAY")) { //支付类
                baseViewHolder.setImageResource(R.id.ivStatus, R.drawable.icon_my_xx04);
            } else if (msgType == "APP_ENTRY" || msgType.equals("APP_ENTRY")) { //入场类
                baseViewHolder.setImageResource(R.id.ivStatus, R.drawable.icon_my_xx02);
            } else  { //系统
               baseViewHolder.setImageResource(R.id.ivStatus, R.drawable.icon_my_xx03);
            }

            baseViewHolder.setOnClickListener(R.id.btDelete, (view) -> deteleMessage(baseViewHolder, messageInfoBean));
        }

        /**Main
         * 删除
         */
        public void deteleMessage(BaseViewHolder baseViewHolder, MessageInfoBean messageInfoBean) {
            ((SwipeMenuLayout) baseViewHolder.getConvertView()).smoothCloseMenu();
            createDialog(MyMsgTypeActivity.this, messageInfoBean);
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
