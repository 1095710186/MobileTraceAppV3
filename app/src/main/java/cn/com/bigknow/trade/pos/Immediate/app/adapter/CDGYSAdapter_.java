package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.widget.ProvinceView;
import cn.com.bigknow.trade.pos.Immediate.base.interface_.OntemClickIListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;

/**
 * Created by laixiaoyang.
 *
 * 产地
 */

public class CDGYSAdapter_ extends BaseQuickAdapter<VendorAreaBean> {

    public OntemClickIListener listener;

    public void setListener(OntemClickIListener listener) {
        this.listener = listener;
    }

    boolean isCheck = false;
    private String selectAreaBeanId;

    public CDGYSAdapter_() {
        super(R.layout.jh_cd_item, null);
    }

    public void setSelectAreaBeanId(String selectAreaBeanId , boolean isC) {
        this.isCheck = isC;
        this.selectAreaBeanId = selectAreaBeanId;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final  VendorAreaBean vendorAreaBean) {

        ImageView checkIV = baseViewHolder.getView(R.id.checkbox);

        if (vendorAreaBean.getId().equals(selectAreaBeanId)) {
            if (isCheck) {
                checkIV.setSelected(true);
            }else {
                checkIV.setSelected(false);
            }
        } else {
            checkIV.setSelected(false);
        }
        TextView cdTV = baseViewHolder.getView(R.id.cdTV);

        cdTV.setText(vendorAreaBean.getProdArea());

        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VendorAreaBean ven = vendorAreaBean;
                if (selectAreaBeanId!=null && selectAreaBeanId.equals(vendorAreaBean.getId())){
                    if (isCheck){
                        isCheck = false;
                        ven = new VendorAreaBean();
                    }else {
                        isCheck = true;
                    }
                }else {
                    selectAreaBeanId = vendorAreaBean.getId();
                    if (isCheck){
                        isCheck = false;
                        ven = new VendorAreaBean();
                    }else {
                        isCheck = true;
                    }
                }
//                isCheck = !isCheck;/
                listener.OnItemClick(baseViewHolder.getConvertView(), baseViewHolder.getAdapterPosition(), ven);
                notifyDataSetChanged();
            }
        });

    }
}
