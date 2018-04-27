package opencv.study;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. floodFill 漫水|水漫填充<br/>
 * 2. pyrUp 向上采样<br/>
 * 3. pyrDown 向下采样<br/>
 * 4. resize 放大缩小<br/>
 */
public class StudySection_10 extends OpenCVProcessBase {


    private String save_dir = "study-output/study-opencv-10";

    /**--------------------*
     *  第一节 floodFill
     **--------------------*/

    /**
     * 漫水填充
     *
     * 又叫水漫填充，用特定的颜色填充连通区域，常用来标记或分离图像的一部分。
     * 漫水填充，将种子点颜色相近的区域进行连通，类似 photoshop 的魔术棒工具。
     */
    @Test
    public void testFloodFill(){

        /*
         * 官方说明 : https://docs.opencv.org/3.0-beta/modules/imgproc/doc/miscellaneous_transformations.html#floodfill
         *
         * 原型方法 :
         *      floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal)  这个原型方法，用参数测试了下，没效果。不知道原因。
         *      floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal, Rect rect, Scalar loDiff, Scalar upDiff, int flags)
         *
         *      参数 :
         *          image : Mat 输入图像 输入/输出图像，1通道或3通道，8位图像
         *          mask : Mat 操作掩膜，单通道，8位，比image 长宽都大2个像素的图像。
         *          seedPoint : Point ,起始点,漫水填充的种子点
         *          newVal : Scalar ,像素点被染色的值，即通过起始点漫水填充后的区域填充的颜色
         *          rect : Rect ,不传的话，默认为0。设置漫水填充的最小区域。如果设置了，而漫水填充找出的区域 < rect 那么不会填充。
         *          loDiff : Scalar ,在当前观察到的像素和它的一个相邻的像素点之间的最大亮度/颜色的负差 最大值。
         *          upDiff : Scalar ,在当前观察到的像素和它的一个相邻的像素点之间的最大亮度/颜色的正差 最大值。
         *          flags : int ,操作标识符。参看下面示例
         *
         *          一般不需要borderType，borderValue，均有默认值。如果需要使用，可参考官网获取更多信息
         */

        Mat src = Imgcodecs.imread(this.p_test_file_path + "/caoyuan.jpg");
        // mask 单通道，8位图，比原始图片宽高多2像素
        Mat mask = new Mat(src.height()+2,src.width()+2 , CvType.CV_8UC1);
        Point seedPonit = new Point(30,35);
        Scalar loDiff = new Scalar(20,20,20); //没怎么明白，建议要了解的看下官方说明
        Scalar upDiff = new Scalar(20,20,20); //没怎么明白，建议要了解的看下官方说明
        /*
         * flags 此参数参看示例设置
         *
         * 分别分为3个部分，低、高、中，参数之间用为运算符号 "|" （按位或）链接
         *
         * 1. 低八位
         *      -- 可选值： 4 、8 ，2个选项。
         *      -- 4 只计算水平、垂直方向，8 多了个对角线方向
         * 2. 高八位
         *      -- 可选值 ，0 或 FLOODFILL_MASK_ONLY 、 FLOODFILL_FIXED_RANGE，除0外，2个常量可以组合或单独使用
         *      -- FLOODFILL_MASK_ONLY  不会填充原始图，只填充 mask 。 即 newVal 参数会失效
         *      -- FLOODFILL_FIXED_RANGE 设置后：会考虑当前像素与种子像素之间的色差，不设置：会考虑当前像素与邻近像素之间的差。
         * 3. 中八位
         *      -- 当高八位设置成 FLOODFILL_MASK_ONLY 时，由于不填充 src，填充 mask，而 mask 为单通道，newVal 为 RGB，所以不适用，导致 newVal 失效。
         *          这里就给一个值，用于填充 mask 。
         *      -- 如果为 0 ，则 mask 会用1来填充
         *      -- 注意，非0 的情况，中八位的值要填充高八位，需要向左位移8位。如填充 值为38 ，38二进制位 100110 , 向左位移8位 10011000000000
         */
        int flags = 8 | Imgproc.FLOODFILL_MASK_ONLY | Imgproc.FLOODFILL_FIXED_RANGE | (38<<8);
        // flags 计算规则(位运算)
        // 原始数字                                      二进制
        // 8                                     =                    1000
        // Imgproc.FLOODFILL_MASK_ONLY = 131072  =      100000000000000000
        // Imgproc.FLOODFILL_FIXED_RANGE = 65536 =       10000000000000000
        // 38<<8 = 100110 << 8                   =          10011000000000
        //
        // 按位或（"|"）计算：                            110010011000001000

        Rect rect = new Rect(2,2,1,1);

        Scalar newVal = new Scalar(0,0,255);

        //这个原型方法，用参数测试了下，没效果。不知道原因。
        //Imgproc.floodFill(src,mask,seedPonit,newVal);

        Imgproc.floodFill(src,mask,seedPonit,newVal,rect,loDiff,upDiff,flags);
        this.saveImage(this.save_dir + "/image_process_floodfill_src.png",src);
        this.saveImage(this.save_dir + "/image_process_floodfill_mask.png",mask);

    }

    /**--------------------*
     *  第二节 pyrUp、pyrDown
     **--------------------*/

    /*
     * 在学习 pyrUp、pyrDown 之前先了解下图像金字塔
     *
     * 图像金字塔：
     *      图像金字塔是图像中多尺度表达图像的一种，最主要的用途是图像的分割。用分辨率来解释图片的的简单结构。
     *      比如，同一张图片：原始为 1024x768 ，改变下: (1024x0.9)x(768x0.9)=922x691 依次类推.... 分辨率 越低，则图像越小。
     *      用金字塔来比喻图像分辨率,会以梯度往下采集图像，直至达到结束条件:
     *
     * 图像金字塔示例：
     *      金字塔的底层为原始图像分辨率，金字塔层级越高分辨率越低。
     *
     *          3 缩小 ---------      ...
     *          2 缩小 ---------    830x622
     *          1 缩小 ---------    922x691
     *          0 原始 ---------  1024 x 768
     *
     *      在金字塔中层级方向是： 【上】 0,1,2,3.....x 【下】
     *          -- pyrDown 向下采样，可以理解为缩小
     *          -- pyrUp 向上采样，可以理解为放大
     *
     *      注意：pyrDown、pyrUp 不是互逆的。 既 原始图通过pyrDown 缩小后，再通过 pyrUp放大后的图像像素与原始图像像素是不一样的。
     *
     * -- pyrUp 放大，对原始图像扩大，在新增的行、列像素中，根据周围的像素去估计可能"丢失"的像素

     */

    /**
     * 图像向上采样
     */
    @Test
    public void testPyrUp(){

        /*
         * 原文地址: https://docs.opencv.org/3.0-beta/modules/imgproc/doc/filtering.html?highlight=pyrup
         *
         * 原型方法
         *      pyrUp(Mat src, Mat dst, Size dstsize, int borderType)
         *      pyrUp(Mat src, Mat dst, Size dstsize)
         *      pyrUp(Mat src, Mat dst)
         *
         *      参数说明:
         *          src : Mat 输入图像
         *          dst : Mat 输出图像
         *          dstsize : Size 输出图像的大小，默认值 Size(src.clos*2 , src.rows*2)
         *          borderType : int 边界模式，不明白意思,一些资料说不管即可 //Pixel extrapolation method (only BORDER_DEFAULT supported). See borderInterpolate for details.
         */

        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat dstImage = new Mat();
        Imgproc.pyrUp(sourceImage , dstImage);
        this.saveImage(this.save_dir + "/image_process_pyrUp_1.png",dstImage);

    }

    /**
     * 图像向下采样
     */
    @Test
    public void testPyrDown(){

        /*
         * 原文地址: https://docs.opencv.org/3.0-beta/modules/imgproc/doc/filtering.html?highlight=pyrdown
         *
         * 原型方法
         *      pyrDown(Mat src, Mat dst, Size dstsize, int borderType)
         *      pyrDown(Mat src, Mat dst, Size dstsize)
         *      pyrDown(Mat src, Mat dst)
         *
         *      参数说明:
         *          src : Mat 输入图像
         *          dst : Mat 输出图像
         *          dstsize : Size 输出图像的大小，默认值 Size((src.clos+1)/2 , (src.rows+1)/2)
         *          borderType : int 边界模式，不明白意思,一些资料说不管即可 //Pixel extrapolation method (only BORDER_DEFAULT supported). See borderInterpolate for details.
         */

        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat dstImage = new Mat();
        Imgproc.pyrDown(sourceImage , dstImage);
        this.saveImage(this.save_dir + "/image_process_pyrDown_1.png",dstImage);

        /*
         *  pyrDown 采用高斯金字塔算法来向下采样
         *  1. 对图像进行高斯卷积
         *  2. 将所有偶数行、列去掉
         *
         *  简单如下：
         *
         *              1 2 3 4 5 6     1 3 5
         *              2 2 3 4 5 6     3 3 5
         *              3 2 3 4 5 6
         *              4 2 3 4 5 6
         *              原始矩阵(6x4)    pyrDown 后(3x2)【去掉偶数行、列】每个矩阵缩小了一倍
         */
    }


    /**--------------------*
     *  第三节 resize
     **--------------------*/

    /*
     * resize 是 OpenCV 里专门用来调整图像大小的函数
     *
     * 原型方法 :
     *      resize(Mat src, Mat dst, Size dsize)
     *      resize(Mat src, Mat dst, Size dsize, double fx, double fy, int interpolation)
     *
     * 参数说明
     *      src: 输入，原图像，即待改变大小的图像
     *      dst: 输出，改变大小之后的图像
     *      dsize: 输出图像的大小 Size(width,height) ，通过 new Size(0,0) 可以设置其值为0，当 dsize 为0时，按照 fx、fy 缩放图片
     *          -- 当 dsize 为 0 时，通过计算 fx、fy 来对图片进行缩放，缩放的公式为：new Size(round(fx*src.cols), round(fy*src.rows))
     *      fx: 宽度的缩放比例，当 dsize 为 0 时才生效。
     *      fy: 高度的缩放比例，当 dsize 为 0 时才生效。
     *      interpolation: int 插值方式，默认 INTER_LINEAR 线性插值
     *          -- INTER_LINEAR 线性插值
     *          -- INTER_NEAREST 最近邻插值
     *          -- INTER_AREA 区域插值（利用像素区域关系的重采样插值）
     *          -- INTER_CUBIC 三次样条插值（超过4x4像素邻域内的双三次插值)
     *          -- INTER_LANCZOS4 Lanczos 插值（超过8x8像素邻域内的Lanczos插值）
     *
     *          *** 缩小一般用 INTER_AREA,放大2种 INTER_CUBIC(效率不高，速度慢)、INTER_LANCZOS4（效率高、速度快）
     *
     * 注意：
     * dsize 与 fx，fy 分别为2种模式，必须启用一种。要么自定义大小（dszie），要么按照比例（fx,fy）; 也就是说，dsize、fx、fy 3者不能同时都为 0。
     *
     */

    /**
     * 图像缩小
     */
    @Test
    public void testResize_small(){

        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        // 定义一个比原图小的 mat
        Mat dstImage = Mat.ones(400,400,CvType.CV_8UC3);
        // 会根据输出图像 dstImage的大小，自动计算出 fx，fy，进行缩放
        Imgproc.resize(sourceImage,dstImage,dstImage.size());
        this.saveImage(this.save_dir + "/image_process_resize_small_1.png",dstImage);

        // 等比缩放
        dstImage = Mat.ones(sourceImage.rows()/3,sourceImage.cols()/3,CvType.CV_8UC3);
        Imgproc.resize(sourceImage,dstImage,dstImage.size());
        this.saveImage(this.save_dir + "/image_process_resize_small_2.png",dstImage);

        // 指定比例的缩放
        dstImage = new Mat();
        Imgproc.resize(sourceImage,dstImage,new Size(0,0) , 0.2, 0.4 ,Imgproc.INTER_AREA);
        this.saveImage(this.save_dir + "/image_process_resize_small_3.png",dstImage);


        //比较插值不同，缩小后的效果
        Mat src1 = new Mat();
        sourceImage.copyTo(src1);

        Mat src2 = new Mat();
        sourceImage.copyTo(src2);

        Mat dstImage1 = null,dstImage2 = null;
        for(int i=0; i<3; i++){

            dstImage1 = Mat.ones(src1.rows()/2,src1.cols()/2,CvType.CV_8UC3);
            dstImage2 = Mat.ones(src2.rows()/2,src2.cols()/2,CvType.CV_8UC3);

            // 线性插值
            Imgproc.resize(src1,dstImage1,dstImage1.size(),0,0,Imgproc.INTER_LINEAR);
            // 区域插值
            Imgproc.resize(src2,dstImage2,dstImage2.size(),0,0,Imgproc.INTER_AREA);


            dstImage1.copyTo(src1);
            dstImage2.copyTo(src2);
        }

        //输出内容可以放大看看效果
        this.saveImage(this.save_dir + "/image_process_resize_small_Linear.png",dstImage1);
        this.saveImage(this.save_dir + "/image_process_resize_small_area.png",dstImage2);


    }

    /**
     * 图像放大
     */
    @Test
    public void testResize_big(){

        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        // 定义一个比原图大的 mat
        Mat dstImage = Mat.ones(1000,1000,CvType.CV_8UC3);
        // 会根据输出图像 dstImage的大小，自动计算出 fx，fy，进行缩放
        Imgproc.resize(sourceImage,dstImage,dstImage.size());
        this.saveImage(this.save_dir + "/image_process_resize_big_1.png",dstImage);

        // 等比放大
        dstImage = Mat.ones((int)(sourceImage.rows()*1.3),(int)(sourceImage.cols()*1.3),CvType.CV_8UC3);
        Imgproc.resize(sourceImage,dstImage,dstImage.size());
        this.saveImage(this.save_dir + "/image_process_resize_big_2.png",dstImage);

        // 指定比例的放大
        dstImage = new Mat();
        Imgproc.resize(sourceImage,dstImage,new Size(0,0) , 1.3, 1.8 ,Imgproc.INTER_CUBIC);
        this.saveImage(this.save_dir + "/image_process_resize_big_3.png",dstImage);


        //比较插值不同，放大后的效果
        Mat src1 = new Mat();
        sourceImage.copyTo(src1);

        Mat src2 = new Mat();
        sourceImage.copyTo(src2);

        Mat src3 = new Mat();
        sourceImage.copyTo(src3);

        Mat dstImage1 = null,dstImage2 = null,dstImage3 = null;
        for(int i=0; i<6; i++){

            dstImage1 = Mat.ones((int)(src1.rows()*1.3),(int)(src1.cols()*1.3),CvType.CV_8UC3);
            dstImage2 = Mat.ones((int)(src2.rows()*1.3),(int)(src2.cols()*1.3),CvType.CV_8UC3);
            dstImage3 = Mat.ones((int)(src3.rows()*1.3),(int)(src3.cols()*1.3),CvType.CV_8UC3);

            // 线性插值
            Imgproc.resize(src1,dstImage1,dstImage1.size(),0,0,Imgproc.INTER_LINEAR);
            // 三次条插值
            Imgproc.resize(src2,dstImage2,dstImage2.size(),0,0,Imgproc.INTER_CUBIC);
            // Lanczos插值
            Imgproc.resize(src3,dstImage3,dstImage3.size(),0,0,Imgproc.INTER_LANCZOS4);

            dstImage1.copyTo(src1);
            dstImage2.copyTo(src2);
            dstImage3.copyTo(src3);
        }

        //输出内容可以放大看看效果，看看那种放大要平滑点
        this.saveImage(this.save_dir + "/image_process_resize_big_linear.png",dstImage1);
        this.saveImage(this.save_dir + "/image_process_resize_big_cubic.png",dstImage2);
        this.saveImage(this.save_dir + "/image_process_resize_big_lanczos.png",dstImage3);

    }

}
