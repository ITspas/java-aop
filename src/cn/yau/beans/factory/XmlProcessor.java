package cn.yau.beans.factory;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cn.yau.beans.BeanType;
import cn.yau.utils.BuildPropertyUtils;
import cn.yau.utils.Log4jUtils;

public class XmlProcessor {
	static Logger logger = Log4jUtils.getLogger(XmlProcessor.class);
	String filePath;

	Map<String, BeanType> beansMap = null;

	public XmlProcessor(String filePath, Map<String, BeanType> beansMap) {
		this.beansMap = beansMap;
		this.filePath = filePath;
		logger.info("准备解析资源文件///"+this.filePath);
		try{
			this.Resolve();
		}catch (Exception e) {
			logger.error("解析资源文件失败///" + e);
			throw new ExceptionInInitializerError(e);
		}
	}

	// 获取DOM
	private Document getDocument() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document document = null;
		try {
			logger.info("准备构建DOM树");
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			document = documentBuilder.parse(filePath);
			logger.info("构建DOM树完毕///"+document.toString());
			return document;
		} catch (Exception e) {
			logger.error("构建DOM树失败///" + e);
			throw new RuntimeException(e);
		}
	}

	// 开始解析XML
	private void Resolve() {
		Element element = this.getDocument().getDocumentElement();
		try {
			logger.info("准备构建Bean集合");
			this.resolveBean(element.getElementsByTagName("bean"));
			logger.info("准备完善Bean属性");
			this.resolveProperty();
			logger.info("资源文件解析完毕");
		} catch (Exception e) {
			logger.error("构建Bean实体失败///"+e);
			throw new RuntimeException(e);
		}
	}

	// 解析Beans
	private void resolveBean(NodeList nodeList){
		Element element = null;
		BeanType beanType = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (Element) nodeList.item(i);
			beanType = new BeanType();

			try {
				beanType.setClazz(Class.forName(element.getAttribute("class")));
			} catch (ClassNotFoundException e) {
				logger.error("加载类失败///"+e);
				continue;
			}
			if ("prototype".equals(element.getAttribute("scope"))){
				this.beansMap.put(element.getAttribute("id"), beanType);
				continue;
			}
			logger.info("构建Bean实体///" + beanType.getClazz());
			try {
				beanType.setInstance(beanType.getClazz().newInstance());
			} catch (Exception e) {
				logger.error("构建Bean实体失败///" +e);
				continue;
			}
			beanType.setPropertys(element.getElementsByTagName("property"));
			this.beansMap.put(element.getAttribute("id"), beanType);
		}
	}

	//解析bean的属性
	private void resolveProperty(){
		BeanType beanType = null;
		for (Entry<String, BeanType> entry : this.beansMap.entrySet()) {
			beanType = entry.getValue();
			if(beanType.getInstance()!=null){
				BuildPropertyUtils.build(beanType.getInstance(), beanType.getPropertys());
			}
		}	
	}
}
