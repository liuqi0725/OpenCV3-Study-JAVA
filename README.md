# OpenCV3-Study-JAVA

> 网上 JAVA 学习 OpenCV 的知识太少，自己通过学习把整理的示例，参数说明放在这里，大家一起互相学习。


以下内容会不定期更新，更新周期最多不会超过3天。直至更新完毕。

## 开发环境及IDE
+ JDK 1.8
+ OpenCV 3.4
+ MacOS Sierra 10.12.4
+ IDEA 2017

## 主要参考：

+ [官方文档](http://opencv-java-tutorials.readthedocs.io/en/latest/)
+ [OpenCV 3.0 Computer Vision with Java](http://pdf.th7.cn/down/files/1602/OpenCV%203.0%20Computer%20Vision%20with%20Java.pdf)
+ [OpenCV3 编程入门](https://www.86mall.com/item/520404025009.html?p=5074&m=ae05433eb15809bfcb7a9a7109f64d9e)


## OpenCV 安装
+ [MacOS 安装 OpenCV JAVA 版](https://my.oschina.net/u/3767256/blog/1614886)
+ MacOS 安装 OpenCV Python3 版（暂无）
+ Window 安装 OpenCV JAVA 版（暂无）
+ Window 安装 OpenCV Python3 版（暂无）

## 一、OpenCV3 函数学习

**所有示例均放在 `src/test/java` 目录下**.

### 1. StudyTest_1

1. 创建 Mat 对象，Mat 对象的参数介绍
2. 通过Matlab 创建 Mat 对象
3. 创建彩色图像
4. 创建透明图像
5. 期望值来创建随机的初始化矩阵图像

[代码链接](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/StudyTest_1.java)

### 2. StudyTest_2

1. 图像的读取
2. 对 ROI 区域描边
3. 截取 ROI 区域
4. 用图片在原始图片上划定 ROI 区域，并替换

[代码链接](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/StudyTest_2.java)

### 3. StudyTest_3

1. resize 图像缩放<br/>
2. rect 矩形对象<br/>
3. cvtColor 颜色空间转化转换<br/>

[代码链接](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/StudyTest_3.java)

### 4. StudyTest_4

1. 画椭圆
2. 画实心圆
3. 画线
4. 画矩形
5. 结合例子

[代码链接](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/StudyTest_4.java)


### 5. StudyTest_5
1. 图像空间压缩（如何还原还没处理）

[代码链接](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/StudyTest_5.java)


### 6. StudyTest_6

1. 图像线性混合addWeighted
2. 图像通道的拆分、合并
3. 离散傅里叶变换 DFT

[代码链接](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/StudyTest_6.java)


### 7. StudyTest_7
1. 3种线性滤波
    + BoxBlur -- 方框滤波   
    + Blur -- 均值滤波
    + GaussianBlur -- 高斯滤波
2. 2种非线性滤波
    + medianBlur -- 中值滤波
    + BilateralFilter -- 双边滤波
    
[代码链接](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/StudyTest_7.java)


### 8. StudyTest_8
1. 图像腐蚀
2. 图像膨胀
3. 图像边缘检测

[代码链接](https://github.com/liuqi0725/OpenCV3-Study-JAVA/blob/master/src/test/java/opencv/StudyTest_8.java)


### 9. StudyTest_9
...待续

## 二、案例实践

### 1. 表格边缘检测

### 2. 表格数据结构化

### 3. 图像中二维码识别

### 4. 其他

> 第二部分之前已经实现了一点，但是效果并不好，原理是不晓得 OpenCV 函数的定义。
还是静下心来先从基础开始把OpenCV 基础的内容学习了，再来做整合，后续会更新代码。
