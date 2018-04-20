package opencv;

import com.liuqi.opencv.base.OpenCVProcessBase;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * @Author : alexliu
 * @Description : 主要学习<br/>
 * 1. 画椭圆 <br/>
 * 2. 画实心圆 <br/>
 * 3. 画线 <br/>
 * 4. 画矩形 <br/>
 * 5. 结合例子 <br/>
 */
public class ProcessTest_4 extends OpenCVProcessBase {

    private String save_dir = "study-output/study-opencv-4";


    /**
     * 在图像上画一个椭圆(ellipse)
     *
     * 构造函数 ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness, int lineType, int shift)
     *
     * img : 类型 Mat ， 输入图像
     * center : 类型 Point ， 圆心坐标。比如你想把图像画在输入图像的中间， 取 new Point(img.width/2   , img.height/2) 即可
     * axes : 类型 Size ，图像以中心点从最小角度开始延伸出去的轴长。最短为 width，最大为 height，如果 width、height 相等，并且角度0-360则画出来的图像为圆形
     * angle : 倾斜角度
     * startAngle : 开始角度
     * endAngle : 结束角度
     * color : 线条颜色
     * thickness : 线宽度
     * lineType : 线条类型 4,8,16   Imgproc.LINE_<str>
     *     -- Imgproc.LINE_4   值 4 ，4连通线性
     *     -- Imgproc.LINE_8   值 8 ，8连通线性
     *     -- Imgproc.LINE_AA  值 16，抗锯齿线性
     * shift : 圆心坐标点和数轴的精度
     *
     * 对角度的理解可以参考 （https://blog.csdn.net/gxiaob/article/details/9396955）
     */
    @Test
    public void drawEllipse() {

        Mat sourceImage = new Mat(500,500,CvType.CV_8UC3,Scalar.all(255));

        Imgproc.line(sourceImage ,
                new Point(0,sourceImage.height()/2),
                new Point(sourceImage.width(), sourceImage.height()/2),
                Scalar.all(0),
                1,
                8,
                0
        );

        Imgproc.line(sourceImage ,
                new Point(sourceImage.width()/2,0),
                new Point(sourceImage.width()/2, sourceImage.height()),
                Scalar.all(0),
                1,
                8,
                0
        );

        Imgproc.ellipse(sourceImage,
                new Point(sourceImage.width()/2, sourceImage.height()/2),
                new Size(50, 100),
                20,//偏移不算
                -170,
                -10,
                new Scalar(255, 129, 0),
                2,
                Imgproc.LINE_8,
                0
        );

        this.saveImage(this.save_dir + "/image_draw_ellipse.png",sourceImage);

    }

    /**
     * 画实心圆
     *
     * circle(Mat img, Point center, int radius, Scalar color, int thickness, int lineType, int shift)
     * img : 类型 Mat ， 输入图像
     * center : 类型 Point ， 圆心坐标。比如你想把图像画在输入图像的中间， 取 new Point(img.width/2   , img.height/2) 即可
     * radius : 圆的半径长度
     * color  : 圆的颜色
     * thickness : 描边线宽， -1为没有描边
     * lineType  : 线类型，画圆，建议取 LINE_AA 抗锯齿
     * shift : 圆心坐标点和数轴的精度 填 0 即可
     */
    @Test
    public void drawFilledCircle(){

        Mat sourceImage = Mat.ones(new Size(500,500),CvType.CV_8UC3);
        sourceImage.setTo(Scalar.all(255));

        Imgproc.circle(sourceImage,
                new Point(sourceImage.width()/2, sourceImage.height()/2),
                50,
                    Scalar.all(0),
                -1,
                    Imgproc.LINE_AA,
                0
                );

        this.saveImage(this.save_dir + "/image_draw_filledCircle.png",sourceImage);

    }

    /**
     * 画实心与画椭圆结合画一个图形
     */
    @Test
    public void drawLogo(){

        //定义画布
        Mat canvas = Mat.ones(new Size(500,500),CvType.CV_8UC3);
        canvas.setTo(Scalar.all(255));

        //定义圆心
        Point center = new Point(canvas.width()/2 , canvas.height()/2);

        //画圆心
        Imgproc.circle(canvas,
                center,
                20,
                Scalar.all(0),
                -1,
                Imgproc.LINE_AA,
                0
        );

        //画椭圆，分别画 倾斜 90 ，45 ， 0 ，-45
        Imgproc.ellipse(canvas,
                center,
                new Size(50, 100),
                90,//偏移不算
                0,
                360,
                new Scalar(255, 129, 0),
                2,
                Imgproc.LINE_8,
                0
        );

        Imgproc.ellipse(canvas,
                center,
                new Size(50, 100),
                45,//偏移不算
                0,
                360,
                new Scalar(255, 129, 0),
                2,
                Imgproc.LINE_8,
                0
        );

        Imgproc.ellipse(canvas,
                center,
                new Size(50, 100),
                0,//偏移不算
                0,
                360,
                new Scalar(255, 129, 0),
                2,
                Imgproc.LINE_8,
                0
        );

        Imgproc.ellipse(canvas,
                center,
                new Size(50, 100),
                -45,//偏移不算
                0,
                360,
                new Scalar(255, 129, 0),
                2,
                Imgproc.LINE_8,
                0
        );


        this.saveImage(this.save_dir + "/image_draw_logo.png",canvas);


    }


}
