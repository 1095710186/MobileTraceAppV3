package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.Logger;
import cn.com.bigknow.trade.pos.Immediate.base.util.StringUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by hujie on 16/10/12.
 */

public class MCNumKeyboardView extends LinearLayout {

    private OnKeyBoardClick onKeyBoardClick;
    private GridLayout gridLayout;
    private TextView showTextView;
    boolean hasViewSW = false;
    private AssetManager assetManager;//获得该应用的AssetManager
    private SoundPool soundPool;
    private AudioManager mgr;
    private StringBuilder showText = new StringBuilder();

    private boolean openRing = true;


    private SimpleOnKeyBoardClick simpleOnKeyBoardClick;

    public void setSimpleOnKeyBoardClick(SimpleOnKeyBoardClick simpleOnKeyBoardClick) {
        this.simpleOnKeyBoardClick = simpleOnKeyBoardClick;
    }

    public void setOpenRing(boolean openRing) {
        this.openRing = openRing;
    }


    public boolean isOpenRing() {
        return openRing;
    }

    public void setShowTextView(TextView showTextView) {
        this.showTextView = showTextView;
    }

    public void setShowText(String showText) {
        this.showText = new StringBuilder(showText);
    }


    public MCNumKeyboardView(Context context) {
        super(context);
        init();
    }

    public MCNumKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MCNumKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MCNumKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void playRing(String num) {
//


        AssetFileDescriptor assetFileDescriptor = null;
        try {
//            assetFileDescriptor = assetManager.openFd("news/" + num + ".mp3");
            if (num.equals("delete")) {

                assetFileDescriptor = assetManager.openFd("news/" + num + ".ogg");

            } else {
                assetFileDescriptor = assetManager.openFd("news/" + num +  ".ogg");//".wav");
            }


            float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
            float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = streamVolumeCurrent / streamVolumeMax;
            soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> soundPool1.play(sampleId, volume, volume, 1, 0, 1f));
            soundPool.load(assetFileDescriptor, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public float getNumValue() {
        if (showText.length() == 0) {
            return 0;
        } else {
            if (showText.toString().startsWith(".")) {
                return Float.parseFloat("0" + showText.toString());
            } else {
                return Float.parseFloat(showText.toString());
            }
        }

    }


    public void init() {

        mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        assetManager = getContext().getAssets();
        if (soundPool == null) {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        }

        gridLayout = (GridLayout) LayoutInflater.from(getContext()).inflate(R.layout.a_mc_num_keyboard_layout, null);

        ButterKnife.bind(this, gridLayout);
        addView(gridLayout);

        gridLayout.setVisibility(View.INVISIBLE);
        gridLayout.getLayoutParams().width = GridLayout.LayoutParams.MATCH_PARENT;
        gridLayout.getLayoutParams().height = GridLayout.LayoutParams.MATCH_PARENT;

        onKeyBoardClick = new OnKeyBoardClick() {
            @Override
            public void onClickNum(String num) {

                if (isOpenRing()) {
                    playRing(num);
                }

                if (showText.length() == 0) {
                    if (!num.equals("00")) {
                        showText.append(num);
                    }
                } else {
                    if (showText.length() == 1) {
                        if (showText.substring(0, 1).equals("0")) {
                            if (num.equals("00")) {
                                return;
                            }
                            showText.delete(0, 1);
                        }
                    }
                    showText.append(num);
                }
                String text = StringUtil.getResultString(0,showText.toString());
                showText = new StringBuilder(text);
                showTextView.setText(showText);
            }

            @Override
            public void onClickDot() {
                if (showText.toString().length() == 0) {
                    showText.append("0.");
                }
                if (!showText.toString().contains(".")) {
                    showText.append(".");
                }
                if (isOpenRing()) {
                    playRing("point");
                }

                showTextView.setText(showText);
            }

            @Override
            public void onClickDel() {

                if (isOpenRing()) {
                    playRing("delete");
                }
                if (showText.length() == 0) {
                    return;
                }
                showText.delete(showText.length() - 1, showText.length());
                showTextView.setText(showText);
            }

            @Override
            public void onClickClear() {
                if (isOpenRing()) {
                    playRing("clear");
                }
                showText = new StringBuilder();
                showTextView.setText(showText);
            }

            @Override
            public void onClickTab() {
                if (simpleOnKeyBoardClick != null) {
                    simpleOnKeyBoardClick.onClickTab();
                }
            }
        };


        ViewTreeObserver vto = getViewTreeObserver();


        vto.addOnGlobalLayoutListener(this::handleGridItemWidthAndHeight
        );
    }

    private void handleGridItemWidthAndHeight() {
        int height = getHeight();
        int width = getWidth();

        if (!hasViewSW && height > 10 && width > 10) {
            Logger.writeLog("height-width", height + ":" + width);
            hasViewSW = true;
            setGridItemWidthAndHeight(width, height);
        } else {
            Observable.timer(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    handleGridItemWidthAndHeight();
                }
            });
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void setGridItemWidthAndHeight(int width, int height) {
        int childCount = gridLayout.getChildCount();
        gridLayout.setVisibility(View.VISIBLE);

        MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
        int wspace = getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin;

        int everyW = (width - wspace - dip2px(30)) / 4;

        int hspace = getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin;
        int everyH = (height - hspace - dip2px(30)) / 4;

        Logger.writeLog("everyW-everyH", everyW + ":" + everyH);

        for (int i = 0; i < childCount; i++) {
            View view = gridLayout.getChildAt(i);


            view.getLayoutParams().height = everyH;
            view.getLayoutParams().width = everyW;
            view.invalidate();
            gridLayout.updateViewLayout(view, view.getLayoutParams());


            if (onKeyBoardClick != null) {
                //设置点击事件
                view.setOnClickListener(v -> {
                    String tag = (String) view.getTag();

                    if (showTextView == null) {
                        return;
                    }
                    if ("del".equals(tag)) {
                        onKeyBoardClick.onClickDel();
                        return;
                    }

                    switch (tag) {
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "9":
                        case "0":
                        case "00":
                            onKeyBoardClick.onClickNum(tag);
                            break;
                        case ".":
                            onKeyBoardClick.onClickDot();
                            break;
                        case "C":
                        case "c":
                            onKeyBoardClick.onClickClear();
                            break;
                        case "tab":
                            onKeyBoardClick.onClickTab();
                            break;


                    }

                });
            }
        }
        gridLayout.requestLayout();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public interface OnKeyBoardClick {
        void onClickNum(String num);

        void onClickDot();

        void onClickDel();

        void onClickClear();

        void onClickTab();
    }


}
