package cn.com.bigknow.trade.pos.Immediate.base.android;

/**
 * Created by hujie on 16/5/29.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yatatsu.autobundle.AutoBundle;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.ui.LoginActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MainActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.TXJHDActivityAutoBundle;
import cn.com.bigknow.trade.pos.Immediate.app.util.BugtagsManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.Yuying;
import cn.com.bigknow.trade.pos.Immediate.base.activitymanager.TheActivityManager;
import cn.com.bigknow.trade.pos.Immediate.base.util.ProgressHudUtil;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import onactivityresult.ActivityResult;

/**
 * Created by hujie on 16/5/24.
 *
 * 基础Activity activity的类已A 开头
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IUI {

    public Activity mActivity;
    private LinearLayout mRootView;
    private Unbinder unbinder;
    private Toolbar mToolbar;
    private TextView mTitleTV;
    public ProgressHudUtil progressHudUtil;
    public Yuying yuying;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTheme();
        setWindowsFeature();
        super.onCreate(savedInstanceState);
        mActivity = this;
        progressHudUtil = new ProgressHudUtil(this);
        setUpView(layoutId());
        setSupportActionBar();
        bindView();
        setStatusColor();
        bindBundle(savedInstanceState);
        bindEvent();
        init();


    }

    public void setYuying(EditText edt, View imv){
        if (yuying!=null) {
            yuying.init(mActivity, edt, imv);
        }else {
            yuying = new Yuying();
            yuying.init(mActivity, edt, imv);
        }
    }
    public abstract void init();

    public void setStatusColor()
    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mRootView.setFitsSystemWindows(true);
//            // 透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
//        }
    }


    //添加自定义菜单
    public void addCustomMenu(View menuView) {
        if (supportActionbar()) {
            LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
            menuLayout.setVisibility(View.VISIBLE);
            menuLayout.removeAllViews();
            menuLayout.addView(menuView);
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
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
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
     * 默认绑定
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
        super.onDestroy();
        gc();
        if (yuying!=null){
            yuying.onDestroy();
        }
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


    /**
     * 获取到布局id
     * @return
     */
    public abstract int layoutId();





    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        BugtagsManager.onResume(this);
        if (yuying!=null){
            yuying.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        BugtagsManager.onPause(this);
        if (yuying!=null){
            yuying.onPause();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        BugtagsManager.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }


    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    private long firstTime = 0;

    private long jianClickTime;
    private long jiaClickTime;

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            //监控/拦截Home键
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

        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_VOLUME_UP) {//音量减小增大


            if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    jianClickTime=System.currentTimeMillis();
                }
            }
            if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    jiaClickTime=System.currentTimeMillis();
                }
            }




            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 100) {                                         //如果两次按键时间间隔大于0.1秒，则不操作
                firstTime = secondTime;//更新firstTime
            } else {

                if(jianClickTime>=jiaClickTime){
                    if(jianClickTime-jiaClickTime<100){
                        //两次按键小于0.1秒时，打开设置界面
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                }else{
                    if(jiaClickTime-jianClickTime<100){
                        //两次按键小于0.1秒时，打开设置界面
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                }


            }

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


