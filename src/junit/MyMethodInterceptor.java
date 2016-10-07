package junit;

import cn.yau.aop.advice.MethodInterceptor;
import cn.yau.beans.MethodInfo;

public class MyMethodInterceptor extends MethodInterceptor {

	@Override
	public Object invoke(MethodInfo methodInfo, Object[] args) throws Exception {
		System.out.println("À¹½ØÇ°");
		Object object =methodInfo.processor(args);
		System.out.println("À¹½Øºó");
		return object;
	}


}
