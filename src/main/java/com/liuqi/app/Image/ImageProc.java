package com.liuqi.app.Image;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 图像处理的 OpenCV 扩展包
 * @Date : Create at 上午10:47 2018/2/2
 */
public class ImageProc {



    public static void extractTable(String source_img){

    }

    private static void threshold(String source_img){

    }

    /**
     * 二值化图片
     * @param source_img 原始图片路径
     * @param blockSize 对像素点周围采样的范围。
     * @param C 在范围内找到的目标值该减去的常数。   正数，白底黑字， 负数 黑底白字
     * @return Mat
     */
    private static Mat threshold(String source_img,int blockSize, double C){
        //通过灰度加载，已经是8位通道的格式
        Mat image = Imgcodecs.imread(source_img,Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        //二值化处理   cv_8uc1 8位单通道格式
        Mat binaryMat = new Mat(image.height(), image.width(), CvType.CV_8UC1);
        /*
         * https://docs.opencv.org/3.0.0/d7/d1b/group__imgproc__misc.html#ga72b913f352e4a1b1b397736707afcde3
         *
         * 参数说明
         * InputArray src  , Source 8-bit single-channel image (传入图像, 8位通道的图像)
         * OutputArray dst , Destination image of the same size and the same type as src (传出图像和 src 图像类型一致的图像）
         * double maxValue , Non-zero value assigned to the pixels for which the condition is satisfied (给满足条件的非0值 赋值的颜色)
         * int adaptiveMethod , Adaptive thresholding algorithm to use, see cv::AdaptiveThresholdTypes. (使用自适应阈值算法,参考::AdaptiveThresholdTypes）
         * int thresholdType , Thresholding type that must be either THRESH_BINARY or THRESH_BINARY_INV, see cv::ThresholdTypes (阈值类型必须是阈值二进制或阈值binary_inv，请参见cv::阈值类型)
         * int blockSize , Size of a pixel neighborhood that is used to calculate a threshold value for the pixel: 3, 5, 7, and so on （对某个像素周围进行采样的范围）
         * double C , Constant subtracted from the mean or weighted mean (see the details below). Normally, it is positive but may be zero or negative as well  （从平均值或加权平均值减去常数。通常情况下，是正的，但也可能是零或负）。
         */

        // blocksize 和 C 最关键， 需要不断的调整来找到最佳的值
        // blocksize 就是区域，以一个像素点辐射周围的范围来找阈值，在通用处理中，设置一个较大的居中值即可，需要提取文字等信息，反正控制在10以内最好。
        // 在设置 C 的时候，默认我会把 maxValue 设置为255（白色），
        // 当 C 为正数，会过滤掉灰色区域，最终是白底，黑字。
        // 为 C 为负数，会过滤掉白色区域，文字区域在 blockSize 范围内的白色保留， 这样就变成了 黑底白字。也就是取反。

        // 个人觉得 C 在13-20 之间
        // block 在15-25之间(注意，要奇数)
        // 能处理大部分图片

        Imgproc.adaptiveThreshold(image, binaryMat,255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,blockSize,C);

        return binaryMat;
    }




}
