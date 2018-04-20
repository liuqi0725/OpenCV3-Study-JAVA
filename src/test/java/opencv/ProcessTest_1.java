package opencv;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. 创建 Mat 对象，Mat 对象的参数介绍 <br/>
 * 2. 通过Matlab 创建 Mat 对象<br/>
 * 3. 创建彩色图像<br/>
 * 4. 创建透明图像<br/>
 * 5. 期望值来创建随机的初始化矩阵图像<br/>
 */
public class ProcessTest_1 extends OpenCVProcessBase {

    private String save_dir = "study-output/study-opencv-1";

    /*
     * 学习构建 Mat（图像）
     *
     * 图像通道：
     *      OpenCV 中，图像可以分别为1，2，3，4 通道。
     *      -- 1 通道为灰度图；
     *      -- 2 通道的图像是RGB555和RGB565。2通道图在程序处理中会用到，如傅里叶变换，可能会用到，一个通道为实数，一个通道为虚数，主要是编程方便。RGB555是16位的，2个字节，5+6+5，第一字节的前5位是R，后三位+第二字节是G，第二字节后5位是B，可见对原图像进行压缩了
     *      -- 3 通道为彩色图（RGB）；
     *      -- 4 通道为 RGBA ，是RGB加上一个A通道，也叫alpha通道，表示透明度，PNG图像是一种典型的4通道图像。alpha通道可以赋值0到1，或者0到255，表示透明到不透明
     *
     *      ** 其中常见的是1通道和3通道，2通道和4通道不常见
     *
     * OpenCV CvType 说明：
     *
     * opencv 中表示图像的类型的常量，通过 CvType.[name] 调用。
     *
     * 1. CvType 格式
     *      -- CV_<bit_depth>(S|U|F)C<number_of_channels>
     *          +-- bit_depth [bite、比特数] , 有8bite，16bite，32bite，64bite
     *              如果你现在创建了一个存储 [灰度图] 片的Mat对象,这个图像的大小为宽100,高100,那么,现在这张
     *              灰度图片中有10000 (100x100) 个像素点，它每一个像素点在内存空间所占的空间大小是8bite,即8位
     *              name 它对应的 <bit_depth> 即为 8 --> CV_8
     *          +-- S|U|F
     *              S : signed int , 有符号整形
     *              U : unsigned int , 无符号整形
     *              F : float , 单精度浮点型
     *          +-- C<number_of_channels>  图像的通道数
     *
     *          CV_8UC3 即 8位无符号的3通道（RGB 彩色）图像
     *
     * 2. 各 Bite 的数据
     *      +-- 8U  无符号的8位图     CV_8UC1，CV_8UC2，CV_8UC3，CV_8UC4     取值范围 0~255
     *      +-- 8S  有符号的8位图     CV_8SC1，CV_8SC2，CV_8SC3，CV_8SC4     取值范围 -128~127
     *      +-- 16U 无符号的16位图    CV_16UC1，CV_16UC2，CV_16UC3，CV_16UC4 取值范围 0~65535
     *      +-- 16S 有符号的16位图    CV_16SC1，CV_16SC2，CV_16SC3，CV_16SC4 取值范围 -32768~32767
     *      +-- 32S 无符号的32位图    CV_32SC1，CV_32SC2，CV_32SC3，CV_32SC4 取值范围 2147483648~2147483647
     *      +-- 32F 浮点型32位图      CV_32FC1，CV_32FC2，CV_32FC3，CV_32FC4 取值范围 1.18*(10[-38次方])~3.40*(10[38次方])
     *      +-- 64F 浮点型64位图      CV_64FC1，CV_64FC2，CV_64FC3，CV_64FC4 取值范围 2.23*(10[-308次方])~1.79*(10[308次方])
     *      +-- 1位  IPL_DEPTH_1U 取值范围 0~1
     *
     * OpenCV Scalar 说明：
     *
     * opencv 中用以表示通道值对象，其构造值根据 CvType 通道 S|U|F 来决定取值范围。
     *
     * 构造函数：
     *      new Scalar(param)
     *      new Scalar(param,param)
     *      new Scalar(param,param,param)
     *      new Scalar(param,param,param,param)
     *
     *      1. 构造函数的入参（值）的数量 <= 通道数量 [如果是2通道图像，用4个参数的构造函数，后面2个参数不起作用]
     *      2. 构造函数的入参（值）的数量 < 通道数量 时，未传入值的通道值为0
     *      3. 多通道图像时，入参顺序 new Scalar(B,G,R,alpha)
     *          举例：
     *          构造蓝色的图，CvType.8UC3 [3通道,RGB 彩色] , new Salar(255), 后面2个参数默认为0
     *          构造绿色的图，CvType.8UC3 [3通道,RGB 彩色] , new Salar(0,255), 后面1个参数默认为0
     *          构造红色的图，CvType.8UC3 [3通道,RGB 彩色] , new Salar(0,0,255)
     *          构造蓝色的半透明图，CvType.8UC3 [4通道,RGBA 彩色+alpha 图像] , new Salar(255,0,0,125) 即可
     */


    /**
     * 创建图像 A 方法
     */
    @Test
    public void createMat_1(){
        // size 为 width / height

        //以 Size、CvType、Scalar 创建一个图像，构造函数如下：
        //public Mat(Size size, int type, Scalar s)

        Mat newImage = new Mat(new Size(200,300),CvType.CV_8UC3, new Scalar(255));

        this.saveImage(this.save_dir + "/new_mat_fn_1.png",newImage);

    }

    /**
     * 创建图像 B 方法
     */
    @Test
    public void createMat_2(){
        // size 为 width / height

        //以 Size、CvType、Scalar 创建一个图像，构造函数如下：
        //public Mat(Size size, int type, Scalar s)

        Mat sourceImage = new Mat(100,100, CvType.CV_8UC4, new Scalar(255,0,0,125));
        this.saveImage(this.save_dir + "/new_mat_fn_2.png",sourceImage);
    }

    /**
     * 以下是3种通过 Matlab 方式的初始化 Mat 方式。
     */
    @Test
    public void createMat_Matlab_eye(){
        Mat sourceImage = Mat.eye(new Size(4,4),CvType.CV_8UC(4));
        System.out.println(sourceImage.dump());
    }

    @Test
    public void createMat_Matlab_ones(){
        Mat sourceImage_1 = Mat.ones(new Size(4,4),CvType.CV_8UC(4));
        System.out.println(sourceImage_1.dump());

        Mat sourceImage_2 = Mat.ones(4,4,CvType.CV_8UC(2));
        System.out.println(sourceImage_2.dump());
    }

    @Test
    public void createMat_Matlab_zeros(){
        Mat sourceImage_1 = Mat.zeros(new Size(4,4),CvType.CV_8UC(4));
        System.out.println(sourceImage_1.dump());

        Mat sourceImage_2 = Mat.zeros(4,4,CvType.CV_8UC(2));
        System.out.println(sourceImage_2.dump());
    }

    /**
     * 创建灰色图像
     */
    @Test
    public void createGrayImage(){
        //选择一个单通道类型 CV_8UC1
        //单通道下 0 为黑，255为白，取中间值 125 ，灰色。
        Mat sourceImage = new Mat(100,100, CvType.CV_8UC1, new Scalar(125));
        this.saveImage(this.save_dir + "/new_mat_gay.png",sourceImage);
    }

    /**
     * 创建蓝色图像
     */
    @Test
    public void createBlueImage(){
        //蓝色为彩色图，所以通道类型为3通道，CV_8UC3
        //蓝色是 RGB 中的 B，其RGB值为 R：0  ,  G：0  ,  B：255
        //Scalar 入参顺序为 new Scalar(B,G,R,A)。
        //new Scalar(255) 的意思为 B 通道值为255 ，G、R 值不传递的话，默认为0
        Mat sourceImage = new Mat(100,100, CvType.CV_8UC3, new Scalar(255));
        this.saveImage(this.save_dir + "/new_mat_blue.png",sourceImage);
    }

    /**
     * 创建绿色图像
     */
    @Test
    public void createGreenImage(){
        //绿色为彩色图，所以通道类型为3通道，CV_8UC3
        //绿色是 RGB 中的  G，其RGB值为 R：0  ,  G：255  ,  B：0
        //Scalar 入参顺序为 new Scalar(B,G,R,A)。
        //new Scalar(0,255) 的意思为 B 通道值为0，G通道值为255，R 值不传递的话，默认为0

        Mat sourceImage = new Mat(100,100, CvType.CV_8UC3, new Scalar(0,255));
        this.saveImage(this.save_dir + "/new_mat_green.png",sourceImage);

    }

    /**
     * 创建红色图像
     */
    @Test
    public void createRedImage(){
        //红色为彩色图，所以通道类型为3通道，CV_8UC3
        //红色是 RGB 中的 R，其RGB值为 R：255  ,  G：0  ,  B：0
        //Scalar 入参顺序为 new Scalar(B,G,R,A)。
        //new Scalar(0,0,255) 的意思为 B 通道值为 0，G 通道值为 0，R 通道值为 255

        Mat sourceImage = new Mat(100,100, CvType.CV_8UC3, new Scalar(0,0,255));

        this.saveImage(this.save_dir + "/new_mat_red.png",sourceImage);

        new ProcessTest_2();
    }


    /**
     * 创建透明图像
     */
    @Test
    public void createAlphaImage(){
        //透明图像为4通道图像，所以通道类型为4通道，CV_8UC4
        //创建一个半透明的红色图像来试试
        //红色是 RGB 中的 R，其RGB值为 R：255  ,  G：0  ,  B：0
        //Scalar 入参顺序为 new Scalar(B,G,R,A)。
        //new Scalar(0,0,255,125) 的意思为 B 通道值为 0，G 通道值为 0，R 通道值为 255，Alpha 通道值为125 （255为不透明，0为全透明会忽略所有的 RGB 颜色）

        Mat sourceImage = new Mat(100,100, CvType.CV_8UC4, new Scalar(0,0,255,125));

        this.saveImage(this.save_dir + "/new_mat_alpha_red.png",sourceImage);

    }

    /**
     * 通过期望值来创建随机的初始化矩阵图像
     *
     */
    @Test
    public void createRandImage(){

        // 初始化 Mat
        Mat sourceImage = Mat.eye(new Size(200,200),CvType.CV_8UC3);

        // 随机给通道填充值， randu(src,low,high)   low=最小值， high=最大值，  0~255
        Core.randu(sourceImage,125,255);

        this.saveImage(this.save_dir + "/new_mat_rand_color.png",sourceImage);

    }
}
