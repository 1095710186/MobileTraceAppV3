package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;

/**
 * Created by hujie on 16/10/11.
 *
 * 产地供应商
 */

public class CDGYSAdapter extends BaseQuickAdapter<VendorAreaBean> {

    private String selectAreaBeanId;

    public CDGYSAdapter() {
        super(R.layout.cd_gys_item, null);
    }

    public void setSelectAreaBeanId(String selectAreaBeanId) {
        this.selectAreaBeanId = selectAreaBeanId;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, VendorAreaBean vendorAreaBean) {

        ImageView checkIV = baseViewHolder.getView(R.id.checkbox);

        if (vendorAreaBean.getId().equals(selectAreaBeanId)) {
            checkIV.setSelected(true);
        } else {
            checkIV.setSelected(false);
        }
        TextView gysTV = baseViewHolder.getView(R.id.gysTV);
        TextView cdTV = baseViewHolder.getView(R.id.cdTV);

        gysTV.setText(vendorAreaBean.getVendor());
        cdTV.setText(vendorAreaBean.getProdArea());

        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAreaBeanId = vendorAreaBean.getId();
                notifyDataSetChanged();
            }
        });

    }
}
