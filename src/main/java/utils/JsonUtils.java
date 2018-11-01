package utils;
 
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import utils.beans.ChnMedicineDic;
 
/**
 * json转换工具类
 */
public class JsonUtils {
 
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();
 
    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将json结果集转化为对象
     * 
     * @param jsonData json数据
     * @param clazz 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    //测试
   	public static void main(String[] args) {
   		List<ChnMedicineDic> lists =new ArrayList<ChnMedicineDic>();
	   		for(int i=0;i<2;i++){
	   			ChnMedicineDic  dic =new ChnMedicineDic();
	   			dic.setQytbdm("药品区域统编代码"+i);
	   			dic.setYpybdm("药品医保代码"+i);
	   			dic.setYptym("药品通用名"+i);
	   			dic.setYpbc("药品别称"+i);
	   			dic.setPzff("代煎"+i);
	   			dic.setBz("111"+i);
	   			System.out.println(objectToJson(dic));
	   			lists.add(dic);
	   		}
	   		System.out.println(objectToJson(lists));
   	}
}