package cn.yau.beans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.yau.aop.advice.Advice;
import cn.yau.aop.advice.ExceptionAdvice;
import cn.yau.aop.advice.MethodAfterAdvice;
import cn.yau.aop.advice.MethodBerforeAdvice;
import cn.yau.aop.advice.MethodInterceptor;
import cn.yau.utils.Log4jUtils;

public final class ProxyFactoryBean implements InvocationHandler {
	static Logger logger = Log4jUtils.getLogger(ProxyFactoryBean.class);

	private List<String> interfaceList;
	private List<Advice> adviceList;
	private Object terget;

	List<Advice> beforeAdvice;
	List<Advice> afterAdvice;
	MethodInterceptor interceptor;
	ExceptionAdvice exceptionAdvice;

	public Object getProxy() {
		Class<?>[] clazzs = new Class<?>[this.interfaceList.size()];
		for (int i = 0; i < this.interfaceList.size(); i++) {
			clazzs[i] = loadClass(this.interfaceList.get(i));
		}
		this.adviceFilter();
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), clazzs,
				this);
	}

	private void adviceFilter() {
		this.beforeAdvice = new LinkedList<Advice>();
		this.afterAdvice = new LinkedList<Advice>();
		for (Advice advice : this.adviceList) {
			if (advice instanceof MethodBerforeAdvice) {
				this.beforeAdvice.add(advice);
			} else if (advice instanceof MethodAfterAdvice) {
				this.afterAdvice.add(advice);
			} else if (advice instanceof MethodInterceptor) {
				this.interceptor = (MethodInterceptor) advice;
			} else if (advice instanceof ExceptionAdvice) {
				this.exceptionAdvice = (ExceptionAdvice) advice;
			}
		}
	}

	private Class<?> loadClass(String name) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(name);
		} catch (Exception e) {
			logger.error("º”‘ÿ¿‡ ß∞‹////" + e);
		}
		return clazz;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object object = null;
		try {
			for (Advice advice : this.beforeAdvice) {
				advice.getMethod().invoke(advice, method, args, this.terget);
			}
			if (this.interceptor != null) {
				MethodInfo methodInfo = new MethodInfo(method, this.terget);
				object = this.interceptor.getMethod().invoke(interceptor,
						methodInfo, args);
			} else {
				object = method.invoke(this.terget, args);
			}
			for (Advice advice : this.afterAdvice) {
				advice.getMethod().invoke(advice, object, method, args,
						this.terget);
			}
		} catch (Exception e) {
			if (this.exceptionAdvice != null) {
				this.exceptionAdvice.getMethod().invoke(this.exceptionAdvice,e,this.terget);
			} else {
				throw e;
			}
		}
		return object;
	}
}
