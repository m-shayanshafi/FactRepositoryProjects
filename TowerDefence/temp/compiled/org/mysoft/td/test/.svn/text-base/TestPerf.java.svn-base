package org.mysoft.td.test;

public class TestPerf {

	
	public static void main(String[] args) {
		TestPerf tp = new TestPerf();
		tp.start();
	}

	public void start() {
		
		TestObj to = new TestObj();
		
		double t;
		
		long t1 = System.currentTimeMillis();
		
		for(int i=0;i<100000000;i++) {
			t = to.getX(this);
			t=t/2;
		}
		
		System.out.println("method : " + (System.currentTimeMillis() - t1));
		
		t1 = System.currentTimeMillis();
		
		for(int i=0;i<100000000;i++) {
			t = to.x;
			t=t/2;
		}
		
		System.out.println("direct : " + (System.currentTimeMillis() - t1));
		
	}

	
}

class TestObj {
	double x = Math.random();
	
	public final double getX(Object o) {
		//int a = 9;
		return x;
	}
	
}
