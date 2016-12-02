package org.mysoft.td.test;

import org.mysoft.td.engine.action.sample.SampleActionController;
import org.mysoft.td.engine.objects.sample.SampleEnemy;
import org.mysoft.td.engine.world.sample.SampleWorld;

public class Test1 {

	public static void main(String[] args) {
		
		SampleWorld w = new SampleWorld();
		SampleEnemy e1 = new SampleEnemy(w, 1,1);

		
//		SampleTurrent t1 = new SampleTurrent(50, 50);
		
	
		SampleActionController ac = new SampleActionController();
		ac.setWorld(w);
		ac.addObject(e1);

		
	//	ac.addObject(t1);
		
		int turn = 1;
		
		while(ac.calculateTurn() && turn < 1000) {
//			System.out.print(turn + " ");
//			System.out.print("[" + e1.toString() + "] ");
//			System.out.print("[" + e2.toString() + "] ");
//			System.out.print("[" + e3.toString() + "] ");
//			System.out.print("[" + e4.toString() + "] ");
			
	//		System.out.println();
			
			turn++;
		}
	}
	
}
