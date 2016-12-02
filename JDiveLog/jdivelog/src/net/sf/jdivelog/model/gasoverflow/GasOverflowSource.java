package net.sf.jdivelog.model.gasoverflow;

public class GasOverflowSource {
	
	
	
    public GasOverflowSource() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String tankdescription;
	private double tankpressure;
	private double tanksize;
	
	public double getTankpressure() {
		return tankpressure;
	}
	public void setTankpressure(double tankpressure) {
		this.tankpressure = tankpressure;
	}
	public double getTanksize() {
		return tanksize;
	}
	public void setTanksize(double tanksize) {
		this.tanksize = tanksize;
	}

	public String getTankdescription() {
		return tankdescription;
	}
	public void setTankdescription(String tankdescription) {
		this.tankdescription = tankdescription;
	}
        
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<GasOverflowSource>");
        sb.append("<tankdescription>");
        sb.append(tankdescription);
        sb.append("</tankdescription>");
        sb.append("<tankpressure>");
        sb.append(tankpressure);
        sb.append("</tankpressure>");
        sb.append("<tanksize>");
        sb.append(tanksize);
        sb.append("</tanksize>");
        sb.append("</GasOverflowSource>");
        return sb.toString();
    }

}
