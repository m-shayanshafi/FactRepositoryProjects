package Augments;

public class Frost extends AugmentDecorator{

	public Frost(Augment newAugment) {
		super(newAugment);
		
		//System.out.println("Augment: Adding Frost");
		
	}
	
	public String getName() {
		if(tempAugment.getName() == "None") {
			return "Frost";
		} else {
			return tempAugment.getName() + ", Frost";
		}
	}
	
	public double getModifier() {
		return tempAugment.getModifier() + 1.5;
	}
}
