# OpenCV3-Study-JAVA

> 网上 JAVA 学习 OpenCV 的知识太少，自己通过学习把整理的示例，参数说明放在这里，大家一起互相学习。


<br/>



## 开发环境及IDE
+ JDK 1.8
+ OpenCV 3.4
+ MacOS Sierra 10.12.4
+ IDEA 2017

## 主要参考：

+ [官方 OpenCV 3.0 开发文档](https://docs.opencv.org/3.0-beta/index.html)
+ [官方 OpenCV 3.0 JAVA 教程](http://opencv-java-tutorials.readthedocs.io/en/latest/)
+ [OpenCV 3.0 Computer Vision with Java](http://pdf.th7.cn/down/files/1602/OpenCV%203.0%20Computer%20Vision%20with%20Java.pdf)
+ [OpenCV3 编程入门](https://book.douban.com/subject/26320896)


## OpenCV 安装
+ [MacOS 安装 OpenCV JAVA 版](https://35liuqi.com/2018/01/29/macOS%20%E5%AE%89%E8%A3%85%20opencv3.4/)
+ MacOS 安装 OpenCV Python3 版（暂无）
+ Window 安装 OpenCV JAVA 版（暂无）
+ Window 安装 OpenCV Python3 版（暂无）

## 一、章节目录

+--------------------------------------------------------------------------+<br/>
          **所有示例均放在 `src/test/java/opencv/study` 目录下**               <br/>
+--------------------------------------------------------------------------+<br/>


1. [StudySection_1 ----------- 认识 Mat 对象，创建图像矩阵](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_1.java)
    
    * 创建 Mat 对象，Mat 对象的参数介绍
    * 通过Matlab 创建 Mat 对象
    * 创建彩色图像
    * 创建透明图像
    * 期望值来创建随机的初始化矩阵图像
2. [StudySection_2 ----------- 图像的读取、图像 ROI 区域的使用](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_2.java)

    * 图像的读取
    * 对 ROI 区域描边
    * 截取 ROI 区域
    * 用图片在原始图片上划定 ROI 区域，并替换
3. [StudySection_3 ----------- 矩形、颜色空间转化](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_3.java)
4. [StudySection_4 ----------- 绘图像(直线、矩形、圆形、椭圆等)](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_4.java)

    * 画椭圆
    * 画实心圆
    * 画线
    * 画矩形
    * 结合例子
5. [StudySection_5 ----------- 图像空间压缩](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_5.java)
6. [StudySection_6 ----------- 图像线性混合、通道拆分合并、离散傅里叶变换](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_6.java)
7. [StudySection_7 ----------- 图像滤波](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_7.java)
    
    + 3种线性滤波
        + BoxBlur -- 方框滤波   
        + Blur -- 均值滤波
        + GaussianBlur -- 高斯滤波
    + 2种非线性滤波
        + medianBlur -- 中值滤波
        + BilateralFilter -- 双边滤波
8. [StudySection_8 ----------- 图像腐蚀(erode)、膨胀(dilate)、查找条形码案例。](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_8.java)
9. [StudySection_9 ----------- 形态学滤波(开、闭运算、顶帽等)、识别图中二维码案例](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_9.java)
    
    + 对本章节内容学习深入不够，图像的专业知识0-较多，后续随着业务的深入再回头来深入理解
10. [StudySection_10 ----------- floodFill(漫水填充)、图片的放大缩小 pyrUp、pyrDown、Resize](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_10.java)
11. [StudySection_11 ----------- threshold 固定阈值操作、adaptiveThreshold 自适应阈值操作](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_11.java)
12. [StudySection_12 ----------- Canny 算法、Sobel 算法、Laplacian 算法、Scharr 滤波器](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/study/StudySection_12.java)

    + Sobel 、Laplacian 、Scharr 后续研究，看了教程与官网内容，属于边缘使用场景
       
+--------------------------------------------------------------------------+<br/>
         **完**            <br/>
+--------------------------------------------------------------------------+<br/>




## 二、案例实践

### 1. 表格边缘检测

### 2. 表格数据结构化

### 3. 其他

> 第二部分之前已经实现了一点，但是效果并不好，原理是不晓得 OpenCV 函数的定义。
还是静下心来先从基础开始把OpenCV 基础的内容学习了，再来做整合，后续会更新代码。
