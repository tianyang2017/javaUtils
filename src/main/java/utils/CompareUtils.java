package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * @author : tianyang
 * @description :
 * @date :2018年10月31日
 */
public class CompareUtils {

    private static final ObjectMapper mapper=new ObjectMapper();

    /***
     * @description: 比较对象的指定(或全部)属性，并返回不相同的属性与对应的值
     * @param source 对象1
     * @param target 对象2
     * @param comparedProperties  需要比较的对象属性String[属性1,属性2,属性3]  为null时默认全部比较
     * @return : 返回的Map中的Object有可能为空,String或者List 根据target是否为空而有所区别 需要根据Map.size、getClass来判断
     */
    public static Map<String,Object> getModifyContent(Object source, Object target,String[] comparedProperties) {

        Map<String,Object> modifies=new HashMap<>();
        //都为空时候 返回空集合
        if(null == source || null == target) {
            if(null==source&&null==target) return modifies;
                //target为空的时候 返回 source 的全部的属性和值
            else if(null == target) return modifies;
            else {return mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});}
        }
        if(!Objects.equals(source.getClass().getName(), target.getClass().getName())){
            throw new ClassCastException("source and target are not same class type");
        }
        Map<String, Object> sourceMap= mapper.convertValue(source, new TypeReference<Map<String,Object>>(){});
        Map<String, Object> targetMap = mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});
        sourceMap.forEach((k,v)->{
            //如果传入传入的值为空 默认比较全部的属性
            if(comparedProperties==null){
                Object targetValue=targetMap.get(k);
                //返回属性名、source对应属性的值、target对应属性的值 放入list中方便取值
                if (!equals(v,targetValue)){
                    List<Object> list=new ArrayList<>();
                    list.add(v);
                    list.add(targetValue);
                    modifies.put(k,list);
                }
            }else {
                //不为空则比较指定的属性
                List<String> proList=Arrays.asList(comparedProperties);
                if (proList.contains(k)){
                    Object targetValue=targetMap.get(k);
                    if (!CompareUtils.equals(v,targetValue)){
                        List<Object> list=new ArrayList<>();
                        list.add(v);
                        list.add(targetValue);
                        modifies.put(k,list);
                    }
                }
            }

        });
        return modifies;
    }


    /***
     * @description: 比较对象是否相等(可以指定属性进行比较)
     * @param source 对象1
     * @param target 对象2
     * @param comparedProperties  需要比较的对象属性String[属性1,属性2,属性3]  为null时默认全部比较
     * @return : boolean
     */
    public static boolean isEuqal(Object source, Object target,String[] comparedProperties) {

        if(null == source || null == target) {
            return null == source && null == target;
        }
        if(!Objects.equals(source.getClass().getName(), target.getClass().getName())){
            return false;
        }
        Map<String, Object> sourceMap= mapper.convertValue(source, new TypeReference<Map<String,Object>>(){});
        Map<String, Object> targetMap = mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});

        //为空时候默认比较全部
        if(comparedProperties==null){
            for (String k:sourceMap.keySet()){
                if (!CompareUtils.equals(sourceMap.get(k),targetMap.get(k))){
                    return false;
                }
            }
        //不为空则遍历指定的属性
        }else {
            for (String k:sourceMap.keySet()){
                List<String> proList=Arrays.asList(comparedProperties);
                if (proList.contains(k)&&!CompareUtils.equals(sourceMap.get(k),targetMap.get(k))){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean equals(Object a, Object b) {
        if ("".equals(a) && b==null || "".equals(b) && a==null){
            return true;
        }else{
            return (a == b) || (a != null && a.equals(b));
        }
    }
}
