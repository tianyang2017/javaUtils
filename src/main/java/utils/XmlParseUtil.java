package utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import utils.beans.RequHead;
import utils.beans.Response;


/**
 * @author : tianyang
 * @description :
 * @date :2018年10月31日
 */
public class XmlParseUtil {
	private static final Logger LOG = LoggerFactory.getLogger(XmlParseUtil.class);

    /**
     * 将xml格式的字符串解析成map集合
     * @param mainClz<T>
     * @param detailClzz<V>
     * @param rec 要解析的字符串
     */
    public static <T, V> Response<T, V> parserXML(String xmlData,Class<T> mainClz,Class<V> detailClzz)
    {
        Document doc = null;

        doc = parseXMLDocument(xmlData);
        
        NodeList nodeList = doc.getChildNodes();
        
        Map<String,Object> headMap = new HashMap<String,Object>();
        
        Map<String,Object> mainMap = new HashMap<String,Object>();
        
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        //解析xml文件串
        putNodeValueToMap(headMap,mainMap,detailList,nodeList,"HEAD","MAIN","DETAIL","STRUCT");
        
        Response<T, V> res = null;
        //将map转换成对象
        try {
        	res = new Response<T, V>();
        	//生成头信息对象
        	RequHead head = MapChangeToBeanUtil.changeMapToBean(headMap,RequHead.class);
                res.setHead(head);

            if(mainClz != Void.class){
                //生成主信息对象
                T t = MapChangeToBeanUtil.changeMapToBean(mainMap,mainClz);
                res.setMain(t);
            }
            if(detailClzz != Void.class){
                //生成明细信息对象
                List<V> vList = new ArrayList<V>();
                for(Map<String,Object> map : detailList){
                    V v = MapChangeToBeanUtil.changeMapToBean(map,detailClzz);
                    vList.add(v);
                }
                res.setDetails(vList);
            }
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
        return res;
    }
    
   /**
    * 将xml的文件转换成map的键值对
    * @param headMap 接受头信息
    * @param mainMap 接收主体信息
    * @param detailMap 接收明细信息
    * @param nodeList
    * @param head
    * @param main
    * @param detail
    */
    private static void putNodeValueToMap(Map<String,Object> headMap,Map<String,Object> mainMap,List<Map<String,Object>> detailList,
            NodeList nodeList,String head,String main,String detail,String struct){
        for(int i=0;i< nodeList.getLength();i++){
            //得到当前节点的每个元素
            Node node = nodeList.item(i);
            //获取节点名称
            String nodeName = node.getNodeName();
            if(nodeName.equalsIgnoreCase(head)){//处理头信息
                getHeadInfo(headMap,node.getChildNodes());
            }else if(nodeName.equalsIgnoreCase(main)){//处理主体信息
                getMainInfo(mainMap,node.getChildNodes());
            }else if(nodeName.equalsIgnoreCase(detail)){//处理明细信息
                getdetailInfo(detailList,node.getChildNodes(),struct);
            }else if(node.getNodeType() == Node.TEXT_NODE){
                //do nothing
            }else{//继续递归
        		//递归调用解析方法
                putNodeValueToMap(headMap,mainMap,detailList,node.getChildNodes(),head,main,detail,struct);
            }
        }
    }
    /**
     * 处理xml中头信息
     * @param headMap
     * @param nodeList
     */
    private static void getHeadInfo(Map<String,Object> headMap,NodeList nodeList){
        for(int i=0;i< nodeList.getLength();i++){
            //得到当前节点的每个元素
            Node node = nodeList.item(i);
            //如果当前节点有子节点 且子节点是元素类型
            if(node.hasChildNodes()&&node.getFirstChild().getNodeType() == Node.ELEMENT_NODE){
                //递归调用解析方法
                getHeadInfo(headMap,node.getChildNodes());
            }else if(node.getNodeType() == Node.TEXT_NODE){
                //do nothing
            }else{//当前节点不是元素类型  为#text文本型
                //获取节点名称
                String name = node.getNodeName().toLowerCase();
                //获取文本值
                String value = node.getTextContent();
                if(value !=null){
                    //添加至map对象
                    headMap.put(name,value);
                }
            }
        }
    }
    /**
     * 处理xml的主体信息
     * @param mainMap
     * @param nodeList
     */
    private static void getMainInfo(Map<String,Object> mainMap,NodeList nodeList){
        for(int i=0;i< nodeList.getLength();i++){
            //得到当前节点的每个元素
            Node node = nodeList.item(i);
            //如果当前节点有子节点 且子节点是元素类型
            if(node.hasChildNodes()&&node.getFirstChild().getNodeType() == Node.ELEMENT_NODE){
                //递归调用解析方法
                getMainInfo(mainMap,node.getChildNodes());
            }else if(node.getNodeType() == Node.TEXT_NODE){
                //do nothing
            }else{//当前节点不是元素类型  为#text文本型
                //获取节点名称
                String name = node.getNodeName().toLowerCase();
                //获取文本值
                String value = node.getTextContent();
                if(value !=null){
                    //添加至map对象
                    mainMap.put(name,value);
                }
            }
        }
    }
    /**
     * 处理明细信息
     * @param detailMap
     * @param nodeList
     */
    private static void getdetailInfo(List<Map<String,Object>> detailList,NodeList nodeList,String struct){
        for(int i=0;i< nodeList.getLength();i++){
            //得到当前节点的每个元素
            Node node = nodeList.item(i);
            //获取节点名称
            String nodeName = node.getNodeName();
            if(nodeName.equalsIgnoreCase(struct)){//处理结构体信息
            	Map<String, Object> detailMap = new HashMap<String, Object>(); 
            	getStructInfo(detailList,detailMap,node.getChildNodes());
            }else if(node.getNodeType() == Node.TEXT_NODE){
            	//do nothing
            }else{
            	//递归调用解析方法
                getdetailInfo(detailList,node.getChildNodes(),struct);
            }
        }
    }
    /**
     * 处理xml的Struct信息
     * @param detailMap
     * @param nodeList
     */
    private static void getStructInfo(List<Map<String,Object>> detailList,Map<String,Object> detailMap,NodeList nodeList){
        for(int i=0;i< nodeList.getLength();i++){
            //得到当前节点的每个元素
            Node node = nodeList.item(i);
            //如果当前节点有子节点 且子节点是元素类型
            if(node.hasChildNodes()&&node.getFirstChild().getNodeType() == Node.ELEMENT_NODE){
                //递归调用解析方法
            	getStructInfo(detailList,detailMap,node.getChildNodes());
            }else if(node.getNodeType() == Node.TEXT_NODE){
                //do nothing
            }else{//当前节点不是元素类型  为#text文本型
                //获取节点名称
                String name = node.getNodeName().toLowerCase();
                //获取文本值
                String value = node.getTextContent();
                if(value !=null){
                    //添加至map对象
                	detailMap.put(name,value);
                }
            }
        }
        detailList.add(detailMap);
    }
    /**  
     * 初始化一个DocumentBuilder  
     *  
     * @return a DocumentBuilder  
     * @throws ParserConfigurationException  
     */  
    private static DocumentBuilder newDocumentBuilder()  
            throws ParserConfigurationException {  
        return newDocumentBuilderFactory().newDocumentBuilder();  
    }  
  
    /**  
     * 初始化一个DocumentBuilderFactory  
     *  
     * @return a DocumentBuilderFactory  
     */  
    private static DocumentBuilderFactory newDocumentBuilderFactory() {  
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        dbf.setNamespaceAware(true);
        return dbf;  
    }  
  
    /**  
     * 将传入的一个XML String转换成一个org.w3c.dom.Document对象返回。  
     *  
     * @param xmlString  
     *            一个符合XML规范的字符串表达。  
     * @return a Document  
     */  
    private static Document parseXMLDocument(String xmlString) {  
        if (xmlString == null) {  
            throw new IllegalArgumentException();  
        }  
        try {
            return newDocumentBuilder().parse(  
                    new InputSource(new StringReader(xmlString)));  
        } catch (Exception e) {  
            throw new RuntimeException(e.getMessage());  
        }  
    }   
}
