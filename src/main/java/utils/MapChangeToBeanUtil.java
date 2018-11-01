package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author : tianyang
 * @description :
 * @date :2018年10月31日
 */
public class MapChangeToBeanUtil {

    /**
     * 将传入参数转化成需要保存的实体类
     * @param mapper Map键与实体类属性对应关系
     * @param map 前台传入的map集合
     * @param cls 要生成的类
     * @return
     * @throws Exception
     */
    public static <T> T changeMapToBeanByMapper(Map<String,String> mapper,Map<String,Object> map,Class<T> cls) throws Exception
    {
        //动态创建传入类的实例
        T t = cls.newInstance();
        //得到该类中的所有属性字段
        Field [] fields = cls.getDeclaredFields();

        //设置属性值
        setFieldValuesByMapper(mapper,map,fields,cls,t);
        //设置父类属性的值
        setSupperFieldValuesByMapper(mapper,map,cls,t);
        //返回处理后的对象
        return t;
    }
    
    /**
     * 将传入参数转化成需要保存的实体类  默认map的key即为cls类的属性
     * @param map 前台传入的map集合
     * @param cls 要生成的类
     * @return
     * @throws Exception
     */
    public static <T> T changeMapToBean(Map<String,? extends Object> map,Class<T> cls) throws Exception
    {
        //动态创建传入类的实例
        T t = cls.newInstance();
        //得到该类中的所有属性字段
        Field [] fields = cls.getDeclaredFields();
        //设置字段值
        setFieldValues(map,fields,cls,t);
        //设置父类属性值
        setSupperFieldValues(map,cls,t);
        //返回处理后的对象
        return t;
    }
    /**
     * 设置对象父类属性的值
     * @param map
     * @param cls
     * @param t
     * @throws Exception
     */
    private static <T> void setSupperFieldValues(Map<String,? extends Object> map,Class<?> cls,T t) throws Exception
    {
        //得到该类的父类
        Class<?> supperClass = cls.getSuperclass();
        //如果存在父类
        if(supperClass == null)
        {
            return;
        }
        //如果父类是Object类
        if(supperClass == Object.class)
        {
            return;
        }
        
        //得到父类声明的所有字段
        Field[] fields = supperClass.getDeclaredFields();
        //设置字段值
        setFieldValues(map,fields,supperClass,t);
        //递归调用
        setSupperFieldValues(map,supperClass,t);
    }
    /**
     * 设置对象父类属性的值
     * @param map
     * @param cls
     * @param t
     * @throws Exception
     */
    private static <T> void setSupperFieldValuesByMapper(Map<String,String> mapper,
            Map<String,? extends Object> map,Class<?> cls,T t) throws Exception
    {
        //得到该类的父类
        Class<?> supperClass = cls.getSuperclass();
        //如果存在父类
        if(supperClass == null)
        {
            return;
        }
        //如果父类是Object类
        if(supperClass == Object.class)
        {
            return;
        }
        //得到父类声明的所有字段
        Field[] fields = supperClass.getDeclaredFields();
        //设置字段值
        setFieldValuesByMapper(mapper,map,fields,supperClass,t);
        //递归调用
        setSupperFieldValuesByMapper(mapper,map,supperClass,t);
    }
    /**
     * 赋值字段值
     * @param 用户传递的值map
     * @param fields 需要设置值的属性字段
     * @param cls类
     * @param t类对象
     * @throws Exception
     */
    private static <T> void setFieldValues(Map<String,? extends Object> map,Field[] fields,Class<?> cls,T t) throws Exception
    {
        for(Field f : fields)
        {
            Method method;
            //得到属性名称
            String fieldName = f.getName();
            //根据属性 手工拼接set方法
            String setFieldName = getSetMethod(fieldName);
            try 
            {
                method = cls.getDeclaredMethod(setFieldName,f.getType());
            } 
            catch (Exception e) 
            {
                throw new Exception(getShortClassName(cls.getName())+"类，属性["+fieldName+"]的set封装方法有误");
            }
            //得到所有map 的key对应的value 属性对应的值
            Object obj = map.get(fieldName);
            //需要赋值
            if(obj != null)
            {
                try 
                {
                    //转换map值的类型
                    obj = parObjectToFeildType(f,obj);
                } 
                catch (Exception e)
                {
                    throw new Exception(getShortClassName(cls.getName())+"类，属性["+fieldName+"]"+e.getMessage()); 
                }
                try
                {
                    //方法自调用
                    method.invoke(t,obj);
                } 
                catch (Exception e) 
                {
                    throw new Exception(getShortClassName(cls.getName())+"类，方法["+setFieldName+"]调用失败！");
                }
            }
            else//设置默认值
            {
                //方法自调用--为传递 设置默认值
                method.invoke(t,returnDefaultObj(f));
            }
        }
    }
    /**
     * 赋值字段值
     * @param map的键与T对象属性的对应关系mapper
     * @param 用户传递的值map
     * @param fields 需要设置值的属性字段
     * @param cls类
     * @param t类对象
     * @throws Exception
     */
    private static <T> void setFieldValuesByMapper(Map<String,String> mapper,Map<String,? extends Object> map,
            Field[] fields,Class<?> cls,T t) throws Exception
    {
        for(Field f : fields)
        {
            Method method;
            String fieldName = f.getName();
            //根据属性 手工拼接set方法
            String setFieldName = getSetMethod(fieldName);
            try 
            {
                method = cls.getDeclaredMethod(setFieldName,f.getType());
            } 
            catch (Exception e) 
            {
                throw new Exception(getShortClassName(cls.getName())+"类，属性["+fieldName+"]的set封装方法有误");
            }
            //获得与之相对应的cls类属性
            String mapKey = mapper.get(fieldName);
            if(mapKey != null)
            {
                //得到所有map 的key对应的value
                Object obj = map.get(mapKey);
                if(obj != null)
                {
                    try 
                    {
                        obj = parObjectToFeildType(f,obj);
                    } 
                    catch (Exception e) 
                    {
                        throw new Exception(getShortClassName(cls.getName())+"类，属性["+fieldName+"]传入值有误！"); 
                    }
                    try 
                    {
                        //方法自调用
                        method.invoke(t,obj);
                    } 
                    catch (Exception e)
                    {
                        throw new Exception(getShortClassName(cls.getName())+"类，方法["+setFieldName+"]调用失败！");
                    }
                }else
                {
                    throw new Exception(getShortClassName(cls.getName())+"类，值Map中不包含key["+mapKey+"]");
                }
            }else
            {
                //方法自调用--为传递 设置默认值
                method.invoke(t,returnDefaultObj(f));
            }
        }
    }
    /**
     * 根据属性 手工拼接set方法
     * @param fieldName
     * @return
     */
    private static String getSetMethod(String fieldName)
    {
        return "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1, fieldName.length());
    }
    
    /**
     * 返回类的名称 去除包
     * @param className
     * @return
     */
    private static String getShortClassName(String className)
    {
        return className.substring(className.lastIndexOf(".")+1, className.length());
    }
    /**
     * 将值转化成对象属性类型
     * @param f
     * @param obj
     * @throws Exception 
     */
    private static Object parObjectToFeildType(Field f,Object obj) throws Exception{
        String value = obj.toString();
        //如果是空字符串 则返回属性的默认值
        if("".equals(value)){
            return returnDefaultObj(f);
        }
        if(f.getType() == Long.class || f.getType().getName().equals("long")){
            if(obj instanceof Double){
                return (long) Double.parseDouble(obj.toString().trim());
            }
            if(obj instanceof Float){
                return (long) Float.parseFloat(obj.toString().trim());
            }
           return Long.parseLong(obj.toString().trim());
        }
        if(f.getType() == Double.class || f.getType().getName().equals("double")){
            return Double.parseDouble(obj.toString().trim());
        }
        if(f.getType() == Integer.class || f.getType().getName().equals("int")){
            if(obj instanceof Double){
                return (int) Double.parseDouble(obj.toString().trim());
            }
            if(obj instanceof Float){
                return (int) Float.parseFloat(obj.toString().trim());
            }
            return Integer.parseInt(obj.toString().trim());
        }
        if(f.getType() == Float.class || f.getType().getName().equals("float")){
            return Float.parseFloat(obj.toString().trim());
        }
        if(f.getType() == Boolean.class || f.getType().getName().equals("boolean")){
            return Boolean.parseBoolean(obj.toString().trim());
        }
        if(f.getType() == Character.class || f.getType().getName().equals("char")){
            return obj.toString().charAt(0);
        }
        if(f.getType() == Byte.class || f.getType().getName().equals("byte")){
            return Byte.parseByte(obj.toString().trim());
        }
        if(f.getType() == Short.class || f.getType().getName().equals("short")){
            if(obj instanceof Double){
                return (short) Double.parseDouble(obj.toString().trim());
            }
            if(obj instanceof Float){
                return (short) Float.parseFloat(obj.toString().trim());
            }
            return Short.parseShort(obj.toString().trim());
        }
        if(f.getType() == String.class){
            return obj.toString().trim();
        }
        if(f.getType() == Date.class){
            return dataFormat(obj,Date.class);
        }
        if(f.getType() == java.sql.Date.class){
            return dataFormat(obj,java.sql.Date.class);
        }
        if(f.getType() == byte[].class){
            return (byte[])obj;
        }
        return null;
    }
    
    /**
     * 将对象转化成需要的日期格式
     * @param obj 支持String.class Date.class java.sql.Date.class Long.class 
     * @param cls
     * @return
     * @throws Exception
     */
    private static Object dataFormat(Object obj,Class<?> cls) throws Exception
    {
        //定义初始日期格式
        SimpleDateFormat sf = null;
        long timestamp = 0L;
        if(obj == null||obj.toString().equalsIgnoreCase("null")||"".equals(obj.toString()))
        {
            return null;
        }
        if(obj.getClass() != String.class && obj.getClass() != Date.class 
                && obj.getClass() != java.sql.Date.class && obj.getClass() != Long.class 
                && !obj.getClass().getName().equals("long"))
        {
            throw new Exception("不支持的时间类型！");
        }
        try {
            if(obj.getClass() == String.class)
            {
            	String strObj = obj.toString().trim();
                //如果传入的是字符串，转换成日期类型，支持y-m-d|y\m\d|y/m/d|ymd
                if(strObj.indexOf("-")!=-1)
                {
                    sf = new SimpleDateFormat("yyyy-MM-dd");
                }
                else if(strObj.matches("[0-9]{8}/[0-9]{6}"))
                {
                    sf = new SimpleDateFormat("yyyyMMdd/HHmmss");
                }else if(strObj.matches("[0-9]{8}/[0-9]{6}/"))
                {
                    sf = new SimpleDateFormat("yyyyMMdd/HHmmss/");
                }
                else if(strObj.indexOf("/")!=-1)
                {
                    sf = new SimpleDateFormat("yyyy/MM/dd");
                }
                else if(strObj.indexOf("\\")!=-1)
                {
                    sf = new SimpleDateFormat("yyyy\\MM\\dd");
                }
                else
                {
                    sf = new SimpleDateFormat("yyyyMMdd");
                }
                Date date = sf.parse(strObj.trim());
                timestamp = date.getTime();
            }
            else if(obj.getClass() == Date.class)
            {
                Date date = (Date)obj;
                timestamp = date.getTime();
            }
            else if(obj.getClass() == java.sql.Date.class)
            {
                java.sql.Date date = (java.sql.Date)obj;
                timestamp = date.getTime();
            }
            else if(obj.getClass() == Long.class 
                    || obj.getClass().getName().equals("long"))
            {
                timestamp = Long.parseLong(obj.toString());
            }
        } catch (ParseException e) {
            throw new Exception("时间格式转化错误！");
        }
        //返回日期类型
        if(cls == Date.class)
        {
            return new Date(timestamp);
        }
        if(cls == java.sql.Date.class)
        {
            return new java.sql.Date(timestamp);
        }
        return null;
    }
    /**
     * 设置属性初始值
     * @param f
     * @param obj
     */
    private static Object returnDefaultObj(Field f)
    {
        if(f.getType() == Long.class || f.getType().getName().equals("long"))
        {
            return 0L;
        }
        if(f.getType() == Double.class || f.getType().getName().equals("double"))
        {
            return 0.0;
        }
        if(f.getType() == Integer.class || f.getType().getName().equals("int"))
        {
            return 0;
        }
        if(f.getType() == Float.class || f.getType().getName().equals("float"))
        {
            return 0.0;
        }
        if(f.getType() == Boolean.class || f.getType().getName().equals("boolean"))
        {
            return false;
        }
        if(f.getType() == Character.class || f.getType().getName().equals("char"))
        {
            return (char)0;
        }
        if(f.getType() == String.class)
        {
            return "";
        }
        return null;
    }
    
 
}
