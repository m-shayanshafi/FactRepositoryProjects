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

import java.util.List;
import java.util.Vector;
import java.nio.FloatBuffer;

import jglcore.JGL_3DPlane;
import jglcore.JGL_3DVector;
import jglcore.JGL_3DTriangle;
import jglcore.JGL_Math;
import phys.Util4Phys;
import entity.Decal;
import sound.Sounds;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.image.Texture;


public class DecalFactory {
	
	private static JGL_3DTriangle s_t1 = new JGL_3DTriangle(new JGL_3DVector(), 
															new JGL_3DVector(), 
															new JGL_3DVector());
	private static JGL_3DVector s_v1 = new JGL_3DVector();
	private static JGL_3DPlane s_p1 = new JGL_3DPlane(1f, 0f, 0f, 0f);
	private static int[] s_i = new int[3];
	
	public static void addBloodDecal(JGL_3DVector pos) {
		
		float offset = 30f;
		float decalSize = 30f;
		
		float tx, ty, tz;
		float nx, ny, nz;
		float u1, v1, u2, v2, u3, v3;
		int i0, i1, i2;
		Vector verts = new Vector();
		Vector texs = new Vector();
		TriMesh mesh;
		FloatBuffer fb;
		int size;
		boolean ok;
		
		Texture[] bloodTex = new Texture[5];
		bloodTex[0] = Entities.getTexture("blood00");
		bloodTex[1] = Entities.getTexture("blood01");
		bloodTex[2] = Entities.getTexture("blood02");
		bloodTex[3] = Entities.getTexture("blood03");
		bloodTex[4] = Entities.getTexture("blood04");
		Texture texture = bloodTex[Math.round(JGL_Math.rnd()*4f)];
		
		List nodes = World.map.nodes;
		
		for (int i=0; i<nodes.size(); i++) {
			DisplayNode dnode = (DisplayNode)nodes.get(i);
			if (dnode.isIn(pos)) {
				Node node = ((DisplayNode)nodes.get(i)).getNode();
				tx = node.getLocalTranslation().x;
				ty = node.getLocalTranslation().y;
				tz = node.getLocalTranslation().z;
				List trimeshes = node.getChildren();
				for (int j=0; j<trimeshes.size(); j++) {
					//verts.clear();
					//texs.clear();
					mesh = (TriMesh)trimeshes.get(j);
					fb = mesh.getVertexBuffer();
					size = mesh.getTriangleCount();
					for (int k=0; k<size; k++) {
						mesh.getTriangle(k, s_i);
						i0 = s_i[0] * 3;
						i1 = s_i[1] * 3;
						i2 = s_i[2] * 3;
						s_t1.point1.assign(fb.get(i0) + tx, fb.get(i0+1) + ty, fb.get(i0+2) + tz);
						s_t1.point2.assign(fb.get(i1) + tx, fb.get(i1+1) + ty, fb.get(i1+2) + tz);
						s_t1.point3.assign(fb.get(i2) + tx, fb.get(i2+1) + ty, fb.get(i2+2) + tz);
						s_p1.assign(s_t1.point1, s_t1.point2, s_t1.point3);
						ok = triangleSphereIntersection(s_t1, s_p1, pos, offset);
						if (ok) {
							nx = s_p1.normal.x * 0.015f;
							ny = s_p1.normal.y * 0.015f;
							nz = s_p1.normal.z * 0.015f;
							s_t1.point1.x += nx; s_t1.point1.y += ny; s_t1.point1.z += nz;
							s_t1.point2.x += nx; s_t1.point2.y += ny; s_t1.point2.z += nz;
							s_t1.point3.x += nx; s_t1.point3.y += ny; s_t1.point3.z += nz;
							if (Math.abs(s_p1.normal.y)>0.001f) {
								u1 = ((s_t1.point1.x - pos.x) / decalSize) + 0.5f;
								v1 = ((s_t1.point1.z - pos.z) / decalSize) + 0.5f;
								u2 = ((s_t1.point2.x - pos.x) / decalSize) + 0.5f;
								v2 = ((s_t1.point2.z - pos.z) / decalSize) + 0.5f;
								u3 = ((s_t1.point3.x - pos.x) / decalSize) + 0.5f;
								v3 = ((s_t1.point3.z - pos.z) / decalSize) + 0.5f;
							}
							else if (Math.abs(s_p1.normal.x)>0.001f) {
								u1 = ((s_t1.point1.z - pos.z) / decalSize) + 0.5f;
								v1 = ((s_t1.point1.y - pos.y) / decalSize) + 0.5f;
								u2 = ((s_t1.point2.z - pos.z) / decalSize) + 0.5f;
								v2 = ((s_t1.point2.y - pos.y) / decalSize) + 0.5f;
								u3 = ((s_t1.point3.z - pos.z) / decalSize) + 0.5f;
								v3 = ((s_t1.point3.y - pos.y) / decalSize) + 0.5f;
							}
							else {
								u1 = ((s_t1.point1.x - pos.x) / decalSize) + 0.5f;
								v1 = ((s_t1.point1.y - pos.y) / decalSize) + 0.5f;
								u2 = ((s_t1.point2.x - pos.x) / decalSize) + 0.5f;
								v2 = ((s_t1.point2.y - pos.y) / decalSize) + 0.5f;
								u3 = ((s_t1.point3.x - pos.x) / decalSize) + 0.5f;
								v3 = ((s_t1.point3.y - pos.y) / decalSize) + 0.5f;
							}
							verts.add(new Float(s_t1.point1.x));
							verts.add(new Float(s_t1.point1.y));
							verts.add(new Float(s_t1.point1.z));
							
							verts.add(new Float(s_t1.point2.x));
							verts.add(new Float(s_t1.point2.y));
							verts.add(new Float(s_t1.point2.z));
							
							verts.add(new Float(s_t1.point3.x));
							verts.add(new Float(s_t1.point3.y));
							verts.add(new Float(s_t1.point3.z));
							
							texs.add(new Float(u1));
							texs.add(new Float(v1));
							
							texs.add(new Float(u2));
							texs.add(new Float(v2));
							
							texs.add(new Float(u3));
							texs.add(new Float(v3));
						}
					}
					/*if (verts.size()>0) {
						World.map.addObject(new Decal(verts, texs, texture, 10000l));
						Sounds.play("splatt");
					}*/
				}
			}
		}
		if (verts.size()>0) {
			World.map.addObject(new Decal(verts, texs, texture, 10000l));
			Sounds.play("splatt");
		}
	}
	
	
	
	public static boolean triangleSphereIntersection(JGL_3DTriangle t, JGL_3DPlane plane, 
														JGL_3DVector center, float offset) {
		
		float d;
		float sqrOffset = offset * offset;
		short pos = Util4Phys.triangle_pointPosition(t, center);
		
		float dist = plane.distance(center);
		if (dist<=0)
			return false;
		
		if (pos==0) {
			if (dist<offset)
				return true;
		}
		
		else if (pos==1 || pos==2) {
			d = Util4Phys.segmentSphereIntersection(center, offset, t.point1, t.point2, s_v1);
			if (d < sqrOffset)
				return true;
		}
		
		else if (pos==3 || pos==4) {
			d = Util4Phys.segmentSphereIntersection(center, offset, t.point2, t.point3, s_v1);
			if (d < sqrOffset)
				return true;
		}
		
		else if (pos==5 || pos==6) {
			d = Util4Phys.segmentSphereIntersection(center, offset, t.point3, t.point1, s_v1);
			if (d < sqrOffset)
				return true;
		}
		
		return false;
	}
	
}
