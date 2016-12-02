package org.mysoft.td.pgui.renderer;

import java.util.HashMap;
import java.util.Map;

import org.mysoft.td.engine.objects.GenericObject;
import org.mysoft.td.engine.world.GenericWorld;

public class RendererManager {

	private static RendererManager instance;
	
	private Map<String, GraphicsRenderer> renderers = new HashMap<String, GraphicsRenderer>();
	
	public static RendererManager getInstance() {
		if(instance == null)
			instance = new RendererManager();
		return instance;
	}
	
	public <T extends GenericObject> GenericObjectRenderer<T> getObjectRenderer(T obj) {
		return (GenericObjectRenderer<T>)renderers.get(obj.getClass().getName());
	}
	
	public <T extends GenericWorld> GenericWorldRenderer<T> getWorldRenderer(T world) {
		return (GenericWorldRenderer<T>)renderers.get(world.getClass().getName());
	}
	
	public void registerRenderer(GraphicsRenderer renderer, Class type) {
		renderers.put(type.getName(), renderer);
	}
	
}
