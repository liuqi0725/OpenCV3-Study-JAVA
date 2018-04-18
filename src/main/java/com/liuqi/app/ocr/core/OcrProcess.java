package com.liuqi.app.ocr.core;

import com.liuqi.app.ocr.common.OcrConstants;
import com.liuqi.app.ocr.common.OcrTools;
import com.liuqi.app.ocr.ex.NoSupportFileTypeException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OcrProcess {

	private static Logger logger = LogManager.getLogger(OcrProcess.class);

	private String tessPath = "";

	private String ocr_psm_num = "3";

	private String ocr_language = "eng";

	private String ocr_result_path = "";

	private String ocr_result_file_path = "";

	private final String OS = System.getProperty("os.name");

	private long procID = 0;

	/**
	 * 文本换行符
	 */
	private final String EOL = System.getProperty("line.separator");
	
	/**
	 * 当前系统，路径分割符
	 */
	private final String FIS = System.getProperties().getProperty("file.separator");
	
	/**
	 * 构造函数
	 * @param tessPath 本地 tesseract 路径
	 */
	public OcrProcess(String tessPath) {
		this.tessPath = tessPath;

		if(logger.isDebugEnabled()){
			logger.debug("OcrProcess Created .");
			logger.debug("OcrProcess Current OS >>> {} ",this.OS);
			logger.debug("OcrProcess User tessPath >>> {} ",this.tessPath);
		}else if(logger.isInfoEnabled()){
			logger.info("OcrProcess Created .");
		}
	}

	/**
	 * 设置 pagesegmode 1-10
	 * 0 = Orientation and script detection (OSD) only.
	 * 1 = Automatic page segmentation with OSD.
	 * 2 = Automatic page segmentation, but no OSD, or OCR.
	 * 3 = Fully automatic page segmentation, but no OSD. (Default)
	 * 4 = Assume a single column of text of variable sizes.
	 * 5 = Assume a single uniform block of vertically aligned text.
	 * 6 = Assume a single uniform block of text.
	 * 7 = Treat the image as a single text line.  // 识别内容为 横行， 可以提供单行文本的识别效率
	 * 8 = Treat the image as a single word.
	 * 9 = Treat the image as a single word in a circle.
	 * 10 = Treat the image as a single character.
	 * @param psm 参考 tesseract 文档
	 */
	public void setPageSegMode(Integer psm){

		if(logger.isDebugEnabled()){
			logger.debug("OcrProcess User set OCR Process PageSeqMode >>> {}",psm);
		}else if(logger.isInfoEnabled()){
			logger.info("OcrProcess User set OCR Process PageSeqMode >>> {}",psm);
		}

	    if(psm == null){
	        throw new IllegalArgumentException("param psm is null,will use default 3");
        }
        if(psm > 10 || psm < 0){
			throw new IllegalArgumentException("param psm only between 0 and 10,will use default 3");
		}

		this.ocr_psm_num = String.valueOf(psm);
    }

	/**
	 * 设置保存路径
	 * @param savePath
	 */
	public void setSaveDir(String savePath) throws FileNotFoundException {

		if(logger.isDebugEnabled()){
			logger.debug("OcrProcess User set OCR Process Result TXT SavePath >>> {}",savePath);
		}else if(logger.isInfoEnabled()){
			logger.info("OcrProcess User set OCR Process Result TXT SavePath >>> {}",savePath);
		}

		File saveDir = new File(savePath);

		if(!saveDir.exists()){
			throw new FileNotFoundException("the savePath is not found!");
		}

		this.ocr_result_path = savePath;

	}
	
	/**
	 * 
	 * @author alexliu
	 * @date：2017年11月22日 下午2:39:42
	 * @Description：文件 ocr
	 * @param file 文件
	 * @param language 语言  chi_sim ,eng
	 * @return 识别后文件路径
	 * @throws NoSupportFileTypeException
	 */
	public String doOCR(File file , String language) throws NoSupportFileTypeException {

		this.procID = OcrTools.createProcessId();

		if(logger.isDebugEnabled()){
			logger.debug("OcrProcess Begin. The file >>> [{}], the procID is [{}] .",file.getName(),this.procID);
		}else if(logger.isInfoEnabled()){
			logger.info("OcrProcess Begin. The file >>> [{}], the procID is [{}] . See More info PLZ set logger Debug or Trace.",file.getName(),this.procID);
		}

		String textSavePath = "";

		if(logger.isDebugEnabled()){
			logger.debug("[{}] OCR process info : FilePath >>> {}",this.procID,file.getAbsolutePath());
			logger.debug("[{}] OCR process info : FileName >>> {}",this.procID,file.getName());
			logger.debug("[{}] OCR process info : FileSize >>> {} KB",this.procID,(file.length() / 1024f));
			logger.debug("[{}] OCR process info : OCR Language >>> {}",this.procID,language);
		}

		this.ocr_language = language;

		//获取文件类型
		int fileType = this.getFileType(file.getName());

		if(logger.isDebugEnabled()){
			logger.debug("[{}] OCR process info : FileType >>> {}",this.procID,fileType);
		}

        if(fileType == OcrConstants.FILE_TYPE_IMAGE){
            if(logger.isDebugEnabled()){
				logger.debug("[{}] OCR process info : The FileType String >>> JPG or JPEG or PNG",this.procID);
			}
			textSavePath = callTesseractCommand(file);
        }

		if(fileType == OcrConstants.FILE_TYPE_IMAGE_BMP){
			if(logger.isDebugEnabled()){
				logger.debug("[{}] OCR process info : The FileType String >>> BMP",this.procID);
			}
			textSavePath = callTesseractCommand(file);
		}

		if(fileType == OcrConstants.FILE_TYPE_IMAGE_TIF){
			if(logger.isDebugEnabled()){
				logger.debug("[{}] OCR process info : The FileType String >>> TIFF",this.procID);
			}
			textSavePath = callTesseractCommand(file);
		}
		
		return textSavePath;
	}

	/**
	 * 获取输出路径
	 * 如果 用户调用了 `setSaveDir` ,那么 OCR 输出到用户设置的目录
	 * 如果 没有调用 `setSaveDir` ，那么 OCR 输出与识别文件同一目录
	 * @param sourceFile 原始文件
	 * @return
	 */
	private String getOutPutDir(File sourceFile){

		if(StringUtils.isEmpty(this.ocr_result_path)){
			return sourceFile.getAbsolutePath().substring(0,sourceFile.getAbsolutePath().lastIndexOf(this.FIS));
		}

		return this.ocr_result_path;
	}

	/**
	 * 获取文件类型
	 * @param fileName
	 * @return
	 * @throws NoSupportFileTypeException
	 */
	private Integer getFileType(String fileName) throws NoSupportFileTypeException {

		Integer type = OcrTools.getFileType(fileName);

		if(type == null){
			throw new NoSupportFileTypeException("["+fileName+"] , 无法识别的文件类型.");
		}

		return type;
	}

	/**
	 * 创建 ProcessBuilder
	 * @param sourceFile
	 * @return
	 */
	private ProcessBuilder createProcessBuilder(File sourceFile){

		ProcessBuilder pb = new ProcessBuilder();

		if(this.OS.startsWith("Mac OS") || this.OS.startsWith("Linux")){

			//设置命令行工作目录，Linux , Mac OS 设置在 tesseract 目录下
			//因为 tesseract 非安装模式，也没有添加到系统的环境变量中
			pb.directory(new File(this.tessPath));
			if(logger.isDebugEnabled()){
				logger.debug("[{}] OCR process info : Set ProcessBuilder working dir >>> {}",this.procID,this.tessPath);
			}

		}else if(this.OS.startsWith("Windows")){
			//设置命令行工作目录,windows 下设置在要解析的文件目录下
			pb.directory(sourceFile.getParentFile());
			if(logger.isDebugEnabled()){
				logger.debug("[{}] OCR process info : Set ProcessBuilder working dir >>> {}",this.procID,sourceFile.getParentFile());
			}
		}

		//输出错误日志流
		pb.redirectErrorStream(true);

		return pb;
	}

	/**
	 * 创建命令行
	 * @param sourceFile
	 * @return
	 */
	private List<String> createCommand(File sourceFile){

		List<String> cmd = new ArrayList<String>();

		//ocr 输出目录
		String result_out_dir = getOutPutDir(sourceFile);

		//ocr 命令输出文件名，没有后缀
		String ocr_result_filename = sourceFile.getName().substring(0 , sourceFile.getName().lastIndexOf("."));

		//ocr 输出文件名,有后缀
		String result_out_filePath = result_out_dir + this.FIS + ocr_result_filename + ".txt";

		this.ocr_result_file_path = result_out_filePath;

		if(logger.isDebugEnabled()) {
			logger.debug("[{}] OCR process info : Result SaveDir >>> {}",this.procID,result_out_dir);
			logger.debug("[{}] OCR process info : Result SaveFilePath >>> {}",this.procID,result_out_filePath);
		}else if(logger.isInfoEnabled()){
			logger.info("[{}] OCR process info : Result SaveFilePath >>> {}",this.procID,result_out_filePath);
		}

		if(this.OS.startsWith("Mac OS") || this.OS.startsWith("Linux")){
			cmd.add("tesseract");
			// Linux or Mac 要设置工作目录为 tesseract 目录 ，所以`原始文件名`需包含路径
			cmd.add(sourceFile.getAbsolutePath());

		}else if(this.OS.startsWith("Windows")){
			cmd.add(this.tessPath + this.FIS + "tesseract");
			// windows 要设置工作目录为文件目录，所以`原始文件名`没有路径，只有文件名
			cmd.add(sourceFile.getName());
		}

		cmd.add(result_out_dir + this.FIS + ocr_result_filename);
		cmd.add(OcrConstants.OCR_PSM_OPITON);
		cmd.add(this.ocr_psm_num);
		cmd.add(OcrConstants.OCR_LANG_OPTION);
		cmd.add(this.ocr_language);

		if(logger.isDebugEnabled()){
			logger.debug("[{}] OCR process info : The Command >>> {}",this.procID,cmd.toString());
		}else if(logger.isInfoEnabled()){
			logger.info("[{}] OCR process info : The Command >>> {}",this.procID,cmd.toString());
		}

		return cmd;
	}

	/**
	 * 处理识别后的空格字符
	 * @param txtFilePath
	 * @throws FileNotFoundException
	 */
	private void processSpace(String txtFilePath) throws FileNotFoundException {

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String str;

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		boolean readSuccess = true;

		File txtFile = new File(txtFilePath);

		if(!txtFile.exists()){
			throw new FileNotFoundException("OCR process Result file is not exist!");
		}

		StringBuilder sb = new StringBuilder();

		try {
			//读取文件
			fis = new FileInputStream(txtFile);
			isr = new InputStreamReader(fis,"UTF-8");
			br = new BufferedReader(isr);

			while ((str = br.readLine()) != null) {
				sb.append(str).append(this.EOL);
			}
		} catch (Exception e){
			logger.error("[{}] OCR process space read file faild .",this.procID,e);
			readSuccess = false;
		} finally {
			try {
				br.close();
			}catch (Exception e){
				//ignore;
			}
			try {
				isr.close();
			}catch (Exception e){
				//ignore;
			}
			try {
				fis.close();
			}catch (Exception e){
				//ignore;
			}
		}

		//处理空格
		if(readSuccess){
			try {
				//写出文件
				fos = new FileOutputStream(txtFile);
				osw = new OutputStreamWriter(fos,"UTF-8");
				bw = new BufferedWriter(osw);
				bw.write(sb.toString().replaceAll(" ", ""));
			} catch (Exception e){
				logger.error("[{}] OCR process space write file faild .",this.procID,e);
			} finally {
				try {
					bw.close();
				}catch (Exception e){
					//ignore;
				}
				try {
					osw.close();
				}catch (Exception e){
					//ignore;
				}
				try {
					fos.close();
				}catch (Exception e){
					//ignore;
				}
			}
		}
	}

	/**
	 * 打印命令行执行错误日志
	 * @param process
	 */
	private void printCommandError(Process process){
		InputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			// 取得命令结果的输出流
			fis = process.getInputStream();
			// 用一个读输出流类去读
			isr = new InputStreamReader(fis);
			// 用缓冲器读行
			br = new BufferedReader(isr);
			String line = null;
			// 直到读完为止
			while ((line = br.readLine()) != null) {
				logger.warn("[{}] OCR process warning : {} ",this.procID,line);
			}
		} catch (Exception e){
			logger.warn("[{}] OCR process print command error Faild!",e);
		} finally {
			try {
				br.close();
			} catch (Exception e){
				//ignore
			}
			try {
				isr.close();
			} catch (Exception e){
				//ignore
			}
			try {
				fis.close();
			} catch (Exception e){
				//ignore
			}
		}
	}

	/**
	 *
	 * @author alexliu
	 * @date：2017年11月22日 下午2:47:25
	 * @Description：用命令行执行ocr
	 * @param imageFile
	 * @return 返回识别后的文件路径目
	 * @throws Exception
	 */
	private String callTesseractCommand(File imageFile) {

		String txt_path = "";

		List<String> comand = this.createCommand(imageFile);

		ProcessBuilder pb = this.createProcessBuilder(imageFile);

		//添加命令行
		pb.command(comand);

		if(logger.isDebugEnabled()){
			logger.debug("[{}] OCR process info : Add command to ProcessBuilder",this.procID);
		}

		Process process = null;
		try {

			if(logger.isDebugEnabled()){
				logger.debug("[{}] OCR process info : Excute OCR Command.",this.procID);
			}
			process = pb.start();

			int w = process.waitFor();
			if(logger.isDebugEnabled()){
				logger.debug("[{}] OCR process info : Command excute result >>> {}",this.procID,w);
			}else if(logger.isInfoEnabled()){
				logger.info("[{}] OCR process info : Command excute result >>> {}",this.procID,w);
			}

			if (w == 0) {
				txt_path = this.ocr_result_file_path;
				//处理空格
				this.processSpace(txt_path);
			} else {
				//打印错误日志
				this.printCommandError(process);
				String msg = "[%s] excute command Faild ! Result %d , Reason : %s !";
				switch (w) {
					case 1:
						msg = String.format(msg,this.procID,w,"无法访问文件，可能文件名中存在空格等特殊字符");
						break;
					case 29:
						msg = String.format(msg,this.procID,w,"无法识别图像或其选定区域");
						break;
					case 31:
						msg = String.format(msg,this.procID,w,"不支持的图片格式");
						break;
					default:
						msg = String.format(msg,this.procID,w,"未知错误");
				}
				throw new RuntimeException(msg);
			}

		} catch (IOException e) {
			logger.error("[{}] OCR process info : Command excute [pb.start()] Faild !",this.procID,e);
		} catch (InterruptedException e) {
			logger.error("[{}] OCR process info : Command excute [process.waitFor()] Faild !",this.procID,e);
		}

		if(logger.isDebugEnabled()){
			logger.debug("[{}] OCR process info : OCR Process End.",this.procID);
		}else if(logger.isInfoEnabled()){
			logger.info("[{}] OCR process info : OCR Process End.",this.procID);
		}

		return txt_path;
	}

}
