package cn.yau.aop.advice;

import java.lang.reflect.Method;

public abstract class ExceptionAdvice implements Advice {

	@Override
	public Method getMethod() throws Exception {
		return ExceptionAdvice.class.getMethod("exceptionRun", Exception.class,Object.class);
	}
	
	public abstract void exceptionRun(Exception e,Object terget);

}
