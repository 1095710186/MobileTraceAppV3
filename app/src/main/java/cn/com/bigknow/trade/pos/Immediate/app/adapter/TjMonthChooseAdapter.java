package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.YearMonthbean;

/**
 * Created by laixiaoyang
 */

public class TjMonthChooseAdapter extends BaseQuickAdapter<YearMonthbean> {


    public TjMonthChooseAdapter() {
        super(R.layout.dialog_choose_m_item, null);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, YearMonthbean string) {


        baseViewHolder
                        .setText(R.id.dialog_month_name,string.getDate());


    }

}
