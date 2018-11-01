package utils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
/**
 * @author : tianyang
 * @description :spring容器中取配置文件的值
 * @date :2018年10月31日
 */
public class ParsePropertie  extends PropertyPlaceholderConfigurer{
    private static Map<String, String> propertiesMap;
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            propertiesMap.put(keyStr, value);
        }

    }
    public static String getContextProperty(String name) {
        return propertiesMap.get(name);
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		
		String value=getContextProperty("tempImgPath");
		System.out.println(value);
	}
}
