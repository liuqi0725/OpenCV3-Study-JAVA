package com.liuqi.app.ocr.common;

/**
 * @Author : alexliu
 * @Description : something do..
 * @Date : Create at 下午6:26 2018/1/14
 */
public class OcrTools {

    public static Integer getFileType(String filename){

        String fileSuffix = filename.substring(filename.lastIndexOf(".")+1);

        if(fileSuffix.equalsIgnoreCase("bmp")){
            return OcrConstants.FILE_TYPE_IMAGE_BMP;
        }else if(fileSuffix.equalsIgnoreCase("tif")){
            return OcrConstants.FILE_TYPE_IMAGE_TIF;
        }else if(fileSuffix.equalsIgnoreCase("jpg") || fileSuffix.equalsIgnoreCase("jpeg")
                || fileSuffix.equalsIgnoreCase("png") ){
            return OcrConstants.FILE_TYPE_IMAGE;
        }

        return null;
    }

    public static long createProcessId(){
        return System.nanoTime() >> 1;
    }
}
