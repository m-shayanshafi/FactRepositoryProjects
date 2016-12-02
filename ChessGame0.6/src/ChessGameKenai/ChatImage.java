package ChessGameKenai;

public class ChatImage {

	private String imagePath;
	private static final String IMAGE_SET = " :image ";

	public ChatImage() {
	}

	public ChatImage(String path) {
		this.imagePath = path;
	}

	/**
	 * setImgPath is used to set where the images are on the harddrive
	 * 
	 * @param imgPath
	 *            the path of where the images are stored
	 */
	public void setImgPath(String imgPath) {
		this.imagePath = imgPath;
	}

	public String getImgPath() {
		return imagePath;
	}

	public String getImageSet() {
		return IMAGE_SET;
	}

}
