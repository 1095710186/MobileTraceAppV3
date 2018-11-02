package cn.com.bigknow.trade.pos.Immediate.app.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.yatatsu.autobundle.AutoBundle;

import org.greenrobot.eventbus.EventBus;

import javax.annotation.Nonnull;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.bigknow.trade.pos.Immediate.app.di.ThirdPartyManaer;
import cn.com.bigknow.trade.pos.Immediate.base.android.IUI;
import me.yokeyword.fragmentation.SupportFragment;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by hujie on 16/6/17.
 *
 * 所有fragment基类  同时 Fragment的类 已F开头
 */
public abstract class BaseSupportFragment extends SupportFragment implements IUI, LifecycleProvider<FragmentEvent> {


    private Unbinder unbinder;


    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }


    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject);
    }


    @Override
    @CallSuper
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    @CallSuper
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
        gc();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
        addWatcher();
    }

    private void addWatcher() {
        ThirdPartyManaer.getInstance().watcher(this);
    }

    @Override
    @CallSuper
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    /**
     * 启动结束 加载数据
     */
    public abstract void initLazyView();

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        initLazyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (layoutId() != 0) {
            View view = inflater.inflate(layoutId(), container, false);
            bindBundle(savedInstanceState);
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindView();
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        bindEvent();
    }

    /**
     * 是否自动绑定view
     * 默认采用butterknife进行view的注入
     * @return
     */
    public boolean autoBindViews() {
        return true;
    }

    /**
     * 获取到布局id
     * @return
     */
    public abstract int layoutId();

    @Override
    public void setUpView(@LayoutRes int layoutId) {

    }

    @Override
    public void bindView() {
        if (autoBindViews()) {
            unbinder = ButterKnife.bind(this, getView());
        }
    }

    @Override
    public void unBindView() {
        if (autoBindViews() && unbinder != null) {
            unbinder.unbind();
        }
    }


    @Override
    public void bindEvent() {
        if (autoBindEvent()) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 是否绑定事件
     * 默认不绑定
     * @return
     */
    public boolean autoBindEvent() {
        return false;
    }

    /**
     * When use AutoBundle to inject arguments, should override this and return {@code true}.
     */
    protected boolean autoBindBundle() {
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (autoBindBundle()) {
            AutoBundle.pack(this, outState);
        }
    }

    @Override
    public void bindBundle(Bundle savedInstanceState) {
        if (autoBindBundle()) {
            if (savedInstanceState == null) {
                AutoBundle.bind(this);
            } else {
                AutoBundle.bind(this, savedInstanceState);
            }
        }
    }


    @Override
    public void unBindEvent() {
        if (autoBindEvent()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void gc() {
        unBindView();
        unBindEvent();
    }
}
