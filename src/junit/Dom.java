package junit;
import org.junit.Test;

import cn.yau.beans.factory.BeansFactory;
public class Dom {
	@Test
	public void test1(){
		BeansFactory  beansFactory = BeansFactory.newInstance("src/xml.xml");
		TestInter2 t = (TestInter2) beansFactory.getBean("proxyFactoryBean");
		t.sayBye();
		System.out.println("****************************************");
		TestInter t2 = (TestInter) t;
		t2.sayHi();
	}
}
