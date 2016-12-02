package fruitwar.core;

import fruitwar.Rules;

class Action {
	public static final int HIDE = 0;
	public static final int THROW = 1;
	//public static final int MOVE_TO_PREV = 2;
	//public static final int MOVE_TO_NEXT = 3;
	
	/**
	 * Action type
	 */
	int action;
	
	/**
	 * Target of throw. Only valid if action is THROW.
	 */
	int target; 

	/**
	 * Check whether the target is a valid value.
	 * @return
	 */
	boolean isValidTarget(){
		return action == THROW && target >= 0 
			&& target <= Rules.BASKET_ID_MAX();
	}
	
	/**
	 * Convert this action to encoded format
	 * @return
	 */
	static char encodeAction(Action a){
		char c = 'x';
		
		if(a == null)
			c = '_';
		else{
			switch(a.action){
			case HIDE:	c = 'H';	break;
			case THROW:
				if(a.isValidTarget()){
					switch(a.target){
					case 0:	c = '0';	break;
					case 1:	c = '1';	break;
					case 2:	c = '2';	break;
					case 3:	c = '3';	break;
					case 4:	c = '4';	break;
					case 5:	c = '5';	break;
					case 6:	c = '6';	break;
					case 7:	c = '7';	break;
					case 8:	c = '8';	break;
					case 9:	c = '9';	break;
					default: 			break;
					}
				}
				break;
			//case MOVE_TO_PREV:	s = "P .";	break;
			//case MOVE_TO_NEXT:	s = "N .";	break;
			default:						break;
			}	
		}
				
		return c;
	}
	
	/**
	 * Create an Action object from an encoded string. 
	 * @param s
	 * @return
	 */
	static Action decodeAction(char c){
		
		if(c == '_')
			return null;
		
		Action a = new Action();
		if(c == 'H')
			a.action = HIDE;
		else{
			a.action = THROW;
			a.target = -1;
			try{
				a.target = Integer.parseInt(String.valueOf(c));
			}catch (NumberFormatException e){
				e.printStackTrace();
			}
			
			if(!a.isValidTarget())
				return null;
		}
//		else if(c =='P')
//			a.action = MOVE_TO_PREV;
//		else if(c =='N')
//			a.action = MOVE_TO_NEXT;
//		else
//			return null;
		return a;
	}
	
	static Action[] convertStringToActions(String s){
		Action[] actions = new Action[s.length()];
		for(int i = 0; i < s.length(); i++)
			actions[i] = Action.decodeAction(s.charAt(i));
		return actions;
	}
	
	static String convertActionsToString(Action[] actions){
		String s = "";
		for(int i = 0; i < actions.length; i++)
			s += Action.encodeAction(actions[i]);
		return s;
	}
	
	/**
	 * Copy another Action object.
	 * @return
	 */
	Action copy(){
		Action a = new Action();
		a.action = action;
		a.target = target;
		return a;
	}
}
