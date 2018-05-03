package opencv.study;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * OpenCV 的边缘检测
 * 1. Canny 算法<br/>
 * 2. Sobel 算法<br/>
 * 3. Laplacian 算法<br/>
 * 4. Scharr 滤波器<br/>
 */
public class StudySection_12 extends OpenCVProcessBase {


    private String save_dir = "study-output/study-opencv-12";

    /**
     * OpenCV 中对图像的边缘检测一般需要如下几个步骤
     *
     * 1. 滤波
     *      -- 边缘检测算子，对噪声比较敏感，为提高检测的效率以及成功率，需要降低噪声对图像的影响。常用的滤波器有"高斯滤波"
     * 2. 增强
     *      -- 边缘强度与邻域内容在灰度下，因该有显著的变化，利用算法加强这种变化，使边缘更明显。常用的增强方式 "计算梯度值"来确定
     * 3. 检测
     *      -- 通过二值化图像，进一步过滤掉业务可能不需要的内容。再进行检测
     */



    /**--------------------*
     *  第一节 canny算法
     *
     *  被很多人推崇为现在最优的边缘检测算法
     *
     *  Canny 算法实现
     *  1. 用高斯滤波器平滑图像（在调用Canny之前自己用blur平滑）
     *  2. 用一阶偏导的有限差分来计算梯度的幅值和方向．
     *  3. 对梯度幅值应用非极大值抑制
     *  4. 用双阈值算法检测和连接边缘．
     **--------------------*/

    /**
     * 边缘检测
     */
    @Test
    public void testCanny(){

        /*
         * 原文地址 : https://docs.opencv.org/3.0-beta/modules/imgproc/doc/miscellaneous_transformations.html?highlight=threshold#double%20threshold(InputArray%20src,%20OutputArray%20dst,%20double%20thresh,%20double%20maxval,%20int%20type)
         *
         *
         *
         * 原型方法 :
         *      Canny(Mat dx, Mat dy, Mat edges, double threshold1, double threshold2, boolean L2gradient)
         *      Canny(Mat dx, Mat dy, Mat edges, double threshold1, double threshold2)
         *      Canny(Mat image, Mat edges, double threshold1, double threshold2, int apertureSize, boolean L2gradient)
         *      Canny(Mat image, Mat edges, double threshold1, double threshold2)
         *
         *      参数：
         *          image : Mat 输入图像， 8位单通道
         *          edges : Mat 输出图像，与原图一样大小，类型
         *          threshold1 : double 滞后性阈值1
         *          threshold2 : double 滞后性阈值2
         *          apertureSize : int 表示 Sobel算法的孔径大小,默认3  ,且值必须是3，5，7
         *          L2gradient : boolean  计算梯度副值的标识。默认 false
         *
         *      threshold1 和 threshold2 中：
         *          -- 较小的值用于边缘链接
         *          -- 较大的值用户控制边缘的初始段
         *
         *          推荐 较大值与较小值比例 2:1 或 3:1
         */

        Mat src = Imgcodecs.imread(this.p_test_file_path + "/face-1.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        // 1. 滤波(消除噪声)

        Mat gsDst = new Mat();
        // 高斯滤波，详见 Section_7 滤波器
        Imgproc.GaussianBlur(src,gsDst,new Size(3,3),0,0);
        this.saveImage(this.save_dir + "/image_canny_0.png",src);

        // 不同 threshold1、threshold2的值
        Mat dst = new Mat();
        Imgproc.Canny(gsDst,dst,10,30);
        this.saveImage(this.save_dir + "/image_canny_1.png",dst);

        dst = new Mat();
        Imgproc.Canny(gsDst,dst,35,70);
        this.saveImage(this.save_dir + "/image_canny_2.png",dst);

        dst = new Mat();
        Imgproc.Canny(gsDst,dst,60,120);
        this.saveImage(this.save_dir + "/image_canny_3.png",dst);

        dst = new Mat();
        Imgproc.Canny(gsDst,dst,60,120,3,true);
        this.saveImage(this.save_dir + "/image_canny_4.png",dst);

        dst = new Mat();
        Imgproc.Canny(gsDst,dst,60,120,5,true);
        this.saveImage(this.save_dir + "/image_canny_5.png",dst);

        dst = new Mat();
        Imgproc.Canny(gsDst,dst,60,120,7,true);
        this.saveImage(this.save_dir + "/image_canny_6.png",dst);

        dst = new Mat();
        Imgproc.Canny(gsDst,dst,60,120,3,false);
        this.saveImage(this.save_dir + "/image_canny_7.png",dst);

        dst = new Mat();
        Imgproc.Canny(gsDst,dst,60,120,5,false);
        this.saveImage(this.save_dir + "/image_canny_8.png",dst);

        dst = new Mat();
        Imgproc.Canny(gsDst,dst,60,120,7,false);
        this.saveImage(this.save_dir + "/image_canny_9.png",dst);

    }

    /** ----------Sobel 、Laplacian 、Scharr 后续研究，看了教程与官网内容，属于边缘使用场景 ----------- **/

    /**
     * Sobel算法
     * 离散微分算子
     */
    public void testSobel(){

    }


}
