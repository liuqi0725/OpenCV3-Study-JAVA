package opencv;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. resize 图像缩放 <br/>
 * 2. rect 矩形对象<br/>
 * 3. cvtColor 颜色空间转化转换<br/>
 */
public class ProcessTest_3 extends OpenCVProcessBase {

    private String save_dir = "study-output/study-opencv-3";

    /**
     * 缩放图片-自定义输出大小
     */
    @Test
    public void testResize_1(){

        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat outImage = new Mat();

        /*
         * 1. resize 是一个具有多态特性，提供2种不同入参的方式
         *
         *      * 直接缩放到对应 Size 大小
         *      public static void resize(Mat src, Mat dst, Size dsize)
         *
         *      * 可根据 Size 缩放，也可以根据 fx、fy 按比例缩放
         *      public static void resize(Mat src, Mat dst, Size dsize, double fx, double fy, int interpolation)
         *
         * 2. 参数说明
         *
         *      * src: 输入，原图像，即待改变大小的图像
         *      * dst: 输出，改变大小之后的图像
         *      * dsize: 输出图像的大小 Size(width,height) ，通过 new Size(0,0) 可以设置其值为0，当 dsize 为0时，按照 fx、fy 缩放图片
         *          -- 当 dsize 为 0 时，通过计算 fx、fy 来对图片进行缩放，缩放的公式为：new Size(round(fx*src.cols), round(fy*src.rows))
         *      * fx: 宽度的缩放比例，当 dsize 为 0 时才生效。
         *      * fy: 高度的缩放比例，当 dsize 为 0 时才生效。
         *      * interpolation:
         *
         * 3. 注意：
         *      * dsize 与 fx，fy 分别为2种缩放模式，必须启用一种。要么自定义大小（dszie），要么按照比例（fx,fy）; 也就是说，dsize、fx、fy 3者不能同时都为 0。
         *
         */
        Imgproc.resize(sourceImage,outImage,new Size(sourceImage.cols()/3 ,sourceImage.rows()/3) , 0, 0 ,Imgproc.INTER_LINEAR);

        this.saveImage(this.save_dir + "/image_process_resize_1.png",outImage);

    }

    /**
     * 缩放图片-根据比例缩放
     */
    @Test
    public void testResize_2(){

        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat outImage = new Mat();

        Imgproc.resize(sourceImage,outImage,new Size(0,0) , 0.2, 0.4 ,Imgproc.INTER_LINEAR);

        this.saveImage(this.save_dir + "/image_process_resize_2.png",outImage);


    }


    /**
     * 主要学习矩形类型 Rect 的操作
     */
    @Test
    public void testRect(){

        /*
         *  Rect(x,y,width,height)4个参数
         *  x,y 代表开始点的位置，如果将矩形画在一张 Mat 中，将在 point(x,y)位置开始画
         *  witdth,height 矩形的宽高
         */

        Rect rect_a = new Rect(30,30,300,250);

        //矩形面积
        System.out.println(rect_a.area());

        //矩形的大小
        System.out.println(rect_a.size());

        //判断一个点是否在矩形中
        System.out.println(rect_a.contains(new Point(120,220)));

        //获取矩形左上角点 tl = top left  返回 Point 对象
        System.out.println(rect_a.tl());

        //获取矩形右下角点 br = below right  返回 Point 对象
        System.out.println(rect_a.br());


        Rect rect_b = new Rect(30,30,300,250);

        //取并集，交集？？？？？？

        //平移，缩放 ？？？？

    }


    /**
     * cvtColor 是颜色空间转化函数，可以实现 RGB 向 HSV、HSI 、灰度等颜色空间转化
     *
     * 函数原型 cvtColor(Mat src, Mat dst, int code, int dstCn)
     *
     * src : 输入图像
     * dst : 输出图像
     * code : 颜色空间转化的标识符 如 Imgproc.COLOR_<NAME>
     * dstCn : 输出图像的通道数，为 0 则取原图的通道数。
     *
     * 常用的
     * Imgproc.COLOR_BGR2BGRA （3通道图转4通道图）,
     * Imgproc.COLOR_RGB2GRAY （3通道转1通道灰度图）,
     * Imgproc.COLOR_GRAY2RGB  (1通道灰度图转3通道图)
     */
    @Test
    public void testCvtColor(){

        //构建一个3通道图
        Mat blue = new Mat(100,100,CvType.CV_8UC3,new Scalar(255));

        //构建一个输出对象
        Mat cvt_ret = new Mat();

        //将图像转化为 RGBA 4通道图像
        Imgproc.cvtColor(blue,cvt_ret,Imgproc.COLOR_BGR2BGRA);

        this.saveImage(this.save_dir + "/image_process_cvtcolor_1.png",cvt_ret);

    }

}
