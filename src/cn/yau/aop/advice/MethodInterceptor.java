package cn.yau.aop.advice;

import java.lang.reflect.Method;

import cn.yau.beans.MethodInfo;

public abstract class MethodInterceptor implements Advice {

	@Override
	public Method getMethod() throws Exception {
		return MethodInterceptor.class.getMethod("invoke",MethodInfo.class,Object[].class);
	}
	
	public abstract Object invoke(MethodInfo methodInfo,Object[] args)throws Exception;
}
