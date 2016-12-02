package conway;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* Class to create an instance of World
*/
class World extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Constructor to create the GUI of the World
	*/
	public World(int a, int b){
		final int row = a;
		final int col = b;
		setSize(row*20,col*20);
		setLocationRelativeTo(null);
		setTitle("Conveys Game Of Life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		JPanel panelTop = new JPanel();
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new GridLayout(row,col));

		JLabel gen = new JLabel("Number of Generations: ");
		final JTextField numOfGen = new JTextField("1",3);
		final JButton clear = new JButton("Clear");
		final JButton nextGen = new JButton("Next Generation");
		final JButton[][] cell;
		cell = new JButton[row+2][col+2];
		
		/*---------- Generating the initial World ----------*/
		for(int i=0; i<row+2; i++){
			for(int j=0; j<col+2; j++){
				cell[i][j] = new JButton();
				cell[i][j].setBackground(Color.YELLOW);

				/*---------- Checking condition so that the extra row and column are not added to the display panel ----------*/
				if(i!=0 && i!=row+1 && j!=0 && j!=col+1 ){
					panelBottom.add(cell[i][j]);
					cell[i][j].addActionListener(new buttonAction());
				}
			}
		}

		panelTop.add(gen);
		panelTop.add(numOfGen);
		panelTop.add(nextGen);	
		panelTop.add(clear);
		
		add(panelTop,BorderLayout.NORTH);
		add(panelBottom);
		
		/**
		* Anonymous Inner Class to create and display the next generation
		*/
		ActionListener nextGeneration = new ActionListener() {
			int genLimit;

			/*---------- Timer to loop through the given number of generations ----------*/
			Timer timer = new Timer(50, new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(genLimit > 0){
						numOfGen.setText(""+genLimit--);
						createNextGeneration(cell,row,col);
					}
					else{
						timer.stop();
						numOfGen.setText("1");
					}
				}
			});
			/**
			* actionPerformed(): Function which is called when "Next Generation" Button is clicked
			*/
			public void actionPerformed(ActionEvent e) {
				if(numOfGen.getText().length() == 0)
					genLimit = 1; // If the field is kept blank then it is considered as a single generation
				else
					genLimit = Integer.parseInt(numOfGen.getText()); // Assuming User does not input any alphabets
				
				timer.start();
			}
		};
		
		nextGen.addActionListener(nextGeneration);
		numOfGen.addActionListener(nextGeneration);
		
		/**
		* ActionListener to clear out all the World and restore the default initial condition
		* It cheanges the color of all the cells to YELLOW
		*/
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=1; i<=row; i++)
					for(int j=1; j<=col; j++){
						cell[i][j].setBackground(Color.YELLOW);	

					}
			}
		});
	} // End of Constructor

	/**
	* createNextGeneration(): Function to create the next generation of the World
	* @params:	cell - Array of two dimentional JButtons that define the existing World
	*			row - Number of rows in the existing World
	*			col - Number of columns in the existing World
	*/
	public void createNextGeneration(JButton[][] cell, int row, int col){
		int neighbours;
		for(int i=1; i<row+1; i++){
			for(int j=1; j<col+1; j++){
					neighbours = checkNeighbour(cell,i,j); // Check number of neighbours

					/*---------- If condition to check which cells are to be made in the next generation ----------*/
					if(neighbours == 3 || (neighbours == 2 && cell[i][j].getBackground() == Color.RED))
						cell[i][j].setText("*"); // Changing the Text of the JButton who is going to be alive in the next Generations
			}
		}

		/*---------- Switching to the next Generation according to the JButton Text ----------*/
		for(int i=1; i<row+1; i++){
			for(int j=1; j<col+1; j++){
				if(cell[i][j].getText() == "*"){
					cell[i][j].setBackground(Color.RED);
					cell[i][j].setText("");
				}
				else
					cell[i][j].setBackground(Color.YELLOW);
			}
		}
	}

	/**
	* checkNeighbour(): Function to check the number of neighbours of a selected cell
	* @params:	cell - Array of two dimentional JButtons that define the existing World
	*			rowId - row id of the selected cell
	*			colId - column id of the selected cell
	* @returns: Value of number of neighbours for the selected cell
	*/
	public int checkNeighbour(JButton[][] cell, int rowId,int colId){
		int neighbours = 0;
		for(int i=rowId-1; i<=rowId+1; i++)
			for(int j=colId-1; j<=colId+1; j++){
				if(cell[i][j].getBackground() == Color.RED)
					neighbours++;
				if(i==rowId && j== colId && cell[i][j].getBackground() == Color.RED)
					neighbours--;
			}
				return neighbours;
	}

}// End of class World

/**
* Class buttonAction that implements ActionListener to change the color of the JButton when clicked
*/
class buttonAction implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		JButton temp = (JButton) e.getSource();

		if(temp.getBackground() == Color.RED)
			temp.setBackground(Color.YELLOW);
		else
			temp.setBackground(Color.RED);
	}
}

/**
* Class to Test the GUI Program
*/
public class ConwaysGameOfLife{
	public ConwaysGameOfLife(){
		final int row = 35;
		final int col = 35;
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				new World(row,col);
			}
		});
	}
}