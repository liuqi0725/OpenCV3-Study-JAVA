package com.liuqi.ocr.ex;

/**
 * @Author : alexliu
 * @Description : something do..
 * @Date : Create at 下午6:37 2018/1/14
 */
public class NoSupportFileTypeException extends Exception{

    public NoSupportFileTypeException(){
        super();
    }

    public NoSupportFileTypeException(String msg){
        super(msg);
    }

}
