package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.snowdream.android.app.DownloadListener;
import com.github.snowdream.android.app.DownloadManager;
import com.github.snowdream.android.app.DownloadTask;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.bigknow.trade.pos.Immediate.BuildConfig;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Version;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * 检测版本更新的dialog
 * Created by MengJie on 2016/4/22.
 */
public class CheckVersionUpdate extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "CheckVersionUpdate";
    private static final String APK_PATH = "/sdcard/pos.apk";
    private static final int GET_UNDATAINFO_UPDATA = 1;
    private static final int GET_UNDATAINFO_ERROR = 2;
    @BindView(R.id.dialog_check_version_sure)
    TextView mSure;
    @BindView(R.id.dialog_check_version_tv_check_updata)
    TextView mCheckUdata;
    @BindView(R.id.dialog_check_version_progress)
    ProgressBar mProgress;
    @BindView(R.id.dialog_check_version_tv_bg_line)
    TextView mLine;
    @BindView(R.id.dialog_check_version_linear)
    LinearLayout mLinearSure;
    //声明AppContext对象
//    private static UpdataInfo info;
    private String addressXml = "";
    private boolean isFirstSure = false;

    public static DialogFragment instanceFragment() {
        CheckVersionUpdate payDialog = new CheckVersionUpdate();
        return payDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_check_version, container, false);

        ButterKnife.bind(this, view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //给窗口加一个透明背景 让圆角能显示出来
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        //创建AppContext对象
//        mApp = (AppContext) getActivity().getApplication();
//        //将该Fragment对应的activity加入AppContext中的activities集合中
//        mApp.addActivity(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {


        mSure.setOnClickListener(this);
        httpGetXmlVersionPath();


    }

    @Override
    public void onResume() {
        super.onResume();
        //设置高宽
        int width = getActivity().getResources().getDisplayMetrics().widthPixels * 3 / 5;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels * 2 / 5;
        getDialog().getWindow().setLayout(width, height);


    }


    public static String DOWNLOAD_URL = ModelConfig.MVP_URL + "servlet/fileupload?oper_type=download&fileId=";
    String Url;
    /**
     * 得到版本信息的xml地址
     */
    private void httpGetXmlVersionPath() {

        String version = BuildConfig.VERSION_NAME;
        Api.api().checkUpdate(version + "", new SimpleRequestListener<Version>() {
            @Override
            public void onSuccess(Version o) {
                mCheckUdata.setText("检测到最新版本"+o.getVersion()+"是否更新！");
//                downLoad(context, DOWNLOAD_URL + o.getFileId());
                Url = DOWNLOAD_URL + o.getFileId();
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_UPDATA;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
            }
        });

    }

    /**
     * 按钮监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_check_version_sure:
                if (!isFirstSure){
                    if (Url!=null) {
                        getFileFromServer();
                        mProgress.setVisibility(View.VISIBLE);
                        mCheckUdata.setText("正在更新最新版本，请不要关闭程序！");
//                        isFirstSure = true;
                    }
                }else {
                    Toast.makeText(getActivity(),"正在更新版本...", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


    /**
     * 获取当前程序的版本号
     */
    private String getVersionName() {
        //获取packagemanager的实例
        PackageManager packageManager = getContext().getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
     */
  /*  public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");//设置解析的数据源
        int type = parser.getEventType();
        info = new UpdataInfo();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        info.setVersion(parser.nextText()); //获取版本号
                    } else if ("url".equals(parser.getName())) {
                        info.setUrl(parser.nextText()); //获取要升级的APK文件
                    } else if ("description".equals(parser.getName())) {
                        info.setDescription(parser.nextText()); //获取该文件的信息
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }
*/
    /**
     * 从服务器获取xml解析并进行比对版本号
     */
    /*public class CheckVersionTask extends Thread {

        public void run() {
            try {
                //从资源文件获取服务器 地址
                String path = addressXml;
                //包装成url的对象
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // conn.setConnectTimeout(5000);
                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    info = getUpdataInfo(is);
                    String currentVersion = getVersionName();

                    if (info.getVersion().equals(currentVersion)) {
                        LogUtil.i(TAG, "版本号相同无需升级");
                        getDialog().dismiss();
                    } else {
                        LogUtil.i(TAG, "版本号不同 ,提示用户升级 ");
                        Message msg = new Message();
                        msg.what = GET_UNDATAINFO_UPDATA;
                        handler.sendMessage(msg);

                    }
                }
                LogUtil.i(TAG, "连接不对 ");
                LogUtil.i(TAG, "连接不对1 ");

            } catch (Exception e) {
                // 待处理
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                LogUtil.i(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }*/

    private void getFileFromServer() {
        DownloadManager downloadManager = new DownloadManager(getContext());

        DownloadTask task = new DownloadTask(getContext());
        task.setUrl(Url);
        task.setPath(APK_PATH);
        downloadManager.add(task, listener); //Add the task
        downloadManager.start(task, listener); //Start the task
        downloadManager.stop(task, listener); //Stop the task if you exit your APP.


      /*  HttpTools mHttpTools = new HttpTools(getContext());
        mHttpTools.download(mApp, info.getUrl(), APK_PATH, false, new HttpCallback() {
            @Override
            public void onStart() {
                LogUtil.i(TAG, "--->download,onStart");
            }

            @Override
            public void onFinish() {
                LogUtil.i(TAG, "--->download,onFinish");
            }

            @Override
            public void onResult(String string) {
                LogUtil.i(TAG, "--->download,onResult:" + string);
                File file = new File(APK_PATH);
                installApk(file);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getActivity(),"网络错误...", Toast.LENGTH_SHORT).show();
                mCheckUdata.setText("更新失败，点击确定重新更新！");
                isFirstSure = false;
            }

            @Override
            public void onCancelled() {
                LogUtil.i(TAG, "--->download,onCancelled");
            }

            @Override
            public void onLoading(long count, long current) {
                LogUtil.i(TAG, "--->download,onLoading");
                mProgress.setMax((int) count);
                mProgress.setProgress((int) current);
                LogUtil.i(TAG, "--->download,onLoading,count:" + count + ",current:" + current);
            }
        });*/
    }

    private DownloadListener listener =new DownloadListener<Integer, DownloadTask>(){
        @Override
        public void onSuccess(DownloadTask downloadTask) {
            super.onSuccess(downloadTask);
            File file = new File(APK_PATH);
            installApk(file);
        }

        @Override
        public void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onStop(DownloadTask downloadTask) {
            super.onStop(downloadTask);
        }

        @Override
        public void onDelete(DownloadTask downloadTask) {
            super.onDelete(downloadTask);
        }

        @Override
        public void onAdd(DownloadTask downloadTask) {
            super.onAdd(downloadTask);
        }


        @Override
        public void onFinish() {
            super.onFinish();
        }

        @Override
        public void onError(Throwable thr) {
            super.onError(thr);
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
        }


        @Override
        public void onStart() {
            super.onStart();
        }


    };
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_UNDATAINFO_UPDATA:
//                    mCheckUdata.setText(info.getDescription());
                    mLine.setVisibility(View.VISIBLE);
                    mLinearSure.setVisibility(View.VISIBLE);

                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    mCheckUdata.setText("获取服务器更新信息失败！");
                    break;

            }
        }
    };
}
