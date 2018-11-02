package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.MaterialDialog;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ShopCarFood;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;

/**
 * Created by hujie on 16/10/11.
 */

public class MCAdapter extends BaseQuickAdapter<ShopCarFood> {
    public MCAdapter() {
        super(R.layout.f_mc_item_layout, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, ShopCarFood mcFood) {

        TextView nameTV = holder.getView(R.id.nameTV);
        ImageView headIV = holder.getView(R.id.headIV);
        TextView zldTV = holder.getView(R.id.zldTV);
        TextView zlxTV = holder.getView(R.id.zlxTV);
        View delete = holder.getView(R.id.btDelete);


        UserManager.getInstance().loadFoodHeadImage(mContext, headIV, mcFood.getImgId());

        nameTV.setText(mcFood.getItemName());
        String[] ss = (mcFood.getQty() + "").split("\\.");
        if (ss.length != 0) {
            if (ss.length == 1) {
                zldTV.setText(ss[0]);
            } else {
                zldTV.setText(ss[0]);
                zlxTV.setText("."+ss[1]);
            }
        }

        holder.getView(R.id.contentView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_EDIT_CONT_MC_FOOD,mcFood);
            }
        });

        delete.setOnClickListener(v -> showDeleteDialog(holder.getLayoutPosition(), mcFood));

    }

    private void showDeleteDialog(int position, ShopCarFood shopCarFood) {


        MaterialDialog dialog = new MaterialDialog(mContext);
        dialog.setTitle("提示");
        dialog.setMessage("确定删除此菜品?");
        dialog.setNegativeButton("取消");
        dialog.setPositiveButton("确定", v -> {
            remove(position);
            EntityManager.deleteShopCarFood(shopCarFood);
            EventMamager.getInstance().postEvent(SimpleEventType.ON_DELETE_MC_FOOD);

        });
        dialog.show();


    }

}
