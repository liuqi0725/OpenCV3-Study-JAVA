package opencv;

import opencv.base.OpenCVStudyBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. 图像腐蚀<br/>
 * 2. 图像膨胀<br/>
 * 3. 查找条形码案例<br/>
 */
public class StudyTest_8 extends OpenCVStudyBase{

    /*
     * 腐蚀，膨胀都属于形态学滤波。
     *
     * 数学形态学中，基本的运算有：
     * 二值腐蚀和膨胀
     * 二值开闭运算
     * 骨架抽取
     * 极限腐蚀
     * 灰值腐蚀和膨胀
     * 灰值开闭运算
     * 灰值形态学梯度
     * .....
     *
     *
     * 腐蚀，膨胀的主要功能如下：
     * 1. 消除噪声
     * 2. 分割（isolate）出独立的图像元素，在图像中连接（join）相邻的元素
     * 3. 寻找图像中的明显的极大值区域或极小值区域
     * 4. 求出图像的梯度
     *
     * 注意：
     * 腐蚀和膨胀仅针对`图像高亮`区域进行操作。
     *
     */

    private String save_dir = "study-output/study-opencv-8";

    /*
     * 如何创建腐蚀、膨胀操作的核
     *
     *      腐蚀和膨胀均有一个 Mat kernel 参数。这个参数就是腐蚀/膨胀操作的核，它是一个矩阵结构元素（Mat）
     *      OpenCV 在 Imgproc 包中，提供了 getStructuringElement 的函数，来方便创建腐蚀/膨胀的核
     *
     * getStructuringElement 原型方法:
     *      getStructuringElement(int shape, Size ksize, Point anchor)
     *      getStructuringElement(int shape, Size ksize)
     *
     *      参数：
     *          shape : Integer 核的结构类型
     *              -- C++ 有四种（多一个用户自定义），其他语言3种
     *              -- MORPH_RECT , 一个矩形结构元素
     *              -- MORPH_ELLIPSE , 一个椭圆结构元素
     *              -- MORPH_CROSS , 一个十字形结构元素
     *          ksize : Size 结构元素的大小
     *          anchor : Point 元素中瞄点的位置。默认值 (-1,-1)表示在元素的中心位置。注意：只有十字形结构元素依赖瞄点，其他形状类型仅仅影响结果的偏移。
     *
     *      原文：
     *          shape – Element shape that could be one of the following:
     *              MORPH_RECT - a rectangular structuring element
     *              MORPH_ELLIPSE - an elliptic structuring element, that is, a filled ellipse inscribed into the rectangle Rect(0, 0, esize.width, 0.esize.height)
     *              MORPH_CROSS - a cross-shaped structuring element
     *              CV_SHAPE_CUSTOM - custom structuring element (OpenCV 1.x API)
     *          ksize – Size of the structuring element.
     *          anchor – Anchor position within the element. The default value  (-1, -1) means that the anchor is at the center.
     *              Note that only the shape of a cross-shaped element depends on the anchor position.
     *              In other cases the anchor just regulates how much the result of the morphological operation is shifted.
     */


    /*
     * ------------------------------------------------------------------------------------------------------------
     *
     * 腐蚀
     *
     * 1. 腐蚀说明：
     *      图像的一部分区域与指定的核进行卷积，求核的最`小`值并赋值给指定区域。
     *      腐蚀可以理解为图像中`高亮区域`的'领域缩小'。
     *      意思是高亮部分会被不是高亮部分的像素侵蚀掉，使高亮部分越来越少。
     *
     * 2. 腐蚀函数(erode)
     *      erode 有3个原型方法
     *
     *      erode(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue)
     *      erode(Mat src, Mat dst, Mat kernel, Point anchor, int iterations)
     *      erode(Mat src, Mat dst, Mat kernel)
     *
     *      参数：
     *          src : Mat 输入图像 对通道数无要求，但是 depth 必须是 CV_8U、CV_16U、CV_16S、CV_32F、CV_64F 之一
     *          dst : Mat 输出图像，与原图以上的尺寸与类型
     *          kernel : Mat 膨胀操作的核 ， null 时表示以当前像素为中心 3x3 为单位的核
     *                  一般使用函数 Imgproc.getStructuringElement 来创建核。该函数会返回指定形状或尺寸的矩阵结构元素。
     *          anchor : Point 瞄点。根据 kernel（核），处理每个核的某个点。 (-1,-1)代表取这个核的中心位置。
     *          interations : Integer 迭代 dilate(膨胀)的次数，默认 1 。。
     *          borderType : Integer 推断图像外部像素的某种边界模式，一般不需要这个参数。
     *          borderValue : Scalar 当 borderType 值为常数时，区域的颜色一般不用管，
     *
     *          腐蚀，一般不需要borderType，borderValue，均有默认值。如果需要使用，可参考官网获取更多信息
     *
     * ------------------------------------------------------------------------------------------------------------
     */

    /**
     * 图像腐蚀处理
     * 不做任何处理的图片
     */
    @Test
    public void testErodeNomal(){
        Mat sourceImage = Imgcodecs.imread(p_test_file_path + "/shufa.png");
        //Mat sourceImage = Imgcodecs.imread(test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        // size 越小，腐蚀的单位越小，图片越接近原图
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(30,30));
        Mat outImage = new Mat();
        //开始腐蚀
        Imgproc.erode(sourceImage,outImage,structImage);
        this.saveImage(this.save_dir + "/image_process_erode_nomal.png",outImage);

    }

    /**
     * 图像腐蚀处理
     * 灰度处理的图片
     */
    @Test
    public void testErodeGray(){

        // 由于shufa.png  背景为白色，字体为黑色，在灰度的0-255显示范围，看不出变化
        // 所以我们换一个背景图不是白色的。
        Mat sourceImage = Imgcodecs.imread(p_test_file_path + "/shufa-1.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,new Size(30,30));
        Mat outImage = new Mat();

        Imgproc.erode(sourceImage,outImage,structImage);
        this.saveImage(this.save_dir + "/image_process_erode_gray.png",outImage);

    }

    /**
     * 图像腐蚀处理
     * 二值化处理的图片
     */
    @Test
    public void testErodeThreshold() {
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/shufa.png");

        //二值化处理   cv_8uc1 8位单通道格式
        Mat binaryMat = new Mat(sourceImage.height(), sourceImage.width(), CvType.CV_8UC1);
        Imgproc.threshold(sourceImage, binaryMat,100, 200, Imgproc.THRESH_BINARY);

        Mat outImage = new Mat();
        //图像腐蚀
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30,30));
        Imgproc.erode(binaryMat, outImage, structImage);

        this.saveImage(this.save_dir + "/image_process_erode_threshold.png",outImage);

    }

    /*
     * ------------------------------------------------------------------------------------------------------------
     *
     * 腐蚀
     *
     * 1. 腐蚀说明：
     *      图像的一部分区域与指定的核进行卷积，求核的最`大`值并赋值给指定区域。
     *      腐蚀可以理解为图像中`高亮区域`的'领域扩大'。
     *      意思是高亮部分会侵蚀不是高亮的部分，使高亮部分越来越多。
     *
     * 2. 腐蚀函数(dilate)
     *      dilate 有3个原型方法
     *
     *      dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue)
     *      dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations)
     *      dilate(Mat src, Mat dst, Mat kernel)
     *
     *      参数：
     *          src : Mat 输入图像 对通道数无要求，但是 depth 必须是 CV_8U、CV_16U、CV_16S、CV_32F、CV_64F 之一
     *          dst : Mat 输出图像，与原图以上的尺寸与类型
     *          kernel : Mat 膨胀操作的核 ， null 时表示以当前像素为中心 3x3 为单位的核
     *                  一般使用函数 Imgproc.getStructuringElement 来创建核。该函数会返回指定形状或尺寸的矩阵结构元素。
     *          anchor : Point 瞄点。根据 kernel（核），处理每个核的某个点。 (-1,-1)代表取这个核的中心位置。
     *          interations : Integer 迭代 dilate(膨胀)的次数，默认 1 。。
     *          borderType : Integer 推断图像外部像素的某种边界模式，一般不需要这个参数。
     *          borderValue : Scalar 当 borderType 值为常数时，区域的颜色一般不用管，
     *
     *          膨胀，一般不需要borderType，borderValue，均有默认值。如果需要使用，可参考官网获取更多信息
     *
     * ------------------------------------------------------------------------------------------------------------
     */

    /**
     * 图像膨胀处理
     * 不做任何处理的图片
     */
    @Test
    public void testDilateNomal(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/shufa.png");

        Mat outImage = new Mat();
        //图像腐蚀
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30,30));
        Imgproc.dilate(sourceImage, outImage, structImage);

        this.saveImage(this.save_dir + "/image_process_dilate_nomal.png",outImage);
    }

    /**
     * 图像膨胀处理
     * 灰度处理的图片
     */
    @Test
    public void testDilateGray(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/shufa-1.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat outImage = new Mat();
        //图像腐蚀
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30,30));
        Imgproc.dilate(sourceImage, outImage, structImage);

        this.saveImage(this.save_dir + "/image_process_dilate_gray.png",outImage);
    }

    /**
     * 图像膨胀处理
     * 二值化处理的图片
     */
    @Test
    public void testDilateThreshold() {

        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/shufa.png");

        //二值化处理   cv_8uc1 8位单通道格式
        Mat binaryMat = new Mat(sourceImage.height(), sourceImage.width(), CvType.CV_8UC1);
        Imgproc.threshold(sourceImage, binaryMat,100, 200, Imgproc.THRESH_BINARY);

        Mat outImage = new Mat();
        //图像腐蚀
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30,30));
        Imgproc.dilate(binaryMat, outImage, structImage);

        this.saveImage(this.save_dir + "/image_process_dilate_threshold.png",outImage);

    }

    /**
     * 查找条形码案例
     *
     * 步骤：
     * 1. 读取灰值图
     * 2. 图像模糊，降噪
     * 3. 图像二值化
     * 4. 腐蚀图像，通过腐蚀过滤掉不是竖线的的区域
     * 5. 膨胀图像，将腐蚀过的线条数据通过膨胀放大
     * 6. 继续用矩形核膨胀图像，使线条链接成矩形图像
     * 7. 查找轮廓
     * 8. 对比所有轮廓，过滤掉宽度小于200，偏斜角<2度的矩形图像
     * 9. 找到图像并截取
     */
    @Test
    public void testFindLineCode() {

        //读取灰值图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/tiaoma.png",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        //图像高斯模糊
        Mat gsMat = Mat.ones(sourceImage.size(),sourceImage.type());
        Imgproc.GaussianBlur(sourceImage,gsMat,new Size(5,5),0,0);
        this.saveImage(this.save_dir + "/image_process_dilate_tiaoma-1.png",gsMat);

        //图像二值化，adaptiveThreshold 后面再二值化的专题里讲解
        Mat thresh_image = Mat.ones(sourceImage.size(),sourceImage.type());
        // C 负数，取反色，超过阈值的为黑色，其他为白色
        Imgproc.adaptiveThreshold(gsMat, thresh_image,255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7,-2);
        this.saveImage(this.save_dir + "/image_process_dilate_tiaoma-2.png",thresh_image);

        //创建输出图像，后续的操作都是这个图像
        Mat outImage = new Mat();

         /*
          * 图像腐蚀操作
          *
          * width=2 ，height=20 在腐蚀的时候，排除大多数细的垂直线。
          * 这个参数不易设置过大，可以通过多次腐蚀来达到排除的效果。
          *
          * ----------------------------------------------------------------------------------
          * 注意 width、height 需根据图像的大小来设置。
          *
          * 比如我这里的示例图片大小是 1271(width)x648(height)。 我通过比较20是比较理想的值。
          * 但是20 并比适用比示例图小或大的图像。所以在设置这个参数前，需要根据图像大小来调整。
          *
          * && 这个值没有固定、动态的大小，不要期望自动设置，除非接入 AI 来学习。 &&
          *
          * 但是我们加入到自己工程的业务里，我们处理的图片通常都是固定的几个图像大小
          *
          * 比如摄像头获取图像，可以指定 500x500
          * 比如扫描仪获取图像，可以指定 KPI 大小，那么获取到的同等材质（如 A4大小）的数码图片大小也是一样的。
          * 比如数码照片，在不同模式下照片大小是一致的，可以根据图片信息分类。
          *
          * 所以，自己根据自己业务经常处理的图片来设置一个比例参数 ，通过 sourceImage.heigth()*xParam 来获取这个参数即可。
          * ----------------------------------------------------------------------------------
          */
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,20));
        Imgproc.erode(thresh_image, outImage, structImage,new Point(-1,-1),3);  //腐蚀了3次
        this.saveImage(this.save_dir + "/image_process_dilate_tiaoma-3.png",outImage);

        // 在用腐蚀的图像，进行膨胀，将线条加粗
        Imgproc.dilate(outImage, outImage, structImage,new Point(-1,-1),3);     // 这里同样进行了3次膨胀
        this.saveImage(this.save_dir + "/image_process_dilate_tiaoma-4.png",outImage);

        /*
         * 这里我划定了一个10x5 的矩形整列。来将剩余的线条通过膨胀连成区域。
         * 范围同样不宜过大或过小。
         * 过大：膨胀区域增大，最终结果图像干扰太多
         * 过小：膨胀区域减少，可满足的条件过多，造成不容易连成一个整理区域。
         */
        // 再次膨胀，使其连成区域
        structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10,5));
        Imgproc.dilate(outImage, outImage, structImage,new Point(-1,-1),3);
        this.saveImage(this.save_dir + "/image_process_dilate_tiaoma-5.png",outImage);


        /*
         * findContours 找轮廓
         *
         * 原型方法：
         *
         * findContours(Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method, Point offset)
         *
         * image : Mat 是输入图像，图像的格式是8位单通道的图像，并且被解析为二值图像（即图中的所有非零像素之间都是相等的）。
         * coutours : List<MatOfPoint> 输出的轮廓数组，所有找到的轮廓都会放在这个数组中。MatOfPoint代表这个对象存储了轮廓的`点`数据
         * hierarchy : Mat 这个参数可以指定，也可以不指定。
         *              如果指定的话，输出hierarchy，将会描述输出轮廓树的结构信息。
         *              0号元素表示下一个轮廓（同一层级）；
         *              1号元素表示前一个轮廓（同一层级）；
         *              2号元素表示第一个子轮廓（下一层级）；
         *              3号元素表示父轮廓（上一层级）
         * mode : Integer 轮廓的模式，将会告诉OpenCV你想用何种方式来对轮廓进行提取，有四个可选的值：
         *      CV_RETR_EXTERNAL （0）：表示只提取最外面的轮廓；
         *      CV_RETR_LIST （1）：表示提取所有轮廓并将其放入列表；
         *      CV_RETR_CCOMP （2）:表示提取所有轮廓并将组织成一个两层结构，其中顶层轮廓是外部轮廓，第二层轮廓是“洞”的轮廓；
         *      CV_RETR_TREE （3）：表示提取所有轮廓并组织成轮廓嵌套的完整层级结构。
         * method : Integer 轮廓如何呈现的方法，有三种可选的方法：
         *      CV_CHAIN_APPROX_NONE （1）：将轮廓中的所有点的编码转换成点；
         *      CV_CHAIN_APPROX_SIMPLE （2）：压缩水平、垂直和对角直线段，仅保留它们的端点；
         *      CV_CHAIN_APPROX_TC89_L1  （3）or CV_CHAIN_APPROX_TC89_KCOS（4）：应用Teh-Chin链近似算法中的一种风格
         * offset : Point 可选，如果指定了点偏移，那么返回的轮廓中的所有点均作指定量的偏移
         */
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(outImage,contours,hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));

        // 根据轮廓可以找到的形状数组
        // Rect[] boundRect = new Rect[contours.size()];

        // 循环找到的所有轮廓
        for(int i = 0; i < contours.size();i++) {

            //将轮廓保存为区域
            // boundRect[i] = Imgproc.boundingRect(contours.get(i));

            // System.out.println(boundRect[i].tl());
            // System.out.println(boundRect[i].br());

            // 获取轮廓内，最小外包矩形
            RotatedRect min = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));

            //偏转角度
            if(min.angle < 2){

                //获取一个矩形
                Rect minRect = min.boundingRect();

                //将宽度<200的排除
                if( ( minRect.br().x - minRect.tl().x ) > 200 ){

                    //截取
                    Mat code = sourceImage.submat(minRect);
                    this.saveImage(this.save_dir + "/image_process_dilate_tiaoma-code-"+i+".png",code);

                }

                //在原图上把该矩形表示出来
                Imgproc.rectangle(sourceImage, minRect.tl(), minRect.br(), new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, 0);

            }

        }

        //输出原图
        this.saveImage(this.save_dir + "/image_process_dilate_tiaoma-6.png",sourceImage);

    }


}
