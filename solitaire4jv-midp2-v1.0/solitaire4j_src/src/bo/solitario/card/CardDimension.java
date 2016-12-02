package bo.solitario.card;

public class CardDimension {

	public int xsize;
	public int ysize;
	public int widthCard;
	public int heightCard;
	public int space;

	public CardDimension(int xsize, int ysize, int widthCard, int heightCard) {

		this.xsize = xsize;
		this.ysize = ysize;
		this.widthCard = widthCard;
		this.heightCard = heightCard;
	}

	public CardDimension(int widthCard, int heightCard, int space) {

		this.widthCard = widthCard;
		this.heightCard = heightCard;
		this.space = space;
	}

	public int getPositionX(int position) {
		// pixelPositionx*dimension.xsize
		int response = position * (space + widthCard) - widthCard;
		return response;
	}

	public int getPositionY(int position) {
		int response = position * (space + heightCard) - heightCard;
		return response;
	}

}
