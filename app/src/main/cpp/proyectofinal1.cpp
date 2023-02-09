#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/features2d.hpp>
#include <iostream>
#include <fstream>
#include <opencv2/objdetect.hpp>
#include <vector>
#include <string>

#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/video.hpp>
#include <opencv2/videoio.hpp>
#include <opencv2/objdetect.hpp>
//#include <opencv2/video/tracking.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/video/detail/tracking.detail.hpp>
#include <opencv2/features2d.hpp>

using namespace cv;

extern "C" {
JNIEXPORT void JNICALL
Java_com_example_proyectofinal1_MainActivity_FindFeatures(JNIEnv *env, jobject thiz,
                                                          jlong addr_gray, jlong addr_bgr) {
    Mat *mGray = (Mat *) addr_gray;
    Mat *mRgba = (Mat *) addr_bgr;
    std::vector<Point2f> Coners;
    goodFeaturesToTrack(*mGray, Coners, 20, 0.01, 10, Mat(), 3, false);
    for (int i = 0; i < Coners.size(); ++i) {
        circle(*mRgba, Coners[i], 10, Scalar(0, 255, 0), 2);

    }// TODO: implement FindFeatures()
}


CascadeClassifier face_cascade;
JNIEXPORT void JNICALL
Java_com_example_proyectofinal1_MainActivity_InitFeceDetector(JNIEnv *jniEnv, jobject,
                                                              jstring archisssvo) {
    const char *jnamestr = jniEnv->GetStringUTFChars(archisssvo, NULL);
    std::string filePath(jnamestr);
    face_cascade.load(filePath);

}


JNIEXPORT void JNICALL
Java_com_example_proyectofinal1_MainActivity_DetecFeatures(JNIEnv *jniEnv, jobject, jlong Gris,
                                                           jlong Color) {
    Mat *mGray  = (Mat *) Gris;
//
    Mat *mRgba = (Mat *) Color;
    std::vector<Rect> faces;
    face_cascade.detectMultiScale(*mGray, faces);
    for (int i = 0; i < faces.size(); ++i) {
        rectangle(*mRgba, Point(faces[i].x, faces[i].y),
                  Point(faces[i].x + faces[i].width, faces[i].y + faces[i].height),
                  Scalar(0, 0, 255), 2);
    }
}


}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_proyectofinal1_Hog_DetecHog(JNIEnv *env, jobject thiz, jlong gris, jlong color) {
    Mat *mGray = (Mat *) gris;
    Mat *mRgba  = (Mat *) color;
    cv::HOGDescriptor hog;
    hog.setSVMDetector(cv::HOGDescriptor::getDefaultPeopleDetector());
    std::vector<Rect> detecciones;
    hog.detectMultiScale(*mGray, detecciones, 0, Size(8, 8), Size(32, 32), 1.05, 2);
    Rect r;
    for(int i=0;i<detecciones.size();i++){
        r = detecciones[i];
        r.x+=cvRound(r.width*0.11);
        r.width=cvRound(r.width*0.9);
        r.y+=cvRound(r.height*0.1);
        r.height=cvRound(r.height*0.9);

        rectangle(*mRgba,r.tl(),r.br(),Scalar(10,10,203), 2);
    }


}