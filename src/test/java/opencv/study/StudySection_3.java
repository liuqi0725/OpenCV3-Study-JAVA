package opencv.study;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. rect 矩形对象<br/>
 * 2. cvtColor 颜色空间转化转换<br/>
 */
public class StudySection_3 extends OpenCVProcessBase {

    private String save_dir = "study-output/study-opencv-3";

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
