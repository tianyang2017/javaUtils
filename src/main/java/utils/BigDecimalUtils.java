package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author : tianyang
 * @description :
 * @date :2018年10月31日
 */
public class BigDecimalUtils {

    //默认除法运算精度
    private static  final  int DEF_DIV_SCALE=4;

    /***
     * 精确加法运算
     * @param v1 加数
     * @param v2 加数
     * @return : double 加法结果
    */
    public static double add(double v1,double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /***
     * 精确减法运算
     * @param v1 减数
     * @param v2 被减数
     * @return : double 减法结果
     */
    public static double sub(double v1,double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /***
     * 精确乘法运算
     * @param v1 乘数
     * @param v2 乘数
     * @return : double 乘法结果
     */
    public static double mul(double v1,double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    /***
     * 精确乘法运算 默认精度
     * @param v1 除数
     * @param v2 被除数
     * @return : double 触发结果
     */
    public static double div(double v1,double v2){
        return div(v1,v2,DEF_DIV_SCALE);
    }


    /***
     * 精确乘法运算 默认精度 除法做四舍五入操作
     * @param v1 除数
     * @param v2 被除数
     * @return : double 触发结果
     */
    public static double div(double v1,double v2,int scale){

        if (scale<0){
         throw  new IllegalArgumentException("scale不能为负数");
        }
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,RoundingMode.HALF_UP).doubleValue();
    }


    /***
     * 精确乘法运算 默认精度 除法做四舍五入操作
     * @param v1 除数
     * @return : double 触发结果
     */
    public static double round(double v1,int scale){

        if (scale<0){
            throw  new IllegalArgumentException("scale不能为负数");
        }
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal("1");
        return b1.divide(b2,scale,RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取一个double类型的数字的小数位有多长
     * @param dd
     * @return
     */
    public static int doueleBitCount(double dd){
        String temp = String.valueOf(dd);
        int i = temp.indexOf(".");
        if(i > -1){
            return temp.length()-i -1;
        }
        return 0;

    }

    public static Integer[] doubleBitCount(double[] arr){
        Integer[] len = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            len[i] = doueleBitCount(arr[i]);
        }
        return len;
    }

	/**
	 * 支持四舍五入
	 * @param num 原数字
	 * @param digit	保留位数
	 * @return 新数字
	 */
	public static Double keepTwoDecimal(Double num, int digit) {
		if(num != null) {
			BigDecimal b = new BigDecimal(num);  
			num = b.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			return num;
		}
		return num;
	}

    public static void main(String[] args) {
        Double d1=2.0;
        Double d2=3.0;
        System.out.println(div(d1,d2));
        System.out.println(mul(d1,d2));
        System.out.println(round(d1,2));
        System.out.println(sub(d1,d2));
    }







}
