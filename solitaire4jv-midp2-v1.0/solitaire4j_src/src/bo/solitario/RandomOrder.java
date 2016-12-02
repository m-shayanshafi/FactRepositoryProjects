package bo.solitario;

import bo.solitario.card.Card;
import bo.solitario.card.Set;
import bo.solitario.position.RandomPosition;

public class RandomOrder extends AbstractOrder {

	private Card card[];

	public RandomOrder() {

		card = new Card[52];

		card[0] = new Card(Set.heart, Set.card_A, Set.HEART_CARD);

		card[1] = new Card(Set.heart, Set.card_2, Set.HEART_CARD);

		card[2] = new Card(Set.heart, Set.card_3, Set.HEART_CARD);

		card[3] = new Card(Set.heart, Set.card_4, Set.HEART_CARD);

		card[4] = new Card(Set.heart, Set.card_5, Set.HEART_CARD);

		card[5] = new Card(Set.heart, Set.card_6, Set.HEART_CARD);

		card[6] = new Card(Set.heart, Set.card_7, Set.HEART_CARD);

		card[7] = new Card(Set.heart, Set.card_8, Set.HEART_CARD);

		card[8] = new Card(Set.heart, Set.card_9, Set.HEART_CARD);

		card[9] = new Card(Set.heart, Set.card_10, Set.HEART_CARD);

		card[10] = new Card(Set.heart, Set.card_J, Set.HEART_CARD);

		card[11] = new Card(Set.heart, Set.card_Q, Set.HEART_CARD);

		card[12] = new Card(Set.heart, Set.card_K, Set.HEART_CARD);

		card[13] = new Card(Set.trebol, Set.card_A, Set.TREBOL_CARD);

		card[14] = new Card(Set.trebol, Set.card_2, Set.TREBOL_CARD);

		card[15] = new Card(Set.trebol, Set.card_3, Set.TREBOL_CARD);

		card[16] = new Card(Set.trebol, Set.card_4, Set.TREBOL_CARD);

		card[17] = new Card(Set.trebol, Set.card_5, Set.TREBOL_CARD);

		card[18] = new Card(Set.trebol, Set.card_6, Set.TREBOL_CARD);

		card[19] = new Card(Set.trebol, Set.card_7, Set.TREBOL_CARD);

		card[20] = new Card(Set.trebol, Set.card_8, Set.TREBOL_CARD);

		card[21] = new Card(Set.trebol, Set.card_9, Set.TREBOL_CARD);

		card[22] = new Card(Set.trebol, Set.card_10, Set.TREBOL_CARD);

		card[23] = new Card(Set.trebol, Set.card_J, Set.TREBOL_CARD);

		card[24] = new Card(Set.trebol, Set.card_Q, Set.TREBOL_CARD);

		card[25] = new Card(Set.trebol, Set.card_K, Set.TREBOL_CARD);

		card[26] = new Card(Set.diamons, Set.card_A, Set.DIAMOND_CARD);

		card[27] = new Card(Set.diamons, Set.card_2, Set.DIAMOND_CARD);

		card[28] = new Card(Set.diamons, Set.card_3, Set.DIAMOND_CARD);

		card[29] = new Card(Set.diamons, Set.card_4, Set.DIAMOND_CARD);

		card[30] = new Card(Set.diamons, Set.card_5, Set.DIAMOND_CARD);

		card[31] = new Card(Set.diamons, Set.card_6, Set.DIAMOND_CARD);

		card[32] = new Card(Set.diamons, Set.card_7, Set.DIAMOND_CARD);

		card[33] = new Card(Set.diamons, Set.card_8, Set.DIAMOND_CARD);

		card[34] = new Card(Set.diamons, Set.card_9, Set.DIAMOND_CARD);

		card[35] = new Card(Set.diamons, Set.card_10, Set.DIAMOND_CARD);

		card[36] = new Card(Set.diamons, Set.card_J, Set.DIAMOND_CARD);

		card[37] = new Card(Set.diamons, Set.card_Q, Set.DIAMOND_CARD);

		card[38] = new Card(Set.diamons, Set.card_K, Set.DIAMOND_CARD);

		card[39] = new Card(Set.spades, Set.card_A, Set.SPADE_CARD);

		card[40] = new Card(Set.spades, Set.card_2, Set.SPADE_CARD);

		card[41] = new Card(Set.spades, Set.card_3, Set.SPADE_CARD);

		card[42] = new Card(Set.spades, Set.card_4, Set.SPADE_CARD);

		card[43] = new Card(Set.spades, Set.card_5, Set.SPADE_CARD);

		card[44] = new Card(Set.spades, Set.card_6, Set.SPADE_CARD);

		card[45] = new Card(Set.spades, Set.card_7, Set.SPADE_CARD);

		card[46] = new Card(Set.spades, Set.card_8, Set.SPADE_CARD);

		card[47] = new Card(Set.spades, Set.card_9, Set.SPADE_CARD);

		card[48] = new Card(Set.spades, Set.card_10, Set.SPADE_CARD);

		card[49] = new Card(Set.spades, Set.card_J, Set.SPADE_CARD);

		card[50] = new Card(Set.spades, Set.card_Q, Set.SPADE_CARD);

		card[51] = new Card(Set.spades, Set.card_K, Set.SPADE_CARD);

	}

	static int MAX_CARD = 52;

	public void setCard(RandomPosition position, int number) {

		// System.out.print("Set [ ");
		for (int i = 0; i < number; i++) {
			Card card = getCard();
			Card response = new Card(card.getImage(), card.getName(), card
					.getPalo());
			response.setOpen(false);
			// System.out.print(" ["+response.getName()+" "+response.getPalo()
			// +"] ");
			position.addCloseCard(response);
		}
		long end = System.currentTimeMillis();
		// System.out.println(" ]");

	}

	public void restart() {
		for (int i = 0; i < card.length; i++) {
			card[i].setOpen(false);
		}
	}

	private Card getCard() {

		long time = RandomOrder.random();
		// System.out.print(" random "+time+" \t" );

		int temp = (int) time;

		Card select = card[temp];
		while (select.isOpen()) {
			long random = RandomOrder.random();
			if (random % 3 == 0) {
				while (select.isOpen()) {
					int num = (++temp) % 52;
					select = card[num];
				}
			} else {
				while (select.isOpen()) {
					temp = temp == 0 ? 52 : temp;
					int num = --temp;
					select = card[num];
				}
			}
		}
		select.setOpen(true);
		return select;
	}

	private static long random() {
		long time = System.currentTimeMillis();
		long first = time % 10 + 1;
		time = time / 10;
		long second = time % 10 + 1;
		time = time / 10;
		long third = time % 10 + 1;
		// System.out.println("seed "+second+" "+first+" "+third);
		long random = ((second * first * third) + (second + first)) % 52;
		// System.out.println("Random "+random);
		try {
			Thread.sleep(random / 3);
		} catch (Exception e) {
			System.out.println("ERROR " + e.toString());
		}

		return random;
	}

	/**
	 * @comment testing mehod
	 */
	private static void test() {
		int i = 0;
		while (i < 20) {
			try {
				long num = random();
				Thread.sleep(num);
			} catch (Exception e) {
				System.out.println("ERROR " + e.toString());
			}
			i++;
		}
	}

	/**
	 * @comment Debuging method
	 */
	private void showOpen() {

		System.out.println(" OPEN CARDS");
		for (int i = 0; i < card.length; i++) {
			if (!card[i].isOpen()) {
				System.out.println(" Card " + card[i].getName() + ", "
						+ card[i].getPalo());
			}
		}

	}

	public static void main(String arg[]) {

		RandomOrder random = new RandomOrder();

		long init = System.currentTimeMillis();

		// System.out.println("INIT SECUENCE 1 ");
		random.setCard(null, 1);

		// System.out.println("INIT SECUENCE 2 ");
		random.setCard(null, 2);

		// System.out.println("INIT SECUENCE 3 ");
		random.setCard(null, 3);

		// System.out.println("INIT SECUENCE 4 ");
		random.setCard(null, 4);

		// System.out.println("INIT SECUENCE 5 ");
		random.setCard(null, 5);

		// System.out.println("INIT SECUENCE 6 ");
		random.setCard(null, 6);

		// System.out.println("INIT SECUENCE 7 ");
		random.setCard(null, 7);

		// System.out.println("INIT SECUENCE 24 ");
		random.setCard(null, 24);
		random.showOpen();
		long end = System.currentTimeMillis();
		System.out.println(" TIME : " + (end - init));
	}

}
