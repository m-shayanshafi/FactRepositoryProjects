/*This class sets the actions that the character is to perform
*/
import java.util.ArrayList;
import java.lang.Thread;

public class Motion extends Thread{
private Character c;
private Item i;
private ArrayList<Arrow> arrows;
private static int upperBound = 0;
private static int leftBound = 0;
private static int rightBound = 740;
private static int lowerBound = 465;
public Motion(Character c){this.c=c;this.start();}
public Motion (Item i, ArrayList<Arrow> arrows){
	this.i = i;
	this.arrows = arrows;
	this.start();
}


public void run(){
	if(c!=null){
		while(c.getMotionBool()){//Start thread loop
		if(c.getID().equals("Link")) 
		{
			if(c.getDoing()=="Stand"){
				c.setDoingBool(true);
				c.setImage(DungeonMaster.getImage(c.getID()+"Stand"+c.getDirection()));
				while(c.getDoingBool()){try{sleep(c.getSleeper());}catch(InterruptedException e){e.printStackTrace();}}
			}
			else if(c.getDoing()=="Move"){
				c.setDoingBool(true);
				String direction=c.getDirection();
				for(int frame=1;c.getDoingBool();frame++){
					if(frame>=8){frame=1;}
					c.setImage(DungeonMaster.getImage(c.getID()+"Move"+direction+frame));
					if(direction=="Left")
					{
						if(c.getX() > leftBound)
						{
							c.setX(-c.getDistance());
						}
						else
						{
							c.setX(0);
						}
					}
					else if(direction=="Right")
					{
						if(c.getX() < rightBound)
						{
							c.setX(c.getDistance());
						}
						else
						{
							c.setX(0);
						}
					}
					else if(direction=="Up")
					{
						if(c.getY() > upperBound)
						{
							c.setY(-c.getDistance());
						}
						else
						{
							c.setY(0);
						}
					}
					else //down
					{
						if(c.getY() < lowerBound)
						{
							c.setY(c.getDistance());
						}
						else
						{
							c.setY(0);
						}
					}
					try{sleep(c.getSleeper());}catch(InterruptedException e){}
				}
			}
			else if(c.getDoing()=="Attack"){
				String direction=c.getDirection();
				for(int frame=1;frame<10;frame++){
					c.setImage(DungeonMaster.getImage("LinkAttack"+direction+frame));
					try{sleep(c.getSleeper()/3);}catch(InterruptedException e){}
				}
				if(c.getDoing()=="Attack"){c.setDoing("Stand");}
			}
			else if(c.getDoing()=="Shoot"){
				String direction=c.getDirection();
				for(int frame=1;frame<6;frame++){
					c.setImage(DungeonMaster.getImage("LinkArrow"+direction+frame));
					try{sleep(c.getSleeper()/3);}catch(InterruptedException e){}
				}
				if(c.getDoing()=="Shoot"){c.setDoing("Stand");}
			}
			else{c.setDoing("Stand");}
		}
		else if(c.getID().equals("Knight"))
		{
			if(c.getDoing()=="Stand"){
				c.setDoingBool(true);
				c.setImage(DungeonMaster.getImage(c.getID()+"Stand"+c.getDirection()));
				while(c.getDoingBool()){try{sleep(c.getSleeper());}catch(InterruptedException e){e.printStackTrace();}}
			}
			else if(c.getDoing()=="Move"){
				c.setDoingBool(true);
				String direction=c.getDirection();
				for(int frame=1;c.getDoingBool();frame++)
				{
					if(frame>=4)
					{
						frame=1;
					}
					c.setImage(DungeonMaster.getImage(c.getID()+"Move"+direction+frame));
					if(direction=="Left")
					{
						if(c.getX() > leftBound)
						{
							c.setX(-c.getDistance());
						}
						else
						{
							c.setX(0);
						}
					}
					else if(direction=="Right")
					{
						if(c.getX() < rightBound)
						{
							c.setX(c.getDistance());
						}
						else
						{
							c.setX(0);
						}
					}
					else if(direction=="Up")
					{
						if(c.getY() > upperBound)
						{
							c.setY(-c.getDistance());
						}
						else
						{
							c.setY(0);
						}
					}
					else //down
					{
						if(c.getY() < lowerBound)
						{
							c.setY(c.getDistance());
						}
						else
						{
							c.setY(0);
						}
					}
					try{sleep(c.getSleeper());}catch(InterruptedException e){}
				}
			}
			else if(c.getDoing()=="Attack"){
				String direction=c.getDirection();
				for(int frame=1;frame<4;frame++){
					c.setImage(DungeonMaster.getImage("KnightMove"+direction+frame));
					try{sleep(c.getSleeper()/3);}catch(InterruptedException e){}
				}
				if(c.getDoing()=="Attack"){c.setDoing("Stand");}
			}
			else{c.setDoing("Stand");}
		}
		}//end while loop
	}
	else if(i!=null){
	while(i.getMotionBool())
	{
		if(i.getID().equals("Arrow")) //movement logic for an arrow
		{
			if(i.getDoing().equals("Move"))
			{
				String direction = i.getDirection();
				if(direction=="Left")
				{
					if(i.getX() > leftBound)
					{
						i.setX(-i.getDistance());
					}
					else
					{
						i.setX(0);
						arrows.remove(i);
					}
				}
				else if(direction=="Right")
				{
					if(i.getX() < rightBound)
					{
						i.setX(i.getDistance());
					}
					else
					{
						i.setX(0);
						arrows.remove(i);
					}
				}
				else if(direction=="Up")
				{
					if(i.getY() > upperBound)
					{
						i.setY(-i.getDistance());
					}
					else
					{
						i.setY(0);
						arrows.remove(i);
					}
				}
				else //down
				{
					if(i.getY() < lowerBound)
					{
						i.setY(i.getDistance());
					}
					else
					{
						i.setY(0);
						arrows.remove(i);
					}
				}
				try{sleep(i.getSleeper());}catch(InterruptedException e){}
			}
		}
	}
	}
}//End run()
}//end class
