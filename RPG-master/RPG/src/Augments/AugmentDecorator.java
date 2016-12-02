package Augments;

public abstract class AugmentDecorator implements Augment {
	
	protected Augment tempAugment;
	
	public AugmentDecorator(Augment newAugment) {
		tempAugment = newAugment;
	}
	
	public String getName() {
		return tempAugment.getName();
	}
	
	public double getModifier() {
		return tempAugment.getModifier();
	}

}
