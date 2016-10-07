package cn.yau.aop.advice;

import java.lang.reflect.Method;

public interface Advice {
	Method getMethod() throws Exception;
}
