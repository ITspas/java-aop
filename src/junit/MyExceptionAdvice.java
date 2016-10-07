package junit;

import cn.yau.aop.advice.ExceptionAdvice;

public class MyExceptionAdvice extends ExceptionAdvice{

	@Override
	public void exceptionRun(Exception e, Object terget) {
		System.out.println("“Ï≥£≤∂ªÒ////"+e);
	}


}
