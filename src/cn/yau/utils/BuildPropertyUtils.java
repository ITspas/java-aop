package cn.yau.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.yau.beans.factory.BeansFactory;

public final class BuildPropertyUtils {
	static Logger logger = Log4jUtils.getLogger(BuildPropertyUtils.class);

	static Method setMethod = null;

	static {
		try {
			setMethod = Field.class
					.getMethod("set", Object.class, Object.class);
		} catch (Exception e) {
			logger.error("BuildPropertyUtils��ʼ��ʧ��///" + e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static void build(Object instance, NodeList nodeList) {
		logger.info("����Beanʵ������///" + instance);
		Element element = null;
		String fieldName = null;
		Field field = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (Element) nodeList.item(i);
			fieldName = element.getAttribute("name");
			if (fieldName.equals(""))
				continue;
			try {
				field = instance.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				setValue(instance, field, getValue(element, field));
			} catch (Exception e) {
				logger.error("���Թ���ʧ��////" + e);
			}
		}
	}

	private static void setValue(Object instance, Field field, Object value) {
		try {
			//logger.debug("/////////////////////"+field.getType().getName()+"  "+value.getClass().getName());
			//field.set(instance, (String[])value);
			field.set(instance, getMethod(field.getType().getName()).invoke(null, value));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Object getValue(Element element, Field field) {
		// logger.debug("////////////////////////////////////"+field.getType().getName());
		NodeList nodeList = element.getChildNodes();
		if (nodeList.getLength() > 0)
			return getArraryValue(nodeList);
		else
			return getBaseValue(element,"ref","value");
	}

	private static Object getArraryValue(NodeList nodeList) {
		Element element = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				element = (Element) nodeList.item(i);
				break;
			}
		}
		Object instance = null;
		try {
			instance = Class.forName(element.getAttribute("type")).newInstance();
		} catch (Exception e) {
			logger.error("����Bean����ʵ��ʧ��///" + e);
			throw new RuntimeException(e);
		}
		if (instance instanceof Collection) {
			logger.info("׼������List/Set��������");
			return getCollection(instance,element);
		} else if (instance instanceof Map) {
			logger.info("׼������Map��������");
			return getMap(instance,element);
		} else {
			logger.info("׼���������鼯������");
			getArrary(instance,element);
			return null;
		}
	}
	
	private static Object getArrary(Object instance, Element element) {
		return null;
		/*
		NodeList nodeList = element.getElementsByTagName("item");
		logger.debug("///////////////////nodeList"+nodeList.getLength());
		ArrayList<Object> list = new ArrayList<Object>();
		String type=instance.getClass().getName();
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (Element) nodeList.item(i);
			Method method = getMethod(type);
			try {
				list.add(method.invoke(null, getBaseValue(element, "ref", "value")));
			} catch (Exception e) {
				logger.error("��������ʧ��////"+e);
			}
		}
		logger.debug("/////////////////////////�����������");
		return list.toArray();*/
	}

	private static Object getMap(Object instance, Element element) {
		@SuppressWarnings("unchecked")
		Map<Object,Object> map = (Map<Object, Object>) instance;
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if(nodeList.item(i).getNodeType()!=Node.ELEMENT_NODE)continue;
			element = (Element) nodeList.item(i);
			Object objK = null;
			Object objV = null;
			Method method = getMethod(element.getAttribute("K"));
			try{
				objK = method.invoke(null,getBaseValue(element,"key-ref","key"));
				method = getMethod(element.getAttribute("V"));
				objV = method.invoke(null,getBaseValue(element,"value-ref","value"));
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
			map.put(objK, objV);
		}
		return map;
	}

	private static Object getCollection(Object obj, Element element){
		@SuppressWarnings("unchecked")
		Collection<Object> collection = (Collection<Object>) obj;
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if(nodeList.item(i).getNodeType()!=Node.ELEMENT_NODE)continue;
			element = (Element) nodeList.item(i);
			Method method = getMethod(element.getAttribute("E"));
			try {
				obj = method.invoke(null, getBaseValue(element,"ref","value"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			collection.add(obj);
		}
		return collection;
	}
	
	private static Object getBaseValue(Element element,String ref,String value){
		String valueName = null;
		if(element.getAttribute(value).matches("")){
			valueName = element.getAttribute(ref);
			return BeansFactory.getCurrentBeansFactory().getBean(valueName);
		}else{
			valueName = element.getAttribute(value);
			return valueName;
		}
	}

	private static Method getMethod(String methodName) {
		Class<?> type = null;
		if (methodName
				.matches("(boolean|byte|char|double|float|int|long|short)")) {
			methodName = methodName.toUpperCase().substring(0, 1)
					+ methodName.substring(1);
			type = String.class;
		} else {
			methodName = "";
			type = Object.class;
		}
		methodName = "get" + methodName;
		Method method = null;
		try {
			method = ConvertBaseTypeUtils.class.getMethod(methodName, type);
		} catch (Exception e) {
			logger.error("��ȡת�����ݷ�ʽʧ��////" + e.getMessage());
			throw new RuntimeException(e);
		}
		return method;
	}
}
