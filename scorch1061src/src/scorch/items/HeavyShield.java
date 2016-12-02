package scorch.items;

/*
  Class:  Shield
  Author: Mikhail Kruk
  Description: 
*/

import scorch.utility.*;
import scorch.ScorchApplet;

public class HeavyShield extends Shield
{
    public HeavyShield()
    {
	type = HeavyShield;
	maxStrength = 3;
	price = 35000;
	damage = 0.99; // fraction of damage absorbed
	thickness = 5;
    }
}
