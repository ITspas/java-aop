package cn.yau.beans.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.yau.beans.BeanType;
import cn.yau.beans.ProxyFactoryBean;
import cn.yau.utils.BuildPropertyUtils;
import cn.yau.utils.Log4jUtils;

public class BeansFactory {
	static Logger logger = Log4jUtils.getLogger(BeansFactory.class);
	
	private static BeansFactory instance;

	Map<String, BeanType> beansMap;

	private BeansFactory() {
		this.beansMap = new HashMap<String, BeanType>();
	}

	public static BeansFactory newInstance(String filePath) {
		if (BeansFactory.instance == null) {
			logger.info("构建Bean工厂");
			BeansFactory.instance = new BeansFactory();
			try{
				new XmlProcessor(filePath,BeansFactory.instance.beansMap);
			}catch (Exception e) {
				logger.error("预构建Bean工厂失败///"+e);
				throw new RuntimeException(e);
			}
		}
		return BeansFactory.instance;
	}

	public static BeansFactory getCurrentBeansFactory() {
		return BeansFactory.instance == null ? null : BeansFactory.instance;
	}
	
	public Object getBean(String beanName){
		if(!this.beansMap.containsKey(beanName)){
			return null;
		}
		BeanType beanType = this.beansMap.get(beanName);
		Object instance = beanType.getInstance();
		if(instance==null){
			try {
				logger.info("构建Bean实体///"+beanType.getClazz());
				instance = beanType.getClazz().newInstance();
			} catch (Exception e) {
				logger.error("构建Bean实体失败///"+e.getMessage());
			} 
			BuildPropertyUtils.build(instance, beanType.getPropertys());
		}
		if(instance instanceof ProxyFactoryBean){
			return ((ProxyFactoryBean)instance).getProxy();
		}
		return instance;
	}
}
