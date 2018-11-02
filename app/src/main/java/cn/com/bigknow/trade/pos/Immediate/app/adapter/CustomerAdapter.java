package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.model.bean.CustomerBean;

/**
 * Created by hujie on 16/10/11.
 */

public class CustomerAdapter extends BaseQuickAdapter<CustomerBean> {
    public CustomerAdapter() {
        super(R.layout.a_customer_item_layout, null);

    }


    @Override
    protected void convert(BaseViewHolder holder, CustomerBean customerBean) {

        holder.setText(R.id.customer_item_name, customerBean.getUserName() + "("
                + customerBean.getUserNo() + ")")
                .setText(R.id.customer_item_name_item_adr, customerBean.getOrgName());
    }
}
