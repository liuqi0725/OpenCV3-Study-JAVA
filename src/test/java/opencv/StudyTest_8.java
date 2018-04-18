package opencv;

import opencv.base.OpenCVStudyBase;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. 图像腐蚀<br/>
 * 2. 图像膨胀<br/>
 * 3. 图像边缘检测<br/>
 */
public class StudyTest_8 extends OpenCVStudyBase{


    private String save_dir = "study-output/study-opencv-8";

    /**
     * 图像腐蚀处理（可以理解为像素化）
     * 直接腐蚀
     */
    @Test
    public void erodeImage_1(){
        Mat sourceImage = Imgcodecs.imread(p_test_file_path + "/5cent.jpg");
        //Mat sourceImage = Imgcodecs.imread(test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        // size 越小，腐蚀的单位越小，图片越接近原图
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(10,10));
        Mat outImage = new Mat();
        //开始腐蚀
        Imgproc.erode(sourceImage,outImage,structImage);
        this.saveImage(this.save_dir + "/image_process_erode_1.png",outImage);


    }

    /**
     * 图像腐蚀处理
     * 二值化后腐蚀
     */
    @Test
    public void erodeImage_2() {
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg");

        //二值化处理   cv_8uc1 8位单通道格式
        Mat binaryMat = new Mat(sourceImage.height(), sourceImage.width(), CvType.CV_8UC1);
        Imgproc.threshold(sourceImage, binaryMat,100, 200, Imgproc.THRESH_BINARY);

        Mat outImage = new Mat();
        //图像腐蚀
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
        Imgproc.erode(binaryMat, outImage, structImage);

        this.saveImage(this.save_dir + "/image_process_erode_2.png",outImage);

    }

    /**
     * 模糊图像/均值滤波
     * 模糊图像可以起到降噪的目的
     */
    @Test
    public void blurImage_1(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg");
        //Mat sourceImage = Imgcodecs.imread(test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat outImage = new Mat();
        //模糊图像，size 单位越大 模糊效果越好
        Imgproc.blur(sourceImage,outImage,new Size(5,10));

        this.saveImage(this.save_dir + "/image_process_normal_blur.png",outImage);

    }

    /**
     * 模糊图像/均值滤波
     * 模糊图像可以起到降噪的目的
     *
     * 高斯模糊
     */
    @Test
    public void blurImage_2(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg");
        //Mat sourceImage = Imgcodecs.imread(test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat outImage = new Mat();

        Imgproc.GaussianBlur(sourceImage,outImage,new Size(3,3),0);

        this.saveImage(this.save_dir + "/image_process_gaussian_blur.png",outImage);

    }

    /**
     * 图像边缘检测
     * 1. 把原始图转化为灰度图片
     * 2. 模糊进行降噪
     * 3. canny 函数边缘处理
     */
    @Test
    public void cannyImage(){
        //直接进行灰度处理
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/table-1.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat outImage = new Mat();

        //利用模糊降噪 ，size 不要过大，根据图像大小调整

        //普通模糊方式
        Imgproc.blur(sourceImage,outImage,new Size(10,10));

        //高斯模糊方式
        //Imgproc.GaussianBlur(sourceImage,outImage,new Size(3,3),0);

        // 20，60 2个参数 符合 1：3   1：2 即可
        Imgproc.Canny(outImage,outImage, 20 ,60);

        this.saveImage(this.save_dir + "/image_process_canny.png",outImage);

    }


}
