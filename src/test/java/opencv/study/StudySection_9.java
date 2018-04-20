package opencv.study;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : alexliu
 * @Description : 主要学习形态学滤波<br/>
 * 1.开运算<br/>
 * 2.闭运算<br/>
 * 3.形态学梯度<br/>
 * 4.顶帽<br/>
 * 5.黑帽<br/>
 * 6.识别图中二维码<br/>
 */
public class StudySection_9 extends OpenCVProcessBase {

    /*
     * 上一节学习了膨胀 dilate、腐蚀 erode,这两种都是形态学操作的基本
     *
     * 这一节学习高级的形态学操作，如开、闭运算等。所有的形态学高级操作都是立足于腐蚀、膨胀这两个基本操作之上的。
     */

    private String save_dir = "study-output/study-opencv-9";

    /*
     * 高级形态学滤波函数
     *
     * OpenCV Imgproc 包提供了 morphologyEx。高级形态学滤波函数
     *
     * 官方说明 : https://docs.opencv.org/3.0-beta/modules/imgproc/doc/filtering.html?highlight=morphologyex#morphologyex
     *
     * 原型方法 :
     *      morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue)
     *      morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations)
     *      morphologyEx(Mat src, Mat dst, int op, Mat kernel)
     *
     *      参数 :
     *          src : Mat 输入图像 对通道数无要求，但是 depth 必须是 CV_8U、CV_16U、CV_16S、CV_32F、CV_64F 之一
     *          dst : Mat 输出图像，与原图以上的尺寸与类型
     *          op : Integer 形态学运算类型，表示当前用什么方式进行运算,常量放在 Imgproc 包下
     *              -- MORPH_OPEN       开运算
     *              -- MORPH_CLOSE      闭运算
     *              -- MORPH_GRADIENT   形态学梯度
     *              -- MORPH_TOPHAT     顶帽
     *              -- MORPH_BLACKHAT   黑帽
     *              -- MORPH_ERODE      腐蚀
     *              -- MORPH_DILATE     膨胀
     *          kernel : Mat 膨胀操作的核 ， null 时表示以当前像素为中心 3x3 为单位的核.
     *                  一般使用函数 Imgproc.getStructuringElement 来创建核。该函数会返回指定形状或尺寸的矩阵结构元素。
     *                  》》》 getStructuringElement上一节（StudyTest_8）有说过。 《《《
     *          anchor : Point 瞄点。根据 kernel（核），处理每个核的某个点。 (-1,-1)代表取这个核的中心位置。
     *          iterations : Integer 迭代 dilate(膨胀)的次数，默认 1
     *          borderType : Integer 推断图像外部像素的某种边界模式，一般不需要这个参数。
     *          borderValue : Scalar 当 borderType 值为常数时，区域的颜色一般不用管，
     *
     *          一般不需要borderType，borderValue，均有默认值。如果需要使用，可参考官网获取更多信息
     *
     */


    /**
     * 开运算
     *
     * Opening operation: dst = open(src,element) = dilate(erode(src,element))
     *
     * 可以看出开运算最终指向的就是 dilate(erode(src,element)) ,也就是先腐蚀，在膨胀的操作
     *
     * 可以理解，开运算就是先腐蚀再膨胀的封装。
     *
     * 开运算可以用来消除小物体，平滑较大物体的边界，同时也不改变其面积（因为腐蚀与膨胀次数相等，大面积的物体受影响小）
     */
    @Test
    public void testMORPH_OPEN(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/shuiguo.jpg");

        Mat outImage = new Mat();
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,20));

        //可以设置次数iterations，他会先腐蚀iterations次，再膨胀iterations次
        Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_OPEN ,structImage);
        this.saveImage(this.save_dir + "/image_process_open_1.png",outImage);

        Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_OPEN ,structImage,new Point(-1,-1),3);
        this.saveImage(this.save_dir + "/image_process_open_2.png",outImage);
    }

    /**
     * 闭运算
     * Closing operation: dst = close(src,element) = erode(dilate(src,element))
     *
     * 理解了开运算，那么闭运算就是先膨胀，再腐蚀。
     *
     * 可以理解，闭运算就是先膨胀再腐蚀的封装。
     *
     * 闭运算可以排除很多黑色区域
     */
    @Test
    public void testMORPH_CLOSE(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/shuiguo.jpg");
        Mat outImage = new Mat();
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(20,20));
        //可以设置次数iterations，他会膨胀先iterations次，再腐蚀iterations次
        Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_CLOSE ,structImage);
        this.saveImage(this.save_dir + "/image_process_close_1.png",outImage);

        Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_OPEN ,structImage,new Point(-1,-1),3);
        this.saveImage(this.save_dir + "/image_process_close_2.png",outImage);

    }

    /**
     * 形态学梯度
     * Morphological gradient: dst = morph_grad(src,element) = dilate(src,element) - erode(src,element)
     *
     * 可以理解为 形态学梯度 = 膨胀结果 - 腐蚀结果 的差
     *
     * 对二值化图像操作，可以使团块（一块区域的图像）边缘突出从而达到保留物体边缘轮廓
     */
    @Test
    public void testMORPH_GRADIENT(){

        List<Mat> matList = new ArrayList<Mat>();

        matList.add(Imgcodecs.imread(this.p_test_file_path + "/tiaoma.png"));
        matList.add(Imgcodecs.imread(this.p_test_file_path + "/shufa.png"));

        for(int i=0; i< matList.size(); i++){

            Mat sourceImage = matList.get(i);

            Mat outImage = new Mat();
            Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,20));

            Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_GRADIENT ,structImage);
            this.saveImage(this.save_dir + "/image_process_gradient_"+i+"_1.png",outImage);

            Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_GRADIENT ,structImage,new Point(-1,-1),3);
            this.saveImage(this.save_dir + "/image_process_gradient_"+i+"_2.png",outImage);

            //二值化图像处理
            Mat binaryMat = new Mat(sourceImage.height(), sourceImage.width(), CvType.CV_8UC1);
            Imgproc.threshold(sourceImage, binaryMat,100, 255, Imgproc.THRESH_BINARY);

            Imgproc.morphologyEx(binaryMat, outImage, Imgproc.MORPH_GRADIENT ,structImage);
            this.saveImage(this.save_dir + "/image_process_gradient_"+i+"_3.png",outImage);

            Imgproc.morphologyEx(binaryMat, outImage, Imgproc.MORPH_GRADIENT ,structImage,new Point(-1,-1),3);
            this.saveImage(this.save_dir + "/image_process_gradient_"+i+"_4.png",outImage);

        }

    }

    /**
     * 顶帽
     *
     * Top hat : dst = tophat(src, element)= src-open(src,element)
     *
     * 顶帽值 = 原图像 - 开运算（先腐蚀再膨胀）之差。
     *
     * 顶帽运算的效果是突出比原轮廓周围区域更明亮的区域，这一操作与核的大小有关。
     *
     * 在一副图像具有大幅背景，而微小物品比较有规律的情况下，可以使用顶帽运算进行背景提取
     */
    @Test
    public void testMORPH_TOPHAT(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/caoyuan.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Mat outImage = new Mat();
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,5));
        //可以设置次数iterations，他会膨胀先iterations次，再腐蚀iterations次
        Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_TOPHAT ,structImage);
        this.saveImage(this.save_dir + "/image_process_tophat_1.png",outImage);

        Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_TOPHAT ,structImage,new Point(-1,-1),3);
        this.saveImage(this.save_dir + "/image_process_tophat_2.png",outImage);
    }

    /**
     * 黑帽
     *
     * Black hat : dst = blackhat(src, element) = close(src,element) - src
     *
     * 黑帽值 = 闭运算（先膨胀再腐蚀）- 原图像 之差。
     *
     * 顶帽运算的效果是突出比原轮廓周围区域更暗的区域，这一操作与核的大小有关。
     *
     * 在分离比邻近点暗一些的斑块，有着非常完美的轮廓
     */
    @Test
    public void testMORPH_BLACKHAT(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/shufa.png");
        Mat outImage = new Mat();
        Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30,30));
        //可以设置次数iterations，他会膨胀先iterations次，再腐蚀iterations次
        Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_BLACKHAT ,structImage);
        this.saveImage(this.save_dir + "/image_process_blackhat_1.png",outImage);

        Imgproc.morphologyEx(sourceImage, outImage, Imgproc.MORPH_BLACKHAT ,structImage,new Point(-1,-1),3);
        this.saveImage(this.save_dir + "/image_process_blackhat_2.png",outImage);
    }


    @Test
    public void testFindQRcode(){
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/yingye.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat blurMat = Mat.ones(sourceImage.size(),sourceImage.type());
        // 图像的噪点不是特变强烈的
        // 采用中值滤波，平滑过渡噪点
        Imgproc.medianBlur(sourceImage,blurMat,9);
        this.saveImage(this.save_dir + "/image_process_findqrcode_1.png",blurMat);

        //二值化
        Mat thresh_image = Mat.ones(sourceImage.size(),sourceImage.type());
        // C 负数，取反色，超过阈值的为黑色，其他为白色
        Imgproc.adaptiveThreshold(blurMat, thresh_image,255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7,-2);
        this.saveImage(this.save_dir + "/image_process_findqrcode_2.png",thresh_image);

        // 创建形态滤波的核，二值化后，高亮部分为白色，所以范围要小
        Mat structElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT , new Size(3,3));

        Mat openMat = Mat.ones(thresh_image.size(),thresh_image.type());
        // 要消除文字等影响，只保留大区域，用开运算
        Imgproc.morphologyEx(thresh_image,openMat,Imgproc.MORPH_OPEN,structElement,new Point(-1,-1),4);
        this.saveImage(this.save_dir + "/image_process_findqrcode_3.png",openMat);

        // 膨胀操作
        Mat structElement_dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT , new Size(35,35));
        Mat dilateMat = Mat.ones(openMat.size(),openMat.type());
        Imgproc.dilate(openMat,dilateMat,structElement_dilate,new Point(-1,-1),3);
        this.saveImage(this.save_dir + "/image_process_findqrcode_4.png",dilateMat);

        // 找轮廓
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(dilateMat,contours,hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));

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

                System.out.println(i+" >> "+( minRect.br().x - minRect.tl().x ) / ( minRect.br().y - minRect.tl().y ));

                // 二维码是正方形
                // 高、宽 均超过200
                // 高、宽比 在0.9 ~ 1.1以内
                if( ( minRect.br().x - minRect.tl().x ) > 200 &&
                        ( minRect.br().y - minRect.tl().y ) > 200 &&
                        ( minRect.br().x - minRect.tl().x ) / ( minRect.br().y - minRect.tl().y ) > 0.9 &&
                        ( minRect.br().x - minRect.tl().x ) / ( minRect.br().y - minRect.tl().y ) < 1.1 ){

                    //截取
                    Mat code = sourceImage.submat(minRect);
                    File codeFile = this.saveImage(this.save_dir + "/image_process_findqrcode_5-code-"+i+".png",code);

                    //解析
                    this.testDecode(codeFile.getAbsolutePath());

                }

                //在原图上把该矩形表示出来
                Imgproc.rectangle(sourceImage, minRect.tl(), minRect.br(), new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, 0);

            }

        }

        //输出原图
        this.saveImage(this.save_dir + "/image_process_findqrcode_6.png",sourceImage);

    }

    /**
     * 解析图像
     */
    private void testDecode(String filePath) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
            System.out.println(result.getText());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

}
