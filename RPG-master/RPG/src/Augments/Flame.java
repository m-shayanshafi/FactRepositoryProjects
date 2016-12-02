package Augments;

public class Flame extends AugmentDecorator {

	public Flame(Augment newAugment) {
		super(newAugment);

		//System.out.println("Augment: Adding Flame");
	}

	public String getName() {
		if(tempAugment.getName() == "None") {
			return "Flame";
		} else {
			return tempAugment.getName() + ", Flame";
		}
	}
	
	public double getModifier() {
		return tempAugment.getModifier() + 1.5;
	}
	
}
