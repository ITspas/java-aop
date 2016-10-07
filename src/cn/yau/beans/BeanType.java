package cn.yau.beans;

import org.w3c.dom.NodeList;


public class BeanType {
	private Class<?> clazz;
	
	private Object instance;
	
	private NodeList propertys;

	public NodeList getPropertys() {
		return propertys;
	}

	public void setPropertys(NodeList propertys) {
		this.propertys = propertys;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}
}
