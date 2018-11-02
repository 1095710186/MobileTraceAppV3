package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.iflytek.cloud.SpeechUtility;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.ui.PayCuccessActivity;

public class Main2Activity extends Activity {

    EditText shuru;

    TextView dianji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        SpeechUtility.createUtility(Main2Activity.this, "appid=5812c770");

        shuru = (EditText) findViewById(R.id.iat_text_test);

        dianji = (TextView) findViewById(R.id.iat_recognize_test);
        SpeechUtility.createUtility(this, "appid=5812c770");
        Yuying yuyin=new Yuying();
        yuyin.init(this,shuru,dianji);

    }

}
