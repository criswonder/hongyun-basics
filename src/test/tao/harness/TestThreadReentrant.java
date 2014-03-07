package test.tao.harness;

public class TestThreadReentrant extends Widget{

	public synchronized void doSomething(){
		System.out.println("child dosomething");
		super.doSomething();
	}
}
class Widget
{
	
	public synchronized void doSomething(){
		System.out.println("super dosomething");
	} 
}