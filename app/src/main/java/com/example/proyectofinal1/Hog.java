package com.example.proyectofinal1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Hog extends CameraActivity {
    private static String LOGTAG = "OpenCV_log";
    private CameraBridgeViewBase mOpenCVCameraView;
    private File cascadaFile;

    static {
        System.loadLibrary("proyectofinal1");
    }
    public native void DetecHog(long gris, long color);


    private BaseLoaderCallback mloaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.v(LOGTAG, "OpenCV Loaded");
                    mOpenCVCameraView.enableView();
                }

                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_hog);
        mOpenCVCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_surface_view);
        mOpenCVCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCVCameraView.setCvCameraViewListener(cvCameraViewListener);
    }
    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCVCameraView);
    }


    private CameraBridgeViewBase.CvCameraViewListener2 cvCameraViewListener = new CameraBridgeViewBase.CvCameraViewListener2() {
        @Override
        public void onCameraViewStarted(int width, int height) {

        }

        @Override
        public void onCameraViewStopped() {

        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            Mat input_rgba = inputFrame.rgba();
            Mat input_gray = inputFrame.gray();

            //FindFeatures(input_gray.getNativeObjAddr(),input_rgba.getNativeObjAddr());
            DetecHog(input_gray.getNativeObjAddr(), input_rgba.getNativeObjAddr());
            return input_rgba;
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCVCameraView != null) {
            mOpenCVCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(LOGTAG, "OpenCV no Found,Initializing");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mloaderCallback);
        } else {
            mloaderCallback.onManagerConnected((LoaderCallbackInterface.SUCCESS));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCVCameraView != null) {
            mOpenCVCameraView.disableView();
        }
    }

}