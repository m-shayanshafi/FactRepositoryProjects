package thaigo.object;

import java.awt.Color;

/**
 * Model of <code>Board</code>.
 * 
 * @author Nol Pasurapunya 5510546018
 * @version 9/5/2013
 *
 */
public enum BoardModel {
	
	Classic(new Color(0,0,0), new Color(255,255,255),new Color(100,100,100), new Color(150,150,150)),
	Wood(new Color(56,22,13) ,new Color(205,193,171), new Color(36,72,63), new Color(185,243,221)),
	Stone(new Color(100,100,100),new Color(255,255,255), new Color(100,170,100), new Color(150,210,150)),
	Frosty(new Color(115,158,230),new Color(223,243,249), new Color(180,100,180), new Color(210,130,210)),
	Flame(new Color(140,0,0),new Color(255,210,210), new Color(160,50,50), new Color(210,100,100)),
	Forest(new Color(0,98,0),new Color(183,255,183), new Color(150,100,20), new Color(200,150,70));

	private Color color1,color2;
	private Color hilight1, hilight2;
	
	private BoardModel(Color c1, Color c2, Color hi1, Color hi2){
		color1 = c1;
		color2 = c2;
		hilight1 = hi1;
		hilight2 = hi2;
	}
	
	/**
	 * Return first color.
	 * @return the first color
	 */
	public Color getColor1(){
		return color1;
	}
	
	/**
	 * Return second color.
	 * @return the second color
	 */
	public Color getColor2(){
		return color2;
	}
	
	/**
	 * Return highlight color of the first color.
	 * @return highlight color of the first color
	 */
	public Color getHighlightColor1(){
		return hilight1;
	}
	
	/**
	 * Return highlight color of the second color.
	 * @return highlight color of the second color
	 */
	public Color getHighlightColor2(){
		return hilight2;
	}
}
