package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by hujie on 16/11/2.
 *
 *
 * 特殊字体
 */

public class PoliticaTextView extends TextView {
    public PoliticaTextView(Context context) {
        super(context);
        init();
    }

    public PoliticaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoliticaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PoliticaTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        String fontPath = "fonts/Politica XT Bold.ttf";
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
        setTypeface(tf);
    }
}
