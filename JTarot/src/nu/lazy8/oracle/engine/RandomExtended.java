
package nu.lazy8.oracle.engine;


public class RandomExtended extends java.util.Random{
	public int get12bitInt(){
		return next(12);
	}
	public boolean getBoolean(){
		int oneBit= next(1);
		return (oneBit==0);
	}
}
