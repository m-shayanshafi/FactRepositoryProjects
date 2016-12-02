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
import entity.Entity;
import phys.Bsp_tree;
import ai.PathGraph;


/**
 * Class storing the game Nodes.
 * 
 * @author Nicolas Devere
 *
 */
public final class Entities {
	
	private static Texture[] textures = new Texture[0];
	private static String[] texIds = new String[0];
	
	private static Bsp_tree[] bsps = new Bsp_tree[0];
	private static String[] bspIds = new String[0];
	
	private static PathGraph[] paths = new PathGraph[0];
	private static String[] pathIds = new String[0];
	
	private static Node[] nodes = new Node[0];
	private static String[] nodeIds = new String[0];
	
	private static Entity[] entities = new Entity[0];
	private static String[] entIds = new String[0];
	
	private static Kinematic[] kinematics = new Kinematic[0];
	private static String[] kinIds = new String[0];
	
	
	
	/**
	 * Adds a Texture given its ID.
	 * 
	 * @param texture : the Texture
	 * @param id : the ID
	 */
	public static void addTexture(Texture texture, String id) {
		
		Texture[] newStructs = new Texture[textures.length+1];
		String[] newIds = new String[textures.length+1];
		
		for (int i=0; i<textures.length; i++) {
			newStructs[i] = textures[i];
			newIds[i] = texIds[i];
		}
		
		newStructs[textures.length] = texture;
		newIds[textures.length] = id;
		
		textures = newStructs;
		texIds = newIds;
	}
	
	
	/**
	 * Return the Texture given its ID (or null if it does'nt exist).
	 * 
	 * @param id : the Texture ID
	 * @return the Texture, or null
	 */
	public static Texture getTexture(String id) {
		for (int i=0; i<texIds.length; i++)
			if (texIds[i].equals(id))
				return textures[i];
		return null;
	}
	
	
	/**
	 * Adds a BSP tree given its ID.
	 * 
	 * @param bsp : the BSP
	 * @param id : the ID
	 */
	public static void addBsp(Bsp_tree bsp, String id) {
		
		Bsp_tree[] newStructs = new Bsp_tree[bsps.length+1];
		String[] newIds = new String[bsps.length+1];
		
		for (int i=0; i<bsps.length; i++) {
			newStructs[i] = bsps[i];
			newIds[i] = bspIds[i];
		}
		
		newStructs[bsps.length] = bsp;
		newIds[bsps.length] = id;
		
		bsps = newStructs;
		bspIds = newIds;
	}
	
	
	/**
	 * Return the BSP tree given its ID (or null if it does'nt exist).
	 * 
	 * @param id : the BSP ID
	 * @return the BSP, or null
	 */
	public static Bsp_tree getBsp(String id) {
		for (int i=0; i<bspIds.length; i++)
			if (bspIds[i].equals(id))
				return bsps[i];
		return null;
	}
	
	
	/**
	 * Adds a PathGraph given its ID.
	 * 
	 * @param path : the PathGraph
	 * @param id : the ID
	 */
	public static void addPathGraph(PathGraph path, String id) {
		
		PathGraph[] newStructs = new PathGraph[paths.length+1];
		String[] newIds = new String[paths.length+1];
		
		for (int i=0; i<paths.length; i++) {
			newStructs[i] = paths[i];
			newIds[i] = pathIds[i];
		}
		
		newStructs[paths.length] = path;
		newIds[paths.length] = id;
		
		paths = newStructs;
		pathIds = newIds;
	}
	
	
	/**
	 * Return the PathGraph given its ID (or null if it does'nt exist).
	 * 
	 * @param id : the PathGraph ID
	 * @return the PathGraph, or null
	 */
	public static PathGraph getPathGraph(String id) {
		for (int i=0; i<pathIds.length; i++)
			if (pathIds[i].equals(id))
				return paths[i];
		return null;
	}
	
	
	/**
	 * Adds a Node given its ID.
	 * 
	 * @param model : the Node
	 * @param id : the ID
	 */
	public static void addNode(Node model, String id) {
		
		Node[] newStructs = new Node[nodes.length+1];
		String[] newIds = new String[nodes.length+1];
		
		for (int i=0; i<nodes.length; i++) {
			newStructs[i] = nodes[i];
			newIds[i] = nodeIds[i];
		}
		
		newStructs[nodes.length] = model;
		newIds[nodes.length] = id;
		
		nodes = newStructs;
		nodeIds = newIds;
	}
	
	
	/**
	 * Return the Node given its ID (or null if it does'nt exist).
	 * 
	 * @param id : the Node ID
	 * @return the Node, or null
	 */
	public static Node getNode(String id) {
		for (int i=0; i<nodeIds.length; i++)
			if (nodeIds[i].equals(id))
				return nodes[i];
		return null;
	}
	
	
	/**
	 * Adds an Entity given its ID.
	 * 
	 * @param model : the Entity
	 * @param id : the ID
	 */
	public static void addEntity(Entity model, String id) {
		
		Entity[] newStructs = new Entity[entities.length+1];
		String[] newIds = new String[entities.length+1];
		
		for (int i=0; i<entities.length; i++) {
			newStructs[i] = entities[i];
			newIds[i] = entIds[i];
		}
		
		newStructs[entities.length] = model;
		newIds[entities.length] = id;
		
		entities = newStructs;
		entIds = newIds;
	}
	
	
	/**
	 * Return the Entity given its ID (or null if it does'nt exist).
	 * 
	 * @param id : the Entity ID
	 * @return the Entity, or null
	 */
	public static Entity getEntity(String id) {
		for (int i=0; i<entIds.length; i++)
			if (entIds[i].equals(id))
				return entities[i];
		return null;
	}
	
	
	
	/**
	 * Adds an Kinematic given its ID.
	 * 
	 * @param model : the Kinematic
	 * @param id : the ID
	 */
	public static void addKinematic(Kinematic model, String id) {
		
		Kinematic[] newStructs = new Kinematic[kinematics.length+1];
		String[] newIds = new String[kinematics.length+1];
		
		for (int i=0; i<kinematics.length; i++) {
			newStructs[i] = kinematics[i];
			newIds[i] = kinIds[i];
		}
		
		newStructs[kinematics.length] = model;
		newIds[kinematics.length] = id;
		
		kinematics = newStructs;
		kinIds = newIds;
	}
	
	
	/**
	 * Return the Kinematic given its ID (or null if it does'nt exist).
	 * 
	 * @param id : the Kinematic ID
	 * @return the Kinematic, or null
	 */
	public static Kinematic getKinematic(String id) {
		for (int i=0; i<kinIds.length; i++)
			if (kinIds[i].equals(id))
				return kinematics[i];
		return null;
	}
	
	
	
	/**
	 * clears the lists.
	 */
	public static void clear() {
		textures = new Texture[0];
		texIds = new String[0];
		
		bsps = new Bsp_tree[0];
		bspIds = new String[0];
		
		paths = new PathGraph[0];
		pathIds = new String[0];
		
		nodes = new Node[0];
		nodeIds = new String[0];
		
		entities = new Entity[0];
		entIds = new String[0];
		
		kinematics = new Kinematic[0];
		kinIds = new String[0];
	}
	
}
