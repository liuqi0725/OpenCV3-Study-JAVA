package com.liuqi.ocr.core;//package com.shenshu.deepCapture.ocr.core;
//
//import com.shenshu.deepCapture.dam.core.sys.SystemParam;
//import com.shenshu.deepCapture.ocr.common.OcrConstants;
//import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel;
//import net.sourceforge.tess4j.Tesseract1;
//import net.sourceforge.tess4j.TesseractException;
//import net.sourceforge.tess4j.Word;
//import net.sourceforge.tess4j.util.ImageHelper;
//import net.sourceforge.tess4j.util.ImageIOHelper;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import javax.imageio.IIOImage;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OcrProcess_bak {
//
//	private final String LANG_OPTION = "-l";
//
//	private static Logger logger = LogManager.getLogger(OcrProcess_bak.class);
//
//	private Tesseract1 instance = new Tesseract1();
//
//	private boolean isWindows;
//
//	private boolean useCommandLine = false;
//
//	private String tessPath = "";
//
//	/**
//	 * 文本换行符
//	 */
//	public final String EOL = System.getProperty("line.separator");
//
//	/**
//	 * 当前系统，路径分割符
//	 */
//	public final String FIS = System.getProperties().getProperty("file.separator");
//
//	/**
//	 * 设置 tess4j home 是否成功
//	 */
//	private boolean setTess4jHome = true;
//
//	/**
//	 * 采用 tess4j 识别
//	 * @param tessPath
//	 */
//	public OcrProcess_bak(String tessPath) {
//		init(tessPath,false);
//	}
//
//	/**
//	 * 混合模式
//	 * @param tessPath
//	 * @param useCommandLine 是否使用命令行,仅 windows 下可用
//	 */
//	public OcrProcess_bak(String tessPath , boolean useCommandLine) {
//		//(SystemParam.OCR_TESSDATA_PATH);
//		init(tessPath,useCommandLine);
//	}
//
//	private void init(String tessPath , boolean useCommandLine){
//
//		this.tessPath = tessPath;
//
//		this.useCommandLine = useCommandLine;
//
//		this.isWindows = this.checkWinOS();
//
//		String tessDataPath = tessPath + this.FIS + "tessdata";
//
//		if(logger.isDebugEnabled()){
//			logger.debug("[OCR 处理对象] > 初始化 OCR 处理对象，tessPath > {} ",this.tessPath);
//			logger.debug("[OCR 处理对象] > 初始化 OCR 处理对象，useCommandLine > {} ",this.useCommandLine);
//			logger.debug("[OCR 处理对象] > 初始化 OCR 处理对象，isWindows > {} ",this.isWindows);
//			logger.debug("[OCR 处理对象] > 初始化 OCR 处理对象，tessDataPath > {} ",tessDataPath);
//		}
//
//		try {
//			if(logger.isDebugEnabled()){
//				logger.debug("[OCR 处理对象] > 设置 TESS4j 的 Tesseract 路径 tessDataPath > {}",tessDataPath);
//			}
//
//			instance.setDatapath(tessDataPath);
//		} catch (RuntimeException e){
//			logger.error("[OCR 处理对象] > 设置 TESS4j 的 Tesseract 路径失败.",e);
//			e.printStackTrace();
//			this.setTess4jHome = true;
//
//		} catch (Exception e){
//			logger.error("[OCR 处理对象] > 设置 TESS4j 的 Tesseract 路径失败。",e);
//			e.printStackTrace();
//			this.setTess4jHome = true;
//		}
//
//		if(logger.isDebugEnabled()){
//			logger.debug("[OCR 处理对象] > 设置 TESS4j 的 Tesseract 路径成功。");
//		}
//	}
//
//	private boolean checkWinOS(){
//		String os = System.getProperty("os.name");
//
//		if(logger.isDebugEnabled()){
//			logger.debug("[OCR 处理对象] > 获取 System.getProperty(\"os.name\") > {}",os);
//		}
//
//		return os.toLowerCase().startsWith("win") ? true : false;
//	}
//
//	/**
//	 *
//	 * @author alexliu
//	 * @date：2017年11月22日 下午2:39:42
//	 * @Description：文件 ocr
//	 * @param file 文件
//	 * @param fileType 文件类型
//	 * @see OcrConstants
//	 * @param language 语言  chi_sim ,en
//	 * @return 识别后文件路径
//	 * @throws Exception
//	 */
//	public String doOCR(File file , int fileType, String language) throws Exception {
//
//		String path = "";
//		String txt_result = "";
//
//		if(logger.isDebugEnabled()){
//			logger.debug("[OCR 处理对象] > [{}] OCR 处理 filename > {}",file.getName(),file.getName());
//			logger.debug("[OCR 处理对象] > [{}] OCR 处理 filepath > {}",file.getName(),file.getAbsolutePath());
//			logger.debug("[OCR 处理对象] > [{}] OCR 处理 filesize > {} KB",file.getName(),file.length() / 1024f);
//			logger.debug("[OCR 处理对象] > [{}] OCR 处理 filetype > {}",file.getName(),fileType);
//			logger.debug("[OCR 处理对象] > [{}] OCR 处理 language > {}",file.getName(),language);
//		}else{
//			logger.info("[OCR 处理对象] > FileName [{}] OCR 处理。",file.getName());
//		}
//
//        //使用命令行执行
//        if(logger.isDebugEnabled()){
//            logger.debug("[OCR 处理对象] > [{}] 使用 命令行 执行 OCR。",file.getName());
//        }else{
//            logger.info("[OCR 处理对象] > [{}] 使用 命令行 执行 OCR。",file.getName());
//        }
//
//        if(fileType == OcrConstants.FILE_TYPE_IMAGE){
//            if(logger.isDebugEnabled()){
//                logger.debug("[OCR 处理对象] > [{}] OCR 处理,FileType 解析为 [JPG/PNG]",file.getName());
//            }
//            path = doOCR_Image_Tesseract(file, language);
//        }
//
////		if(this.isWindows && this.useCommandLine){
////			//使用命令行执行
////			if(logger.isDebugEnabled()){
////				logger.debug("[OCR 处理对象] > [{}] 使用 命令行 执行 OCR。",file.getName());
////			}else{
////				logger.info("[OCR 处理对象] > [{}] 使用 命令行 执行 OCR。",file.getName());
////			}
////
////			if(fileType == OcrConstants.FILE_TYPE_IMAGE){
////				if(logger.isDebugEnabled()){
////					logger.debug("[OCR 处理对象] > [{}] OCR 处理,FileType 解析为 [JPG/PNG]",file.getName());
////				}
////				path = doOCR_Image_Tesseract(file, language);
////			}
////		}else{
////			// 使用 tess4j 执行
////			if(this.setTess4jHome){
////				if(logger.isDebugEnabled()){
////					logger.debug("[OCR 处理对象] > [{}] 使用 Tess4j 执行 OCR。",file.getName());
////				}else{
////					logger.info("[OCR 处理对象] > [{}] 使用 Tess4j 执行 OCR。",file.getName());
////				}
////
////				if(fileType == OcrConstants.FILE_TYPE_IMAGE){
////					if(logger.isDebugEnabled()){
////						logger.debug("[OCR 处理对象] > [{}] OCR 处理,FileType 解析为 [JPG/PNG]",file.getName());
////					}
////					txt_result = doOCR_JPEG_PNG_Tess4j(file, language);
////
////				}else if(fileType == OcrConstants.FILE_TYPE_PDF){
////					if(logger.isDebugEnabled()){
////						logger.debug("[OCR 处理对象] > [{}] OCR 处理,FileType 解析为 [PDF]",file.getName());
////					}
////					txt_result = doOCR_PDF_Tess4j(file, language);
////
////				}else if(fileType == OcrConstants.FILE_TYPE_IMAGE_BMP){
////					if(logger.isDebugEnabled()){
////						logger.debug("[OCR 处理对象] > [{}] OCR 处理,FileType 解析为 [BMP]",file.getName());
////					}
////					txt_result = doOCR_JPEG_PNG_Tess4j(file, language);
////
////				}else if(fileType == OcrConstants.FILE_TYPE_IMAGE_TIF){
////					if(logger.isDebugEnabled()){
////						logger.debug("[OCR 处理对象] > [{}] OCR 处理,FileType 解析为 [TIF]",file.getName());
////					}
////					txt_result = doOCR_TIF_Tess4j(file, language);
////				}
////
////				if(logger.isDebugEnabled()){
////					logger.debug("[OCR 处理对象] > [{}] OCR 处理,保存文件到 [{}]",file.getName(),file.getPath());
////				}
////				path = this.saveFile(file.getPath(),txt_result);
////
////			}else{
////				logger.warn("[OCR 处理对象] > [{}] OCR 处理,使用 Tess4j 执行 OCR.但是 Tess4jHome 设置失败.",file.getName());
////			}
////
////		}
//
//		instance = null;
//
//		return path;
//	}
//
//	/**
//	 *
//	 * @author alexliu
//	 * @date：2017年11月22日 下午2:47:25
//	 * @Description：用命令行执行ocr
//	 * @param imageFile
//	 * @param language
//	 * @return 返回识别后的目录
//	 * @throws Exception
//	 */
//	private String doOCR_Image_Tesseract(File imageFile , String language) throws Exception {
//		StringBuffer strB = new StringBuffer();
//
//		List<String> cmd = new ArrayList<String>();
//
//		String txt_path = "";
//
//		//String textFileName = TEMPFILE_DIR.getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()+new Random().nextInt());
//		//logger.info("temp file name {}",textFileName);
//
//
//        File output_dir = new File(imageFile.getAbsolutePath().substring(0,imageFile.getAbsolutePath().lastIndexOf(this.FIS)));
//        //String output_filename = imageFile.getName().substring(0 , imageFile.getName().lastIndexOf("."));
//
//        String output_filename = imageFile.getAbsolutePath().substring(0 , imageFile.getAbsolutePath().lastIndexOf("."));
//
//        /*
//         * 1-14 测试修改
//         */
//		//String output_filename = imageFile.getName().substring(0 , imageFile.getName().lastIndexOf("."));
//
//		if(logger.isDebugEnabled()){
//			logger.debug("[OCR 处理对象] > [{}] OCR 执行,输出目录 [{}] ",imageFile.getName(),output_dir.getAbsolutePath());
//			logger.debug("[OCR 处理对象] > [{}] OCR 执行,输出文件名 [{}]",imageFile.getName(),output_filename);
//		}
//
//        /*
//         * 1-14 测试修改
//         */
//		//cmd.add(this.tessPath + this.FIS + "tesseract");
//        cmd.add("tesseract");
//
//        boolean psm = false;
//        String psm_num = "3";
//        /**
//         *    0 = Orientation and script detection (OSD) only.
//         *    1 = Automatic page segmentation with OSD.
//         *    2 = Automatic page segmentation, but no OSD, or OCR.
//         *    3 = Fully automatic page segmentation, but no OSD. (Default)
//         *    4 = Assume a single column of text of variable sizes.
//         *    5 = Assume a single uniform block of vertically aligned text.
//         *    6 = Assume a single uniform block of text.
//         *    7 = Treat the image as a single text line.  // 识别内容为 横行， 可以提供单行文本的识别效率
//         *    8 = Treat the image as a single word.
//         *    9 = Treat the image as a single word in a circle.
//         *    10 = Treat the image as a single character.
//         */
//        if(psm){
//            cmd.add("-psm");
//            cmd.add(psm_num);
//        }
//
//        cmd.add("");
//		cmd.add(output_filename);
//		cmd.add(LANG_OPTION);
//		cmd.add(language);
//
//		ProcessBuilder pb = new ProcessBuilder();
//
//		/*
//		 * 设置命令行工作目录
//		 */
//        /*
//         * 1-14 测试修改
//         */
//		//pb.directory(imageFile.getParentFile());
//        pb.directory(new File(this.tessPath));
//
//        if(logger.isDebugEnabled()){
//			logger.debug("[OCR 处理对象] > [{}] OCR 执行,设置命令行工作目录 [{}] ",imageFile.getName(),imageFile.getParentFile().getAbsolutePath());
//		}
//
//        /*
//         * 1-14 测试修改
//         */
//        //cmd.set(1, imageFile.getName());
//        cmd.set(1, imageFile.getAbsolutePath());
//		pb.command(cmd);
//		pb.redirectErrorStream(true);
//
//		if(logger.isDebugEnabled()){
//			logger.debug("[OCR 处理对象] > [{}] OCR 执行,command [{}] ",imageFile.getName(),cmd.toString());
//		}else{
//			logger.info("[OCR 处理对象] > [{}] OCR 执行,command [{}] ",imageFile.getName(),cmd.toString());
//		}
//
//		Process process = pb.start();
//
//	 	int w = process.waitFor();
//
//		if(logger.isDebugEnabled()){
//			logger.debug("[OCR 处理对象] > [{}] OCR 执行结果 [{}] ",imageFile.getName(),w);
//		}else{
//			logger.info("[OCR 处理对象] > [{}] OCR 执行结果 [{}] ",imageFile.getName(),w);
//		}
//		if (w == 0) {
//			//0 为正常
//
//            /*
//             * 1-14 测试修改
//             */
//            //txt_path = output_dir + this.FIS + output_filename + ".txt";
//			txt_path = output_filename + ".txt";
//            if(logger.isDebugEnabled()){
//				logger.debug("[OCR 处理对象] > [{}] OCR 执行结果输出文件 > {}",imageFile.getName(),txt_path);
//			}
//			//读取文件文本
//			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(txt_path) ,"UTF-8"));
//
//			String str;
//
//			while ((str = in.readLine()) != null) {
//				strB.append(str).append(SystemParam.EOL);
//			}
//			in.close();
//
//			//处理空格
//			BufferedWriter write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txt_path) , "UTF-8"));
//			write.write(strB.toString().replaceAll(" ", ""));
//
//			write.close();
//
//		} else {
//			String msg;
//			switch (w) {
//				case 1:
//					msg = "文件["+imageFile.getName()+"] OCR ,执行 command 失败，返回值1，无法访问文件，可能文件名中存在空格等特殊字符。";
//					break;
//				case 29:
//					msg = "文件["+imageFile.getName()+"] OCR ,执行 command 失败，返回值29，无法识别图像或其选定区域。";
//					break;
//				case 31:
//					msg = "文件["+imageFile.getName()+"] OCR ,执行 command 失败，返回值31，不支持的图片格式。";
//					break;
//				default:
//					msg = "文件["+imageFile.getName()+"] OCR ,执行 command 失败，返回值31，未知错误。";
//			}
//			throw new RuntimeException(msg);
//		}
//		//new File(textFileName + ".txt").delete();
//		return txt_path;
//	}
//
//
//	/**
//	 * @Title: doOCR_PDF_Tess4j
//	 * @Description: PDF解析
//	 * @param pdfFile
//	 * @param language
//	 * @throws TesseractException
//	 * @throws IOException
//	 * @return String  识别字符串
//	 */
//	private String doOCR_PDF_Tess4j(File pdfFile,String language) throws TesseractException, IOException{
//		instance.setLanguage(language);
//		List<IIOImage> imageList = ImageIOHelper.getIIOImageList(pdfFile);
//		String result = instance.doOCR(imageList, null);
//        return result;
//	}
//
//	/**
//	 * @Title: doOCR_TIF_Tess4j
//	 * @Description: TIF文件解析
//	 * @param imageFile
//	 * @param language
//	 * @throws IOException
//	 * @return String  识别字符串
//	 */
//	private String doOCR_TIF_Tess4j(File imageFile,String language) throws IOException{
//		instance.setLanguage(language);
//		int pageIteratorLevel = TessPageIteratorLevel.RIL_WORD;
//        BufferedImage bi = ImageIO.read(imageFile);
//        List<Word> result = instance.getWords(bi, pageIteratorLevel);
//        return result.toString();
//	}
//
//	/**
//	 * @Title: doOCR_JPEG_PNG_Tess4j
//	 * @Description: jpeg/png文件解析
//	 * @param imageFile
//	 * @param language
//	 * @throws TesseractException
//	 * @throws IOException
//	 * @return String  识别字符串
//	 */
//	private String doOCR_JPEG_PNG_Tess4j(File imageFile,String language) throws IOException, TesseractException{
//		instance.setLanguage(language);
//    	BufferedImage bi = ImageIO.read(imageFile);
//    	//放大5倍识别
//    	bi = ImageHelper.getScaledInstance(bi, bi.getWidth() * 5, bi.getHeight() * 5);
//		return instance.doOCR(bi);
//	}
//
//	/**
//	 *
//	 * @author alexliu
//	 * @date：2017年11月22日 下午2:51:06
//	 * @Description：保存文本文件
//	 * @param sourceFilePath 原始文件路径，包含原始文件名的路径.保存的 txt 和其在同一目录下
//	 * @param txt
//	 * @return 保存后的路径
//	 */
//	private String saveFile(String sourceFilePath ,String txt){
//
//		String saveFilePath = sourceFilePath.substring(0,sourceFilePath.lastIndexOf("."))+".txt";
//
//        OutputStreamWriter out = null;
//		try {
//			out = new OutputStreamWriter(new FileOutputStream(new File(saveFilePath)),"UTF-8");
//			out.write(txt.toCharArray());
//			out.flush();
//		} catch (IOException e) {
//
//			logger.error("[OCR 处理对象] > 保存处理文件到 [{}] 失败.",sourceFilePath,e);
//			e.printStackTrace();
//			return null;
//		} finally{
//			try {
//				out.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return saveFilePath;
//	}
//}
