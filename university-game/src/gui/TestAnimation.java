/**
 * Main.java
 * 
 * Classe de test aniamtion.
 * 
 * @author Si-Mohamed Lamraoui
 * @date 22.05.10
 */

package gui;

import javax.swing.JFrame;


public class TestAnimation implements Runnable
{






	public static void main(String args[])
	{
		Thread t = new Thread(new TestAnimation());
		t.start();
	}

	
	
	public void run()
	{
		JFrame frame = new JFrame("Test animation");
		TestAireDessin area = new TestAireDessin();
		frame.add(area);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(100,100);
		frame.setVisible(true);
		//frame.pack();
	}

}
