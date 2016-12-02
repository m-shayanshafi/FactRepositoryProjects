package bo.solitario;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import bo.solitario.card.Card;
import bo.solitario.card.CardDimension;
import bo.solitario.card.Set;

import bo.solitario.position.FinalPosition;
import bo.solitario.position.GamePosition;
import bo.solitario.position.MainPosition;
import bo.solitario.position.OpenPosition;
import bo.solitario.position.CardPosition;
import bo.solitario.position.RandomPosition;
import bo.solitario.position.WinSplash;

public class Game extends Canvas
// implements CommandListener
{

	// Grid Unit X and Y
	// private final int Xunit=28;
	// private final int Yunit=16;

	private int xsize;
	private int ysize;
	private int widthCard;
	private int heightCard;
	private int width;
	private int height;

	public CardPosition position[];

	public CardDimension dimension;

	private AbstractOrder random = new RandomOrder();

	private Command menuCommand;

	public Game(final Menu menu) {

		width = super.getWidth();
		height = super.getHeight();

		// tamano de cada celda
		//
		// xsize = (width*13)/100;
		// xsize = width/Xunit;
		// ysize = height/Yunit;

		int space = (width * 3) / 100;

		// IT DEPENDS RESOLUTION DEVICE
		widthCard = (width * 11) / 100;
		// heightCard= 3*ysize
		heightCard = (height * 20) / 100;

		dimension = new CardDimension(widthCard, heightCard, space);

		position = new CardPosition[13];

		position[1] = new OpenPosition(this);

		position[0] = new MainPosition(this, position[1]);

		position[4] = new FinalPosition(this, 4, 1);
		position[5] = new FinalPosition(this, 5, 1);
		position[3] = new FinalPosition(this, 6, 1);
		position[2] = new FinalPosition(this, 7, 1);

		position[6] = new GamePosition(this, 1, 2);
		position[7] = new GamePosition(this, 2, 2);

		position[8] = new GamePosition(this, 3, 2);
		position[9] = new GamePosition(this, 4, 2);
		position[10] = new GamePosition(this, 5, 2);
		position[11] = new GamePosition(this, 6, 2);
		position[12] = new GamePosition(this, 7, 2);

		menuCommand = new Command("MENU", Command.ITEM, 0);

		this.setCommandListener(new CommandListener() {
			public void commandAction(Command arg0, Displayable arg1) {
				menu.show();
			}
		});

		this.addCommand(menuCommand);
	}

	public void startGame() {

		//#debug info
		System.out.println("START GAME 1");

		// we start in the 1, 1 position
		actPosX = 1;
		actPosY = 1;
		WinSplash.getSingleton().reset();
		Preference.reset();

		for (int i = 0; i < position.length; i++) {
			position[i].restart();
			movingCard = false;
		}

		random.restart();

		random.setCard((RandomPosition) position[0], 24);
		random.setCard((RandomPosition) position[6], 1);
		random.setCard((RandomPosition) position[7], 2);
		random.setCard((RandomPosition) position[8], 3);
		random.setCard((RandomPosition) position[9], 4);
		random.setCard((RandomPosition) position[10], 5);
		random.setCard((RandomPosition) position[11], 6);
		random.setCard((RandomPosition) position[12], 7);

		//#debug info
		System.out.println("START GAME 3");

		repaint();
	}

	protected void paint(Graphics g) {

		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
				Font.SIZE_LARGE));
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, width, height);
		g.setColor(0, 0, 0);
		g.drawImage(Set.background, 0, 0, Graphics.TOP | Graphics.LEFT);
		for (int i = 0; i < position.length; i++) {
			position[i].draw(g, actPosX, actPosY, movingCard,
					(position[i] == lastPosition));
		}

	}

	private int maxPosX = 7;
	private int maxPosY = 2;

	private int actPosX = 1;
	private int actPosY = 1;

	public void keyPressed(int codigo) {

		CardPosition position = getPosition();

		// code of the action game
		int cod = getGameAction(codigo);

		switch (cod) {
		case Canvas.LEFT: {
			if (actPosX != 1) {
				actPosX = actPosX - 1;
			}
			// we skip the blank 3 position
			if (actPosY == 1 && actPosX == 3) {
				actPosX = 2;
			}
		}
			break;
		case Canvas.RIGHT: {

			if (actPosX != maxPosX) {
				actPosX = actPosX + 1;
			}

			if (actPosY == 1 && actPosX == 3) {
				actPosX = 4;
			}
		}
			break;

		case Canvas.UP: {
			if (movingCard || !position.moveInside(Canvas.UP)) {
				actPosY = actPosY == 1 ? actPosY : actPosY - 1;
				if (actPosY == 1 && actPosX == 3) {
					actPosX = 2;
				}
			}
		}
			break;

		case Canvas.DOWN: {
			if (movingCard || !position.moveInside(Canvas.DOWN)) {
				actPosY = actPosY == maxPosY ? actPosY : actPosY + 1;
			}

		}
			break;

		case Canvas.FIRE: {
			setFire();
		}
			break;

		}
		// repaint(0,40,super.getWidth(),super.getHeight());
		repaint();
	}

	private boolean movingCard = false;

	private CardPosition lastPosition;

	private void setFire() {

		if (!movingCard) {
			// moving around
			CardPosition position = getPosition();
			if (position.isCloseCard()) {
				position.openCard();
				// movingCard = false;
			} else {
				movingCard = position.getCard() != null;
				lastPosition = position;
			}
		} else {
			CardPosition current = getPosition();
			// This can return a Card Object or a vector
			/**
			 * Object obj=current.getCard(); if(obj !=null){ Card card=null;
			 * if(obj instanceof Card){ card= (Card)obj; } else{ card=
			 * (Card)((Vector)obj).firstElement(); } //#mdebug info
			 * System.out.println("SELECTED CURRENT CARD "+ card.getName()+" "
			 * +card.getPalo()); //#enddebug } //#debug info
			 * System.out.println(" CURRENT "+lastPosition.current+"
			 * "+current.current);
			 */

			if (!lastPosition.equals(current) && !current.isCloseCard()
					&& current.putCard(lastPosition.getCard())// getCard
																// method can
																// return a Card
																// Object or a
																// vector
			) {
				lastPosition.removeCard(lastPosition.getCard());
			}

			lastPosition = null;
			movingCard = false;
		}
	}

	private CardPosition getPosition() {
		int res = 0;
		boolean next = true;
		int i = 0;
		while (i < position.length && next) {
			if (position[i].isPosition(actPosX, actPosY)) {
				res = i;
				next = false;
			}
			i++;
		}
		return position[res];
	}
}
