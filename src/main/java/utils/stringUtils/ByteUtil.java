package utils.stringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author : tianyang
 * @description :
 * @date :2018年10月31日
 */
public class ByteUtil {
	
	private static Map<String, String> map = new HashMap<String, String>();
	

	static{
		map.put("ZHS16GBK", "GBK");
		map.put("AL32UTF8", "UTF-8");
	}
	/**
	 * 根据字符集编码获得字节长度
	 * @param targetString目标字符串
	 * @return
	 * @throws Exception
	 */
	public static   int getStringByteLength(String targetString,String dbcharset)
            throws Exception {
        return targetString.getBytes(map.get(dbcharset)).length;
    }
	
    //测试用例
	public static void main(String[] args) {
		try {
			int length=getStringByteLength("张无忌大侠6666！","ZHS16GBK");
			System.out.println(length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
