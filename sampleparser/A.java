package sample;

public class A {

	int a;
	int b;
	//hello
	public A(){
		System.out.print("A");
	}
	B ob = new B();
	public void setter(int m,int n){
		a = m;
	    b = n;
	}
	int c = ob.add(a);
}

class B extends A
{
	int a;
	
	B(){
		System.out.print("B");
	
	}
	
	public int add(int a){
		int m = a+10;
		return m;
	}
}
	