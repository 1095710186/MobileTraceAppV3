package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.model.bean.CustomerBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;

/**
 * Created by laixiaoyang
 */

public class FoodInfoAreaAdapter extends BaseQuickAdapter<VendorAreaBean> {

    public interface OnListItemChildClick {
        void OnClick(int position,int type,String vendor);
    }

    private OnListItemChildClick itemChildClick;

    public void setItemClick(OnListItemChildClick itemClick) {
        this.itemChildClick = itemClick;
    }
    public FoodInfoAreaAdapter() {
        super(R.layout.food_area_item_layout, null);

    }

    int height;


    private int jsItemWidth() {
        int sWidth = DensityUtil.getScreenW(mContext);
        int ww = (sWidth - DensityUtil.dip2px(mContext, 20)) / 3;
        return ww;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, VendorAreaBean vendorAreaBean) {

        if (height == 0) {
            height = jsItemWidth();
        }

        baseViewHolder.setVisible(R.id.food_area_item_imvChooseArea,true)
                        .setVisible(R.id.food_area_item_imvArea,false)
                        .setVisible(R.id.food_area_item_relayoutSureAdd,false)
                        .setVisible(R.id.food_area_item_layoutEdt,true)   //
//                        .addOnClickListener(R.id.food_area_item_imvChooseArea)   //选择地区
//                        .addOnClickListener(R.id.food_area_item_relayoutDelet)   //delete
//                        .addOnClickListener(R.id.food_area_item_relayoutEitor);  //editor
                        .setText(R.id.food_area_item_edtArea,vendorAreaBean.getProdArea())
                        .setText(R.id.food_area_item_edtGys,vendorAreaBean.getVendor());
        baseViewHolder.getView(R.id.food_area_item_edtGys).setEnabled(true);

        int type =0;
        baseViewHolder.setOnClickListener(R.id.food_area_item_imvChooseArea, (view) -> doClick(baseViewHolder,view,0));
        baseViewHolder.setOnClickListener(R.id.food_area_item_relayoutEitor, (view) -> doClick(baseViewHolder ,view,1));
        baseViewHolder.setOnClickListener(R.id.food_area_item_relayoutDelet, (view) -> doClick(baseViewHolder,view,2));

        EditText edtGys = baseViewHolder.getView(R.id.food_area_item_edtGys);
        edtGys.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemChildClick.OnClick(baseViewHolder.getAdapterPosition(),3,edtGys.getText().toString());
            }
        });

        int position = baseViewHolder.getLayoutPosition();
        baseViewHolder.getPosition();
        baseViewHolder.getAdapterPosition();

//        FrameLayout ff = baseViewHolder.getView(R.id.pLayout);

//        ff.getLayoutParams().height = height;
//        ff.getLayoutParams().width = height;





    }
    public void doClick(BaseViewHolder baseViewHolder,View v,int type) {  //0选择地区 ;1 editor;2，dele
//        ((SwipeMenuLayout) baseViewHolder.getConvertView()).smoothCloseMenu();
        View view = baseViewHolder.getConvertView();
        if (type == 0){
            itemChildClick.OnClick(baseViewHolder.getAdapterPosition(),type,null);
        }else if (type ==1){
//            TextView tvEditor = (TextView) v.findViewById(R.id.food_area_item_tvEitor);
//            if (tvEditor.getText()!=null &&tvEditor.getText().equals("修改") ){
//                baseViewHolder.getView(R.id.food_area_item_edtGys).setEnabled(false);
//                baseViewHolder.setText(R.id.food_area_item_tvEitor,"编辑");
//                baseViewHolder.setVisible(R.id.food_area_item_imvChooseArea,false)
//                        .setVisible(R.id.food_area_item_imvArea,true);
////                getItem(baseViewHolder.getAdapterPosition()-1).setVendor(baseViewHolder.getView(R.id.food_area_item_edtGys).toString());
//                EditText edtGys = baseViewHolder.getView(R.id.food_area_item_edtGys);
//                itemChildClick.OnClick(baseViewHolder.getAdapterPosition(),type,edtGys.getText().toString());
////                notifyDataSetChanged();
//            }else if (tvEditor.getText()!=null &&tvEditor.getText().equals("编辑")){
//                baseViewHolder.getView(R.id.food_area_item_edtGys).setEnabled(true);
//                baseViewHolder.setText(R.id.food_area_item_tvEitor,"修改");
//                baseViewHolder.setVisible(R.id.food_area_item_imvChooseArea,true)
//                        .setVisible(R.id.food_area_item_imvArea,false);
//            }

        }else if (type ==2){
            int position = baseViewHolder.getLayoutPosition();
            int po =  baseViewHolder.getPosition();
            int posi = baseViewHolder.getAdapterPosition();
            itemChildClick.OnClick(baseViewHolder.getAdapterPosition(),type,null);

        }

//        requestChangeFood(customerBean, null, "delete");
    }
}
