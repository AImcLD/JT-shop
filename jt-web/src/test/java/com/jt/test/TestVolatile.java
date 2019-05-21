package com.jt.test;

public class TestVolatile {
	public static void main(String[] args) {
		T t = new T();
		t.run();
		t.run();
		Run r = new Run();
		Thread thread = new Thread(r);
	}

}

class T extends Thread{
	Boolean i;
	@Override
	public void run() {
		while(!i) {
			
		}
	}
}
	
class Run implements Runnable{
	Boolean i;
	@Override
	public void run() {
		while(!i) {}
	}
}

