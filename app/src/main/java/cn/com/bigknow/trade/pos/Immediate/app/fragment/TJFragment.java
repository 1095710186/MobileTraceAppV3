package cn.com.bigknow.trade.pos.Immediate.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioGroup;

import butterknife.BindView;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment.TjDzFragment;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment.TjJhFragment;
import cn.com.bigknow.trade.pos.Immediate.app.fragment.tj_fragment.TjPzFragment;
import icepick.Icepick;
import icepick.State;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hujie on 16/10/11.
 */

public class TJFragment extends BaseSupportFragment {

    @BindView(R.id.f_tj_radipgroup)
    RadioGroup radioGroup;


    @State
    int index = 0;
    SupportFragment[] mFragments = new SupportFragment[3];

    public static TJFragment newInstance() {
        Bundle args = new Bundle();
        TJFragment fragment = new TJFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initLazyView() {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
        initFragments(savedInstanceState);

    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mFragments[0] = TjDzFragment.newInstance();
            mFragments[1] = TjJhFragment.newInstance();
            mFragments[2] = TjPzFragment.newInstance();
            loadMultipleRootFragment(R.id.f_tj_contentLayout, index, mFragments[0],
                    mFragments[1], mFragments[2]);
            selectTab(1);
        } else {
            // 这里库已经做了Fragment恢复工作，不需要额外的处理
            // 这里我们需要拿到mFragments的引用，用下面的方法查找更方便些，也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些)
            mFragments[0] = findFragment(TjDzFragment.class);
            mFragments[1] = findFragment(TjJhFragment.class);
            mFragments[2] = findFragment(TjPzFragment.class);
            selectTab(index);
        }


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> switchList(group, checkedId));

    }

    private void selectTab(int i) {
        if (index == i) {
            return;
        }
        if (mFragments[i - 1]==null){
            mFragments[0] = findFragment(TjDzFragment.class);
            mFragments[1] = findFragment(TjJhFragment.class);
            mFragments[2] = findFragment(TjPzFragment.class);
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
            case R.id.f_tj_mc://对账
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
    @Override
    public int layoutId() {
        return R.layout.f_tj_layout;
    }
}
