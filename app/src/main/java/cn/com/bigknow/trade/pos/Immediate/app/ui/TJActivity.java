package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.yatatsu.autobundle.AutoBundleField;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment.TjDz1_Fragment;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment.TjJhFragment;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment.TjPzFragment;
import cn.com.bigknow.trade.pos.Immediate.base.util.UtilsPopWindow;
import cn.com.bigknow.trade.pos.Immediate.model.bean.DzInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PayResult;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import icepick.Icepick;
import icepick.State;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by laixy
 * 统计分析
 */

public class TJActivity extends  AFragmentSupport {

    @BindView(R.id.actionbar)
    Toolbar mToolbar;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.f_tj_radipgroup)
    RadioGroup radioGroup;

    @State
    int index = 0;
    SupportFragment[] mFragments = new SupportFragment[3];

    @Override
    public boolean supportActionbar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setToolbar();
        addRightMenu();
//        RequestBaseData();
        setStatusColor();
        initFragments(savedInstanceState);
       init();
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationContentDescription("返回");
        mToolbar.setNavigationOnClickListener(v -> finish());

    }

    private void addRightMenu() {
        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuView);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.removeAllViews();

        ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.index_1_right_menu, null);
        view.setBackgroundResource(R.drawable.ico_gd);
        menuLayout.addView(view);
        view.setOnClickListener(view1 -> {
            UtilsPopWindow.showMenuWindow(view1,mActivity);

        });

    }


    public void setStatusColor() {
        //5.0上面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addStatusView(R.color.colorAccent);
        }

    }

    View mStatusBarTintView;

    public void addStatusView(int color) {

        mainLayout.setFitsSystemWindows(false);
        int statusHeight = getStatusHeight();
        mStatusBarTintView = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusHeight);
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(getResources().getColor(color));
        mStatusBarTintView.setVisibility(View.VISIBLE);
        mainLayout.addView(mStatusBarTintView, 0);

    }

    private static int getInternalDimensionSize(Resources res, String key) {

        //status_bar_height

        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusHeight() {
        return getInternalDimensionSize(Resources.getSystem(), "status_bar_height");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public boolean autoBindEvent() {
        return false;
    }

    public void init() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> switchList(group, checkedId));
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState ==null) {
            mFragments[0] = TjDz1_Fragment.newInstance();
            mFragments[1] = TjJhFragment.newInstance();
            mFragments[2] = TjPzFragment.newInstance();
            loadMultipleRootFragment(R.id.f_tj_contentLayout, index, mFragments[0],
                    mFragments[1], mFragments[2]);
            selectTab(1);
        } else {
            // 这里库已经做了Fragment恢复工作，不需要额外的处理
            // 这里我们需要拿到mFragments的引用，用下面的方法查找更方便些，也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些)
            mFragments[0] = findFragment(TjDz1_Fragment.class);
            mFragments[1] = findFragment(TjJhFragment.class);
            mFragments[2] = findFragment(TjPzFragment.class);
            selectTab(index);
        }
    }
    private void selectTab(int i) {
        if (index == i) {
            return;
        }
        if (index == 0) {
            showHideFragment(mFragments[0], mFragments[0]);
        } else {
            showHideFragment(mFragments[i - 1], mFragments[index - 1]);
        }
        index = i;

    }
    public void switchList(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.f_tj_mc://卖菜
                selectTab(1);
                break;
            case R.id.f_tj_jh://进货
                selectTab(2);
                break;
            case R.id.f_tj_pz://品种
                selectTab(3);
                break;
        }
    }


    @Override
    public int layoutId() {
        return R.layout.a_tj_layout;
    }
}
