package via.aventurica.view.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * Klasse, die es erlaubt, ein Bild in Binärform in die Systemzwischenablage zu kopieren. 
 *@author Snowbound - http://www.snowbound.com/tech_tips/m_system_clipboard.html
 */
public class ClipboardImageData implements Transferable, ClipboardOwner
{

	private byte[] image;

	public ClipboardImageData(byte[] im)
	{
		image = im;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]{DataFlavor.imageFlavor};
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return DataFlavor.imageFlavor.equals(flavor); 
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
	{
		if (!isDataFlavorSupported(flavor)) 
			throw new UnsupportedFlavorException(flavor);
		return Toolkit.getDefaultToolkit().createImage(image);
	}

	public void lostOwnership(java.awt.datatransfer.Clipboard clip, java.awt.datatransfer.Transferable tr)
	{
		return;
	}

}