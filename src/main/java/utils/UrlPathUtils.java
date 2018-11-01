package utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class UrlPathUtils {

	public static void main(String[] args) throws IOException {
		// file:/D:/workspace/zhongzhi/javaUtils/target/classes/
		URL url1=Thread.currentThread().getContextClassLoader().getResource(""); 
		
		// file:/D:/workspace/zhongzhi/javaUtils/target/classes/
		URL url2=UrlPathUtils.class.getClassLoader().getResource("");
		
		// /D:/workspace/zhongzhi/javaUtils/target/classes/logback.xml
		String fileName = UrlPathUtils.class.getClassLoader().getResource("logback.xml").getPath();
		
		//file:/D:/workspace/zhongzhi/javaUtils/target/classes/com/tianyang/javaUtils/
		URL url3=UrlPathUtils.class.getResource(""); 
		
		//file:/D:/workspace/zhongzhi/javaUtils/target/classes/
		URL uri = UrlPathUtils.class.getResource("/");
		String path=uri.getPath();
		
		///D:/workspace/zhongzhi/javaUtils/target/classes/uploadFilePath.properties
		String fileUtl = UrlPathUtils.class.getResource("/uploadFilePath.properties").getFile();
		
		//D:\workspace\zhongzhi\javaUtils 
		String courseFile = new File("").getCanonicalPath();
		
		//D:\workspace\zhongzhi\javaUtils 
		String author =new File("").getAbsolutePath();//绝对路径;
		//D:\
		String author1=new File("/").getAbsolutePath(); 
		//D:\workspace\zhongzhi\javaUtils
		String property =System.getProperty("user.dir");
		//file:/D:/workspace/zhongzhi/javaUtils/target/classes/
        URL s=ClassLoader.getSystemResource(""); 
        System.out.println(author1);
	}

}
