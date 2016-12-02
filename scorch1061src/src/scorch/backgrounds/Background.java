package scorch.backgrounds;

/*
  Class:  Background
  Author: Mikhail Kruk

  Description: the parent class of all the Scorch backgrounds
*/

public abstract class Background
{
    public int width, height;
   
    public Background(int width, int height)
	{
	    this.width = width;
	    this.height = height;
	}

    public abstract int getPixelColor(int x, int y);
}
