package cn.yau.aop.advice;

import java.lang.reflect.Method;


public abstract class MethodBerforeAdvice implements Advice {
	@Override
	public Method getMethod() throws Exception {
		return MethodBerforeAdvice.class.getMethod("before", Method.class,Object[].class,Object.class);
	}
	
	public abstract void before(Method method, Object[] args, Object target)throws Exception;
}
