package utils.http;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
/**
 * @author : tianyang
 * @description :
 * @date :2018年10月31日
 */
public class HttpClientUtils {
	static int socketTimeout = 30000;// 请求超时时间
	static int connectTimeout = 30000;// 传输超时时间
	private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);
	
	public static Map<String,String> sendMessage(Map<String,Object> sendMap) {
		// 发送数据转为JSON
		String sendJson = JSONArray.toJSONString(sendMap);
		// 发送信息
		String xmlData = send(sendJson);
		// 接受数据转为JSON
		String acceptJson = readStringXml(xmlData);
		// 封装接受数据
		Map acceptMap = (Map) JSONArray.parse(acceptJson);
		return acceptMap;
	}
	
	private static String postUrl = "http://192.168.10.1:10418/Interface/Service.asmx";
	// 发送信息
	private static String send(String jsonData) {
		
		String orderSoapXml = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tem='http://tempuri.org/'>"
    			+ "<soapenv:Header/>"
    			+ "<soapenv:Body>"
    			+ "<tem:Execute>"
    			+ "<tem:Parameter>"
    				+jsonData
             	+ "</tem:Parameter>"
             	+ "</tem:Execute>"
             	+ "</soapenv:Body>"
             	+ "</soapenv:Envelope>";
		String retStr = "";
		
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(postUrl);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
		httpPost.setConfig(requestConfig);
		try {
			httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			StringEntity data = new StringEntity(orderSoapXml, Charset.forName("UTF-8"));
			httpPost.setEntity(data);
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8");
				LOG.info("response:" + retStr);
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			LOG.error("发短信HttpClientUtil.send()："+jsonData+"\r\n", e);
		}
		return retStr;
	}
    
	// 获取接受的JSON数据
	private static String readStringXml(String xml) {
    	try {
        	Document doc = DocumentHelper.parseText(xml);
        	Element envelopeElement = doc.getRootElement();
          	Element bodyElement = envelopeElement.element("Body");
         	Element executeResponseElement = bodyElement.element("ExecuteResponse");
         	String elementText = executeResponseElement.elementText("ExecuteResult");
          	return elementText;
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
        return null;
    }
    
    
   public static void main(String[] args) {
	    // 操作人信息
		Map<String,String> map1 = new HashMap<String,String>();
		map1.put("MedicalInstitutionID", "99999999999");// 医疗机构代码 
		map1.put("ID", "");// 操作员ID
		map1.put("Code", "");// 操作员工号
		map1.put("IpAddress", "127.0.0.1");// 操作员IP
		map1.put("BusinessNo", "A6A22155AC86AAAA");// 商户号
		// 参数
		Map<String,String> map2 = new HashMap<String,String>();
		map2.put("TargetTel", "17521678848");// 手机号
		map2.put("MessageContent", "110");// 短信内容
		map2.put("MsgSource", "MsgSource");// 短信来源
		// 封装发送数据
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("Method", "SendMessage");// 方法名
		sendMap.put("OperateInfo", map1);
		sendMap.put("Content", map2);
		
		// 发送短息
		Map<String, String> message = HttpClientUtils.sendMessage(sendMap);
   }
}  