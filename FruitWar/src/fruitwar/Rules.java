package fruitwar;

/**
 * Define all the numeric constants of Fruit War.
 * Values are provided through method rather than public method, because
 * if we change the constants, it's a disaster for the already-built robots!
 *  
 * @author wangnan
 *
 */
public class Rules {
	
	private static final int BASKET_CNT = 3;
	
	/**
	 * How many baskets do we have each side.
	 */
	public static final int BASKET_CNT(){
		return BASKET_CNT;
	}
	
	/**
	 * Max basket ID. Basket ID range: [0, BASKET_ID_MAX].
	 */
	public static final int BASKET_ID_MAX(){
		return BASKET_CNT - 1;
	}
	
	private static final int COWARD_LIMIT = 5;
	
	/**
	 * Limit of continuous rounds without attack.
	 * A fruit thrower exceeds this limit will be treated as
	 * "out of play". 
	 */
	public static final int COWARD_LIMIT(){
		return COWARD_LIMIT;
	}
	
	private static final int ROUND_TOTAL = 1000;
	
	/**
	 * How many rounds in total.
	 */
	public static final int ROUND_TOTAL(){
		return ROUND_TOTAL;
	}
	
	
	private static final int FRUIT_THROWER_HP = 100;
	
	/**
	 * Hit Points (HP) of fruit thrower 
	 */
	public static final int FRUIT_THROWER_HP(){
		return FRUIT_THROWER_HP;
	}
	
	
	//public static final int ROBOT_CALCULATION_TIMEOUT = 0;
	private static final int ROBOT_CALCULATION_TIMEOUT = 2000;
	
	/**
	 * Time limit for each calculation of robot in milliseconds. The calculation
	 * includes methods: "IFruitWarRobot.strategy" and "IFruitWarRobot.result".
	 * If any of these methods take longer than this value to complete,
	 * it's treated as violation and results lose.
	 * If this value is 0, it means no time limitation.
	 */
	public static final int ROBOT_CALCULATION_TIMEOUT(){
		return ROBOT_CALCULATION_TIMEOUT;
	}

	
	private static final int ROBOT_CALCULATION_TOTAL_TIMEOUT = 10000;		//(not supported yet)
	
	/**
	 * Time limit (in milliseconds) for the total time a robot have. 
	 * The calculation includes methods: "IFruitWarRobot.strategy" 
	 * and "IFruitWarRobot.result".
	 */
	public static final int ROBOT_CALCULATION_TOTAL_TIMEOUT(){
		return ROBOT_CALCULATION_TOTAL_TIMEOUT;
	}


}
