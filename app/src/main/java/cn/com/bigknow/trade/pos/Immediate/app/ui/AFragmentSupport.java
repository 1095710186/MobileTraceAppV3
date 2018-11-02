package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yatatsu.autobundle.AutoBundle;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.activitymanager.TheActivityManager;
import cn.com.bigknow.trade.pos.Immediate.base.android.IUI;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import me.yokeyword.fragmentation.SupportActivity;
import onactivityresult.ActivityResult;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by hujie on 16/6/17.
 *
 * 集成Fragment 管理 以及RX
 */
public abstract class AFragmentSupport extends SupportActivity implements IUI, LifecycleProvider<ActivityEvent> {


    public Activity mActivity;
    private LinearLayout mRootView;
    private Unbinder unbinder;
    private Toolbar mToolbar;
    private TextView mTitleTV;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTheme();
        setWindowsFeature();
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        mActivity = this;
        setUpView(layoutId());
        setSupportActionBar();
        bindView();
        bindBundle(savedInstanceState);
        bindEvent();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (autoBindBundle()) {
            AutoBundle.pack(this, outState);
        }
    }

    /**
     * When use AutoBundle to inject arguments, should override this and return {@code true}.
     */
    protected boolean autoBindBundle() {
        return false;
    }


    /**
     * 是否支持toolbar作为导航栏
     * 如果不需要导航栏 则return false
     * @return
     */
    public boolean supportActionbar() {
        return true;
    }


    /**
     * 设置toobar作为导航栏
     */
    public void setSupportActionBar() {
        if (supportActionbar()) {
            mToolbar = (Toolbar) findViewById(R.id.actionbar);
            mTitleTV = (TextView) mToolbar.findViewById(R.id.toolbarTitleTV);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setBackNavication();
        }
    }


    /**
     * 是否显示返回按钮
     * @return
     */
    public boolean showBackNavication() {
        return true;
    }

    /**
     * 设置返回导航
     */
    public void setBackNavication() {
        if (showBackNavication()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationContentDescription("返回");
            mToolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }


    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (supportActionbar() && mTitleTV != null) {
            if (color != 0) {
                mTitleTV.setTextColor(color);
            }
            mTitleTV.setText(title);
        }
    }

    /**
     * 设置主题
     * 默认采用AppTheme主题
     */
    private void setTheme() {
        if (getThemeId() != 0) {
            setTheme(getThemeId());
        }
    }


    /**
     * 获取自定义主题ID
     * @return
     */
    public int getThemeId() {
        return R.style.AppTheme;
    }


    @Override
    public void setUpView(@LayoutRes int layoutId) {
        if (layoutId != 0) {
            mRootView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.root_layout, null);
            if (supportActionbar()) {
                View toolBarView = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
                mRootView.addView(toolBarView);
            }
            View contentView = LayoutInflater.from(this).inflate(layoutId, null);
            mRootView.addView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            setContentView(mRootView);
        }
    }

    @Override
    public void bindView() {
        if (autoBindViews()) {
            unbinder = ButterKnife.bind(this);
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

    @Override
    public void bindEvent() {
        if (autoBindEvent()) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 是否自动绑定view
     * 默认采用butterknife进行view的注入
     * @return
     */
    public boolean autoBindViews() {
        return true;
    }

    @Override
    public void unBindView() {
        if (autoBindViews() && unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void unBindEvent() {
        if (autoBindEvent()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        gc();
    }

    public void setWindowsFeature() {

        // 强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (handleActivityResult()) {
            ActivityResult.onResult(requestCode, resultCode, data).into(this);
        }
    }


    public View getRootView() {
        return mRootView;
    }


    /**
     * 有返回值
     * @return
     */
    protected boolean handleActivityResult() {
        return false;
    }


    /**
     * 内存释放等
     */
    public void gc() {
        unBindView();
        unBindEvent();

        mActivity = null;
        unbinder = null;
        mRootView = null;
    }

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject);
    }


    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }


    /**
     * 获取到布局id
     * @return
     */
    public abstract int layoutId();


    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            LogUtil.d("mActivity",mActivity.getClass().getSimpleName());
            if(!(mActivity instanceof MainActivity || mActivity instanceof LoginActivity)){
                LogUtil.d("mActivity",mActivity.getClass().getSimpleName());
                Intent intent = new Intent(mActivity, MainActivity.class);
                startActivity(intent);
                EventMamager.getInstance().postEvent(SimpleEventType.ON_KEY_HOME);
                finish();
//                showExitJHDialog();
            }
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_MENU) {//MENU键
            //监控/拦截菜单键
            return true;
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    Display display;
    static AlertDialog dialog;
    public void dissclose() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
    //弹出提示框 退出
    private void showExitJHDialog() {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_wxit_jh, null);

        View cd_back = (View) view1.findViewById(R.id.cd_layout_quxiao);
        View cd_sure = (View) view1.findViewById(R.id.cd_layout_sure);
        TextView tvNews = (TextView) view1.findViewById(R.id.tvNews);

        tvNews.setText("确定要回到主页？");
        cd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();
            }
        });
        cd_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissclose();
                Intent intent = new Intent(mActivity, MainActivity.class);
                startActivity(intent);
                EventMamager.getInstance().postEvent(SimpleEventType.ON_KEY_HOME);
                finish();
            }
        });
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new AlertDialog.Builder(mActivity)
                .create();
        dialog.show();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        WindowManager windowManager = mActivity.getWindowManager();
        display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth())*3/4; //设置宽度
//        lp.height = (int) (display.getHeight())*1/3;
//                (int) (DensityUtil.dip2px(mActivity, 550));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view1);

    }
}
