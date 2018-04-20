package opencv;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. 图像的读取 <br/>
 * 2. 对 ROI 区域描边<br/>
 * 3. 截取 ROI 区域<br/>
 * 4. 用图片在原始图片上划定 ROI 区域，并替换<br/>
 */
public class ProcessTest_2 extends OpenCVProcessBase {

    private String save_dir = "study-output/study-opencv-2";

    /*
     * Imgcodecs.imread(String filename, int flags)
     *
     * 1. 只传 filename flags 默认为 1 彩色图
     * 2. flags = Imgcodecs 下 CV_LOAD 常量
     *      +-- CV_LOAD_IMAGE_UNCHANGED = -1;   载入包含 Alpha 通道时，需要
     *      +-- CV_LOAD_IMAGE_GRAYSCALE = 0;    加载一个张灰度图，设置该值，图像将会转换为灰度后返回
     *      +-- CV_LOAD_IMAGE_COLOR = 1;        加载一张彩色图，设置为该值，图像将会转化为3通道彩色图返回
     *      +-- CV_LOAD_IMAGE_ANYDEPTH = 2;     如果载入的图像为16Bite 或者 32Bite 则载入对应 Bite 的图像，反之载入的图像为8Bite
     *      +-- CV_LOAD_IMAGE_ANYCOLOR = 4;     如果载入的图像为16Bite 或者 32Bite 则载入对应 Bite 的图像，反之载入的图像为8Bite
     *
     *      一张高位彩色图如果用 CV_LOAD_IMAGE_COLOR 载入，则会转化为8 Bite 图，如果想载入无损图片，使用 ANYDEPTH、ANYCOLOR 载入。
     */

    /**
     * 读取图像的第一种方式
     * 此方式读取图像后，为原始未转化的图像
     */
    @Test
    public void readImage_1(){

        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg");
        //输出文件
        this.saveImage(this.save_dir + "/read_image_fn_1.png",sourceImage);
    }

    /**
     * 读取图像的第二种方式
     * 此方式读取后，图像是一个 8位通道图像，也就是经过灰度处理的
     */
    @Test
    public void readImage_2(){
        Mat sourceImage = Imgcodecs.imread(p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        this.saveImage(this.save_dir + "/read_image_fn_2.png",sourceImage);

        //如果不采用此方式进行灰度处理，可以手动进行灰度处理
        //Mat image = Imgcodecs.imread(test_file_path + "/5cent.jpg");
        //Imgproc.cvtColor(image,image,Imgproc.COLOR_RGB2GRAY);
    }

    /**
     * ROI -- Region Of Interest [感兴趣区域]
     *
     * 把感兴趣的区域勾画出来
     */
    @Test
    public void testROI_1(){

        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        // 划线，设置2个点，分别为开始点，结束点，设置线条颜色
        Imgproc.rectangle(sourceImage,new Point(30,30),new Point(500,500) , new Scalar(0,255,0));

        this.saveImage(this.save_dir + "/ROI_draw_area.png",sourceImage);
    }

    /**
     * 把感兴趣的区域截取出来
     */
    @Test
    public void testROI_2(){
        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        /*
         * Rect 矩形  4个参数 ，开始点的 x,y 坐标， width,height 截取的宽高
         */
        Mat mat_roi = sourceImage.submat(new Rect(30,30,500,500));

        this.saveImage(this.save_dir + "/ROI_cut_area.png",mat_roi);

    }

    /**
     * 用图片在原始图片上划定 ROI 区域，并替换(如添加 logo)
     *
     * 方法-1
     */
    @Test
    public void testROI_3(){
        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        // 读取logo 尽量用读取原图
        Mat logoImage = Imgcodecs.imread(this.p_test_file_path + "/logo-88-88.png",Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);

        //根据 logo 的宽高，在原图上划定感兴趣区域
        Mat mat_roi = sourceImage.submat(new Rect(30,30,logoImage.cols(),logoImage.rows()));

        System.out.println(logoImage.channels());
        System.out.println(mat_roi.channels());

        // 在合并图像前注意，保证2个图像的通道是一致的，如果不一致需要转化
        //Imgproc.cvtColor(logoImage,newLogoImage,Imgproc.COLOR_GRAY2BGR);

        //第一第四个参数就是各自权重
        Core.addWeighted(mat_roi,0.1, logoImage, 0.9, 0., mat_roi);

        this.saveImage(this.save_dir + "/ROI_add_area_image_1.png",sourceImage);


    }


    /**
     * 用图片在原始图片上划定 ROI 区域，并替换(如添加 logo)
     *
     * 方法-2
     */
    @Test
    public void testROI_4(){
        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        // 读取logo 尽量用读取原图
        Mat logoImage = Imgcodecs.imread(this.p_test_file_path + "/logo-468-230.jpeg",Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);

        //原始图片转 BGRA 4通道图像（带透明层），作为 mask（遮罩）
        Mat mask = Imgcodecs.imread(this.p_test_file_path + "/logo-468-230.jpeg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        //根据 logo 的宽高，在原图上划定感兴趣区域
        Mat mat_roi = sourceImage.submat(new Rect(30,30,logoImage.cols(),logoImage.rows()));

        logoImage.copyTo(mat_roi,mask);

        this.saveImage(this.save_dir + "/ROI_add_area_image_2.png",sourceImage);


    }

}
