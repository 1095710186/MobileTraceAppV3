package cn.com.bigknow.trade.pos.Immediate.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;


/**
 * recyclerView的adapter的基类
 * @param <VH>--继承的子类adapter
 * @param <T>--实体泛型
 */
public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder,T extends Object> extends RecyclerView.Adapter<VH> {
    protected final String TAG = getClass().getSimpleName();
    protected final Context mContext;
    protected LayoutInflater mLayoutInflater;

    protected List<T> mDataList = new ArrayList<>();

    public BaseRecyclerAdapter(Context context, List<T> data) {
        super();
        mContext = context;
        if(context==null)
            return;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDataList = data;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public T getItemData(int position) {
        return position < mDataList.size() ? mDataList.get(position) : null;
    }


    @Override
    public int getItemCount() {

        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 移除某一条记录
     *
     * @param position 移除数据的position
     */
    public void removeItem(int position) {
        if (position < mDataList.size()) {
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 添加一条记录
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    public void addItem(T data, int position) {
        if (position <= mDataList.size()) {
            mDataList.add(position, data);
            notifyItemInserted(position);
        }
    }

    /**
     * 添加一条记
     * @param data 需要加入的数据结构
     */
    public void addItem(T data) {
        addItem(data, mDataList.size());
    }

    /**
     * 移除所有记录
     */
    public void clearItems() {
        int size = mDataList.size();
        if (size > 0) {
            mDataList.clear();
            LogUtil.d(TAG, "clearItems --> ");
            notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * 批量添加记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    public void addItems(List<T> data, int position) {
        if (position <= mDataList.size() && data != null && data.size() > 0) {
            LogUtil.d(TAG, "addItems --> position" + position);
            mDataList.addAll(position, data);
            notifyItemRangeChanged(position, data.size());
        }
    }

    /**
     * 批量添加记录
     * @param data 需要加入的数据结构
     */
    public void addItems(List<T> data) {
        addItems(data, mDataList.size());
        notifyDataSetChanged();
    }
    public void upDateItems(List<T> data) {
        mDataList = data;
        notifyDataSetChanged();
    }

}
