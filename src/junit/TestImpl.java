package junit;

public class TestImpl implements TestInter,TestInter2 {

	@Override
	public void sayHi() {
		// TODO Auto-generated method stub
		System.out.println("sayHi");
	}

	@Override
	public void sayBye() {
		// TODO Auto-generated method stub
		System.out.println("sayBye");
		//System.out.println( 9/0);
	}
}
