package util;


import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Location;

public class ConvertUtil {

/*	public static Location getLocationFromPonit(Point p){

		  return new Location((p.x)/size,(p.y)/size);
		  
		}*/
	
 /*public static Point getPosition(Location l){
	   return new Point(l.getX()*UI.size,l.getY()*size);
 }*/
	
	public static byte[] img2Bytes(BufferedImage img){
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
        try {
			 ImageIO.write(img, "bmp", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
       return out.toByteArray();  
	}
	
	public static BufferedImage bytes2Img(byte[] bytes) {
		BufferedImage bi = null;
		try {
			ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
			bi = ImageIO.read(bin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return   bi;
	}
}
