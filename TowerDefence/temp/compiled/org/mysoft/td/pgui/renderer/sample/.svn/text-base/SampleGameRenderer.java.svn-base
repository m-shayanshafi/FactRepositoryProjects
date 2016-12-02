package org.mysoft.td.pgui.renderer.sample;

import org.mysoft.td.engine.action.GenericActionController;
import org.mysoft.td.engine.objects.sample.ArtilleryTurrent;
import org.mysoft.td.engine.objects.sample.MachineGunTurrent;
import org.mysoft.td.engine.objects.sample.SampleEnemy;
import org.mysoft.td.engine.world.sample.SampleWorld;
import org.mysoft.td.pgui.renderer.GenericGameRenderer;
import org.mysoft.td.pgui.renderer.RendererConfig;

import processing.core.PGraphics;

public class SampleGameRenderer extends GenericGameRenderer {

	public SampleGameRenderer(PGraphics g, GenericActionController ctrl) {
		super(g, ctrl, new RendererConfig(10));
	}

	@Override
	public void registerRenderers() {
		manager.registerRenderer(new SampleWorldRenderer(g, config), SampleWorld.class);
		manager.registerRenderer(new MachineGunTurrentRenderer(g, config), MachineGunTurrent.class);
		manager.registerRenderer(new SampleEnemyRenderer(g, config), SampleEnemy.class);
		manager.registerRenderer(new ArtilleryTurrentRenderer(g, config), ArtilleryTurrent.class);
	}

	@Override
	protected void customRender() {

	
	}
	
}
