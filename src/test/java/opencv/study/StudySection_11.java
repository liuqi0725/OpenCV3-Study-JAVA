package opencv.study;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. threshold 固定阈值操作<br/>
 * 2. adaptiveThreshold 自适应阈值操作<br/>
 */
public class StudySection_11 extends OpenCVProcessBase {


    private String save_dir = "study-output/study-opencv-11";

    /**--------------------*
     *  第一节 threshold
     **--------------------*/

    /**
     * 固定阈值操作
     *
     * 得到二值化图像。
     */
    @Test
    public void testThreshold(){

        /*
         * 原文地址 : https://docs.opencv.org/3.0-beta/modules/imgproc/doc/miscellaneous_transformations.html?highlight=threshold#double%20threshold(InputArray%20src,%20OutputArray%20dst,%20double%20thresh,%20double%20maxval,%20int%20type)
         *
         * 原型方法 :
         *      threshold(Mat src, Mat dst, double thresh, double maxval, int type)
         *
         *      参数 :
         *          src : Mat 输入图像 单通道 8 或 32浮点图像
         *          dst : Mat 输出图像 阈值操作结果填充在此图像
         *          thresh : double 阈值
         *          maxval : double 当 type 为 THRESH_BINARY 或 THRESH_BINARY_INV 时的最大值
         *          type : int , 阈值类型。对对象取阈值的方式
         *              -- 【0】THRESH_BINARY : src(x,y) > thresh ? maxval : 0 。 当前像素点的灰度值 > thresh ，当前像素点值为 maxval ，反之为0
         *              -- 【1】THRESH_BINARY_INV : src(x,y) > thresh ? 0 : maxval 。 当前像素点灰度值 > thresh , 当前像素点值为 0 反之为 maxval
         *              -- 【2】THRESH_TRUNC : src(x,y) > thresh ? threshold : src(x,y)。当前像素点灰度值 > thresh , 设定为thresh ，反之保持不变
         *              -- 【3】THRESH_TOZERO : src(x,y) > thresh ? src(x,y) : 0 。 当前像素点灰度值 > thresh , 当前像素点值保持不变，其他情况为0
         *              -- 【4】THRESH_TOZERO_INV : src(x,y) > thresh ? 0 : src(x,y) 。 当前像素点灰度值 > thresh , 当前像素点值为0 ，其他情况保持不变
         */

        Mat src = Imgcodecs.imread(this.p_test_file_path + "/mao.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        this.saveImage(this.save_dir + "/image_thresh_0.png",src);

        Mat dst = new Mat();
        // 像素灰度值 > 125（灰色）为 255（白色），其他为 0（黑色）
        Imgproc.threshold(src,dst,125,255,Imgproc.THRESH_BINARY);
        this.saveImage(this.save_dir + "/image_thresh_1.png",dst);

        // 像素灰度值 > 125（灰色）为 0（黑色），其他为 255（白色）
        Imgproc.threshold(src,dst,125,255,Imgproc.THRESH_BINARY_INV);
        this.saveImage(this.save_dir + "/image_thresh_2.png",dst);

        // 像素灰度值 > 125（灰色）为 125（灰色），其他为保持原来颜色
        // 第4个参数 maxval 的值，不会影响结果
        Imgproc.threshold(src,dst,125,0,Imgproc.THRESH_TRUNC);
        this.saveImage(this.save_dir + "/image_thresh_3.png",dst);

        // 像素灰度值 > 125（灰色）保持不变，其他为0
        // 第4个参数 maxval 的值，不会影响结果
        Imgproc.threshold(src,dst,125,0,Imgproc.THRESH_TOZERO);
        this.saveImage(this.save_dir + "/image_thresh_4.png",dst);

        // 像素灰度值 > 125（灰色）为 0，其他保持不变
        // 第4个参数 maxval 的值，不会影响结果
        Imgproc.threshold(src,dst,125,0,Imgproc.THRESH_TOZERO_INV);
        this.saveImage(this.save_dir + "/image_thresh_5.png",dst);
    }

    /**--------------------*
     *  第二节 adaptiveThreshold
     **--------------------*/

    /**
     * 自适应阈值操作
     */
    @Test
    public void testAdaptiveThreshold(){

        /*
         * 原文地址: https://docs.opencv.org/3.0-beta/modules/imgproc/doc/miscellaneous_transformations.html?highlight=adaptivethreshold
         *
         * 原型方法
         *      adaptiveThreshold(Mat src, Mat dst, double maxValue, int adaptiveMethod, int thresholdType, int blockSize, double C)
         *
         *      参数说明:
         *          src : Mat 输入图像 单通道 8位图像
         *          dst : Mat 输出图像 阈值操作结果填充在此图像
         *          maxValue : double 分配给满足条件的像素的非零值
         *          adaptiveMethod : int 自定义使用的阈值算法，ADAPTIVE_THRESH_MEAN_C 、 ADAPTIVE_THRESH_GAUSSIAN_C
         *              -- ADAPTIVE_THRESH_MEAN_C 时，T(x,y) = blockSize * blockSize【b】
         *                  blockSize【b】= 邻域内(x,y) - C
         *              -- ADAPTIVE_THRESH_GAUSSIAN_C 时，T(x,y) = blockSize * blockSize【b】
         *                  blockSize【b】= 邻域内(x,y) - C与高斯窗交叉相关的加权总和
         *
         *          thresholdType : int 阈值类型，只能是THRESH_BINARY 、 THRESH_BINARY_INV
         *              -- THRESH_BINARY 时， src(x,y) > T(x,y) ? maxValue : 0 。当前像素点的灰度值 > T(x,y) 则为 maxValue，反之为0
         *              -- THRESH_BINARY_INV 时， src(x,y) > T(x,y) ? 0 : maxValue 。当前像素点的灰度值 > T(x,y) 则为 0，反之为maxValue
         *              >>>>> T(x,y) 根据 adaptiveMethod 算法计算出的像素阈值。<<<<<
         *
         *          blockSize : int 用来计算阈值的邻域尺寸 3，5，7等等，奇数
         *          C : double 减去平均值或加权平均值的常数，通常情况下，它是正的，但也可能是零或负。
         *
         */

        // blocksize 和 C 最关键， 需要不断的调整来找到最佳的值
        // blocksize 就是区域，以一个像素点辐射周围的范围来找阈值，在通用处理中，设置一个较大的居中值即可，需要提取文字等信息，反正控制在10以内最好。
        // 在设置 C 的时候，默认我会把 maxValue 设置为255（白色），
        // 当 C 为正数，会过滤掉灰色区域，最终是白底，黑字。
        // 为 C 为负数，会过滤掉白色区域，文字区域在 blockSize 范围内的白色保留， 这样就变成了 黑底白字。也就是取反。

        Mat src = Imgcodecs.imread(this.p_test_file_path + "/mao.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        this.saveImage(this.save_dir + "/image_process_adaptiveThreshold_0.png",src);

        Mat dst = new Mat();

        Imgproc.adaptiveThreshold(src,dst,255,Imgproc.ADAPTIVE_THRESH_MEAN_C , Imgproc.THRESH_BINARY,13,5);
        this.saveImage(this.save_dir + "/image_process_adaptiveThreshold_1.png",dst);

        Imgproc.adaptiveThreshold(src,dst,255,Imgproc.ADAPTIVE_THRESH_MEAN_C , Imgproc.THRESH_BINARY_INV,13,5);
        this.saveImage(this.save_dir + "/image_process_adaptiveThreshold_2.png",dst);

        Imgproc.adaptiveThreshold(src,dst,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C , Imgproc.THRESH_BINARY,13,5);
        this.saveImage(this.save_dir + "/image_process_adaptiveThreshold_3.png",dst);

        Imgproc.adaptiveThreshold(src,dst,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C , Imgproc.THRESH_BINARY_INV,13,5);
        this.saveImage(this.save_dir + "/image_process_adaptiveThreshold_4.png",dst);

    }
}
