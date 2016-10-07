package junit;

import java.lang.reflect.Method;

import cn.yau.aop.advice.MethodAfterAdvice;

public class MyAfterAdvice extends MethodAfterAdvice {

	@Override
	public void after(Object returnValue, Method method, Object[] args,
			Object target) throws Exception {
		System.out.println("∫Û÷√Õ®÷™////////////");

	}

}
