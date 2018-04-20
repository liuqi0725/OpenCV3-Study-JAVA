package opencv.study;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. 3种线性滤波 <br/>
 * 2. 2种非线性滤波<br/>
 */
public class StudySection_7 extends OpenCVProcessBase {

    /*
     * 一、图像滤波介绍
     *
     * 图像滤波，是指保留图像细节特征的条件下，对目标图像的噪点进行抑制。
     * 其处理结果的好坏直接影响后续图像处理、分析的有效性和可靠性
     *
     * 消除图像中的噪声成分，叫做图像的滤波操作或平滑化。
     * 图像的能量大部分集中在低频、中频，在较高频段有用的信息经常被噪声淹没，因此才需要滤波操作使噪声的影响减少。
     *
     * 图像滤波目的：
     *      1. 抽出图像的特征作为图像识别的特征模式；
     *      2. 为适应图像处理的要求，消除图像数字化时产生的噪声
     *
     * 滤波处理的要求：
     *      1. 不能损坏图像的轮廓及边缘等重要信息
     *      2. 使图像清晰视觉效果好
     *
     * 二、OpenCV 滤波函数介绍
     *
     * 1. BoxBlur -- 方框滤波           【线性】
     * 2. Blur -- 均值滤波              【线性】
     * 3. GaussianBlur -- 高斯滤波      【线性】
     * 4. medianBlur -- 中值滤波        【非线性】
     * 5. bilateralFilter -- 双边滤波   【非线性】
     *
     * 三、 滤波器介绍
     *
     * 线性滤波使用的滤波器，用于剔除输入信号中不想要的频率或者从多个频率中获取自己想要的。
     *
     * 常见滤波器：
     *      1. 低通滤波器 ： 允许低频率通过
     *      2. 高通滤波器 ： 允许高频率通过
     *      3. 带通滤波器 ： 允许一定范围频率通过
     *      4. 带阻滤波器 ： 阻止一定范围频率通过，并允许其他范围的频率通过
     *      5. 全通滤波器 ： 允许所有频率通过，仅仅改变相位关系
     *      6. 陷波滤波器 ： 阻止一个狭窄频率范围通过，是一种特殊的`带阻滤波器`
     *
     *
     * 四、滤波与模糊的概念
     *
     * 滤波与模糊，容易混淆。滤波操作，根据滤波器可分为低通滤波、高通滤波。
     * 比如高斯滤波，低通滤波叫模糊，高通滤波叫锐化
     *
     */


    private String save_dir = "study-output/study-opencv-7";

    /**
     * 方框滤波
     */
    @Test
    public void testBoxFilter(){

        /*
         * boxFilter 有3个原型方法
         * boxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor, boolean normalize, int borderType)
         * boxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor, boolean normalize)
         * boxFilter(Mat src, Mat dst, int ddepth, Size ksize)
         *
         * src : Mat 输入图像 对通道数无要求，但是 depth 必须是 CV_8U、CV_16U、CV_16S、CV_32F、CV_64F 之一
         * dst : Mat 输出图像，与原图以上的尺寸与类型
         * ddepth : Integer 输出图像的深度，-1 代表使用原图的深度（即 src.depth()）。
         * ksize : Size 内核大小，即每一个运算的核站多大，单位像素。
         * anchor : Point 瞄点，处理的点。根据 ksize 划分出 N 个核，然后处理每个核的某个点。 (-1,-1)代表取这个核的中心位置。
         * normalize : Boolean 内核是否被其区域归一化了
         * borderType : Integer 推断图像外部像素的某种边界模式，一般不需要这个参数。
         */
        Mat src = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat dst = Mat.ones(src.size(),src.type());

        Imgproc.boxFilter(src,dst,-1,new Size(3,3));

        /*
         * 求和    可参考 https://blog.csdn.net/dcrmg/article/details/52589749
         */
        Mat sum = new Mat();
        Imgproc.integral(dst,sum);

        Core.normalize(sum,sum,0,255,Core.NORM_MINMAX);

        this.saveImage(this.save_dir + "/image_process_boxFilter.png",dst);
        this.saveImage(this.save_dir + "/image_process_boxFilter_sum.png",sum);
    }

    /**
     * 均值滤波
     *
     * 均值滤波概念，取以当前像素点 A（x,y）为中心，周围 N x N - 1 的像素点的均值，赋值给 当前像素点 A(x,y)
     *
     * 比如定义A（2,2）,范围为 3x3 ，那么就是取当前像素点(2,2)为中心周围 3x3范围内所有像素点，并 -1（排除自身）剩下的8个像素点的均值，
     * 并赋值给A
     */
    @Test
    public void testBlur(){

        /*
         * blur 有3个原型方法
         *  blur(Mat src, Mat dst, Size ksize, Point anchor, int borderType)
         *  blur(Mat src, Mat dst, Size ksize, Point anchor)
         *  blur(Mat src, Mat dst, Size ksize)
         *
         *  blur 内部直接调用了 boxfilter， 实际是 normalize（归一化）为 true 的 boxfilter
         *
         * src : Mat 输入图像 对通道数无要求，但是 Depth 必须是 CV_8U、CV_16U、CV_16S、CV_32F、CV_64F 之一
         * dst : Mat 输出图像，与原图以上的尺寸与类型
         * ksize : Size 内核大小，即每一个运算的核站多大，单位:像素。
         * anchor : Point 瞄点，处理的点。根据 ksize 划分出 N 个核，然后处理每个核的某个点。 (-1,-1)代表取这个核的中心位置。
         * borderType : Integer 推断图像外部像素的某种边界模式，一般不需要这个参数。
         */
        Mat src = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat dst = Mat.ones(src.size(),src.type());

        Imgproc.blur(src,dst,new Size(7,7));

        /*
         * 求和    可参考 https://blog.csdn.net/dcrmg/article/details/52589749
         */
        Mat sum = new Mat();
        Imgproc.integral(dst,sum);

        Core.normalize(sum,sum,0,255,Core.NORM_MINMAX);

        this.saveImage(this.save_dir + "/image_process_blur.png",dst);
        this.saveImage(this.save_dir + "/image_process_blur_sum.png",sum);

    }

    /**
     * 高斯滤波
     *
     * 高斯滤波又是一种线性的平滑滤波，广泛应用于图像处理的减噪过程
     */
    @Test
    public void testGaussianBlur(){

        /*
         * GaussianBlur 有3个原型方法
         * GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX, double sigmaY, int borderType)
         * GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX, double sigmaY)
         * GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX)
         *
         * src : Mat 输入图像 对通道数无要求，但是 Depth 必须是 CV_8U、CV_16U、CV_16S、CV_32F、CV_64F 之一
         * dst : Mat 输出图像，与原图以上的尺寸与类型
         * ksize : Size 内核大小，即每一个运算的核站多大，单位:像素。 必须为 正数 和 奇数，或者 0
         * sigmaX : 高斯核函数在X方向上的标准偏差
         * sigmaY : 高斯核函数在Y方向上的标准偏差，
         *      如果sigmaY是0，则函数会自动将sigmaY的值设置为与sigmaX相同的值，
         *      如果sigmaX和sigmaY都是0，这两个值将由ksize.width和ksize.height计算而来
         * borderType : Integer 推断图像外部像素的某种边界模式，一般不需要这个参数。
         *
         * 为保证结果的正确性，最好把 ksize、sigmaX、sigmaY 都设置了
         */
        Mat src = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat dst = Mat.ones(src.size(),src.type());

        Imgproc.GaussianBlur(src,dst,new Size(3,5),0,0);

        /*
         * 求和    可参考 https://blog.csdn.net/dcrmg/article/details/52589749
         */
        Mat sum = new Mat();
        Imgproc.integral(dst,sum);

        Core.normalize(sum,sum,0,255,Core.NORM_MINMAX);

        this.saveImage(this.save_dir + "/image_process_gaussianBlur.png",dst);
        this.saveImage(this.save_dir + "/image_process_gaussianBlur_sum.png",sum);
    }

    /**
     * 中值滤波
     *
     * 与线性滤波取加权之和不同，中值滤波采用取相邻区域中，灰度值的中指来替换当前像素点的灰度值
     * 这样，噪点突出的值就很难被选中，中值滤波更容易平滑过滤噪声
     *
     * 但是其运行速度是均值滤波的5倍以上
     */
    @Test
    public void testMedianFilter(){
        /*
         * 原型方法
         * medianBlur(Mat src, Mat dst, int ksize)
         *
         * src : Mat 输入图像 对通道数无要求，但是 Depth 必须是 CV_8U、CV_16U、CV_16S、CV_32F、CV_64F 之一
         * dst : Mat 输出图像，与原图以上的尺寸与类型
         * ksize : Integer 孔径的线性尺寸，必须是>1的奇数，如3，5，7，9....
         *
         */
        Mat src = Imgcodecs.imread(this.p_test_file_path + "/zaodian.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat dst = Mat.ones(src.size(),src.type());

        Imgproc.medianBlur(src,dst,3);

        /*
         * 求和    可参考 https://blog.csdn.net/dcrmg/article/details/52589749
         */
        Mat sum = new Mat();
        Imgproc.integral(dst,sum);

        Core.normalize(sum,sum,0,255,Core.NORM_MINMAX);

        this.saveImage(this.save_dir + "/image_process_medianFilter.png",dst);
        this.saveImage(this.save_dir + "/image_process_medianFilter_sum.png",sum);
    }

    /**
     * 双边滤波
     *
     * 可以做到对图像边缘最大限度的保留。
     *
     * 对高频（边缘）的过滤不明显，但是由于保留了过多的高频信息，对图像中的噪点过滤也不明显，但是对低频信息有很好的过滤效果（细节、纹理）
     */
    @Test
    public void testBilateralFilter(){
        /*
         * 原型方法
         * bilateralFilter(Mat src, Mat dst, int d, double sigmaColor, double sigmaSpace, int borderType)
         * bilateralFilter(Mat src, Mat dst, int d, double sigmaColor, double sigmaSpace)
         *
         * src : Mat 输入图像，需要8位或浮点型的(CV_8U,CV_32F、CV_64F)、并且只支持【单通道】 或 【三通道】的图像
         * dst : Mat 输出图像，与原图以上的尺寸与类型
         * d : Integer 过滤过程中每个像素邻域的直径。 如果为非正数，那么通过 sigmaSpace 来计算得到
         * sigmaColor : double 颜色空间滤波器的 sigma 值，这个值越大，就表示像素的邻域内有越宽广的颜色会被混合到一起，生成一大块颜色相近的区域
         * sigmaSpace : double 坐标空间滤波器的 sigma 值，这个值越大，意味着越远的像素会受到影响，从而使更大的区域中足够相似的颜色获取相同的颜色
         *      当 d > 0（第3个参数） 时，d 指定了邻域的大小且与 sigmaSpace 无关。否则，d 正比于 sigmaSpace。
         * borderType : Integer 推断图像外部像素的某种边界模式，一般不需要这个参数。
         */
        Mat src = Imgcodecs.imread(this.p_test_file_path + "/zaodian.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat dst = Mat.ones(src.size(),src.type());

        Imgproc.bilateralFilter(src,dst,20,20*2,20/2);

        /*
         * 求和    可参考 https://blog.csdn.net/dcrmg/article/details/52589749
         */
        Mat sum = new Mat();
        Imgproc.integral(dst,sum);

        Core.normalize(sum,sum,0,255,Core.NORM_MINMAX);

        this.saveImage(this.save_dir + "/image_process_bilateralFilter.png",dst);
        this.saveImage(this.save_dir + "/image_process_bilateralFilter_sum.png",sum);
    }

}
