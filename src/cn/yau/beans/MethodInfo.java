package cn.yau.beans;

import java.lang.reflect.Method;

public class MethodInfo {
	private Method method;
	private Object target;
	
	public MethodInfo(Method method,Object target) {
		this.method =method;
		this.target =target;
	}
	
	public Object processor(Object[] args)throws Exception{
		return  method.invoke(target, args);
	}
}
