/*
Copyright (C) 2002 Dav Coleman

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
/*
http://www.danger-island.com/~dav/
*/
package dav.chess.app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.datatransfer.*;
import java.lang.reflect.Method;

import dav.chess.piece.*;
import dav.chess.board.*;

//import com.neva.*;

public class ChessReviewerApplication extends JFrame {

    YahooChessReviewer chessReviewer = null;
    ChessBoard[] gameStates = null;
    int current = 0;

    JFrame frame = null;
    JPanel boardPanel = null;

    JTextArea textArea = null;
    JScrollPane scrollPane = null;

    JLabel[][] boardPositions = null;

    private static final Color blackColor = new Color(51,153,51);
    private static final Color whiteColor = new Color(204,204,153);

    ImageIcon wPawn = null;
    ImageIcon wRook = null;

    ImageIcon wKnight = null;
    ImageIcon wBishop = null;
    ImageIcon wQueen = null;
    ImageIcon wKing = null;

    ImageIcon bPawn = null;
    ImageIcon bRook = null;
    ImageIcon bKnight = null;
    ImageIcon bBishop = null;
    ImageIcon bQueen = null;
    ImageIcon bKing = null;

    ImageIcon blankSquare = null;
    //ImageIcon blackSquare = new ImageIcon( this.getClass().getResource("images/squareb.gif");
    //ImageIcon whiteSquare = new ImageIcon( this.getClass().getResource("images/squareb.gif");

    public ChessReviewerApplication() {

        java.lang.Class cbClass = null;

        try {
            cbClass = Class.forName("dav.chess.board.ChessBoard");
        } catch (ClassNotFoundException e) {}

        frame = this;

        wPawn = new ImageIcon( cbClass.getResource("pawnw.gif"));
        wRook = new ImageIcon( cbClass.getResource("rookw.gif"));

        wKnight = new ImageIcon( cbClass.getResource("knightw.gif"));
        wBishop = new ImageIcon( cbClass.getResource("bishopw.gif"));
        wQueen = new ImageIcon( cbClass.getResource("queenw.gif"));
        wKing = new ImageIcon( cbClass.getResource("kingw.gif"));

        bPawn = new ImageIcon( cbClass.getResource("pawnb.gif"));
        bRook = new ImageIcon( cbClass.getResource("rookb.gif"));
        bKnight = new ImageIcon( cbClass.getResource("knightb.gif"));
        bBishop = new ImageIcon( cbClass.getResource("bishopb.gif"));
        bQueen = new ImageIcon( cbClass.getResource("queenb.gif"));
        bKing = new ImageIcon( cbClass.getResource("kingb.gif"));

        blankSquare = new ImageIcon( cbClass.getResource("blank.gif"));

        this.chessReviewer  = new YahooChessReviewer();

        ///////////////////////////////////////////////////////////////////

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8,8));
        boardPositions = new JLabel[8][8];

        boolean startWhite=true;
        for (int r = 0; r<8; r++) {
            boolean white = startWhite;
            for (int c=0; c<8; c++) {
                boardPositions[r][c] = new JLabel( blankSquare );
                boardPositions[r][c].setOpaque(true);
                boardPositions[r][c].setBackground( white?whiteColor:blackColor );
                boardPanel.add( boardPositions[r][c] );
                white=!white;
            }
            startWhite = !startWhite;
        }
                
        //textArea = new JTextArea(10,40);
        //textArea.setFont( new Font( "Courier", Font.PLAIN, 12 ) );
        JButton nextButton = new JButton( "Next" );
        JButton prevButton = new JButton( "Prev" );
        nextButton.addActionListener( new MoveAction(1) );
        prevButton.addActionListener( new MoveAction(-1) );
        //scrollPane = new JScrollPane( textArea );
        Box vbox = Box.createVerticalBox();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        //vbox.add( scrollPane );
        vbox.add( boardPanel );
        vbox.add(  buttonPanel );
        this.getContentPane().add( vbox );

        ///////////////////////////////////////////////////////////////////

        //frame.getContentPane().add(me);

        this.addWindowListener( new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
                } );

        this.setJMenuBar(createMenuBar());

        this.pack();
        this.setVisible(true);



        ///////////////////////////////////////////////////////////////////


        //setVisible(true);

    }

    YahooChessReviewer getChessReviewer() {
        return chessReviewer;
    }

    public void displayBoard( ChessBoard chessBoard ) {
        ChessPosition[][] board = chessBoard.getBoard();

        for (int modelRow = 0; modelRow<8; modelRow++) {
            for (int modelCol=0; modelCol<8; modelCol++) {
                ChessPosition position = board[modelRow][modelCol];
                int r = 7-modelRow;
                //int c = 7-modelCol;
                int c = modelCol;
                ChessPiece piece = position.getPiece();
                if (piece==null) {
                    boardPositions[r][c].setIcon(blankSquare);
                } else if (piece instanceof King && piece.isWhite()) {
                    boardPositions[r][c].setIcon(wKing);
                } else if (piece instanceof King && piece.isBlack()) {
                    boardPositions[r][c].setIcon(bKing);

                } else if (piece instanceof Queen && piece.isWhite()) {
                    boardPositions[r][c].setIcon(wQueen);
                } else if (piece instanceof Queen && piece.isBlack()) {
                    boardPositions[r][c].setIcon(bQueen);

                } else if (piece instanceof Rook && piece.isWhite()) {
                    boardPositions[r][c].setIcon(wRook);
                } else if (piece instanceof Rook && piece.isBlack()) {
                    boardPositions[r][c].setIcon(bRook);
                
                } else if (piece instanceof Knight && piece.isWhite()) {
                    boardPositions[r][c].setIcon(wKnight);
                } else if (piece instanceof Knight && piece.isBlack()) {
                    boardPositions[r][c].setIcon(bKnight);
                
                } else if (piece instanceof Bishop && piece.isWhite()) {
                    boardPositions[r][c].setIcon(wBishop);
                } else if (piece instanceof Bishop && piece.isBlack()) {
                    boardPositions[r][c].setIcon(bBishop);
                
                } else if (piece instanceof Pawn && piece.isWhite()) {
                    boardPositions[r][c].setIcon(wPawn);
                } else if (piece instanceof Pawn && piece.isBlack()) {
                    boardPositions[r][c].setIcon(bPawn);
                }
            }
        }
    }


    public void exit() {
        System.exit(0);
    }

    public class MoveAction implements ActionListener {
        int direction = 0;

        public MoveAction( int direction ) {
            this.direction = direction;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                ChessBoard board = gameStates[current+direction];
                //textArea.setText(board.toString());
                displayBoard(board);
                current+=direction;
            } catch (ArrayIndexOutOfBoundsException exception) {
                //exception.printStackTrace();
            }
        }
    }

    public void loadGame( YahooChessReviewer game ) {
        this.chessReviewer = game;
        Vector gh = game.getGameHistory();
        this.gameStates = (ChessBoard[])(gh.toArray( new ChessBoard[gh.size()] ));
        displayBoard( gameStates[0] );
        current = 0;
        setTitle("W: "+game.getWhitePlayer()+", B: "+game.getBlackPlayer()+" @ "+game.getDateString());
    }

    public JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMI = new JMenuItem("Open File");
        loadMI.setMnemonic('O');
        loadMI.addActionListener( new ActionListener() {
                    public void actionPerformed(ActionEvent event) {

                        String cwd = System.getProperty("user.dir");

                        JFileChooser fchoos = null;
                        if (cwd!=null && cwd.trim().length()>0) {
                            fchoos = new JFileChooser(cwd);
                        } else {
                            fchoos = new JFileChooser();
                        }
                        int option = fchoos.showOpenDialog(boardPanel);
                        if (option==JFileChooser.APPROVE_OPTION) {
                            File file = fchoos.getSelectedFile();
                            try {
                                BufferedReader reader = new BufferedReader(new FileReader(file));
                                chessReviewer.loadHistory(reader);
                                loadGame(chessReviewer);
                            } catch (IOException ioexception) {
                                ioexception.printStackTrace();
                                //JOptionPane.showMessage()
                                System.out.println("Unable to load Yahoo Chess History file "+file);
                            }
                        }

                    }
                } );

        JMenuItem pasteMI = new JMenuItem("Open paste window");
        pasteMI.setMnemonic('P');
        pasteMI.addActionListener( new ActionListener() {
                    public void actionPerformed(ActionEvent event) {

                        String pasteText = null;


                        PastePanel pastePanel = new PastePanel();

                        int option = JOptionPane.showConfirmDialog(frame, 
                                                                 pastePanel,
                                                                 "Paste History Panel",
                                                                 JOptionPane.OK_CANCEL_OPTION);

                        if (option==JOptionPane.OK_OPTION) {
                            try {
                                BufferedReader reader = new BufferedReader(new StringReader(pastePanel.getText()));
                                chessReviewer.loadHistory(reader);
                                loadGame(chessReviewer);
                            } catch (IOException ioexception) {
                                ioexception.printStackTrace();
                                //JOptionPane.showMessage()
                                System.out.println("Unable to load Yahoo Chess History from input text");
                            }
                        }

                    }
                } );

        fileMenu.add(loadMI);
        fileMenu.add(pasteMI);
        menubar.add(fileMenu);

        return menubar;
    }
    //#########################################################################
    //#########################################################################

    private class PastePanel extends JPanel {

        JLabel label = new JLabel("Paste in a chess history:");
        JTextArea textArea = new JTextArea(10, 30);
        JScrollPane scrollPane;

        PastePanel() {
            scrollPane = new JScrollPane(textArea, 
                                         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                         JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            //scrollPane.getViewport().setViewSize(new Dimension( 300, 300 ));
            setLayout(new BorderLayout());
            add(BorderLayout.NORTH, label);
            add(BorderLayout.CENTER, scrollPane);
        }

        public String getText() {
            return textArea.getText();
        }

    }

    //#########################################################################
    //#########################################################################
    //#########################################################################
    //#########################################################################
    //#########################################################################

    public static void main(String args[]) {

        ChessReviewerApplication me = new ChessReviewerApplication();

        if (args.length==1) {
            YahooChessReviewer game = me.getChessReviewer();
            String historyFile = args[0];
            try {
                game.loadHistory(historyFile);
                me.loadGame(game);
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
                System.out.println("Unable to load Yahoo Chess History file "+historyFile);
                System.exit(0);
            }
        }
    }


}

