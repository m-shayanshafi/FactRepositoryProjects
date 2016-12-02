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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.StringTokenizer;

import jglcore.*;
//import jglanim.*;



/**
 * Milkshape 3D ASCII Files reader.
 * 
 * @author Nicolas Devere
 *
 */
public class Reader_Milkshape implements Reader3D {
	
	private String path;
	private BufferedReader br;
	
	private Vector points;
	private Vector triangles;
	private Vector couleurs;
	
	private Vector subMeshes;
	
	//private Vector   bones;
	
	private Vector map_triangles_couleurs;
	private Vector map_points_bones;
	
	
	
	
	/**
	 * Constructs a reader with the specified file path.
	 * 
	 * @param path : the Milkshape file path
	 */
	public Reader_Milkshape(String path) {
		
		this.path = path;
		
		points = new Vector();
		triangles = new Vector();
		couleurs = new Vector();
		
		subMeshes = new Vector();
		
		//bones = new Vector();
		
		map_triangles_couleurs = new Vector();
		map_points_bones = new Vector();
	}
	
	
	
	/**
	 * Returns the 3D data from the reader's file.
	 * 
	 * @return the 3D data
	 * @throws Exception
	 */
	public Data3D getData() throws Exception {
		try {
			
			String ligne = "";
			
			br = LoadHelper.getBufferedReader(path);
			
			while((ligne=br.readLine())!=null) {
				
				StringTokenizer st = new StringTokenizer(ligne);
				if(st.hasMoreElements()) {
					String tag = st.nextToken();
					if(tag.equals("Meshes:"))
						getTriangles(Integer.parseInt(st.nextToken()));
			
					if(tag.equals("Materials:"))
						getColors(Integer.parseInt(st.nextToken()));
			
					//if(tag.equals("Bones:"))
					//	getBones(Integer.parseInt(st.nextToken()));
				}
			}
			
			br.close();
			return build_Data3D();
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	
	
	private Data3D build_Data3D() {
		
		Data3D data = new Data3D();
		
		//Chargement du mesh
		if(!triangles.isEmpty()) {
			JGL_3DMesh mesh = new JGL_3DMesh();
			for(int i=0; i<triangles.size(); i++)
				mesh.addFace((JGL_3DTriangle)triangles.get(i));
			data.mesh = mesh;
			data.subMeshes = subMeshes;
			
			//Chargement couleurs
			if(!couleurs.isEmpty() && map_triangles_couleurs.size() == triangles.size())
				//Assigne les couleurs aux triangles
				for(int i=0; i<triangles.size(); i++) {
					int indice_couleur = ((Integer)map_triangles_couleurs.get(i)).intValue();
					if(indice_couleur>=0 && indice_couleur<couleurs.size())
						((JGL_3DTriangle)triangles.get(i)).color = (Color)couleurs.get(indice_couleur);
				}
			
			//chargement du mapping points/bones
			if(!map_points_bones.isEmpty() && map_points_bones.size()==points.size())
				data.map_points_bones = map_points_bones;
		}
		
		//chargement du squelette
		//if(!bones.isEmpty())
		//	data.skeleton = new JGL_Skeleton(bones);
		
		return data;
	}
	
	
	
	private void getTriangles(int nb_meshes) throws Exception {
		
		int i = 0;
		int j = 0;
		StringTokenizer st;
		
		try {
			
			for(i=0; i<nb_meshes; i++) {
				
				int taille_points = points.size();
				
				//Nom + matériau
				int indice_materiau = 0;
				st = new StringTokenizer(br.readLine());
				if(st.countTokens() == 3) {
					st.nextToken();
					st.nextToken();
					indice_materiau = Integer.parseInt(st.nextToken());
				}
				
				//Points
				st = new StringTokenizer(br.readLine());
				int nb_points = Integer.parseInt(st.nextToken());
				
				for(j=0; j<nb_points; j++) {
					st = new StringTokenizer(br.readLine());
					st.nextToken();
					float x = Float.parseFloat(st.nextToken());
					float y = Float.parseFloat(st.nextToken());
					float z = Float.parseFloat(st.nextToken());
					
					points.add(new JGL_3DVector(x, y, z));
					st.nextToken();
					st.nextToken();
					
					map_points_bones.add(new Integer(Integer.parseInt(st.nextToken())));
				}
				
				//Normales, on les passe
				st = new StringTokenizer(br.readLine());
				int nb_norms = Integer.parseInt(st.nextToken());
				
				for(j=0; j<nb_norms; j++)
					br.readLine();
				
				//Triangles
				st = new StringTokenizer(br.readLine());
				int nb_faces = Integer.parseInt(st.nextToken());
				
				subMeshes.add(new JGL_3DMesh());
				for(j=0; j<nb_faces; j++) {
					st = new StringTokenizer(br.readLine());
					st.nextToken();
					
					JGL_3DVector v1 = (JGL_3DVector)points.get(Integer.parseInt(st.nextToken()) + taille_points);
					JGL_3DVector v2 = (JGL_3DVector)points.get(Integer.parseInt(st.nextToken()) + taille_points);
					JGL_3DVector v3 = (JGL_3DVector)points.get(Integer.parseInt(st.nextToken()) + taille_points);
					
					JGL_3DTriangle t = new JGL_3DTriangle(v1, v2, v3);
					((JGL_3DMesh)subMeshes.get(subMeshes.size()-1)).addFace(t);
					triangles.add(t);
					map_triangles_couleurs.add(new Integer(indice_materiau));
				}
			}
			
		}
		catch(FileNotFoundException e) {
			System.out.println("Reader_Milkshape - getTriangles() : " + e.getMessage());
			throw new Exception("Adresse du fichier inexistante.");
		}
		catch(IOException e) {
			System.out.println("Reader_Milkshape - getTriangles() : " + e.getMessage());
			throw new Exception("Echec de lecture du fichier.");
		}
	}
	
	
	private void  getColors(int nb_coul) throws Exception {
		
		int i = 0;
		StringTokenizer st;
		
		try {
			
			for(i=0; i<nb_coul; i++) {
				
				br.readLine(); //On passe le nom
				br.readLine(); //On passe la color ambiante
				
				//On charge la color diffuse.
				st = new StringTokenizer(br.readLine());
				float r = Float.parseFloat(st.nextToken());
				float g = Float.parseFloat(st.nextToken());
				float b = Float.parseFloat(st.nextToken());
				
				couleurs.add(new Color(r, g, b));
				
				br.readLine(); //On passe la color spéculaire
				br.readLine(); //On passe la color ?
				br.readLine(); //On passe la luminosite
				br.readLine(); //On passe l'alpha (?)
				br.readLine(); //On passe la texture 1
				br.readLine(); //On passe la texture 2
			}
			
		}
		catch(FileNotFoundException e) {
			System.out.println("Reader_Milkshape - getTriangles() : " + e.getMessage());
			throw new Exception("Adresse du fichier inexistante.");
		}
		catch(IOException e) {
			System.out.println("Reader_Milkshape - getTriangles() : " + e.getMessage());
			throw new Exception("Echec de lecture du fichier.");
		}
	}
	
	
	
	/*private void  getBones(int nb_bones) throws Exception {
		
		int i = 0;
		int j = 0;
		StringTokenizer st;
		
		try {
			
			for(i=0; i<nb_bones; i++) {
				
				String id = br.readLine();			//Bone id
				String id_parent = br.readLine();	//Parent id
				
				st = new StringTokenizer(br.readLine());
				st.nextToken();
				float x_t = Float.parseFloat(st.nextToken());
				float y_t = Float.parseFloat(st.nextToken());
				float z_t = Float.parseFloat(st.nextToken());
				
				float x_r = Float.parseFloat(st.nextToken());
				float y_r = Float.parseFloat(st.nextToken());
				float z_r = Float.parseFloat(st.nextToken());
				
				JGL_3DVector position = new JGL_3DVector(x_t, y_t, z_t);
				JGL_3DVector orientation = new JGL_3DVector(x_r, y_r, z_r);
				
				Vector trans = new Vector();
				Vector rotat = new Vector();
				
				int nb_keys_1 = Integer.parseInt(br.readLine()); //Translation keys number
				
				for(j=0; j<nb_keys_1; j++) {
					st = new StringTokenizer(br.readLine());
					st.nextToken();
					float x_t_k = Float.parseFloat(st.nextToken());
					float y_t_k = Float.parseFloat(st.nextToken());
					float z_t_k = Float.parseFloat(st.nextToken());
					
					trans.add(new JGL_3DVector(x_t_k, y_t_k, z_t_k));
				}
				
				int nb_keys_2 = Integer.parseInt(br.readLine()); //Rotation keys number
				
				for(j=0; j<nb_keys_2; j++) {
					st = new StringTokenizer(br.readLine());
					st.nextToken();
					float x_r_k = Float.parseFloat(st.nextToken());
					float y_r_k = Float.parseFloat(st.nextToken());
					float z_r_k = Float.parseFloat(st.nextToken());
					
					rotat.add(new JGL_3DVector(x_r_k, y_r_k, z_r_k));
				}
				
				if(nb_keys_1 != nb_keys_2)
					throw new Exception("JGL_Bone " + id + " : Keyframes number problem");
				
				JGL_Bone current_bone = new JGL_Bone(id, id_parent, position, orientation);
				for(j=0; j<nb_keys_1; j++)
					current_bone.addKeyframe(new JGL_Keyframe(	(JGL_3DVector)trans.get(j), 
																(JGL_3DVector)rotat.get(j)));
				bones.add(current_bone);
			}
			
		}
		catch(FileNotFoundException e) {
			System.out.println("Reader_Milkshape - getTriangles() : " + e.getMessage());
			throw new Exception("Adresse du fichier inexistante.");
		}
		catch(IOException e) {
			System.out.println("Reader_Milkshape - getTriangles() : " + e.getMessage());
			throw new Exception("Echec de lecture du fichier.");
		}
	}*/

}
