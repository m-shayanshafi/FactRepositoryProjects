package thaigo.property;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import thaigo.network.client.Client;
import thaigo.network.server.ClientHandler;
import thaigo.object.Board;
import thaigo.object.GOPanel;
import thaigo.object.Pawn;
import thaigo.object.PawnModel;
import thaigo.state.UpdateTask;
import thaigo.utility.PropertyManager;
import thaigo.view.GameUI;
import thaigo.view.MouseOverHighlighter;
import thaigo.view.LoseGame;
import thaigo.view.WinGame;

/**
 * Ruler of Thai GO game.
 * 
 * @author Rungroj Maipradit 5510546654
 * @version 9/5/2013
 */
public class ThaiGORuler extends AbstractRuler {

	private UpdateTask updateTask;

	/**
	 * Constructor of this class.
	 */
	private ThaiGORuler() {
		super();
		updateTask = UpdateTask.getInstance(this);

		timer = new Timer();
		long delay = 1000 - System.currentTimeMillis() % 1000;

		timer.scheduleAtFixedRate(updateTask, delay, 1000);
	}
	
	/**
	 * Create abstract ruler only one time.
	 * @return abstract ruler
	 */
	public static AbstractRuler getInstance() {
		if (ruler == null)
			ruler = new ThaiGORuler();
		return ruler;
	}

	/**
	 * @see thaigo.property.AbstractRuler#initPawn(thaigo.object.Board)
	 */
	@Override
	public void initPawn(Board board) {
		MouseListener mouseOverListener = new MouseOverHighlighter(board);
		this.board = board;

		Owner youowner = new Owner();
		for (int i = 0; i < TABLE; i++) {
			String pawnPath = PropertyManager.getProperty("thaigo.pawn.currentmodel");
			for( PawnModel p : PawnModel.values()){
				if(p.name().equalsIgnoreCase(pawnPath))
					yourPawn.add(new Pawn(p.getFirstPawn(), new Position(i, 0), this, youowner));
			}

			yourPawn.get(i).addMouseListener(mouseOverListener);
			board.addPawn(yourPawn.get(i), new Position(i, 0));
		}
		String foename = PropertyManager.getProperty("foe");
		String foemode = PropertyManager.getProperty("mode").equals("server") ? "client" : "server";
		Owner foeowner = new Owner(foename, foemode);
		for (int i = 0; i < TABLE; i++) {
			String pawnPath = PropertyManager.getProperty("thaigo.pawn.currentmodel");
			for( PawnModel p : PawnModel.values()){
				if(p.name().equalsIgnoreCase(pawnPath))
					foePawn.add(new Pawn(p.getSecondPawn(), new Position(i, 7), this, foeowner));
			}

			foePawn.get(i).addMouseListener(mouseOverListener);
			board.addPawn(foePawn.get(i), new Position(i, 7));
		}

	}
	/**
	 * Check in the block what is inside 0 nothing 1 foe 2 move 3 ally.
	 */
	public void check(){
		for(int i = 0;i<8;i++)
			for(int j = 0;j<8;j++)
				checkarr[i][j] = 0;
		for (Pawn p : yourPawn) {
			checkarr[p.getPosition().getX()][p.getPosition().getY()] = 3; 	
		}
		for (Pawn p : foePawn) {
			checkarr[p.getPosition().getX()][p.getPosition().getY()] = 1; 	
		}
	}

	@Override
	public void walking(MouseEvent e) {
		if (((GOPanel)e.getSource()).getPosition().equals(pawnPos)) {
			return;
		}
		if(checkarr[gopenelPos.getX()][gopenelPos.getY()] == 2){
			rightPosition = true;
			for (Pawn p : yourPawn) {
				if(pawnPos.equals(p.getPosition())){
					p.setPosition(new Position(gopenelPos.getX(), gopenelPos.getY()));
					board.render(yourPawn, foePawn);	
					sendPawnPositionsToFoe();
					break;
				}				
			}
		}
		check();
		setPawnPosition(((GOPanel)e.getSource()).getPosition());
		eating();
	}
	
	/**
	 * Set Client.
	 * @param client reference of client.
	 */
	public void setClient(Client client) {
		super.setClient(client);
	}

	/**
	 * Get client.
	 * @return client.
	 */
	public Client getClient() {
		return super.getClient();
	}

	/**
	 * Get server.
	 * @return server.
	 */
	public ClientHandler getClientHandler() {
		return super.getClientHandler();
	}

	/**
	 * @see thaigo.property.AbstractRuler#eating()
	 */
	@Override
	public void eating() {
		if (pawnPos == null)
			return;
		if (checkarr[pawnPos.getX()][pawnPos.getY()] != 1) {
			if (pawnPos.getX() < TABLE-1 && pawnPos.getX() > 0 && checkarr[pawnPos.getX() - 1][pawnPos.getY()] == 1 && checkarr[pawnPos.getX() + 1][pawnPos.getY()] == 1){
				for (int i = 0; i < foePawn.size(); i++) {
					if (foePawn.get(i).equals(new Position(pawnPos.getX() - 1, pawnPos.getY()))) {
						sendRemoveCommandToFoe(foePawn.get(i).getPosition());
						foePawn.remove(foePawn.get(i));
						board.render(yourPawn, foePawn);
					}
					if (foePawn.get(i).equals(new Position(pawnPos.getX() + 1, pawnPos.getY()))) {
						sendRemoveCommandToFoe(foePawn.get(i).getPosition());
						foePawn.remove(foePawn.get(i));
						board.render(yourPawn, foePawn);
						i--;
					}
				}
			}
			if (pawnPos.getY() < TABLE-1 && pawnPos.getY() > 0 && checkarr[pawnPos.getX()][pawnPos.getY() - 1] == 1 && checkarr[pawnPos.getX()][pawnPos.getY() + 1] == 1){
				for (int i = 0; i < foePawn.size(); i++){
					if (foePawn.get(i).equals(new Position(pawnPos.getX(), pawnPos.getY() - 1))){
						sendRemoveCommandToFoe(foePawn.get(i).getPosition());
						foePawn.remove(foePawn.get(i));
						board.render(yourPawn, foePawn);
					}
					if (foePawn.get(i).equals(new Position(pawnPos.getX(), pawnPos.getY() + 1))){
						sendRemoveCommandToFoe(foePawn.get(i).getPosition());
						foePawn.remove(foePawn.get(i));
						board.render(yourPawn, foePawn);
						i--;
					}
				}
			}
			if (pawnPos.getX() < TABLE-2 && checkarr[pawnPos.getX() + 1][pawnPos.getY()] == 1 && checkarr[pawnPos.getX() + 2][pawnPos.getY()] == 3) {
				for (int i = 0; i < foePawn.size(); i++) {
					if (foePawn.get(i).equals(new Position(pawnPos.getX() + 1, pawnPos.getY()))) {
						sendRemoveCommandToFoe(foePawn.get(i).getPosition());
						foePawn.remove(foePawn.get(i));
						board.render(yourPawn, foePawn);
						i--;
					}
				}
			}
			if (pawnPos.getX() > 1 && checkarr[pawnPos.getX() - 1][pawnPos.getY()] == 1 && checkarr[pawnPos.getX() - 2][pawnPos.getY()] == 3) {
				for (int i = 0; i < foePawn.size(); i++) {
					if (foePawn.get(i).equals(new Position(pawnPos.getX() - 1, pawnPos.getY()))) {
						sendRemoveCommandToFoe(foePawn.get(i).getPosition());
						foePawn.remove(foePawn.get(i));
						board.render(yourPawn, foePawn);
						i--;
					}
				}
			}
			if (pawnPos.getY() < TABLE-2 && checkarr[pawnPos.getX()][pawnPos.getY() + 1] == 1 && checkarr[pawnPos.getX()][pawnPos.getY() + 2] == 3) {
				for (int i = 0; i < foePawn.size(); i++) {
					if (foePawn.get(i).equals(new Position(pawnPos.getX(), pawnPos.getY() + 1))) {
						sendRemoveCommandToFoe(foePawn.get(i).getPosition());
						foePawn.remove(foePawn.get(i));
						board.render(yourPawn, foePawn);
						i--;
					}
				}
			}
			if (pawnPos.getY() > 1 && checkarr[pawnPos.getX()][pawnPos.getY() - 1] == 1 && checkarr[pawnPos.getX()][pawnPos.getY() - 2] == 3) {
				for (int i = 0; i < foePawn.size(); i++) {
					if (foePawn.get(i).equals(new Position(pawnPos.getX(), pawnPos.getY() - 1))) {
						sendRemoveCommandToFoe(foePawn.get(i).getPosition());
						foePawn.remove(foePawn.get(i));
						board.render(yourPawn, foePawn);
						i--;
					}
				}
			}
		}
		checkWinDrawLose();
	}
	
	/**
	 * @see thaigo.property.AbstractRuler#showing()
	 */
	@Override
	public void showing() {
		check();
		for (int i = pawnPos.getY() + 1; i < TABLE; i++) {
			if (checkarr[pawnPos.getX()][i] == 1 || checkarr[pawnPos.getX()][i] == 3)
				break;
			checkarr[pawnPos.getX()][i] = 2;
			board.highLight(new Position(pawnPos.getX(), i));
		}
		for (int i = pawnPos.getY() - 1; i >= 0; i--) {
			if (checkarr[pawnPos.getX()][i] == 1 || checkarr[pawnPos.getX()][i] == 3)
				break;
			checkarr[pawnPos.getX()][i] = 2;
			board.highLight(new Position(pawnPos.getX(), i));
		}
		for (int i = pawnPos.getX() + 1; i < TABLE; i++) {
			if (checkarr[i][pawnPos.getY()] == 1 || checkarr[i][pawnPos.getY()] == 3)
				break;
			checkarr[i][pawnPos.getY()] = 2;
			board.highLight(new Position(i, pawnPos.getY()));
		}
		for (int i = pawnPos.getX() - 1; i >= 0; i--) {
			if (checkarr[i][pawnPos.getY()] == 1 || checkarr[i][pawnPos.getY()] == 3)
				break;
			checkarr[i][pawnPos.getY()] = 2;
			board.highLight(new Position(i, pawnPos.getY()));
		}
	}

	/**
	 * @see thaigo.property.AbstractRuler#checkWinDrawLose()
	 */
	@Override
	public void checkWinDrawLose() {
		if (foePawn.size() == 1 && yourPawn.size() == 1) {
			GameUI.getInstance().draw();
		}
		if (foePawn.size() <= 0) {
			GameUI.getInstance().win();
		}
		if (yourPawn.size() <= 0) {
			GameUI.getInstance().lose();
		}
		updateTask.setToEndPhase();
	}

	/**
	 * Ask for created new game.
	 */
	public void newGame(){
		foePawn.clear();
		yourPawn.clear();
		board.render(yourPawn, foePawn);
		String mode = PropertyManager.getProperty("mode");
		if( mode.equalsIgnoreCase("server") )
			UpdateTask.getInstance(gameUI.getRuler()).setToMainPhase();
		if( mode.equalsIgnoreCase("client") )
			UpdateTask.getInstance(gameUI.getRuler()).setToWaitingPhase();
		initPawn(board);
	}
}