package cn.yau.aop.advice;

import java.lang.reflect.Method;

public abstract class MethodAfterAdvice implements Advice {

	@Override
	public Method getMethod() throws Exception {
		return MethodAfterAdvice.class.getMethod("after", Object.class,Method.class, Object[].class, Object.class);
	}

	public abstract void after(Object returnValue, Method method,
			Object[] args, Object target) throws Exception;
}
