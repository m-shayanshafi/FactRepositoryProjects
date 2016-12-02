package Augments;

public class Lightning extends AugmentDecorator{

	public Lightning(Augment newAugment) {
		super(newAugment);

		//System.out.println("Augment: Adding Lightning");
	}

	public String getName() {
		if(tempAugment.getName() == "None") {
			return "Flame";
		} else {
			return tempAugment.getName() + ", Flame";
		}
	}
	
	public double getModifier() {
		return tempAugment.getModifier() + 2.0;
	}
	
}
