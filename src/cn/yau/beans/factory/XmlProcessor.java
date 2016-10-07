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
		logger.info("׼��������Դ�ļ�///"+this.filePath);
		try{
			this.Resolve();
		}catch (Exception e) {
			logger.error("������Դ�ļ�ʧ��///" + e);
			throw new ExceptionInInitializerError(e);
		}
	}

	// ��ȡDOM
	private Document getDocument() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document document = null;
		try {
			logger.info("׼������DOM��");
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			document = documentBuilder.parse(filePath);
			logger.info("����DOM�����///"+document.toString());
			return document;
		} catch (Exception e) {
			logger.error("����DOM��ʧ��///" + e);
			throw new RuntimeException(e);
		}
	}

	// ��ʼ����XML
	private void Resolve() {
		Element element = this.getDocument().getDocumentElement();
		try {
			logger.info("׼������Bean����");
			this.resolveBean(element.getElementsByTagName("bean"));
			logger.info("׼������Bean����");
			this.resolveProperty();
			logger.info("��Դ�ļ��������");
		} catch (Exception e) {
			logger.error("����Beanʵ��ʧ��///"+e);
			throw new RuntimeException(e);
		}
	}

	// ����Beans
	private void resolveBean(NodeList nodeList){
		Element element = null;
		BeanType beanType = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (Element) nodeList.item(i);
			beanType = new BeanType();

			try {
				beanType.setClazz(Class.forName(element.getAttribute("class")));
			} catch (ClassNotFoundException e) {
				logger.error("������ʧ��///"+e);
				continue;
			}
			if ("prototype".equals(element.getAttribute("scope"))){
				this.beansMap.put(element.getAttribute("id"), beanType);
				continue;
			}
			logger.info("����Beanʵ��///" + beanType.getClazz());
			try {
				beanType.setInstance(beanType.getClazz().newInstance());
			} catch (Exception e) {
				logger.error("����Beanʵ��ʧ��///" +e);
				continue;
			}
			beanType.setPropertys(element.getElementsByTagName("property"));
			this.beansMap.put(element.getAttribute("id"), beanType);
		}
	}

	//����bean������
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
