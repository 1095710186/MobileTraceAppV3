package cn.com.bigknow.trade.pos.Immediate.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fyales.tagcloud.library.TagBaseAdapter;

import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodAlias;
import cn.com.bigknow.trade.pos.Immediate.model.bean.VendorAreaBean;

/**
 * Created by laixiaoyang
 */

public class TgbaseAdapter extends  BaseAdapter{

    private Context mContext;
    private List<FoodAlias> mList;
    int checkPosition =-1;

    public TgbaseAdapter(Context context) {
        mContext = context;
    }

    public TgbaseAdapter(Context context, List<FoodAlias> list) {
        mContext = context;
        mList = list;
    }

    public void update( List<FoodAlias> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setCheck(int position) {
        checkPosition  = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public FoodAlias getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TgbaseAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tag_list_item,null);//tag_list_item, null);
            holder = new TgbaseAdapter.ViewHolder();
            holder.tagBtn = (RadioButton) convertView.findViewById(R.id.tag_item_name);
            convertView.setTag(holder);
        }else{
            holder = (TgbaseAdapter.ViewHolder)convertView.getTag();
        }
        final String text = getItem(position).getAlias();
        holder.tagBtn.setText(text);
       /* if (checkPosition == position){
            holder.tagBtn.setChecked(true);
        }else {
            holder.tagBtn.setChecked(false);
        }*/
        return convertView;
    }

    static class ViewHolder {
        RadioButton tagBtn;
    }
}
