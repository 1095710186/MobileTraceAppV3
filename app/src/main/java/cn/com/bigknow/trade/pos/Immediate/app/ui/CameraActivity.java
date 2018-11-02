package cn.com.bigknow.trade.pos.Immediate.app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.widget.BitmapUtil;
import cn.com.bigknow.trade.pos.Immediate.base.android.BaseActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;

/**
 * Created by zw on 2016/11/23.
 */

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback{


        @BindView(R.id.svCamera)
    SurfaceView mSvCamera;

        @BindView(R.id.ivPlateRect)
    ImageView mIvPlateRect;

        @BindView(R.id.ivCapturePhoto)
    ImageView mIvCapturePhoto;

        @BindView(R.id.tvPlateResult)
    TextView mTvPlateResult;



    private SurfaceHolder mSvHolder;
    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private MediaPlayer mShootMP;



    @Override
    public void init() {
        initData();

    }


    @Override
    public void onStart() {
        super.onStart();
        if (this.checkCameraHardware(this) && (mCamera == null)) {
            // 打开camera
            mCamera = getCamera();
            if(mCamera!=null){
            // 设置camera方向
            mCameraInfo = getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK);
            if (null != mCameraInfo) {
                adjustCameraOrientation();
            }

            if (mSvHolder != null) {
                setStartPreview(mCamera, mSvHolder);
            }
            }else{
                ToastUtil.makeToast("请允许权限！mCamera");
                finish();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * 记得释放camera，方便其他应用调用
         */
        releaseCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 初始化相关data
     */
    private void initData() {
        // 获得句柄
        mSvHolder = mSvCamera.getHolder(); // 获得句柄

        if(mSvHolder!=null){
        mSvCamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                autoFocus();
                return false;
            }
        });
        // 添加回调
        mSvHolder.addCallback(this);
        }else{
            ToastUtil.makeToast("请允许权限！mSvHolder");
            finish();

        }
    }

    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            camera = null;
            Log.e("", "Camera is not available (in use or does not exist)");
        }
        return camera;
    }

    private Camera.CameraInfo getCameraInfo(int facing) {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                return cameraInfo;
            }
        }
        return null;
    }



    Camera.PictureCallback jpgPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.startPreview();


        }
    };


    private void adjustCameraOrientation() { // 调整摄像头方向
        if (null == mCameraInfo || null == mCamera) {
            return;
        }

        int orientation = this.getWindowManager().getDefaultDisplay().getOrientation();
        int degrees = 0;

        switch (orientation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (mCameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else {
            // back-facing
            result = (mCameraInfo.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    /**
     * 释放mCamera
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();// 停掉原来摄像头的预览
            mCamera.release();
            mCamera = null;
        }
    }




    @OnClick({R.id.ivCapturePhoto})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivCapturePhoto:
                // 拍照,设置相关参数
//                Camera.Parameters params = mCamera.getParameters();
//                params.setPictureFormat(ImageFormat.JPEG);
//                DisplayMetrics metric = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metric);
//                int width = metric.widthPixels;  // 屏幕宽度（像素）
//                int height = metric.heightPixels;  // 屏幕高度（像素）
//                params.setPreviewSize(width, height);
//                // 自动对焦
//                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//                mCamera.setParameters(params);
                try {
                    mCamera.takePicture(shutterCallback, null, jpgPictureCallback);
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                }

                break;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mSvHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mSvHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        setStartPreview(mCamera, mSvHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 当surfaceview关闭时，关闭预览并释放资源
        /**
         * 记得释放camera，方便其他应用调用
         */
        releaseCamera();
        holder = null;
        mSvCamera = null;
    }


    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    /**
     * 设置camera显示取景画面,并预览
     * @param camera
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder){
        if(holder==null){
            releaseCamera();
            finish();
            ToastUtil.makeToast("请重新拍照！");
        }else{

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d("", "Error starting camera preview: " + e.getMessage());
        }
        }
    }

    /**
     *   播放系统拍照声音+
     */
    private void shootSound() {
        AudioManager meng = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0) {
            if (mShootMP == null)
                mShootMP = MediaPlayer.create(this, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            if (mShootMP != null)
                mShootMP.start();
        }
    }


    /**
     * 获取Preview界面的截图，并存储
     */
    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            shootSound();
            // 获取Preview图片转为bitmap并旋转
            Camera.Size size = mCamera.getParameters().getPreviewSize(); //获取预览大小
            final int w = size.width;  //宽度
            final int h = size.height;
            final YuvImage image = new YuvImage(data, ImageFormat.NV21, w, h, null);
            // 转Bitmap
            ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
            if(! image.compressToJpeg(new Rect(0, 0, w, h), 100, os)) {
                return;
            }
            byte[] tmp = os.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
            Bitmap rotatedBitmap = BitmapUtil.createRotateBitmap(bitmap);
          String path=  cropBitmapAndRecognize(rotatedBitmap);
            if(!path.equals("")){
                Intent rIntent = new Intent();
                rIntent.putExtra("path_url", path);
                setResult(11, rIntent);
                finish();
            }
        }
    };


    public void autoFocus() {
        if (mCamera != null) {
            synchronized (mCamera) {
                try {
                    if (mCamera.getParameters().getSupportedFocusModes() != null
                            && mCamera.getParameters()
                            .getSupportedFocusModes()
                            .contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                        mCamera.autoFocus(new Camera.AutoFocusCallback() {
                            public void onAutoFocus(boolean success,
                                                    Camera camera) {
                            }
                        });
                    } else {
                        Toast.makeText(this, "无对焦功能", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    mCamera.stopPreview();
                    mCamera.startPreview();
                    Toast.makeText(this, "对焦失败", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }




    @Override
    public int layoutId() {
        return R.layout.a_camera_layout;
    }

    /**
     * TakePicture回调
     */
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
//            shootSound();
            mCamera.setOneShotPreviewCallback(previewCallback);
        }
    };

    Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.startPreview();
        }
    };


    public String  cropBitmapAndRecognize(Bitmap originalBitmap) {
        // 裁剪出关注区域
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        Bitmap sizeBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);

        int rectWidth = (int)(mIvPlateRect.getWidth() * 1.1);
        int rectHight = (int)(mIvPlateRect.getHeight() * 1.1);
        int[] location = new int[2];
        mIvPlateRect.getLocationOnScreen(location);
        location[0] -= mIvPlateRect.getWidth() * 1 / 10;
        location[1] -= mIvPlateRect.getHeight() * 1 / 2;
        Bitmap normalBitmap = Bitmap.createBitmap(sizeBitmap, location[0], location[1], rectWidth, rectHight);

//        Bitmap  bm = Bitmap.createScaledBitmap(normalBitmap, normalBitmap.getWidth()/3, normalBitmap.getHeight()/3, true);
//        mIvCapturePhoto.setImageBitmap(bm);
        // 保存图片
        File  pictureFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "PlateRcognizer.jpg");
        if (pictureFile == null) {
            Log.d("", "Error creating media file, check storage permissions: ");
            Toast.makeText(this, "Error creating media file, check storage permissions", Toast.LENGTH_LONG).show();
            return "";
        }

        try {

            FileOutputStream fos = new FileOutputStream(pictureFile);
            if(normalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)){
             fos.flush();
            fos.close();
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (IOException e) {
            Log.d("", "Error accessing file: " + e.getMessage());
            Toast.makeText(this, "Error accessing file", Toast.LENGTH_LONG).show();
            return "";
        }
        return pictureFile.getAbsolutePath();
    }



}
