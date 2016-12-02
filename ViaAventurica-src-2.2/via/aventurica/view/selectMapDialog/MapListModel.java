package via.aventurica.view.selectMapDialog;

import java.util.HashSet;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import via.aventurica.model.map.MapCollection;

public class MapListModel implements ListModel {
	private final static long serialVersionUID = 1L;

	private final List<MapCollection> mapCollections; 
	private final HashSet<ListDataListener> listeners = new HashSet<ListDataListener>();
	
	public MapListModel(List<MapCollection> mapCollections) {
		super();
		this.mapCollections = mapCollections;
	}

	public Object getElementAt(int index) {
		for(MapCollection coll : mapCollections) {
			if(index == 0)
				return coll; 
			index--; 
			if(index < coll.size())
				return coll.get(index); 
			index-=coll.size(); 
		}
		return null; 
	}

	public int getSize() {
		int size = 0; 
		for(MapCollection coll : mapCollections) 
			size+= coll.size() + 1; 
		return size; 
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l); 
	}
	
	public void addListDataListener(ListDataListener l) {
		listeners.add(l); 
	}
	
	public void fireListChanged() { 
		ListDataEvent evt = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()); 
		for(ListDataListener listener : listeners)
			listener.contentsChanged(evt);
	}
}
