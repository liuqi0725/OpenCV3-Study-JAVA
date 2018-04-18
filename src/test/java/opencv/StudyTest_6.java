package opencv;

import com.liuqi.core.opencv.OpenCVHandler;
import opencv.base.OpenCVStudyBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. 图像线性混合addWeighted<br/>
 * 2. 图像通道的拆分、合并<br/>
 * 3. 离散傅里叶变换 DFT<br/>
 */
public class StudyTest_6 extends OpenCVStudyBase{

    private String save_dest_dir = "study-output/study-opencv-6";


    /**
     * 图像线性混合处理
     * 类似图像的淡入淡出
     *
     * Core.addWeighted(Mat src1, double alpha, Mat src2, double beta, double gamma, Mat dst)
     *
     * src1 : Mat 合并图像-1
     * alpha : double 图像1的透明度
     * src2 : Mat 合并图像-2
     * beta : double 图像2的透明度
     * gamma : double 偏差，Scalar added to each sum，意思在融合时，每个像素点颜色的偏差计算阈值，0为无偏差，值越大，颜色偏差越大。
     * dst : Mat 输出图像
     *
     * src1 ,src2 必须相同通道，相同大小
     */
    @Test
    public void testAddWeighted(){
        Mat image_1 = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat image_2 = Imgcodecs.imread(this.p_test_file_path + "/face-1.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        System.out.println(image_1.size());
        System.out.println(image_2.size());

        //将图像1 , 图像2，转化为同等大小
        Mat image_roi_1 = image_1.submat(new Rect(0,0,400,400));
        Mat image_roi_2 = image_2.submat(new Rect(0,0,400,400));

        System.out.println(image_roi_1.size() + "," + image_roi_2.size());
        System.out.println(image_roi_1.channels() + "," + image_roi_2.channels());

        Mat dst = new Mat();
        Core.addWeighted(image_roi_1,0.5,image_roi_2,0.5,0,dst);

        this.saveImage(this.save_dest_dir + "/image_process_addWeighted.png",dst);

    }

    /**
     * 图像通道的拆分（split）、合并（merge）
     */
    @Test
    public void testMergeChannel(){
        Mat src = new Mat(5,5,CvType.CV_8UC3,new Scalar(20,12,30));

        System.out.println("-------------- before split ------------------");
        System.out.println(src.dump());

        List<Mat> channels = new ArrayList<Mat>();
        Core.split(src,channels);

        System.out.println("-------------- after split blue ------------------");
        System.out.println(channels.get(0).dump());
        System.out.println("-------------- after split green ------------------");
        System.out.println(channels.get(1).dump());
        System.out.println("-------------- after split red ------------------");
        System.out.println(channels.get(2).dump());

        Mat mergeMat = new Mat();
        Core.merge(channels,mergeMat);
        System.out.println("-------------- after merge ------------------");
        System.out.println(mergeMat.dump());

    }

    /**
     * 通道拆分利用的情况1
     *
     * 比如把图像添加到原图的特定通道内，然后再合并通道
     */
    @Test
    public void testAddImageInChannel(){

        // 读取彩色图
        Mat sourceImage = Imgcodecs.imread(this.p_test_file_path + "/5cent.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat logoImage = Imgcodecs.imread(this.p_test_file_path + "/logo-468-230.jpeg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        //分离通道
        List<Mat> channels = new ArrayList<Mat>();
        Core.split(sourceImage,channels);

        //将图像添加到蓝色通道
        Mat buleChannel = channels.get(0);

        try {
            OpenCVHandler.addImageInROI(buleChannel,logoImage,0,0);

            this.saveImage(this.save_dest_dir + "/image_process_split_merge_buleChannel.png",buleChannel);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Mat dst = new Mat();

        Core.merge(channels,dst);

        this.saveImage(this.save_dest_dir + "/image_process_split_merge.png",dst);

    }

    /**
     * 离散傅里叶变换
     *
     * 指在时域、频域上都呈现离散的形式。简单来说就是把图像分解成正弦、余弦两部分。
     * 也就是把图像从空间域，转换到频域。
     *
     * 频域里：
     * 高频部分代表图像的细节，纹理
     * 低频部分代表图像的轮廓
     *
     * 图像灰度值变换较大的叫高频，变换较小的是低频。
     * 一副图像，灰度值变换最大的自然是图像与边缘地带（白色背景等），所以高频也代表了边缘。反之则是细节和纹理
     *
     * 应用场景：
     * 图像增强、图像去燥、图像边缘检测、图像压缩、图像纠偏
     */
    @Test
    public void testDFT(){

        Mat src = Imgcodecs.imread(this.p_test_file_path + "/imageTextR.png",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        // OpenCV中的DFT采用的是快速算法，这种算法要求图像的尺寸是2、3和5的倍数时处理速度最快。
        // 所以需要用getOptimalDFTSize()找到最适合的尺寸，
        // 然后用copyMakeBorder()填充多余的部分。

        // 为了进行离散傅里叶变换，需要扩充图像，具体扩充多少，根据 getOptimalDFTSize 来获取
        int new_height = Core.getOptimalDFTSize(src.rows()); // 获取纵向扩充后的距离（高度）
        int new_width = Core.getOptimalDFTSize(src.cols()); // 获取横向扩充后的距离（宽度）

//        System.out.println(new_height + "," + src.rows());
//        System.out.println(new_width + "," + src.cols());

        Mat padded = new Mat();

        /*
         * 扩充图像边界
         * copyMakeBorder(Mat src, Mat dst, int top, int bottom, int left, int right, int borderType, Scalar value)
         * src : 原图
         * dst : 输出图像
         * top,bottom,left,right : 原图往上，下，左，右 分别扩充多少距离（border）
         * borderType : 扩充的 border 类型， 常用 Core.BORDER_CONSTANT
         * value : border 填充颜色
         */
        Core.copyMakeBorder(src,padded, 0,new_height - src.rows(), 0, new_width - src.cols() , Core.BORDER_CONSTANT , Scalar.all(0));

        this.saveImage(this.save_dest_dir + "/image_dft_1.jpg",padded);

        List<Mat> paddedMat_channels = new ArrayList<Mat>();
        List<Mat> new_paddedMat_channels = new ArrayList<Mat>();

        // 转化图像类型
        // 傅里叶变换结果的频域值远超出空间值范围，所以要将结果的频域值保存在 float 中，CvType.CV_32F（32Bite 的 float 类型刚好）
        padded.convertTo(padded,CvType.CV_32F);

        // 傅里叶变换结果为复数，也就是说原图像每个图像值，会有2个值（一个实部，一个虚部），所以要用2个通道来存储
        // 通道1 为 makeborder 后的图像 A
        // 通道2 创建一个 A 一样大小 并且 type 为 CV_32F的图像
        paddedMat_channels.add(padded);//实部
        paddedMat_channels.add(Mat.zeros(padded.size(),CvType.CV_32F));//虚部

        // DFT要分别计算实部和虚部，把要处理的图像作为输入的实部、一个全零的图像作为输入的虚部。dft()输入和输出应该分别为单张图像
        // 所以要先用merge()把实虚部图像合并，分别处于图像complexImage的两个通道内。计算得到的实虚部仍然保存在complexImage的两个通道内。
        Mat complexImage = new Mat();
        Core.merge(paddedMat_channels,complexImage);

        Core.dft(complexImage,complexImage);

        // 傅里叶变换后，拆分通道
        Core.split(complexImage,new_paddedMat_channels);

        // 将变换结果转化为幅值
        // Core.magnitude(Mat x,Mat y,Mat magnitude)
        // x 变换后的实部
        // y 变换后的虚部
        // magnitude 输出的幅值，同 x （实部）有同样的尺寸和类型
        Core.magnitude(new_paddedMat_channels.get(0),new_paddedMat_channels.get(1),new_paddedMat_channels.get(0));
        Mat mag = new_paddedMat_channels.get(0);
        this.saveImage(this.save_dest_dir + "/image_dft_2.jpg",mag);

        Core.add(Mat.ones(mag.size(), CvType.CV_32F), mag , mag);
        this.saveImage(this.save_dest_dir + "/image_dft_3.jpg",mag);

        // 1. 傅里叶变化后，高频值显示为白点，低频值显示为黑点。为了在屏幕上凸显高低变换的连续性，所以要用log函数把数值的范围缩小。
        // 2. 由于幅度的变化范围很大，而一般图像亮度范围只有[0,255]，容易造成一大片漆黑，只有几个点很亮。所以要用log函数把数值的范围缩小。
        Core.log(mag, mag);
        this.saveImage(this.save_dest_dir + "/image_dft_4.jpg",mag);

        // 奇数行 或 奇数列，进行频谱裁剪
        mag = mag.submat(new Rect(0, 0, mag.cols() & -2, mag.rows() & -2));
        this.saveImage(this.save_dest_dir + "/image_dft_5.jpg",mag);


        // 高频部分（白点）在中间，低频部分（黑点）在四个角。
        // 将图像拆分为4个象限， 通过互换位置，使低频部分在中间，频域原点（0，0）位于中心。
        int cx = mag.cols() / 2;
        int cy = mag.rows() / 2;

        Mat q0 = new Mat(mag, new Rect(0, 0, cx, cy));
        Mat q1 = new Mat(mag, new Rect(cx, 0, cx, cy));
        Mat q2 = new Mat(mag, new Rect(0, cy, cx, cy));
        Mat q3 = new Mat(mag, new Rect(cx, cy, cx, cy));

        Mat tmp = new Mat();
        q0.copyTo(tmp);
        q3.copyTo(q0);
        tmp.copyTo(q3);

        q1.copyTo(tmp);
        q2.copyTo(q1);
        tmp.copyTo(q2);

        // 归一化
        // 虽然用log()缩小了数据范围，但仍然不能保证数值都落在[0,255]之内，
        // 所以要先用normalize()规范化到[0,1]内，再用convertTo()把小数映射到[0,255]内的整数。
        // 结果保存在一幅单通道图像内：
        Core.normalize(mag, mag, 0, 255, Core.NORM_MINMAX);

        this.saveImage(this.save_dest_dir + "/image_dft_6.jpg",mag);

    }
}
