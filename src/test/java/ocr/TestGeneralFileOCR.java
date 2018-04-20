package ocr;

import com.liuqi.ocr.core.OcrProcess;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @Author : alexliu
 * @Description : 测试一般文件 OCR
 * @Date : Create at 上午10:28 2018/1/26
 */
public class TestGeneralFileOCR {

    private static String log4jxml = "/Users/alexliu/DEV/InteJ-JAVA/NunaOCR/src/main/resources/log4j2.xml";

    @Before
    public void before() throws FileNotFoundException {
        File config=new File(log4jxml);
        ConfigurationSource source = new ConfigurationSource(new FileInputStream(config),config);
        Configurator.initialize(null, source);
    }

    @Test
    public void test_JPG_Linux(){
        String tessPath = "/Users/alexliu/DEV/InteJ-JAVA/NunaOCR/Tesseract-OCR";
        String img = "/Users/alexliu/Desktop/dam/test/0086.jpg";

        OcrProcess ocr = new OcrProcess(tessPath);

        //测试保存其他目录
//        try {
//            ocr.setSaveDir("/Users/alexliu/Desktop/dam");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        File file = new File(img);
        try {
            String path = ocr.doOCR(file, "chi_sim");
            System.out.println(path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test_TIFF_Linux(){
        String tessPath = "/Users/alexliu/DEV/InteJ-JAVA/NunaOCR/Tesseract-OCR";
        String img = "/Users/alexliu/Desktop/dam/test/0086.tiff";

        OcrProcess ocr = new OcrProcess(tessPath);
        File file = new File(img);
        try {
            String path = ocr.doOCR(file, "chi_sim");
            System.out.println(path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test_JPG_Windows(){

        String tessPath = "D:\\IdeaWorkPlace\\DeepCaptureDAM\\src\\main\\webapp\\Tesseract-OCR";
        String img = "C:\\Users\\Administrator\\Desktop\\TestData\\0001.jpg";

        OcrProcess ocr = new OcrProcess(tessPath);
        //测试保存其他目录
        try {
            ocr.setSaveDir("C:\\Users\\Administrator\\Desktop");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File(img);
        try {
            String path = ocr.doOCR(file, "chi_sim");
            System.out.println(path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
