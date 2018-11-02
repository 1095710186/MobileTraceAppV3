package cn.com.bigknow.trade.pos.Immediate.app.util;

/**
 * Created by hujie on 16/7/4.
 */
public class FIRManager {

//    private static boolean downloading = false;
//    private static boolean checking = false;
//
//    public static void checkUpdate(Activity context) {
//
//
//        if (downloading || checking) {
//            return;
//        }
//
//        checking = true;
//
//        FIR.checkForUpdateInFIR("f1d8d9d6657a3ac5f301564841f124ab", new VersionCheckCallback() {
//            @Override
//            public void onSuccess(String versionJson) {
//
//                JSONObject jo = null;
//                try {
//                    jo = new JSONObject(versionJson);
//
//                    //安装地址
//                    String install_url = jo.getString("install_url");
//                    //更新日志
//                    String changelog = jo.getString("changelog");
//
//                    long updated_at = jo.getLong("updated_at");
//
//
//                    SharedPreferences spf = context.getSharedPreferences("fir", Context.MODE_PRIVATE);
//                    long last = spf.getLong("fir_updated_at", 0);
//
//
//                    if (last == 0) {
//                        last = updated_at;
//                        spf.edit().putLong("fir_updated_at", last).apply();
//                    }
//
//                    if (last < updated_at) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setTitle("新版本提示")
//                                .setMessage(changelog);
//
//                        builder.setPositiveButton("更新", (dialog, which) -> {
//                            downLoad(context, install_url, updated_at);
//                        });
//
//                        builder.create().show();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFail(Exception exception) {
//            }
//
//            @Override
//            public void onStart() {
//            }
//
//            @Override
//            public void onFinish() {
//                checking = false;
//            }
//        });
//    }
//
//
//    private static void downLoad(final Activity activity, String url, final long last) {
//        downloading = true;
//
//        String serviceString = Context.DOWNLOAD_SERVICE;
//        final DownloadManager
//                downloadManager = (DownloadManager) activity.getSystemService(serviceString);
//
//        final Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//
//        request.setMimeType("application/vnd.android.package-archive");
//
//        request.setDestinationInExternalPublicDir("download", "xgs.apk");
//
//        final long myDownloadReference = downloadManager.enqueue(request);
//
//        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//                if (myDownloadReference == reference) {
//                    downloading = false;
//                    try {
//
//
//                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                        Intent updateApk = new Intent(Intent.ACTION_VIEW);
//                        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(myDownloadReference);
//
//                        if (downloadFileUri.toString().startsWith("file")) {
//                            updateApk.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
//                        } else {
//                            String path = MediaUtils.getContentImagePath(context, downloadFileUri);
//                            updateApk.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
//                        }
//                        updateApk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        activity.startActivity(updateApk);
//
//
//                        SharedPreferences spf = activity.getSharedPreferences("fir", Context.MODE_PRIVATE);
//                        spf.edit().putLong("fir_updated_at", last).apply();
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//                }
//            }
//        };
//
//        activity.registerReceiver(receiver, filter);
//
//
//    }


}
