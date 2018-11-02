package cn.com.bigknow.trade.pos.Immediate.base.android;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

/**
 * Created by hujie on 16/5/24.
 */
public interface IUI {

    /**
     * 设置布局
     * @param layoutId
     */
    void setUpView(@LayoutRes int layoutId);

    /**
     * 绑定view
     */
    void bindView();

    /**
     * 解绑view
     */
    void unBindView();

    /**
     * 绑定事件
     */
    void bindEvent();


    /**
     * 自动参数绑定
     */
    void bindBundle(Bundle savedInstanceState);

    /**
     * 解绑事件
     */
    void unBindEvent();

    /**
     * 进行内存释放
     */
    void gc();



}
