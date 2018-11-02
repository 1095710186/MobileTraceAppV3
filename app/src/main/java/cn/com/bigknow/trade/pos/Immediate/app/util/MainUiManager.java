package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.trello.rxlifecycle.android.ActivityEvent;

import java.io.File;

import cn.com.bigknow.trade.pos.Immediate.BuildConfig;
import cn.com.bigknow.trade.pos.Immediate.app.ui.LoginActivity;
import cn.com.bigknow.trade.pos.Immediate.app.ui.MyselfInfoActivity;
import cn.com.bigknow.trade.pos.Immediate.base.activitymanager.TheActivityManager;
import cn.com.bigknow.trade.pos.Immediate.base.util.MediaUtils;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import cn.com.bigknow.trade.pos.Immediate.model.api.Api;
import cn.com.bigknow.trade.pos.Immediate.model.api.SimpleRequestListener;
import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;
import cn.com.bigknow.trade.pos.Immediate.model.bean.Version;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by hujie on 16/6/21.
 */
public class MainUiManager {

    private static boolean downloading = false;

    public static String DOWNLOAD_URL = ModelConfig.MVP_URL + "servlet/fileupload?oper_type=download&fileId=";

    public static void checkUpdate(Context context) {

        String version = BuildConfig.VERSION_NAME;
        Api.api().checkUpdate(version + "", new SimpleRequestListener<Version>() {
            @Override
            public void onSuccess(Version o) {
                createDialog(context,o);
//                downLoad(context, DOWNLOAD_URL + o.getFileId());

            }

            @Override
            public void onError(ApiError error) {
                super.onError(error);
//                ToastUtil.makeToast(error.errorMsg);
            }
        });

    }


    public static void downLoad(final Context activity, String url) {
        if (downloading) {
            ToastUtil.makeToast("下载中...");
            return;
        }
        downloading = true;

        String serviceString = Context.DOWNLOAD_SERVICE;
        final DownloadManager
                downloadManager = (DownloadManager) activity.getSystemService(serviceString);

        final Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setMimeType("application/vnd.android.package-archive");

        request.setDestinationInExternalPublicDir("download", "jkzs.apk");

        final long myDownloadReference = downloadManager.enqueue(request);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (myDownloadReference == reference) {
                    downloading = false;
                    try {


                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        Intent updateApk = new Intent(Intent.ACTION_VIEW);
                        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(myDownloadReference);
                        if (downloadFileUri!=null) {
                            if (downloadFileUri.toString().startsWith("file")) {
                                updateApk.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                            }else {
                                String path = MediaUtils.getContentImagePath(context, downloadFileUri);
                                updateApk.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                            }
                        }
                        updateApk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(updateApk);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
        };

        activity.registerReceiver(receiver, filter);


    }

    public static AlertDialog.Builder builder;
    public static AlertDialog dialog;
    public static void createDialog(Context context,Version o) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage("发现最新版本(V"+o.getVersion()+"),确定要退出登录？");
        builder.setPositiveButton("确定", (dialog, id) -> {
            downLoad(context,DOWNLOAD_URL + o.getFileId());
        });
        builder.setNegativeButton("取消", (dialog, id) -> {
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }
//
//
//
//    /**
//     * 异步登录啊
//     */
//    public static void asyncLogin() {
//        UserManager.getInstance().asyncLogin();
//    }


}
