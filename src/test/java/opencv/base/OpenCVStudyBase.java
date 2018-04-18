package opencv.base;

import com.liuqi.core.opencv.OpenCVHandler;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

/**
 * @Author : alexliu
 * @Description : something do..
 * @Date : Create at 下午2:16 2018/4/11
 */
public class OpenCVStudyBase {

    static {
        //加载OpenCV 动态链接库
        OpenCVHandler.loadLibraries();
    }

    /**
     * 测试文件路径
     */
    protected String p_test_file_path = System.getProperty("user.dir") + File.separator + "testFiles/src";

    /**
     * 保存文件到测试目录
     * @param path
     * @param image
     */
    protected void saveImage(String path,Mat image){

        String outPath = this.p_test_file_path + File.separator + path;

        File file = new File(outPath);
        //目录是否存在
        this.dirIsExist(file.getParent());

        //opencv的写出文件
        Imgcodecs.imwrite(outPath,image);

    }


    private void dirIsExist(String dirPath){
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

}
