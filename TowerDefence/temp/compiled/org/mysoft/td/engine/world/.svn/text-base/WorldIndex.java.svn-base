package org.mysoft.td.engine.world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mysoft.gameutils.data.base.IntPoint;
import org.mysoft.td.engine.objects.GenericObject;
import org.mysoft.td.engine.objects.GenericTurrent;

public class WorldIndex {

	private boolean needRebuild = true;
	
	private List<GenericObject> objectList;
	private List<GenericTurrent> emptyTurrentList = new ArrayList<GenericTurrent>();
	
	private GenericObject[][] objects;
	private int sizeX, sizeY;
	
	private LinkedList<GenericTurrent>[][] ranges;
	
	@SuppressWarnings("unchecked")
	public WorldIndex(GenericWorld world) {
		sizeX = world.getWidth();
		sizeY = world.getHeight();
		objects = new GenericObject[sizeX][sizeY];
		ranges = new LinkedList[sizeX][sizeY];
		objectList = new LinkedList<GenericObject>();
	}
	
	private boolean isInRange(int x, int y) {
		return x>=0 && x<sizeX && y>=0 && y<sizeY;
	}
	
	private void assertInRange(int x, int y) {
		if(!isInRange(x, y))
			throw new RuntimeException("(" + x + ", " + y + ") not in range.");
	}
	
	public void putObject(GenericObject object, IntPoint position) {
		putObject(object, position.x, position.y);
	}

	public void putObject(GenericObject object, int x, int y) {
		assertInRange(x, y);

		for(int i = x; i < x + object.getWidth(); i++)
			for(int j = y; j < y + object.getHeight(); j++)
				objects[i][j] = object;
		
		objectList.add(object);
		needRebuild = true;
	}
	
	public GenericObject getObjectAt(IntPoint position) {
		return getObjectAt(position.x, position.y);
	}

	public GenericObject getObjectAt(int x, int y) {
		assertInRange(x, y);
		return objects[x][y];
	}
	
	public GenericObject removeObjectAt(IntPoint position) {
		return removeObjectAt(position.x, position.y);
	}
	
	public GenericObject removeObjectAt(int x, int y) {
		assertInRange(x, y);
		GenericObject o = objects[x][y];
		objectList.remove(objects[x][y]);
		objects[x][y] = null;
		needRebuild = true;
		return o;
	}
	
	protected void clearRanges() {
		for(int i=0; i<sizeX; i++)
			for(int j=0; j<sizeY; j++) {
				if(ranges[i][j] != null)
					ranges[i][j].clear();
				else
					ranges[i][j] = new LinkedList<GenericTurrent>();
			}
	}
	
	protected void calculateRanges() {
		clearRanges();
		
		for(GenericObject o: objectList) {
			if(o instanceof GenericTurrent) {
				GenericTurrent t = (GenericTurrent)o;
				
				int fx = (int)Math.round(t.getPosition().x - t.getRange() - 1);
				int fy = (int)Math.round(t.getPosition().y - t.getRange() - 1);
				int tx = (int)Math.round(t.getPosition().x + t.getRange() + 1);
				int ty = (int)Math.round(t.getPosition().y + t.getRange() + 1);
				
				fx = fx < 0 ? 0 : fx;
				tx = tx >= sizeX ? sizeX - 1 : tx;
				fy = fy < 0 ? 0 : fy;
				ty = ty >= sizeY ? sizeY - 1 : ty;
				
				for(int i=fx; i<=tx; i++)
					for(int j=fy; j<ty; j++) {
						if(t.isInRange(i + 0.5, j + 0.5))
							if(!ranges[i][j].contains(t))
								ranges[i][j].add(t);
					}
				
			}
		}
		
		needRebuild = false;
	}

	public List<GenericTurrent> getTurrentsInRange(IntPoint position) {
		return getTurrentsInRange(position.x, position.y);
	}
	
	public List<GenericTurrent> getTurrentsInRange(int x, int y) {
		if(needRebuild)
			calculateRanges();
		
		if(!isInRange(x, y) || ranges[x][y] == null)
			return (List<GenericTurrent>)emptyTurrentList;
		else
			return (List<GenericTurrent>)ranges[x][y];
	}
	
	public List<GenericObject> getObjectList() {
		return objectList;
	}
}
