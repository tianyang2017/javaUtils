package utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
/**
 * @author : tianyang
 * @description :
 * @date :2018年10月31日
 */
public class Sha1Util {
	// 字节数组转换为16进制的字符串
	private static String byteArrayToHex(byte[] byteArray) {
		// 用来将字节转换成 16 进制表示的字符
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] resultCharArray = new char[byteArray.length * 2];// 每个字节用 16 进制表示的话，使用两个字符
		int index = 0;// 表示转换结果中对应的字符位置
		for (byte b : byteArray) {// 从第一个字节开始，对每一个字节,转换成 16 进制字符的转换
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
			resultCharArray[index++] = hexDigits[b & 0xf];// 取字节中低 4 位的数字转换
		}
		return new String(resultCharArray);// 换后的结果转换为字符串
	}
	
	//计算消息摘要
	public static String getMessageDigest(String str, String encName) {
		byte[] digest = null;
		if (StringUtils.isBlank(encName)) {
			encName = "SHA-1";
		}
		try {
			MessageDigest md = MessageDigest.getInstance(encName);
			md.update(str.getBytes());// 通过使用 update 方法处理数据,使指定的 byte数组更新摘要
			digest = md.digest();// 获得密文完成哈希计算,产生128 位的长整数
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return byteArrayToHex(digest);
	}
	
	public static void main(String[] args) {
		String xmlData="<?xml version=\"1.0\"  encoding=\"utf-8\"?><XMLDATA><HEAD><FSSJ>20170404/222222</FSSJ><IP>192.168.1.1</IP><MAC>879FFD616332</MAC><BZXX>备注信息</BZXX></HEAD><MAIN><ZJHM>310103193012213267</ZJHM><HZXM>徐来秀</HZXM><YLJGDM></YLJGDM></MAIN><DETAIL><STRUCT></STRUCT></DETAIL></XMLDATA>";
		String sha1Str="C0BB680A953E42BE75F3F539D9166DF5826CFA94";
		String sSign = getMessageDigest(xmlData, "SHA-1");
		System.out.println(sSign);
		System.out.println(byteArrayToHex(sSign.getBytes()));

	}
	
}
