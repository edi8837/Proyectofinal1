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
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class MainActivity extends CameraActivity {

    private static String LOGTAG = "OpenCV_log";
    private CameraBridgeViewBase mOpenCVCameraView;
    private File cascadaFile;

    static {
        System.loadLibrary("proyectofinal1");
    }

    public native void FindFeatures(long addrGray, long addrBGr);
    public native void InitFeceDetector(String archivo);

    public native void DetecFeatures(long gris, long color);

//

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
        setContentView(R.layout.activity_main);

        try {

            cascadaFile = new File(getCacheDir(), "haarcascade_frontalface_default.xml");
            if (!cascadaFile.exists()) {
                InputStream inputStream = getAssets().open("haarcascade_frontalface_default.xml");
                FileOutputStream outputStream = new FileOutputStream(cascadaFile);
                byte[] buffer = new byte[2048];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
            }
           InitFeceDetector(cascadaFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mOpenCVCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_surface_view); //referencia a la vista opencv del disenio
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
            DetecFeatures(input_gray.getNativeObjAddr(), input_rgba.getNativeObjAddr());
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