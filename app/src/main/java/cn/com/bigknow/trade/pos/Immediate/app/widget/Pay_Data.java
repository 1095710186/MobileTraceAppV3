package cn.com.bigknow.trade.pos.Immediate.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.app.util.EntityManager;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.PaymentInformationBean;
import cn.com.bigknow.trade.pos.Immediate.model.bean.RequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleRequestWrap;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by zw on 2016/11/8.  对支付成功但是提交保存失败的数据对象做本地化保存，好下次提交。
 */

public  class  Pay_Data {
//    Context context;
//public void Pay_Data(Context context){
//    this.context=context;
////}
    /**
     * 序列化对象
     *
     * @param person
     * @return
//     * @throws IOException
     */

    long startTime = 0l;
    long endTime = 0l;

    private String serialize(PaymentInformationBean person) throws IOException {
        startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(person);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        endTime = System.currentTimeMillis();
        Log.d("serial", "序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private PaymentInformationBean deSerialization(String str) throws IOException,
            ClassNotFoundException {
        startTime = System.currentTimeMillis();
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        PaymentInformationBean person = (PaymentInformationBean) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        endTime = System.currentTimeMillis();
        Log.d("serial", "反序列化耗时为:" + (endTime - startTime));
        return person;
    }

   public void savepay_data(Context context,PaymentInformationBean javabean) {
        SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
       String string_javab="";
       try {
           string_javab=serialize(javabean);
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       edit.putString("pay_databean", string_javab);
        edit.commit();
    }


public void delet(Context context){
    SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);
    SharedPreferences.Editor edit = sp.edit();
    edit.putString("pay_databean", "");
    edit.commit();
}




   public PaymentInformationBean getpay_data(Context context) {


       PaymentInformationBean paydata_javab=null;
        SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);

       if(TextUtils.isEmpty(sp.getString("pay_databean", ""))){

       }else{

           try {
               paydata_javab = deSerialization(sp.getString("pay_databean", null));
           } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }

       }



       return paydata_javab;
    }

//提交本地数据的请求  断网数据
    public void post_pay(PaymentInformationBean pay_bean,Context context) {

        List<PaymentInformationBean> pay_bean_list = new ArrayList<>();


        pay_bean_list.add(pay_bean);

        RequestWrap<PaymentInformationBean> foodBeanRequestWrap = new RequestWrap<>();

        foodBeanRequestWrap.name = "dsMain";

        foodBeanRequestWrap.data = pay_bean_list;

        List<RequestWrap> requestWraps = new ArrayList<>();

        requestWraps.add(foodBeanRequestWrap);

        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        simpleRequestWrap.dataset = requestWraps;

        ToastUtil.makeToast("同步数据");
        Api.api().PaymentSubmitgb(simpleRequestWrap, new SimpleRequestListener<String>() {
            @Override
            public void onSuccess(String o) {

               delet(context);

            }
            @Override
            public void onError(ApiError error) {
                if(!TextUtils.isEmpty(error.errorCode)){
                    if(error.errorCode.equals("common.data.ood.err_code")||error.errorCode.equals("mvp.pay.used.response.err_code")){

                      delet(context);

                    }

                }

            }
        });


    }




    //断电补偿

//刷卡支付前保存订单信息
    public void saveorder_data(Context context,PaymentInformationBean javabean,int dex) {
        SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String string_javab="";
        try {
            string_javab=serialize(javabean);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        edit.putString("order_databean"+dex, string_javab);
        edit.commit();
    }

    //刷卡支付回调后删除保存的订单信息
    public void deletorder(Context context,int dex){
        SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("order_databean"+dex, "");
        edit.commit();
    }



    //获取保存的订单信息
    public PaymentInformationBean getorder_data(Context context,int dex) {


        PaymentInformationBean paydata_javab=null;
        SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);

        if(TextUtils.isEmpty(sp.getString("order_databean"+dex, ""))){

        }else{

            try {
                paydata_javab = deSerialization(sp.getString("order_databean"+dex, null));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }



        return paydata_javab;
    }









    /**
     * 刷卡支付调用工行app消费返回数据处理
     */
    public PaymentInformationBean parsing_data(String data) {
        String[] sourceStrArray = data.split("\\|");
        PaymentInformationBean zhifu_xinxi=null;
        for (int i = 0; i < sourceStrArray.length; i++) {

            if (sourceStrArray[i].toString().length() > 2) {

                if (sourceStrArray[i].toString().substring(0, 3).equals("039")) {
                    if(sourceStrArray[i].toString().length()>3){
                        String pay_v = sourceStrArray[i].toString().substring(3, sourceStrArray[i].toString().length());

                        if (pay_v.equals("00")) {

                            zhifu_xinxi =tag_data(sourceStrArray);
//                            ToastUtil.makeToast("支付成功");

                        } else {

//                            ToastUtil.makeToast("支付失败");

                        }
                    }else{
//                        ToastUtil.makeToast("此检索号对应的支付状态为支付失败");
                    }
                }
            }
        }
return zhifu_xinxi;
    }


    public PaymentInformationBean tag_data(String[] paydataStrArray) {

        PaymentInformationBean order_bean=new PaymentInformationBean();
        HashMap<String, String> pay_map = new HashMap<String, String>();
        String data_tian="";
        String data_time="";
        String data_jiansuohao="";
        String data_liushuihao="";
        String shanghuhao="";
        for (int i = 0; i < paydataStrArray.length; i++) {

            if (paydataStrArray[i].toString().length() > 2) {


                if (paydataStrArray[i].toString().substring(0, 3).equals("039")) {
                    // 支付状态
                    String pay_zt = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    if (pay_zt.equals("00")) {
                        order_bean.setState("Y");

                    }else{
                        order_bean.setState("N");
                    }

                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("013")) {
                    // 交易日期（例如：(YYYYMMDD)）
                    String pay_time = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("payData", pay_time);
                    order_bean.setPayDate(pay_time);
                    data_tian=pay_time;
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("907")) {
                    // 卡类型（例如：中国银行）
                    String pay_bank = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("bankId", pay_bank);
                    order_bean.setBankId(pay_bank);
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("002")) {
                    // 卡号
                    String pay_card_number = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("accountNo", pay_card_number);
                    order_bean.setAccountNo(pay_card_number);
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("004")) {
                    // 交易金额（例如：000000000001为一分钱，不大于12字节的金额，最后两位是小数金额）
                    String pay_money = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("amt", pay_money);
                    order_bean.setAmt(pay_money);
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("037")) {
                    // 检索号
                    String pjiansuo = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    data_jiansuohao=pjiansuo;
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("042")) {
                    // 商户号
                    String shanghu = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    shanghuhao=shanghu;
                }

                if (paydataStrArray[i].toString().substring(0, 3).equals("012")) {
                    // 交易时间（例如：HHMMSS）
                    String pay_time = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("pay_time", pay_time);
                    order_bean.setResponseTime(pay_time);
                    data_time=pay_time;
                }
                if (paydataStrArray[i].toString().substring(0, 3).equals("902")) {
                    // 结算响应流水ID
                    String pay_ls_id = paydataStrArray[i].toString().substring(3, paydataStrArray[i].toString().length());
                    pay_map.put("responseId", pay_ls_id);
                    data_liushuihao=pay_ls_id;

                }

            }
        }
        order_bean.setResponseId(data_liushuihao+","+data_jiansuohao+","+shanghuhao);
        try {
            order_bean.setPayDate(changeTime(data_tian+data_time));
        } catch (java.lang.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return order_bean;

    }



    public String changeTime(String time)throws Exception{

        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = oldFormat.parse(time);
        return newFormat.format(date);
    }



//处理断电校验后的支付信息

    public void savepay_xinxi(Context context,PaymentInformationBean javabean) {//保存校验后要提交的支付信息
        SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String string_javab="";
        try {
            string_javab=serialize(javabean);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        edit.putString("pay_xinxi", string_javab);
        edit.commit();
    }


    public void delet_xinxi(Context context){
        SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("pay_xinxi", "");
        edit.commit();
    }




    public PaymentInformationBean getpay_xinxi(Context context) {

        PaymentInformationBean paydata_javab=null;
        SharedPreferences sp = context.getSharedPreferences("pay_data", Activity.MODE_PRIVATE);

        if(TextUtils.isEmpty(sp.getString("pay_xinxi", ""))){
        }else{
            try {
                paydata_javab = deSerialization(sp.getString("pay_xinxi", null));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }



        return paydata_javab;
    }


    //提交本地数据的请求  断电数据
    public void post_pay_xinxi(PaymentInformationBean pay_bean,Context context) {

        List<PaymentInformationBean> pay_bean_list = new ArrayList<>();


        pay_bean_list.add(pay_bean);

        RequestWrap<PaymentInformationBean> foodBeanRequestWrap = new RequestWrap<>();

        foodBeanRequestWrap.name = "dsMain";

        foodBeanRequestWrap.data = pay_bean_list;

        List<RequestWrap> requestWraps = new ArrayList<>();

        requestWraps.add(foodBeanRequestWrap);

        SimpleRequestWrap simpleRequestWrap = new SimpleRequestWrap();
        simpleRequestWrap.dataset = requestWraps;

        ToastUtil.makeToast("同步数据");
        Api.api().PaymentSubmitgb(simpleRequestWrap, new SimpleRequestListener<String>() {
            @Override
            public void onSuccess(String o) {
                delet_xinxi(context);
            }
            @Override
            public void onError(ApiError error) {
                if(!TextUtils.isEmpty(error.errorCode)){
                    if(error.errorCode.equals("common.data.ood.err_code")||error.errorCode.equals("mvp.pay.used.response.err_code")){
                        delet_xinxi(context);
                    }

                }

            }
        });


    }





}
