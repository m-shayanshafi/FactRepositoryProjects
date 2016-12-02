//	Copyright 2009 Nicolas Devere
//
//	This file is part of FLESH SNATCHER.
//
//	FLESH SNATCHER is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	FLESH SNATCHER is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with FLESH SNATCHER; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package entity;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import jglcore.JGL_3DVector;
import phys.Motion;
import phys.Motion_NoCollision;
import phys.Mover;
import phys.Mover_none;
import phys.Shape;
import phys.Shape_aabb;
import phys.Trace;

//import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
//import com.jme.scene.Spatial.LightCombineMode;
//import com.jme.scene.Spatial.TextureCombineMode;
//import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;


/**
 * 
 * 
 * @author Nicolas Devere
 *
 */
public class Decal implements Entity {
	
	private int state;
	
	private TriMesh mesh;
	
	private Mover cmover;
	private Motion cmotion;
	private Shape cshape;
	private JGL_3DVector angles;
	
	private long deltaTime, startTime;
	
	
	public Decal(Vector verts, Vector texs, Texture texture, long lifeTime) {
		
		int i;
		
		float[] tmpFloat = new float[verts.size()];
		for (i=0; i<verts.size(); i++) tmpFloat[i] = ((Float)verts.get(i)).floatValue();
		FloatBuffer vbuf = BufferUtils.createFloatBuffer(tmpFloat);
		
		tmpFloat = new float[texs.size()];
		for (i=0; i<texs.size(); i++) tmpFloat[i] = ((Float)texs.get(i)).floatValue();
		FloatBuffer tbuf = BufferUtils.createFloatBuffer(tmpFloat);
		
		int nbVerts = verts.size() / 3;
		int[] tmpInt = new int[nbVerts];
		for (i=0; i<nbVerts; i++) tmpInt[i] = i;
		IntBuffer ibuf = BufferUtils.createIntBuffer(tmpInt);
		
		texture.setWrap(Texture.WrapMode.Clamp);
		texture.setApply(Texture.ApplyMode.Combine);
		texture.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		texture.setMinificationFilter(Texture.MinificationFilter.NearestNeighborNoMipMaps);
		TextureState texState=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		texState.setTexture(texture, 0);
		
		mesh = new TriMesh("", vbuf, null, null, new TexCoords(tbuf), ibuf);
		mesh.setRenderState(texState);
		mesh.setModelBound( new BoundingBox() );
        mesh.updateModelBound();
        BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        as1.setBlendEnabled(true);
        as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as1.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as1.setTestEnabled(true);
        as1.setTestFunction(BlendState.TestFunction.GreaterThan);
        as1.setEnabled(true);
        mesh.setRenderState(as1);
        CullState CS=DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        CS.setCullFace(CullState.Face.Back);
        CS.setEnabled(true);
        mesh.setRenderState(CS);
        ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        buf.setEnabled( true );
        buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo);
        mesh.setRenderState( buf );
        mesh.updateGeometricState( 0.0f, true );
        mesh.updateRenderState();
        
        state = ACTIVE;
        
        cmover = new Mover_none();
		cmotion = new Motion_NoCollision();
		cshape = new Shape_aabb(new JGL_3DVector(), new JGL_3DVector(), new JGL_3DVector());
		angles = new JGL_3DVector();
		
		deltaTime = lifeTime;
		startTime = System.currentTimeMillis();
	}
	
	
	
	@Override
	public Shape getCShape() {
		// TODO Auto-generated method stub
		return cshape;
	}

	@Override
	public Motion getCollider() {
		// TODO Auto-generated method stub
		return cmotion;
	}

	@Override
	public float getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public float getLife() {
		// TODO Auto-generated method stub
		return 0f;
	}

	@Override
	public Mover getMover() {
		// TODO Auto-generated method stub
		return cmover;
	}

	@Override
	public Node getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JGL_3DVector getOrientation() {
		// TODO Auto-generated method stub
		return angles;
	}

	@Override
	public JGL_3DVector getPosition() {
		// TODO Auto-generated method stub
		return cshape.getPosition();
	}

	@Override
	public int getTeam() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return state == ACTIVE;
	}

	@Override
	public boolean isCollidable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return state == DEAD;
	}

	@Override
	public boolean isDying() {
		// TODO Auto-generated method stub
		return state == DYING;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
		//DisplaySystem.getDisplaySystem().getRenderer().setPolygonOffset(0f, -0.0005f);
		DisplaySystem.getDisplaySystem().getRenderer().draw(mesh);
		//DisplaySystem.getDisplaySystem().getRenderer().clearPolygonOffset();
	}

	@Override
	public void setActive() {
		// TODO Auto-generated method stub
		state = ACTIVE;
	}

	@Override
	public void setCShape(Shape arg) {
		// TODO Auto-generated method stub
		cshape = arg;
	}

	@Override
	public void setCollidable(boolean arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCollider(Motion arg) {
		// TODO Auto-generated method stub
		cmotion = arg;
	}

	@Override
	public void setDamage(float arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDead() {
		// TODO Auto-generated method stub
		state = DEAD;
	}

	@Override
	public void setDying() {
		// TODO Auto-generated method stub
		state = DYING;
	}

	@Override
	public void setLife(float arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMover(Mover arg) {
		// TODO Auto-generated method stub
		cmover = arg;
	}

	@Override
	public void setSpeed(float arg) {
		// TODO Auto-generated method stub
		cmover.setSpeed(arg);
	}

	@Override
	public void setTeam(int arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void synchronizeNode() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean touchReact(Entity entity, Trace trace) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (System.currentTimeMillis() - startTime > deltaTime)
				setDead();
	}
	
	
	public Object clone() {
		return this;
	}

}
