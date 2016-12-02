package bo.solitario.card;

import javax.microedition.lcdui.Image;

public class Set {

	private String content[];

	public static String card_A = "A";
	public static String card_2 = "2";
	public static String card_3 = "3";
	public static String card_4 = "4";
	public static String card_5 = "5";
	public static String card_6 = "6";
	public static String card_7 = "7";
	public static String card_8 = "8";
	public static String card_9 = "9";
	public static String card_10 = "10";
	public static String card_J = "J";
	public static String card_K = "K";
	public static String card_Q = "Q";

	public static String HEART_CARD = "HEARDS";
	public static String SPADE_CARD = "SPADES";
	public static String DIAMOND_CARD = "DIAMONS";
	public static String TREBOL_CARD = "TREBOLS";

	public static Image heart;
	public static Image trebol;
	public static Image spades;
	public static Image diamons;
	public static Image backCard;
	public static Image witch;
	public static Image background;
	public static Image mainback;

	static {

		try {

			heart = Image.createImage("/heart.png");
			trebol = Image.createImage("/trebol.png");
			diamons = Image.createImage("/diamond.png");
			spades = Image.createImage("/spades.png");
			background = Image.createImage("/background.png");
			mainback = Image.createImage("/mainback.png");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR " + e.toString());
		}
	}

	private int counter;

	public Set() {

		counter = 0;

		content = new String[13];
		content[0] = card_A;
		content[1] = card_2;
		content[2] = card_3;
		content[3] = card_4;
		content[4] = card_5;
		content[5] = card_6;
		content[6] = card_7;
		content[7] = card_8;
		content[8] = card_9;
		content[9] = card_10;
		content[10] = card_J;
		content[11] = card_Q;
		content[12] = card_K;
	}

	public String[] getContent() {
		return content;
	}

	public void setContent(String content[]) {
		this.content = content;
	}

	public String getNext() {
		return content[counter];
	}

	public void reset() {
		counter = 0;
	}

	public void backCard() {
		if (counter > 0) {
			counter--;
		}
	}

	public boolean jumpNext() {

		//#debug info
		System.out.println(" * SET counter " + counter + "  :   length"
				+ content.length);

		if (counter <= content.length) {
			counter++;
			return true;
		} else {

			return false;
		}
	}

	public boolean isFull() {

		//#debug info
		System.out.println("IS FULL " + (counter == content.length));

		return counter == content.length;
	}

}
