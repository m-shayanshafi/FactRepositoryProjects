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

package struct;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jme.renderer.Renderer;
import com.jme.scene.QuadMesh;
import com.jme.scene.TexCoords;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;
import com.jme.image.Texture;

/**
 * 
 * @author Nicolas Devere
 *
 */
public class Bitmap2D {
	
	public static int ONE = 0;
	public static int ONE_MINUS_SOURCE_ALPHA = 1;
	public static int ONE_MINUS_DESTINATION_ALPHA = 2;
	public static int NO_ALPHA = 3;
	
	private static float Z = -94f;
	private static float RATIO = 0.2f;
	
	private QuadMesh mesh;
	
	
	/**
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Bitmap2D(Texture texture, float x, float y, float width, float height, int alphaBlend) {
		
		float wi = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		float he = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		float format = wi / he;
		
		float w = 800f * (format / (4f / 3f));
		float h = (w / wi) * he;
		
		float xo = -(w * 0.5f) * RATIO;
		float yo = -(h * 0.5f) * RATIO;
		
		float xp = w * RATIO * x;
		float yp = h * RATIO * y;
		
		float xw = w * RATIO * width;
		float yh = h * RATIO * height;
		
		float[] tmp = new float[12];
		tmp[0] = xo + xp; tmp[1] = yo + yp; tmp[2] = Z;
		tmp[3] = xo + xp + xw; tmp[4] = yo + yp; tmp[5] = Z;
		tmp[6] = xo + xp + xw; tmp[7] = yo + yp + yh; tmp[8] = Z;
		tmp[9] = xo + xp; tmp[10] = yo + yp + yh; tmp[11] = Z;
		FloatBuffer vbuf = BufferUtils.createFloatBuffer(tmp);
		
		tmp = new float[8];
		tmp[0] = 0f; tmp[1] = 0f;
		tmp[2] = 1f; tmp[3] = 0f;
		tmp[4] = 1f; tmp[5] = 1f;
		tmp[6] = 0f; tmp[7] = 1f;
		FloatBuffer tbuf = BufferUtils.createFloatBuffer(tmp);
		
		int[] tmpInt = new int[4];
		for (int i=0; i<4; i++) tmpInt[i] = i;
		IntBuffer ibuf = BufferUtils.createIntBuffer(tmpInt);
		
		texture.setApply(Texture.ApplyMode.Combine);
		TextureState texState=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		texState.setTexture(texture, 0);
		
		mesh = new QuadMesh("", vbuf, null, null, new TexCoords(tbuf), ibuf);
		mesh.setRenderState(texState);
		
		if( alphaBlend!=NO_ALPHA) {
			BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
	        as1.setBlendEnabled(true);
	        as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	        if (alphaBlend==ONE_MINUS_SOURCE_ALPHA)
	        	as1.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
	        else if (alphaBlend==ONE_MINUS_DESTINATION_ALPHA)
	        	as1.setDestinationFunction(BlendState.DestinationFunction.OneMinusDestinationAlpha);
	        else
	        	as1.setDestinationFunction(BlendState.DestinationFunction.One);
	        as1.setTestEnabled(true);
	        as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	        as1.setEnabled(true);
	        mesh.setRenderState(as1);
		}
        
		mesh.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		mesh.updateGeometricState( 0.0f, true );
        mesh.updateRenderState();
	}
	
	
	/**
	 * Applies the specified texture to the Bitmap2D.
	 * 
	 * @param texture : the texture to apply
	 */
	public void setTexture(Texture texture) {
		texture.setApply(Texture.ApplyMode.Combine);
		((TextureState)mesh.getRenderState(RenderState.StateType.Texture)).setTexture(texture, 0);
	}
	
	
	
	public void setDimensions(float x, float y, float width, float height) {
		
		float wi = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		float he = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		float format = wi / he;
		
		float w = 800f * (format / (4f / 3f));
		float h = (w / wi) * he;
		
		float xo = -(w * 0.5f) * RATIO;
		float yo = -(h * 0.5f) * RATIO;
		
		float xp = w * RATIO * x;
		float yp = h * RATIO * y;
		
		float xw = w * RATIO * width;
		float yh = h * RATIO * height;
		
		float[] vertex = mesh.getVertexBuffer().array();
		vertex[0] = xo + xp; vertex[1] = yo + yp;
		vertex[3] = xo + xp + xw; vertex[4] = yo + yp;
		vertex[6] = xo + xp + xw; vertex[7] = yo + yp + yh;
		vertex[9] = xo + xp; vertex[10] = yo + yp + yh;
		mesh.updateGeometricState( 0.0f, true );
	}
	
	
	
	
	/**
	 * Displays the bitmap.
	 */
	public void render() {
		//DisplaySystem.getDisplaySystem().getRenderer().setOrtho();
		DisplaySystem.getDisplaySystem().getRenderer().draw(mesh);
		//DisplaySystem.getDisplaySystem().getRenderer().unsetOrtho();
	}
}
