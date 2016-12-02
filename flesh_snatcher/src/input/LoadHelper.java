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

package input;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.sound.sampled.AudioInputStream;

import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.geom.BufferUtils;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.ObjToJme;
import audio.AudioTrack;


/**
 * Static class gathering all the loading operations.
 * 
 * @author Nicolas Devere
 *
 */
public class LoadHelper {
	
	
	private static String TEXTURE_PATH = "data/map/textures/";
	
	
	public static URL getURL(String path) throws Exception {
		return LoadHelper.class.getClassLoader().getResource(path);
	}
	
	
	
	
	public static BufferedReader getBufferedReader(String path) throws Exception {
		try {
			URL url = getURL(path);
			return new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
		}
		catch(Exception ex) {
			throw ex;
		}
	}
	
	
	
	/**
	 * Sets the directory where all the game textures can be located.
	 * 
	 * @param path : the directory
	 */
	public static void setTextureLocator(String path) {
        TEXTURE_PATH = path;
	}
	
	
	/**
	 * Returns the texture contained in the specified file.
	 * 
	 * @param path : the texture file
	 * @param flip : specifies if the texture is flipped vertically
	 * @return the texture, or the default texture if a problem occurs
	 */
	public static Texture getTexture(String path, boolean flip) {
		try {
			
			URL texURL = getURL(TEXTURE_PATH + path);
			Texture t = TextureManager.loadTexture(
						texURL,
						Texture.MinificationFilter.Trilinear,
						Texture.MagnificationFilter.Bilinear, 
						TextureManager.COMPRESS_BY_DEFAULT ? Image.Format.Guess
								: Image.Format.GuessNoCompression, 0.0f, flip);
			return t;
		}
		catch(Exception ex) {
			return TextureState.getDefaultTexture();
		}
	}
	
	
	
	
	/**
	 * Contains a lightmapped TriMesh infos.
	 * 
	 * @author Nicolas Devere
	 *
	 */
	static class MeshBuffer {
		
		private Vector verts;
		private Vector texs1;
		private Vector texs2;
		
		private String t1Path;
		private String t2Path;
		
		
		/**
		 * Constructor.
		 * 
		 * @param t1
		 * @param t2
		 */
		public MeshBuffer(String t1, String t2) {
			verts = new Vector();
			texs1 = new Vector();
			texs2 = new Vector();
			
			t1Path = t1;
			t2Path = t2;
		}
		
		
		/**
		 * Inserts infos if textures correspond.
		 * 
		 * @param x
		 * @param y
		 * @param z
		 * @param t1u
		 * @param t1v
		 * @param t1
		 * @param t2u
		 * @param t2v
		 * @param t2
		 * @return if the infos have been inserted.
		 */
		public boolean insert(float x, float y, float z, float t1u, float t1v, String t1, float t2u, float t2v, String t2) {
			
			if (t1Path.equals(t1) && t2Path.equals(t2)) {
				verts.add(new Float(x));
				verts.add(new Float(y));
				verts.add(new Float(z));
				
				texs1.add(new Float(t1u));
				texs1.add(new Float(t1v));
				
				texs2.add(new Float(t2u));
				texs2.add(new Float(t2v));
				return true;
			}
			else
				return false;
		}
		
		/**
		 * Returns the TriMesh created with the infos.
		 * 
		 * @return the TriMesh
		 */
		public TriMesh createTriMesh() {
			
			try {
				int i;
				
				float[] tmpFloat = new float[verts.size()];
				for (i=0; i<verts.size(); i++) tmpFloat[i] = ((Float)verts.get(i)).floatValue();
				FloatBuffer vbuf = BufferUtils.createFloatBuffer(tmpFloat);
				
				tmpFloat = new float[texs1.size()];
				for (i=0; i<texs1.size(); i++) tmpFloat[i] = ((Float)texs1.get(i)).floatValue();
				FloatBuffer tbuf1 = BufferUtils.createFloatBuffer(tmpFloat);
				
				tmpFloat = new float[texs2.size()];
				for (i=0; i<texs2.size(); i++) tmpFloat[i] = ((Float)texs2.get(i)).floatValue();
				FloatBuffer tbuf2 = BufferUtils.createFloatBuffer(tmpFloat);
				
				int nbVerts = verts.size() / 3;
				int[] tmpInt = new int[nbVerts];
				for (i=0; i<nbVerts; i++) tmpInt[i] = i;
				IntBuffer ibuf = BufferUtils.createIntBuffer(tmpInt);
				
				Texture t1 = getTexture(t1Path, false); t1.setWrap(Texture.WrapMode.Repeat);
				Texture t2 = getTexture(t2Path, false); t2.setWrap(Texture.WrapMode.Repeat);
				t2.setApply(Texture.ApplyMode.Combine);
				
				TextureState texState=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
				texState.setTexture(t1, 0);
				texState.setTexture(t2, 1);
				texState.apply();
				
				TriMesh mesh = new TriMesh("", vbuf, null, null, new TexCoords(tbuf1), ibuf);
				mesh.setTextureCoords(new TexCoords(tbuf2), 1);
				mesh.setRenderState(texState);
				mesh.setModelBound( new BoundingBox() );
	            mesh.updateModelBound();
				return mesh;
			}
			catch(Exception ex) {
				return null;
			}
		}
	}
	
	
	/**
	 * Returns the lightmapped node contained in the specified .tri file.
	 * 
	 * @param path : the .tri file
	 * @param texturesPath : the directory where the node textures are
	 * @return the node, or null if a problem occurs
	 */
	public static Node getTriNode(String path) {
		
		Node node = new Node();
		
		Vector meshBuffers = new Vector();
		
		int i;
		StringTokenizer st;
		
		String t1Path = "";
		String t2Path = "";
		
		float x, y, z, t1u, t1v, t2u, t2v;
		
		String line;
		
		try {
			
			BufferedReader br = getBufferedReader(path);
			br.readLine();
			
			while (br.ready()) {
				
				// read line
				line = br.readLine();
				line = line.replace("\\", " ");
				line = line.replace(";", " ");
				line = line.replace("|", " ");
				st = new StringTokenizer(line);
				x = Float.parseFloat(st.nextToken());
				y = Float.parseFloat(st.nextToken());
				z = Float.parseFloat(st.nextToken());
				t1u = Float.parseFloat(st.nextToken());
				t1v = Float.parseFloat(st.nextToken());
				t1Path = st.nextToken().replace("tga", "png");
				t2u = Float.parseFloat(st.nextToken());
				t2v = Float.parseFloat(st.nextToken());
				while (st.hasMoreTokens()) t2Path = st.nextToken();
				t2Path = t2Path.replace("tga", "png");
				
				// Try to insert in an existing buffer, otherwise create one
				boolean match = false;
				for (i=0; i<meshBuffers.size(); i++) {
					MeshBuffer buffer = (MeshBuffer)meshBuffers.get(i);
					match |= buffer.insert(x, y, z, t1u, t1v, t1Path, t2u, t2v, t2Path);
				}
				if (!match) {
					MeshBuffer buffer = new MeshBuffer(t1Path, t2Path);
					buffer.insert(x, y, z, t1u, t1v, t1Path, t2u, t2v, t2Path);
					meshBuffers.add(buffer);
				}
				
			}
			
			br.close();
			
			for (i=0; i<meshBuffers.size(); i++)
				node.attachChild( ((MeshBuffer)meshBuffers.get(i)).createTriMesh() );
			
			return node;
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	
	
	/**
	 * Returns the node contained in the specified .ms3d file.
	 * 
	 * @param path : the .ms3d file
	 * @return the node, or null if a problem occurs
	 */
	public static Node getMS3DNode(String path) {
		try {
			AlterMilkToJme milkConverter = new AlterMilkToJme();
	        InputStream MSFile=getURL(path).openStream();
	        ByteArrayOutputStream BO=new ByteArrayOutputStream();
	        milkConverter.convert(MSFile,BO);
	        MSFile.close();
	        BO.close();
	        return (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	
	
	/**
	 * Returns the node contained in the specified .md2 file.
	 * 
	 * @param path : the .md2 file
	 * @param texture : the texture file
	 * @return the node, or null if a problem occurs
	 */
	public static Node getMD2Node(String path, String texture) {
		try {
			Node node = null;
			
			Md2ToJme md2Converter=new Md2ToJme();
			InputStream MD2File=getURL(path).openStream();
			ByteArrayOutputStream BO=new ByteArrayOutputStream();
	        md2Converter.convert(MD2File,BO);
	        MD2File.close();
	        BO.close();
	        node = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
	        
	        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	        ts.setEnabled(true);
	        ts.setTexture(getTexture(texture, false), 0);
	        ts.apply();
	        
	        node.setRenderState(ts);
	        return node;
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	
	/**
	 * Returns the node contained in the specified .md3 file.
	 * 
	 * @param path : the .md3 file
	 * @param texture : the texture file
	 * @return the node, or null if a problem occurs
	 */
	public static Node getMD3Node(String path, String texture) {
		try {
			Node node = null;
			
			Md3ToJme md3Converter=new Md3ToJme();
			ByteArrayOutputStream BO=new ByteArrayOutputStream();
	        md3Converter.convert(getURL(path).openStream(),BO);
	        node = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
	        
	        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	        ts.setEnabled(true);
	        ts.setTexture(getTexture(texture, false), 0);
	        ts.apply();
	        
	        node.setRenderState(ts);
	        return node;
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	
	/**
	 * Returns the node contained in the specified .obj file.
	 * 
	 * @param path : the .obj file
	 * @return the node, or null if a problem occurs
	 */
	public static Node getObjNode(String path) {
		try {
			ObjToJme milkConverter = new ObjToJme();
	        URL MSFile=getURL(path);
	        ByteArrayOutputStream BO=new ByteArrayOutputStream();
	        milkConverter.convert(MSFile.openStream(),BO);
	        return (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	
	/**
	 * Returns an AudioInputStream given a sound file path.
	 * 
	 * @param path : the sound file path
	 * @return the AudioInputStream
	 */
	public static AudioInputStream getAudioInputStream(String path) throws Exception {
		try {
			return javax.sound.sampled.AudioSystem.getAudioInputStream(getURL(path));
		}
		catch(Exception ex) {
			throw ex;
		}
	}
	
	
	
	/**
	 * Returns an AudioTrack given a sound file path.
	 * 
	 * @param path : the sound file path
	 * @param stream : if the sound is streamed or not
	 * @return the AudioTrack
	 */
	public static AudioTrack getAudioTrack(String path, boolean stream) throws Exception {
		try {
			return audio.AudioSystem.getSystem().createAudioTrack(getURL(path), stream);
		}
		catch(Exception ex) {
			throw ex;
		}
	}
	
	
	
}
