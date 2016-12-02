package ChessGameUnitTests;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Test;

import ChessGameKenai.About;
import ChessGameKenai.ChessBoardView;
import ChessGameKenai.Chess_Data;

public class AboutUnitTests {

	@Test
	public void testSetJDialogProperties() {
		Chess_Data chessData = Chess_Data.getInstance();
				//new Chess_Data();
		ChessBoardView chessBoardView = ChessBoardView.getInstance(chessData);
				//new ChessBoardView(chessData);
		
		About about = new About(chessBoardView);
		Dimension dimention = new Dimension();
		dimention.setSize(350,300);
		
		JDialog jDialog = new JDialog();
		jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Frame owner = new Frame();		
		
		Dialog dialog = new Dialog(owner);
		dialog.setTitle("About");
		
		assertEquals(dimention.getSize() , about.getSize());
		assertEquals(jDialog.getDefaultCloseOperation() , about.getDefaultCloseOperation());
		assertEquals(dialog.getTitle() , about.getTitle());
	}
	
	@Test
	public void testSetLabelTitle() {
		
		Chess_Data chessData = Chess_Data.getInstance();
		//new Chess_Data();
ChessBoardView chessBoardView = ChessBoardView.getInstance(chessData);
		//new ChessBoardView(chessData);
		
		About about = new About(chessBoardView);
		
		JLabel lblTitle = new JLabel("Chess'N'Chat");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 23));
        lblTitle.setForeground(Color.WHITE);
        
        assertEquals(lblTitle.getText() , about.getLabelTitle().getText());
		assertEquals(lblTitle.getFont() , about.getLabelTitle().getFont());
		assertEquals(lblTitle.getForeground() , about.getLabelTitle().getForeground());
	}

}
