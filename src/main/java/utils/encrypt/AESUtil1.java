package utils.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author : tianyang
 * @description :
 * @date :2018年10月31日
 */
public class AESUtil1 {


    private static final String conStr="0000000000000000";
    //密匙、偏移量要求的字符串长度
    private static final int DEMANDLENGTH=16;
    private static final String AES="AES";
    private static final String UTF8="UTF-8";


    /***
     * @description: 加密方法 先用AES加密后，在用BASE64进行编码
     * @param secretKey 密钥  此处使用AES-128-CBC加密模式，key为长度16的字符串
     * @param vector 偏移量   此处使用AES-128-CBC加密模式，需要使用偏移量 vector需要为16位
     * @param data 需要加密的文本
     * @return : java.lang.String 返回加密后的字符串
     */
    public static String encrypt(String data,String secretKey,String vector){

        //判断字符串的长度 如果有一个为空 则直接返回 表示传值中用户名、密码和机构代码至少有一个是空值
        if (secretKey==null || vector==null){
            return null;
        }else {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //获得AESKey
                SecretKeySpec keySpec=new SecretKeySpec(checkLength(secretKey).getBytes(),AES);
                //使用CBC模式，需要一个向量iv，可增加加密算法的强度
                IvParameterSpec iv=new IvParameterSpec(checkLength(vector).getBytes());
                //初始化加密器
                cipher.init(Cipher.ENCRYPT_MODE,keySpec,iv);
                //进行加密
                byte[] encryptBytes=cipher.doFinal(data.getBytes(UTF8));
                //用BASE64进行转码
                return  new BASE64Encoder().encode(encryptBytes);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /***
     * @description: 解密方法 先用BASE64解密后，在用AES进行解密
     * @param secretKey 密钥  此处使用AES-128-CBC加密模式，key为长度16的字符串
     * @param vector 偏移量   此处使用AES-128-CBC加密模式，需要使用偏移量 vector为长度16的字符串
     * @param encryptData 需要解密的文本
     * @return : java.lang.String 返回解密后的字符串
     */
    public static String decrypt(String encryptData,String secretKey,String vector){

        //判断字符串的长度 如果有一个为空 则直接返回 表示传值中用户名、密码和机构代码至少有一个是空值
        if (secretKey==null || vector==null){
            return null;
        }else {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //获得AESKey
                SecretKeySpec keySpec = new SecretKeySpec(checkLength(secretKey).getBytes(), AES);
                //使用CBC模式，需要一个向量iv，可增加加密算法的强度
                IvParameterSpec iv = new IvParameterSpec(checkLength(vector).getBytes());
                //初始化加密器
                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
                //先用BASE64解密
                byte[] baseBuffer=new BASE64Decoder().decodeBuffer(encryptData);
                //在用AES进行解密
                byte[] aesBuffer=cipher.doFinal(baseBuffer);
                return new String(aesBuffer,UTF8);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    /***
     * @description: 解密方法2（使用同一密匙和偏移量进行解密） 先用BASE64解密后，在用AES进行解密
     * @param encryptData 需要解密的文本
     * @return : java.lang.String 返回解密后的字符串
     */
    public static String decryptAndThrow(String encryptData) throws Exception{

        String secretKey="httpwwwciiccomcn";
        String vector="httpwwwciiccomcn";

        //判断字符串的长度 如果有一个为空 则直接返回 表示传值中用户名、密码和机构代码至少有一个是空值
        if (secretKey==null || vector==null){
            return null;
        }else {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //获得AESKey
                SecretKeySpec keySpec = new SecretKeySpec(checkLength(secretKey).getBytes(), AES);
                //使用CBC模式，需要一个向量iv，可增加加密算法的强度
                IvParameterSpec iv = new IvParameterSpec(checkLength(vector).getBytes());
                //初始化加密器
                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
                //先用BASE64解密
                byte[] baseBuffer=new BASE64Decoder().decodeBuffer(encryptData);
                //在用AES进行解密
                byte[] aesBuffer=cipher.doFinal(baseBuffer);
                return new String(aesBuffer,UTF8);
        }
    }



    //保证密匙和向量的字符串长度 超过长度则截取 低于长度则用"0"在末尾补齐
    private static String checkLength(String string){
        int length=string.length();
        if (length<DEMANDLENGTH){
            string=string+conStr.substring(0,DEMANDLENGTH-string.length());
        }else if (length>DEMANDLENGTH){
            string= string.substring(0,DEMANDLENGTH);
        }
        return string;
    }


    public static void main(String[] args) {
     /*   String s = AESUtil.decrypt("wOc8ano96TWDh0XvkR8s+g==", "httpwwwciiccomcn", "httpwwwciiccomcn");
        System.out.println(s);*/
        String encrypt1 = AESUtil1.encrypt("rjel",
                "httpwwwciiccomcn", "httpwwwciiccomcn");
        System.out.println(encrypt1);

        String encrypt3 = AESUtil1.decrypt("wOc8ano96TWDh0XvkR8s+g==",
                "httpwwwciiccomcn", "httpwwwciiccomcn");
        System.out.println(encrypt3);

        /*String encrypt2 = AESUtil.decrypt("wR48IpuEFb7O5T9Yq3Dxt3dYxRai81ynB/CCGjgC0MlLNhNrDdSc6y2ahGbhaCvx/GzBbO+bLzxl4qm4xy4VCTLkZS9YHKxdLl1kJO28Jz6+sLrep1yxEJT1yB1E7yME9mY8mM0KvXe0d0a0aH5EP4DTMSLfxL+9zIl6Z9UI8ixk/Erz5ZIrnFsTCA4koCiLNpNIpRcOEJDJag25awuXAw==",
                "httpwwwciiccomcn", "httpwwwciiccomcn");
        System.out.println(encrypt2);*/


    }
}

