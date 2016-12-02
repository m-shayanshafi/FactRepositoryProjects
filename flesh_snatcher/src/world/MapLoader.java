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

package world;

import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.state.CullState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;

import entity.ScriptBox;

import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Observable;

import ai.PathGraph;

import phys.Shape;
import phys.Shape_aabb;
import phys.Util4Phys;

import jglcore.JGL_3DVector;
import jglanim.JGL_KeyframesArray;
import jglanim.JGL_Keyframe;
import input.Reader_Milkshape;
import input.Data3D;
import input.LoadHelper;
import struct.DLNode;

import script.Script;



/**
 * Class providing static methods to load a complete map.
 * 
 * @author Nicolas Devere
 *
 */
public final class MapLoader extends Observable {
	
	private int lines;
	
	
	public MapLoader() {
		lines = 0;
	}
	
	
	/**
	 * Loads a map according to the specified file and stores it in the World class.
	 * 
	 * @param path : the map file
	 * @return the map
	 * @throws Exception
	 */
	public final void loadMap(String path) throws Exception {
		
		try {
			
			if (World.map!=null) {
				World.map.clear();
				World.map = null;
			}
			
			lines = 0;
			
			StringTokenizer st;
			
			BufferedReader br = LoadHelper.getBufferedReader(path);
			
			// Resource textures loading
			LoadHelper.setTextureLocator(br.readLine()); lines++; this.setChanged(); this.notifyObservers();
			
			// Skybox loading
			Skybox m_skybox = new Skybox("skybox", 10, 10, 10);
		    m_skybox.setTexture(Skybox.Face.North, LoadHelper.getTexture(br.readLine(), true)); lines++; this.setChanged(); this.notifyObservers();
		    m_skybox.setTexture(Skybox.Face.South, LoadHelper.getTexture(br.readLine(), true)); lines++; this.setChanged(); this.notifyObservers();
		    m_skybox.setTexture(Skybox.Face.East, LoadHelper.getTexture(br.readLine(), true)); lines++; this.setChanged(); this.notifyObservers();
		    m_skybox.setTexture(Skybox.Face.West, LoadHelper.getTexture(br.readLine(), true)); lines++; this.setChanged(); this.notifyObservers();
		    m_skybox.setTexture(Skybox.Face.Up, LoadHelper.getTexture(br.readLine(), true)); lines++; this.setChanged(); this.notifyObservers();
		    m_skybox.setTexture(Skybox.Face.Down, LoadHelper.getTexture(br.readLine(), true)); lines++; this.setChanged(); this.notifyObservers();
		    m_skybox.preloadTextures();
		    m_skybox.updateGeometricState( 0.0f, true );
		    m_skybox.updateRenderState();
			
		    // Display nodes loading
		    Vector nodes = new Vector();
		    int nbNodes = Integer.parseInt(br.readLine()); lines++;
		    for (int i=0; i<nbNodes; i++) {
				Node n;
				st = new StringTokenizer(br.readLine()); lines++; this.setChanged(); this.notifyObservers();
				
				String name = st.nextToken();
				float posX = Float.parseFloat(st.nextToken());
				float posY = Float.parseFloat(st.nextToken());
				float posZ = Float.parseFloat(st.nextToken());
				
				String file = st.nextToken();
				if (file.endsWith("tri"))
					n = LoadHelper.getTriNode(file);
				else
					n = LoadHelper.getMS3DNode(file);
				
				
		        
		        Vector shapes = new Vector();
		        if (st.hasMoreTokens()) {
		        	int nbShapes = Integer.parseInt(st.nextToken());
					for (int j=0; j<nbShapes; j++)
						shapes.add(new Shape_aabb(	new JGL_3DVector(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken())), 
													new JGL_3DVector(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken())), 
													new JGL_3DVector(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()))));
		        }
		        
				if (st.hasMoreTokens()) {
					int nbTex = Integer.parseInt(st.nextToken());
					Texture textures[] = new Texture[nbTex];
					for (int j=0; j<nbTex; j++) {
						textures[j] = LoadHelper.getTexture(st.nextToken(), false);
						textures[j].setApply(Texture.ApplyMode.Combine);
					}
					DLNode dlNode = new DLNode("", n, textures);
					dlNode.preloadAnimation();
					n = dlNode;
				}
				
				n.setLocalTranslation(posX, posY, posZ);
		        
				ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		        buf.setEnabled( true );
		        buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo );
		        n.setRenderState( buf );
		        
		        CullState CS=DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		        CS.setCullFace(CullState.Face.Back);
		        CS.setEnabled(true);
		        n.setRenderState(CS);
		        
		        n.updateGeometricState( 0.0f, true );
		        n.updateRenderState();
		        DisplaySystem.getDisplaySystem().getRenderer().draw(n);
		        
				nodes.add(new DisplayNode(name, n, shapes));
		    }
			
		    
		    // Collision nodes loading
		    int nbCollNodes = Integer.parseInt(br.readLine()); lines++;
			Vector collNodes = new Vector();
		    
			for (int h=0; h<nbCollNodes; h++) {
				st = new StringTokenizer(br.readLine()); lines++; this.setChanged(); this.notifyObservers();
				String type = st.nextToken();
				
				if (type.equals("heightmap")) {
					
					String name = st.nextToken();
					
					float posX = Float.parseFloat(st.nextToken());
					float posY = Float.parseFloat(st.nextToken());
					float posZ = Float.parseFloat(st.nextToken());
					
					int width = Integer.parseInt(st.nextToken());
					int depth = Integer.parseInt(st.nextToken());
					float gap = Float.parseFloat(st.nextToken());
			        
					Vector p = new Reader_Milkshape(st.nextToken()).getData().mesh.getPoints();
					float[][] heights = new float[width][depth];
					
					for (int i=0; i<width; i++)
						for (int j=0; j<depth; j++)
							heights[i][j] = Float.NEGATIVE_INFINITY;
					
					JGL_3DVector v;
					int wi, di;
					for (int i=0; i<p.size(); i++) {
						v = (JGL_3DVector)p.get(i);
						wi = (int)Math.abs(v.x / gap);
						di = (int)Math.abs(v.z / gap);
						if (wi<width && di<depth)
							heights[wi][di] = v.y;
					}
					
					collNodes.add(new CollisionHeightMap(name, posX, posY, posZ, width, depth, gap, heights));
				}
				
				if (type.equals("bsp")) {
					
					String name = st.nextToken();
					
					float posX = Float.parseFloat(st.nextToken());
					float posY = Float.parseFloat(st.nextToken());
					float posZ = Float.parseFloat(st.nextToken());
					
					Data3D data = new Reader_Milkshape(st.nextToken()).getData();
					Shape_aabb shape = Util4Phys.getAABB(data.mesh, 1f);
					
					Vector shapes = new Vector();
					if (st.hasMoreTokens()) {
						int nbShapes = Integer.parseInt(st.nextToken());
						for (int j=0; j<nbShapes; j++)
							shapes.add(new Shape_aabb(	new JGL_3DVector(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken())), 
														new JGL_3DVector(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken())), 
														new JGL_3DVector(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()))));
					}
					collNodes.add(new CollisionBSP(name, data.subMeshes, shapes, new JGL_3DVector(posX, posY, posZ), shape));
				}
				
			}
		    
			
			
			// Map instantiation
		    World.map = new Map(nodes, collNodes, m_skybox);
			
			
			// Characters loading
			while (br.ready()) {
				
				st = new StringTokenizer(br.readLine()); lines++; this.setChanged(); this.notifyObservers();
				if (st.countTokens()==0)
					continue;
				
			    String charToken = st.nextToken();
			    if (charToken.startsWith("!"))
			    	continue;
			    
			    if (charToken.equals(Script.SCRIPT))
			    	Script.execute(st);
			    
			    if (charToken.equals("loadpathgraph")) {
			    	String id = st.nextToken();
			    	int nbPoints = Integer.parseInt(st.nextToken());
					JGL_3DVector points[] = new JGL_3DVector[nbPoints];
					for (int i=0; i<nbPoints; i++) {
						st = new StringTokenizer(br.readLine()); lines++; this.setChanged(); this.notifyObservers();
						points[i] = new JGL_3DVector(Float.parseFloat(st.nextToken()), 
													Float.parseFloat(st.nextToken()), 
													Float.parseFloat(st.nextToken()));
					}
					
					boolean links[][] = new boolean[nbPoints][nbPoints];
					for (int i=0; i<nbPoints; i++) {
						st = new StringTokenizer(br.readLine()); lines++; this.setChanged(); this.notifyObservers();
						for (int j=0; j<nbPoints; j++)
							links[i][j] = !st.nextToken().equals("0");
					}
					
					Entities.addPathGraph(new PathGraph(points, links), id);
			    }
			    
			    if (charToken.equals("loadscriptbox")) {
			    	String id = st.nextToken();
			    	JGL_3DVector pos = new JGL_3DVector(Float.parseFloat(st.nextToken()), 
														Float.parseFloat(st.nextToken()), 
														Float.parseFloat(st.nextToken()));
			    	Shape cshape = Script.getCShape(st);
			    	cshape.setPosition(pos);
			    	int nb = Integer.parseInt(st.nextToken());
			    	Vector scripts = new Vector();
			    	
			    	for (int i=0; i<nb; i++)
			    		scripts.add(br.readLine()); lines++; this.setChanged(); this.notifyObservers();
			    	
			    	Entities.addEntity(new ScriptBox(cshape, scripts), id);
			    }
			    
			    if (charToken.equals("loadkinematic")) {
			    	String id = st.nextToken();
			    	float speed = Float.parseFloat(st.nextToken());
			    	float endDate = Float.parseFloat(st.nextToken());
			    	
			    	int nbKeys = Integer.parseInt(new StringTokenizer(br.readLine()).nextToken()); lines++;
			    	JGL_KeyframesArray kfs = new JGL_KeyframesArray();
			    	for (int i=0; i<nbKeys; i++) {
			    		StringTokenizer st2 = new StringTokenizer(br.readLine()); lines++;
			    		kfs.add(new JGL_Keyframe(new JGL_3DVector(	Float.parseFloat(st2.nextToken()), 
																	Float.parseFloat(st2.nextToken()), 
																	Float.parseFloat(st2.nextToken())), 
												new JGL_3DVector(	Float.parseFloat(st2.nextToken()), 
																	Float.parseFloat(st2.nextToken()), 
																	Float.parseFloat(st2.nextToken()))));
			    	}
			    	
			    	int nbNds = Integer.parseInt(new StringTokenizer(br.readLine()).nextToken()); lines++;
			    	Vector nds = new Vector();
			    	for (int i=0; i<nbNds; i++) {
			    		StringTokenizer st2 = new StringTokenizer(br.readLine()); lines++;
			    		Node node = Script.getNodeCopy(st2.nextToken());
			    		node.setLocalTranslation(	Float.parseFloat(st2.nextToken()), 
			    									Float.parseFloat(st2.nextToken()), 
			    									Float.parseFloat(st2.nextToken()));
			    		nds.add(node);
			    	}
			    	
			    	int nbScripts = Integer.parseInt(new StringTokenizer(br.readLine()).nextToken()); lines++;
			    	Vector scripts = new Vector();
			    	for (int i=0; i<nbScripts; i++)
			    		scripts.add(br.readLine()); lines++;
			    	
			    	Entities.addKinematic(new Kinematic(nds, kfs, speed, endDate, scripts), id);
			    }
			}
			
			
			br.close();
		}
		catch (Exception ex) {
			throw ex;
		}
	}
	
	
	/**
	 * Returns the number of lines currently read.
	 * 
	 * @return the number of lines currently read
	 */
	public int getLines() {
		return lines;
	}
	
}
