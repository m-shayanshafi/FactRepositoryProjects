package kw.chinesechess.model;
import java.util.*;

public final class myList<T extends CopyAble> extends ArrayList<T>
{

    public myList()
    {}
    
    @SuppressWarnings("unchecked")
	public myList(myList<T> aList)
    {
    	
    	Iterator<T> it = (Iterator<T>) aList.iterator();
        
        while(it.hasNext())
        {
            piece pc=new piece();
            pc.copyFrom(it.next());   //?? speed
            this.addElement((T) pc);
        }
        
        
    }
            
    public void addElement(T obj)
    {
        if(obj==null)
            throw new NullPointerException();
        this.add(obj);
    }
    
    public void removeAll()
    {
        this.removeAll();
    }
    
    public boolean removeElement(T obj)
    {
    	return this.remove(obj);
    }
    
/*    public void sort()
    {
        myListEntry unsorted=start.next;
        myListEntry sorted;
        piece aPiece;

        start=new myListEntry(start.val, null);
        while(unsorted!=null)
        {
            aPiece=unsorted.val;
            unsorted=unsorted.next;
            sorted=start;
            while(sorted!=null) 
            {
                if(aPiece.priority<sorted.val.priority)
                {
                    sorted.next=new myListEntry(sorted.val, sorted.next);
                    sorted.val=aPiece;  // exchange the pieces between aPiece and sorted
                    break;  //sorted=null;
                }
                else if(sorted.next==null)
                {
                    sorted.next=new myListEntry(aPiece, null);
                    break;  //sorted=null;
                }
                else
                {
                    sorted=sorted.next;
                }
            }
        }
    }  */      
}