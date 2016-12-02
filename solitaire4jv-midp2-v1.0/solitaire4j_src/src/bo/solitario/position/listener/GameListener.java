package bo.solitario.position.listener;

public class GameListener implements FireListener {

	public void actionEvent(FireEvent ev) {
	}

}

/*
 * public void setFire(){
 * 
 * //#debug info ///System.out.println(" INT FIRE x: "+actPosX+" y: "+actPosY);
 * 
 * int index= getCard(); Position position = ev.getPosition();
 * 
 * if( position.isCloseCard() ){ position.openCard(); move = false; }else{
 * 
 * //#debug info System.out.println(" Index : "+index +" PUT "+put+" move:
 * "+move);
 * 
 * if( put ){
 * 
 * //#debug info System.out.println(" Index : "+index +" PUT "+put+" move:
 * "+move+ " remove : "+remove +" selected: "+selectedPlayCard);
 * 
 * if( index != 1 && remove != index && selectedPlayCard != null &&
 * position[index].putCard(selectedPlayCard) ){
 * position[remove].removeCard(selectedPlayCard); } put=false; move=false; }
 * else if(move){ selectedPlayCard =position[index].getCard(); remove=index;
 * put= true; } } }
 */
