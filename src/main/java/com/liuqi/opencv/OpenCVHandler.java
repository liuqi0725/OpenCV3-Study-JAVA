package com.liuqi.opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @Author : alexliu
 * @Description : java opencv 工具类
 * @Date : Create at 下午2:12 2018/4/11
 */
public class OpenCVHandler {

    /**
     *
     * 加载OpenCV 动态链接库
     *
     */
    public static void loadLibraries() {

        String osName = System.getProperty("os.name");
        String opencvpath = System.getProperty("user.dir");

        try {

            //windows
            if(osName.startsWith("Windows")) {
                int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
                //32位系统
                if(bitness == 32) {
                    opencvpath += "\\opencv\\x86\\Your path to .dll";
                }
                //64位系统
                else if (bitness == 64) {
                    opencvpath += "\\opencv\\x64\\Your path to .dll";
                } else {
                    opencvpath += "\\opencv\\x86\\Your path to .dll";
                }
            }

            // mac os
            else if(osName.equals("Mac OS X")){
                opencvpath = "/usr/local/Cellar/opencv/3.4.0_1/share/OpenCV/java/libopencv_java340.dylib";
            }
            //System.out.println(opencvpath);

            /*
             * 不使用System.loadLibrary(xxx);。 而是使用 绝对路径加载：System.load(xxx);
             *
             * 第一种方式 --------------System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
             * loadLibrary(Core.NATIVE_LIBRARY_NAME); //使用这种方式加载，需要在 IDE 中配置参数.
             * Eclipse 配置：http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html#set-up-opencv-for-java-in-eclipse
             * IDEA 配置 ：http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html#set-up-opencv-for-java-in-other-ides-experimental
             *
             * 第二种方式 --------------System.load(path of lib);
             * System.load(your path of lib) ,方式比较灵活，可根据环境的系统，位数，决定加载内容
             */
            System.load(opencvpath);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load opencv native library", e);
        }
    }


    /**
     * 将图片替换至原图的感兴趣区域
     * @param src 原始图片
     * @param addImage 添加到原始图片上的图片
     * @param src_x 感兴趣区域在原图的开始位置坐标(x)
     * @param src_y 感兴趣区域在原图的开始位置坐标(y)
     */
    public static void addImageInROI(Mat src , Mat addImage, int src_x,int src_y) throws Exception {

        int src_w = src.cols();
        int src_h = src.rows();

        //如果要加在 src 的图片的高宽，必须要小于，根据开始位置画出来的 ROI 区域

        if((src_w - src_x) < addImage.cols()){
            throw new Exception("addImage`s width is more than (src.witdh - src_x) .");
        }

        if((src_h - src_y) < addImage.rows()){
            throw new Exception("addImage`s height is more than (src.height - src_y) .");
        }

        //标记感兴趣区域
        Mat roi = src.submat(new Rect(src_x,src_y,addImage.cols(),addImage.rows()));

        Mat mask = new Mat();
        if(addImage.channels() == 1){
            mask = addImage.clone();
        }else{
            //创建 mask,转为灰度
            Imgproc.cvtColor(addImage,mask,Imgproc.COLOR_RGB2GRAY);
        }

        addImage.copyTo(roi,mask);

    }

    /**
     * 将图片替换至原图的感兴趣区域
     * @param src 原图
     * @param addImage 要添加的图片
     * @param src_x 原图感兴趣的区域 开始坐标 x
     * @param src_y 原图感兴趣的区域 开始坐标 y
     * @param src_weight 原图比重
     * @param addImage_weight 添加图片比重
     * @throws Exception
     */
    public static void addImageInROI(Mat src,Mat addImage,int src_x,int src_y , double src_weight, double addImage_weight) throws Exception {

        int roi_w = addImage.width();
        int roi_h = addImage.height();

        if((src.width() - src_x) < roi_w){
            throw new Exception("addImage`s width is more than (src.witdh - src_x) .");
        }

        if((src.height() - src_y) < roi_h){
            throw new Exception("addImage`s height is more than (src.height - src_y) .");
        }

        Mat src_roi = src.submat(new Rect(src_x,src_y,roi_w,roi_h));

        Core.addWeighted(src_roi,src_weight,addImage,addImage_weight,0.,src_roi);
    }


    /**
     * 融合2个图片，适用于2张图片大小完全一致
     * @param src1 原图1
     * @param src2 原图2
     * @param src1_weight src1融合后站的比重， 0.1 ~ 1
     * @return 融合后图像
     * @throws Exception
     */
    public static Mat imageMixed(Mat src1,Mat src2,double src1_weight) throws Exception {

        if(src1_weight > 1 || src1_weight < 0){
            throw new Exception("src1_weight must double and the val must between 0.1 and 1 ");
        }

        double src2_weight = 1 - src1_weight;

        if(src1.width() != src2.width()){
            throw new Exception("src1.width != src2.width.They must be the same length.");
        }

        if(src1.height() != src2.height()){
            throw new Exception("src1.height != src2.height.They must be the same length.");
        }

        Mat dst = new Mat();

        Core.addWeighted(src1,src1_weight,src2,src2_weight,0.,dst);

        return dst;

    }

    /**
     * 融合2个图片，适用于2张图片大小不一致
     * @param src1 原图1
     * @param src2 原图2
     * @param src1_weight src1融合后站的比重， 0.1 ~ 1
     * @param cut_width 按照此宽度，截取2张原始图片
     * @param cut_height 按照此高度，截取2张原始图片
     * @return 融合后图像
     * @throws Exception
     */
    public static Mat imageMixed(Mat src1,Mat src2,double src1_weight,int cut_width,int cut_height) throws Exception {

        if(src1_weight > 1 || src1_weight < 0){
            throw new Exception("src1_weight must double and the val must between 0.1 and 1 ");
        }

        double src2_weight = 1 - src1_weight;

        if(src1.width() < cut_width || src2.width() < cut_width){
            throw new Exception("src1.width or src2.width is less than (<) param cut_width.");
        }

        if(src1.height() < cut_height || src2.height() < cut_height){
            throw new Exception("src1.height or src2.height is less than (<) param cut_height.");
        }


        Mat srcROI1 = src1.submat(0,cut_height,0,cut_width);
        Mat srcROI2 = src2.submat(0,cut_height,0,cut_width);

        Mat dst = new Mat();

        //gamma = 0.0 颜色没有偏差的融合
        Core.addWeighted(srcROI1,src1_weight,srcROI2,src2_weight,0.,dst);

        return dst;

    }

    /**
     * 通过 findContours 函数返回的轮廓，查找轮廓中心点
     * @param contours 轮廓集合
     * @return Vector<Point> 中心点集合 float mat 矩阵数据
     */
    public static Vector<Point> findContourCenter(List<MatOfPoint> contours){
        Vector<Point> centerPointList = new Vector<Point>();
        for(int i=0; i<contours.size(); i++){
            //Point[] pArr = {findContourCenter(contours.get(i))};
            centerPointList.add(i,findContourCenter(contours.get(i)));
        }

        return centerPointList;

    }

    /**
     * 通过 findContours 函数返回的轮廓，查找轮廓中心点
     * @param contour
     * @return 中心点
     */
    public static Point findContourCenter(MatOfPoint contour){

        //转成2f
        MatOfPoint2f map = new MatOfPoint2f(contour.toArray());

        Moments moments = Imgproc.moments(map);
        Point center = new Point();
        //官方获取轮廓中心点方法
        center.x = (int) (moments.m10 / moments.m00);
        center.y = (int) (moments.m01 / moments.m00);

        return center;
    }



    public static void changeChannel(Mat image , int target){

        Mat new_image = new Mat();

        switch (target){
            case 1 :
                switch (image.channels()){
                    case 1 : break;
                    case 2 : break;
                }

                break;
            case 2 : break;
            case 3 : break;
            case 4 : break;
        }

    }

}
