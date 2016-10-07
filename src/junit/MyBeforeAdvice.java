package junit;

import java.lang.reflect.Method;

import cn.yau.aop.advice.MethodBerforeAdvice;

public class MyBeforeAdvice extends MethodBerforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Ç°ÖÃÍ¨Öª//////////");
	}

	
}
