package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yatatsu.autobundle.AutoBundleField;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.util.DensityUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.GlideHelper;
import cn.com.bigknow.trade.pos.Immediate.app.util.ImageUtils;
import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.PhotoUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UUIDUtil;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.app.widget.NumKeyboardView;
import cn.com.bigknow.trade.pos.Immediate.app.widget.ProvinceView;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.AlertDialogUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.RegularUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.Contant;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ChePai;
import cn.com.bigknow.trade.pos.Immediate.model.bean.FoodImgInfo;
import cn.com.bigknow.trade.pos.Immediate.model.bean.ListResultWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEventType;
import cn.com.bigknow.trade.pos.Immediate.model.bean.TXJHDWrapper;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static cn.com.bigknow.trade.pos.Immediate.R.id.numTV;
import static cn.com.bigknow.trade.pos.Immediate.R.id.tv;
import static cn.com.bigknow.trade.pos.Immediate.R.id.zjcpLL;

/**
 * Created by hujie on 16/10/20.
 *
 * 填写进货单车牌
 */

public class  TXJHDCPActivity extends BaseActivity {
    @BindView(R.id.pTV)
    EditText mPTV;
    @BindView(R.id.cTV)
    TextView mCTV;
    @BindView(R.id.numTV)
    EditText mNumTV;
    @BindView(R.id.zjcpTV1)
    TextView mZjcpTV1;
    @BindView(R.id.zjcpLL1)
    LinearLayout mZjcpLL1;
    @BindView(R.id.zjcpTV2)
    TextView mZjcpTV2;
    @BindView(R.id.zjcpLL2)
    LinearLayout mZjcpLL2;
    @BindView(R.id.zjcpTV3)
    TextView mZjcpTV3;
    @BindView(R.id.zjcpLL3)
    LinearLayout mZjcpLL3;
    @BindView(zjcpLL)
    LinearLayout mZjcpLL;
    @BindView(R.id.totalET)
    TextView mTotalET;
    List<ChePai> chePais;


    @BindView(R.id.imageIV)
    ImageView imageView;


    String selectFilePath;
    @AutoBundleField(required = false)
    TXJHDWrapper wrapper;


    @BindView(R.id.nextTV)
    TextView onNextTV;


    private static final String FILETYPE_entryBill = "entryBill";
    public String IMG_URL = ModelConfig.MVP_URL + "servlet/fileupload?oper_type=img&masterFileType=%1$s&masterId=%2$s&fileId=%3$s";
    private String uploadSession = UUIDUtil.generateFormattedGUID();
    private String userId = UserManager.getInstance().getUserInfo().getUserId();
    private String orgId = UserManager.getInstance().getUserInfo().getOrgId();
    String Where;
    @Override
    public void onBackPressed() {
       Where = getIntent().getStringExtra(Contant.WHERE);
        if (Where != null && Where.equals(Contant.JH)) {
            showExitJHDialog(3);
        } else {
            super.onBackPressed();
        }
    }

    private long firstTime = 0;

    private long jianClickTime;
    private long jiaClickTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            String Where = getIntent().getStringExtra(Contant.WHERE);
            if (Where != null && Where.equals(Contant.JH)) {
                showExitJHDialog(3);

            }
        }
        else  if (keyCode == KeyEvent.KEYCODE_HOME) {
            Message message = Message.obtain();
            String Where = getIntent().getStringExtra(Contant.WHERE);
            if (Where != null && Where.equals(Contant.JH)) {
//                message.what = 0;

                showExitJHDialog(3);
            }else {
//                message.what = 1;
                finish();
            }
//            handler.sendMessage(message);

            return true;

        }else if(keyCode == KeyEvent.KEYCODE_MENU) {//MENU键
            //监控/拦截菜单键
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_VOLUME_UP) {//音量减小增大


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
        return super.onKeyDown(keyCode, event);

    }

    private Handler handler = new Handler() {

        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {
//           int m = (int)msg.obj;
            if(msg.what == 0){ //jh
                showExitJHDialog(3);
            }else if (msg.what == 1){
                finish();
            }
        }
    };
    @Override
    public void init() {
        LogUtil.v("everyW:", DensityUtil.px2dip(mActivity,115)+"---");
        LogUtil.v("everyH:",DensityUtil.px2dip(mActivity,111)+"---"+"");
        loadChePai();
        setData();

        mNumTV.setFocusable(false);// 不让该edittext获得焦点
        mNumTV.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mNumTV.setInputType(InputType.TYPE_NULL); // 关闭软键盘，这样当点击该edittext的时候，不会弹出系统自带的输入法
                return false;
            }

        });
        closeInputMethod();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void closeInputMethod() {
//        mPTV.clearFocus();
        //设置光标不显示,但不能设置光标颜色

        // 关闭输入法
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(mPTV.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    boolean che_fo = true;

    @OnClick(R.id.pai_chepai)
    public void onPaicheClick() {

        Intent intent = new Intent();
        intent.setClass(this, CameraActivity.class);
        startActivityForResult(intent, 11);

//        che_fo=true;
//        PhotoUtil.open(this, true, 1);

    }

    private void uploadImage_che(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("file", file.getName(), photoRequestBody);

            Api.api().uploadChe(bindUntilEvent(ActivityEvent.DESTROY), photo, new SimpleRequestListener<BaseEntity<String>>() {
                @Override
                public void onSuccess(BaseEntity<String> o) {
//                    ToastUtil.makeToast("返回车牌==="+o.data.toString());
                    try {
                        JSONArray jsonArray = new JSONArray(o.data.toString());
                        if (jsonArray.length() > 0) {
                            String jsonObject = jsonArray.get(0).toString();

                            mPTV.setText(jsonObject.substring(0, 1));
                            mCTV.setText(jsonObject.substring(1, 2));
                            mNumTV.setText(jsonObject.substring(2, jsonObject.length()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onStart() {
                    progressHudUtil.showProgressHud();
                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }

                @Override
                public void onError(ApiError error) {
                    super.onError(error);

                }
            });
        }
    }


    @OnClick(R.id.totalET)
    public void onTotalClick() {
        showDialogTotal();
    }

    private void showDialogTotal() {
        View view = LayoutInflater.from(this).inflate(R.layout.zzl_num_keyboard_layout, null);


        TextView showTV = (TextView) view.findViewById(R.id.showTV);
        TextView CPTV = (TextView) view.findViewById(R.id.cpTV);

        CPTV.setText(mPTV.getText().toString() + mCTV.getText().toString() + mNumTV.getText().toString());
        NumKeyboardView numKeyboardView = (NumKeyboardView) view.findViewById(R.id.numkeyboardView);
        numKeyboardView.setShowTextView(showTV);


        AlertDialogUtil.showNumKeyboardDialog(this, view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.sureBtn) {
                    if (numKeyboardView.getNumValue() == 0) {
                        ToastUtil.makeToast("请输入重量");
                        return;
                    }
//                    BigDecimal num = new BigDecimal(String.valueOf(numKeyboardView.getNumValue()));
//                    DecimalFormat df = new  DecimalFormat("#.##");
//                    String res = df.format(num);
                    mTotalET.setText(showTV.getText().toString());
                    AlertDialogUtil.dissMiss();
                }

            }
        });
    }


    public void setData() {
        if (wrapper != null) {
            onNextTV.setText("保存");
            if (wrapper.getChePai() != null) {
                mPTV.setText(wrapper.getChePai().getProvince());
                mCTV.setText(wrapper.getChePai().getCity());
                mNumTV.setText(wrapper.getChePai().getNumber());
            }
            mTotalET.setText(wrapper.getTotal());
            if (wrapper.getImageFilePath() != null) {String filePath = wrapper.getImageFilePath();
                if (filePath != null) {
                    showImage(filePath);
                }
            }

        }
    }


    boolean isP = false;     //是否是第一次走流程
    @OnClick(R.id.pTV)
    public void onPClick() {
        closeInputMethod();
        showDialogP();
    }
    void showDialogP(){
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.provice_layout, null);
        ProvinceView p = (ProvinceView) view1.findViewById(R.id.provinceView);
        p.setOnItemClickListener(item -> {
            AlertDialogUtil.dissMiss();

            if (mPTV.getText().length()>0){
                isP = true;
            }else {
               /* if (!isP){
                    showDialogZm();
                }*/
            }
            if (mCTV.getText().length()== 0){
                showDialogZm();
            }
            mPTV.setText(item);


        });
        AlertDialogUtil.showProvinceDialog(mActivity, view1, view2 -> {

        });
    }

    void showDialogZm(){
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.provice_letter_layout, null);
        ProvinceView p = (ProvinceView) view1.findViewById(R.id.provinceView);
        p.setOnItemClickListener(item -> {
            AlertDialogUtil.dissMiss();

            if (mCTV.getText().length()>0){
                isP = true;
            }else {
//                if (!isP){
//                    showDialogNum();
//                }
            }
            mCTV.setText(item);
            if (mNumTV.getText().length() == 0) {
                showDialogNum();
            }

//            mNumTV.setFocusable(true);
            //打开软键盘
//            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        });
        AlertDialogUtil.showProvinceDialog(mActivity, view1, view2 -> {

        });
    }
    private StringBuffer sb = new StringBuffer();// 用来存放输入数字
    void showDialogNum(){
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.provice_letter_num_layout, null);
        ProvinceView p = (ProvinceView) view1.findViewById(R.id.provinceView);
        TextView CPTV = (TextView) view1.findViewById(R.id.cpTV);
        CPTV.setText(""+mPTV.getText().toString() + mCTV.getText().toString());
        TextView showTV = (TextView) view1.findViewById(R.id.showTV);
        ImageButton dele =(ImageButton) view1.findViewById(R.id.deleBtn);//
        dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb.length() - 1 >= 0) {
                    sb.delete(sb.length() - 1, sb.length());
                    showTV.setText(sb.toString().trim());
                }
            }
        });
        p.setOnItemClickListener(item -> {
            if (sb.length()>5)
                return;
            sb.append(item);
            showTV.setText(sb.toString().trim());
        });
       /* AlertDialogUtil.showProvinceDialog(mActivity, view1, view2 -> {


        });*/
        AlertDialogUtil.showProvinceDialog(mActivity, view1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtil.dissMiss();
                if (v.getId() == R.id.sureBtn) {


                    if (mNumTV.getText().length()>0){

                    }else {

                    }
                    mNumTV.setText(sb.toString().trim());
                    if (mTotalET.getText().toString().trim().length()==0) {
                        showDialogTotal();
                    }


                }else if (v.getId() == R.id.cancelBtn){
                    sb = new StringBuffer();
                    mNumTV.setText(sb.toString().trim());

                }

                sb = new StringBuffer();
            }
        });
    }
    private String lastString = "";

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
       /* if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            *//*隐藏软键盘*//*
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
            showDialogTotal();

            return true;
        }*/
        return super.dispatchKeyEvent(event);
    }
    private void loadChePai() {
      /*  mNumTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                *//*判断是否是“GO”键*//*
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    *//*隐藏软键盘*//*
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    ToastUtil.makeToast("123");

                    return true;
                }
                return false;
            }
        });*/
        mPTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    mPTV.setCursorVisible(false);
                    closeInputMethod();
                }

            }
        });
        mNumTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(lastString)) {
                    String s1 = s.toString().toUpperCase();
                    lastString = s1;
                    mNumTV.setText(s1);
//                    if (s.length()>0) {



//                    }

                }
                mNumTV.setSelection(mNumTV.length());

            }

            @Override
            public void afterTextChanged(Editable s) {
                 chePais = EntityManager.getChePais(s.toString());
                if (chePais != null && chePais.size() > 0) {
                    mZjcpLL.setVisibility(View.GONE);
                    mZjcpLL1.setVisibility(View.GONE);
                    mZjcpLL1.setVisibility(View.GONE);
                    mZjcpLL2.setVisibility(View.GONE);
                    setChepai();
               }

            }
        });


        chePais = EntityManager.getChePais();
        setChepai();

    }

    void setChepai(){
        if (chePais != null && chePais.size() > 0) {
            mZjcpLL.setVisibility(View.VISIBLE);
            if (chePais.size()>3){
                mZjcpLL1.setVisibility(View.VISIBLE);
                mZjcpLL2.setVisibility(View.VISIBLE);
                mZjcpLL3.setVisibility(View.VISIBLE);

                ChePai chePai = chePais.get(0);
                ChePai chePai2 = chePais.get(1);
                ChePai chePai3 = chePais.get(2);

                mZjcpTV1.setText(chePai.getProvince() + chePai.getCity() + "  " + chePai.getNumber());
                mZjcpTV2.setText(chePai2.getProvince() + chePai2.getCity() + "  " + chePai2.getNumber());
                mZjcpTV3.setText(chePai3.getProvince() + chePai3.getCity() + "  " + chePai3.getNumber());

            }else {
                if (chePais.size() == 1) {
                    ChePai chePai = chePais.get(0);
                    mZjcpLL1.setVisibility(View.VISIBLE);
                    mZjcpLL2.setVisibility(View.GONE);
                     mZjcpLL3.setVisibility(View.GONE);
                    mZjcpTV1.setText(chePai.getProvince() + chePai.getCity() + "  " + chePai.getNumber());
                } else if (chePais.size() == 2) {
                    mZjcpLL1.setVisibility(View.VISIBLE);
                    mZjcpLL2.setVisibility(View.VISIBLE);
                    mZjcpLL3.setVisibility(View.GONE);
                    ChePai chePai = chePais.get(0);
                    ChePai chePai2 = chePais.get(1);

                    mZjcpTV1.setText(chePai.getProvince() + chePai.getCity() + "  " + chePai.getNumber());
                    mZjcpTV2.setText(chePai2.getProvince() + chePai2.getCity() + "  " + chePai2.getNumber());

                } else if (chePais.size() == 3) {
                    mZjcpLL1.setVisibility(View.VISIBLE);
                    mZjcpLL2.setVisibility(View.VISIBLE);
                    mZjcpLL3.setVisibility(View.VISIBLE);

                    ChePai chePai = chePais.get(0);
                    ChePai chePai2 = chePais.get(1);
                    ChePai chePai3 = chePais.get(2);

                    mZjcpTV1.setText(chePai.getProvince() + chePai.getCity() + "  " + chePai.getNumber());
                    mZjcpTV2.setText(chePai2.getProvince() + chePai2.getCity() + "  " + chePai2.getNumber());
                    mZjcpTV3.setText(chePai3.getProvince() + chePai3.getCity() + "  " + chePai3.getNumber());

                }
            }


        } else {
            mZjcpLL.setVisibility(View.GONE);
        }
    }
    @Override
    protected boolean autoBindBundle() {
        return true;
    }

    @OnClick(R.id.cTV)
    public void onCClick() {
        if (mPTV.getText().length() == 0) {
            showDialogP();
        }else {
            showDialogZm();    //车牌第二位字母
        }

    }
    @OnClick(R.id.numTV)
    public  void onNumClick(){
        if (mPTV.getText().length() == 0) {
            showDialogP();
        }else if (mCTV.getText().length() == 0 ){
            showDialogZm();
        }else {
            showDialogNum();
        }
    }
    Display display;
    static AlertDialog dialog;
    public void dissclose() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
    //弹出提示框 退出
    private void showExitJHDialog( int type) {
        View view1 = LayoutInflater.from(mActivity).inflate(R.layout.dialog_wxit_jh, null);

        View cd_back = (View) view1.findViewById(R.id.cd_layout_quxiao);
        View cd_sure = (View) view1.findViewById(R.id.cd_layout_sure);
        TextView tvNews = (TextView) view1.findViewById(R.id.tvNews);
        if (type ==0 || type == 1) {
            if (chepai != null) {
                tvNews.setText(chepai + "\r\n\r\n 确定要继续吗？");

            } else {
                if (zZ != null) {
                    tvNews.setText(zZ + "\r\n\r\n 确定要继续吗？");
                } else {
                    if (gbD != null) {
                        tvNews.setText(gbD + "\r\n\r\n 确定要继续吗？");
                    } else {

                    }

                }

            }
        }else  if (type == 3){
            tvNews.setText("确定要退出进货？");
        }else  if (type == 4){
            tvNews.setText("确定要回到主页？");
        }
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
                Message message = Message.obtain();
                if (type == 0) {
                    startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).build(mActivity));
                   /* message.what = 10;
                    handler.sendMessage(message);*/
                    saveChepai(wrapper.getChePai());  //保存失败
                    finish();
                }else if (type == 1){
                    EventMamager.getInstance().postObjEvent(SimpleEventType.ON_CHANGE_CHEPAI_BACK, wrapper);
                  /*  message.what = 11;
                    handler.sendMessage(message);*/
                    saveChepai(wrapper.getChePai());//保存失败
                    finish();
                }else if (type == 3){
                    finish();
                }else if (type == 4){
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    startActivity(intent);
                    EventMamager.getInstance().postEvent(SimpleEventType.ON_KEY_HOME);
                    finish();
                }

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

    boolean isNewsAll = true;
    String chepai = null;
    String zZ = null;
    String gbD = null;
    ChePai chePai = null;
    @OnClick(R.id.onNextTV)
    public void onNextClick() {

        String typeNews = null;
        if (mCTV.getText().length() == 0 || mPTV.getText().length() == 0 || mNumTV.getText().toString().trim().length() == 0) {
//            ToastUtil.makeToast("请输入车牌");
//            return;
            isNewsAll = false;
            chepai = "车牌信息未填写，";
        }else {
            chePai = new ChePai();
            chePai.setProvince(mPTV.getText().toString());
            chePai.setCity(mCTV.getText().toString());
            chePai.setNumber(mNumTV.getText().toString());
            if (!RegularUtil.isChePai(chePai.getChePai())) {
                ToastUtil.makeToast("请输入合法的车牌");
                return;
            }
        }


        if (mTotalET.getText().toString().trim().length() == 0) {
//            ToastUtil.makeToast("请输入货物总重量");
//            return;
            isNewsAll = false;
            zZ = "货物重量信息未填写，";
        }


        if (wrapper == null && selectFilePath == null) {
//            ToastUtil.makeToast("请上传过磅单据");
//            return;
            isNewsAll = false;
            gbD = "您还未上传过磅单，";

        }
        if (wrapper != null && wrapper.getImageFilePath() == null) {
//            ToastUtil.makeToast("请上传过磅单据");
//            return;
            if (selectFilePath == null) {
                isNewsAll = false;
                gbD = "您还未上传过磅单，";
            }else {
//                isNewsAll = true;
//                gbD = null;
            }

        }


        if (wrapper == null) {
            wrapper = new TXJHDWrapper();
            wrapper.setChePai(chePai);
            wrapper.setImageFilePath(selectFilePath);
            wrapper.setFileId(fileId);
            wrapper.setTotal(mTotalET.getText().toString().trim());
            if (!isNewsAll){
                showExitJHDialog(0);
            }else {
                startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).build(this));
                saveChepai(chePai);
                finish();
            }
        } else {
            wrapper.setChePai(chePai);
            if (selectFilePath != null) {
                wrapper.setImageFilePath(selectFilePath);
            }
            if (fileId != null) {
                wrapper.setFileId(fileId);
            }
//            if (chepai==null) {
                wrapper.setTotal(mTotalET.getText().toString().trim());
//            }
//            if (!isNewsAll){
            String Where = getIntent().getStringExtra(Contant.WHERE);
                if (Where != null && Where.equals(Contant.JH)) {
                    if (!isNewsAll) {
                        showExitJHDialog(0);
                    }else {
                        startActivity(TXJHDActivityAutoBundle.createIntentBuilder(wrapper).build(mActivity));
                        saveChepai(chePai);
                        finish();
                    }
                }else {
                    if (!isNewsAll) {
                        showExitJHDialog(1);
                    }else {
                        EventMamager.getInstance().postObjEvent(SimpleEventType.ON_CHANGE_CHEPAI_BACK, wrapper);
                        saveChepai(chePai);
                        finish();
                    }
                }
//            }else {
//                EventMamager.getInstance().postObjEvent(SimpleEventType.ON_CHANGE_CHEPAI_BACK, wrapper);
//                saveChepai(chePai);
//                finish();
//            }
        }
        isNewsAll = true;
         chepai = null;
         zZ = null;
         gbD = null;
         chePai = null;
        isP = false;

    }
    void saveChepai(ChePai chePai){
        boolean isChepai = false;
        if (chePai!=null) {
            if (chePais!=null  && chePais.size()>0) {
                for (ChePai chePai1 : chePais) {
                    if (chePai.getProvince().equals(chePai1.getProvince())) {
                        if (chePai.getCity().equals(chePai1.getCity())) {
                            if (chePai.getNumber().equals(chePai1.getNumber())) {
                                isChepai = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!isChepai) {
                EntityManager.saveChePai(chePai);
            }
        }
    }
    boolean isCHoose = false;
    @OnClick(value = {R.id.zjcpLL1, R.id.zjcpLL2, R.id.zjcpLL3})
    public void onZJCPCLICK(View v) {

        v.setSelected(true);
        ChePai chePai = null;
        if (chePais.size()>=3) {
            if (v == mZjcpLL1) {
                chePai = chePais.get(0);
                mZjcpLL2.setSelected(false);
                mZjcpLL3.setSelected(false);
            }
            if (v == mZjcpLL2) {
                chePai = chePais.get(1);
                mZjcpLL1.setSelected(false);
                mZjcpLL3.setSelected(false);
            }
            if (v == mZjcpLL3) {
                chePai = chePais.get(2);
                mZjcpLL1.setSelected(false);
                mZjcpLL2.setSelected(false);
            }
        }else if (chePais.size() ==2){
             if (v == mZjcpLL1) {
                 chePai = chePais.get(0);
                 mZjcpLL2.setSelected(false);
                 mZjcpLL3.setSelected(false);
             }
             if (v == mZjcpLL2) {
                 chePai = chePais.get(1);
                 mZjcpLL1.setSelected(false);
                 mZjcpLL3.setSelected(false);
             }
        }  else if (chePais.size() == 1){
            if (v == mZjcpLL1) {
                  chePai = chePais.get(0);
                mZjcpLL2.setSelected(false);
                mZjcpLL3.setSelected(false);
            }

        }
        if (chePai!=null) {
            mPTV.setText(chePai.getProvince());
            mCTV.setText(chePai.getCity());
            isCHoose = true;
            mNumTV.setText(chePai.getNumber());
        }
    }

String url_gp_path=null;
    @OnClick(R.id.imageLL)
    public void onClick() {
        che_fo = false;
        url_gp_path=null;
//        PhotoUtil.open(this, true, 1);
        url_gp_path=  PhotoUtil.takePhoto(TXJHDCPActivity.this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            if (che_fo) {
//                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
//                if (pathList != null && pathList.size() > 0) {
//                    String selectFilePath = pathList.get(0);
//                    uploadImage_che(selectFilePath);
//                }
//            } else {
//                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
//                if (pathList != null && pathList.size() > 0) {
//                    String selectFilePath = pathList.get(0);
//                    uploadImage(selectFilePath);
//                }
//            }
//        }
        if (data != null && resultCode == 11) {
            String path = data.getStringExtra("path_url");
            uploadImage_che(path);
        }

        if (requestCode == PhotoUtil.REQUESTCODE_TAKE_PHOTO) {
          uploadImage(url_gp_path);
        }

    }

    String fileId;

    private void uploadImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {

            byte[] fileByte = null;
            Bitmap bitmap = ImageUtils.getScaleBitmap(filePath);
            if (bitmap == null) {
                ToastUtil.makeToast("图片不存在");
                return;
            }

            progressHudUtil.showProgressHud();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100表示不压缩，将不压缩的数据存放到baos中
            int per = 100;
            int size = baos.toByteArray().length / 1024;

            while (size > 400 ) { // 循环判断如果压缩后图片是否大于400kb,大于继续压缩
                if (per > 10) {
                    per -= 10;// 每次都减少10
                } else {
                    break;
                }
                baos.reset();// 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, per, baos);// 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
                size = baos.toByteArray().length / 1024;
            }
            try {
                baos.flush();
                baos.close();
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                    System.gc();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileByte = baos.toByteArray();

            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), fileByte);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("file", file.getName(), photoRequestBody);

            Api.api().uploadFile(bindUntilEvent(ActivityEvent.DESTROY), photo, uploadSession, null, FILETYPE_entryBill, userId, orgId, new SimpleRequestListener() {
                @Override
                public void onSuccess(Object o) {
                    selectFilePath = filePath;
                    fileId = ((BaseEntity) o).fileid;
                    showImage(filePath);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {
                    progressHudUtil.dismissProgressHud();
                }

                @Override
                public void onError(ApiError error) {
                    super.onError(error);

                }
            });
        }
    }


    private void getImagesData(String id) {
        Api.api().getFoodImg(bindUntilEvent(ActivityEvent.DESTROY), id, FILETYPE_entryBill, new SimpleRequestListener<ListResultWrap<List<FoodImgInfo>>>() {
                    @Override
                    public void onSuccess(ListResultWrap<List<FoodImgInfo>> o) {
                        if (o != null && o.rows != null && o.rows.size() > 0) {
                            String url = String.format(IMG_URL, id, FILETYPE_entryBill, o.rows.get(0).getId());
                            wrapper.setFileId(o.rows.get(0).getId());
                            wrapper.setImageFilePath(url);
                            showImage(url);
                        }
                    }

                    @Override
                    public void onError(ApiError error) {
                        super.onError(error);
                        progressHudUtil.dismissProgressHud();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressHudUtil.dismissProgressHud();
                    }
                }
        );
    }


    private void showImage(String filePath) {

        GlideHelper.load(this, filePath, true, true, imageView);

    }


    @Override
    protected void onPause() {
        super.onPause();
//        saveChepai(wrapper.getChePai());
    }

    @Override
    public int layoutId() {
        return R.layout.txjhd_cp_layout;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(0x80000000, 0x80000000);


    }
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80;//android 4.0版本以上的屏蔽home键

}
