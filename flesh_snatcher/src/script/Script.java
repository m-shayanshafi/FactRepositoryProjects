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

package script;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.StringTokenizer;
import java.util.Vector;

import main.Player;

import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.BillboardNode;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.QuadMesh;
import com.jme.scene.TexCoords;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.image.Texture;
import com.jme.light.Light;
import com.jme.light.SpotLight;
import com.jme.light.PointLight;
import com.jme.light.DirectionalLight;
import com.jme.system.DisplaySystem;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.geom.BufferUtils;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.model.animation.KeyframeController;
import com.jmex.model.animation.JointController;

import phys.Bsp_tree;
import phys.Shape;
import phys.Shape_bsp;
import phys.Shape_cylinder;
import phys.Shape_sphere;
import phys.Shape_aabb;
import phys.Mover;
import phys.Mover_none;
import phys.Mover_linear;
import phys.Mover_cycle;
import phys.Mover_gravity;
import phys.Motion;
import phys.Motion_slide;
import phys.Motion_slide_2;
import phys.Motion_stop;
import phys.Motion_bounce;
import phys.Motion_NoCollision;
//import phys.Util4Phys;

import ai.TurretAI;
import ai.SkeletonAI;
import ai.ZombieAI;
import ai.ZombieAIpath;
import ai.ZombieAIsimple;

import jglcore.JGL;
import jglcore.JGL_3DMatrix;
import jglcore.JGL_3DVector;
import jglcore.JGL_3DMesh;
import entity.Entity;
import entity.PlayerEntity;
import entity.Shoot;
import entity.Weapon;
import entity.Zombie01;
import entity.Rocket01;
import entity.Turret01;
import entity.Skeleton01;
import entity.Factory;
import entity.Blood;
import entity.ScriptBox;
import entity.KeyObject;
import entity.DoorObject;
import entity.StaticObject;
import entity.Explosible;
import entity.Explosion;
import struct.Explode;
import struct.Explode2D;
import struct.Explode3D;
import struct.DLNode;
import sound.Sounds;
import world.World;
import world.Entities;
import world.Kinematic;
import input.Data3D;
import input.LoadHelper;
import input.Reader_Milkshape;


/**
 * Class executing the game scripts.
 * 
 * @author Nicolas Devere
 *
 */
public final class Script {
	
	// Script request
	public static String SCRIPT				= "script";
	
	// Verbs
	public static String LOAD_SOUND			= "loadsound";
	public static String LOAD_TEXTURE		= "loadtexture";
	public static String LOAD_BSP			= "loadbsp";
	public static String LOAD_NODE			= "loadnode";
	public static String LINK_NODES			= "linknodes";
	public static String STORE_ENTITY		= "storeentity";
	public static String FREE				= "free";
	public static String FREE_ALL			= "freeall";
	public static String INSERT_PLAYER		= "insertplayer";
	public static String INSERT_CHARA		= "insertchara";
	public static String INSERT_OBJECT		= "insertobject";
	public static String INSERT_SCRIPT		= "insertscript";
	public static String PLAY_MUSIC			= "playmusic";
	public static String STOP_MUSIC			= "stopmusic";
	public static String PLAY_KINEMATIC		= "playkinematic";
	public static String STOP_KINEMATIC		= "stopkinematic";
	public static String END_LEVEL			= "endlevel";
	public static String SET_PROPERTY		= "setproperty";
	public static String RESET_PROPERTIES	= "resetproperties";
	public static String ROTATE				= "rotate";
	public static String TRANSLATE			= "translate";
	public static String SCALE				= "scale";
	public static String START_DLIGHT		= "startdlight";
	public static String STOP_DLIGHT		= "stopdlight";
	public static String SET_DLIGHT			= "setdlight";
	
	// Entities
	public static String ZOMBIE01 	= "zombie01";
	public static String ROCKET01 	= "rocket01";
	public static String TURRET01 	= "turret01";
	public static String SKELETON01 = "skeleton01";
	public static String FACTORY 	= "factory";
	public static String SCRIPTBOX 	= "scriptbox";
	public static String KEY		= "key";
	public static String DOOR		= "door";
	public static String OBJECT		= "object";
	public static String EXPLOSIBLE	= "explosible";
	public static String EXPLOSION	= "explosion";
	public static String EXP2D		= "exp2d";
	public static String EXP3D		= "exp3d";
	public static String STORED 	= "stored";
	
	public static String PARTICLES 	= "particles";
	public static String EXP_QUAD	= "explosionquad";
	
	// Subjects
	public static String AI_SIMPLE 	= "aisimple";
	public static String AI_PATH 	= "aipath";
	public static String USER 		= "user";
	
	// Devices
	public static String SPHERE 	= "sphere";
	public static String AABB 		= "aabb";
	public static String CYLINDER 	= "cylinder";
	public static String STOP 		= "stop";
	public static String SLIDE 		= "slide";
	public static String SLIDE2 	= "slide2";
	public static String BOUNCE 	= "bounce";
	public static String NOCOLLISION= "nocollision";
	public static String LINEAR 	= "linear";
	public static String GRAVITY 	= "gravity";
	public static String CYCLE 		= "cycle";
	public static String CUBIC 		= "cubic";
	public static String NONE 		= "none";
	public static String WEAPON 	= "weapon";
	public static String LIGHT 		= "light";
	public static String LIGHT_SPOT = "spot";
	public static String LIGHT_POINT= "point";
	public static String LIGHT_DIR 	= "direction";
	
	// Properties
	public static String SPEED 		= "speed";
	
	
	
	public static void execute(StringTokenizer script) {
		
		if (script.hasMoreTokens()) {
			String verb = script.nextToken();
			
			if (verb.equals(LOAD_SOUND))
				loadSound(script);
			
			if (verb.equals(LOAD_TEXTURE))
				loadTexture(script);
			
			if (verb.equals(LOAD_BSP))
				loadBsp(script);
			
			if (verb.equals(LOAD_NODE))
				loadNode(script);
			
			if (verb.equals(LINK_NODES))
				linkNodes(script);
			
			if (verb.equals(STORE_ENTITY)) {
				String id = script.nextToken();
				Entities.addEntity(getEntity(script), id);
			}
			
			if (verb.equals(FREE)) {}
			
			if (verb.equals(FREE_ALL)) {
				Entities.clear();
				Sounds.clear();
			}
			
			if (verb.equals(INSERT_PLAYER))
				Player.init(getPlayer(script));
			
			if (verb.equals(INSERT_CHARA)) {
				Entity entity = getEntity(script);
				if (entity!=null)
					World.map.addCharacter(entity);
			}
			
			if (verb.equals(INSERT_OBJECT)) {
				Entity entity = getEntity(script);
				if (entity!=null)
					World.map.addObject(entity);
			}
			
			if (verb.equals(INSERT_SCRIPT)) {
				Entity entity = getEntity(script);
				if (entity!=null)
					World.map.addScriptBox(entity);
			}
			
			if (verb.equals(PLAY_MUSIC))
				Sounds.playMusic(script.nextToken(), Float.parseFloat(script.nextToken()));
			
			if (verb.equals(STOP_MUSIC))
				Sounds.stopMusic();
			
			if (verb.equals(PLAY_KINEMATIC))
				playKinematic(script.nextToken());
			
			if (verb.equals(STOP_KINEMATIC))
				stopKinematic();
			
			if (verb.equals(END_LEVEL))
				World.map.setFinished();
			
			if (verb.equals(SET_PROPERTY))
				setProperty(script);
			
			if (verb.equals(RESET_PROPERTIES))
				resetProperties();
			
			if (verb.equals(START_DLIGHT)) {
				String name = script.nextToken();
				float fps = Float.parseFloat(script.nextToken());
				((DLNode)World.map.getDisplayNode(name).getNode()).startAnimation(fps);
			}
			
			if (verb.equals(STOP_DLIGHT))
				((DLNode)World.map.getDisplayNode(script.nextToken()).getNode()).stopAnimation();
			
			if (verb.equals(SET_DLIGHT)) {
				String name = script.nextToken();
				int index = Integer.parseInt(script.nextToken());
				((DLNode)World.map.getDisplayNode(name).getNode()).setAnimationTexture(index);
			}
		}
	}
	
	
	
	
	public static PlayerEntity getPlayer(StringTokenizer script) {
		
		float posX = Float.parseFloat(script.nextToken());
	    float posY = Float.parseFloat(script.nextToken());
	    float posZ = Float.parseFloat(script.nextToken());
	    float angleX = Float.parseFloat(script.nextToken());
	    float angleY = Float.parseFloat(script.nextToken());
	    float angleZ = Float.parseFloat(script.nextToken());
	    
	    PlayerEntity player = new PlayerEntity(	posX, posY, posZ, angleX, angleY, angleZ, 
	    										getNodeCopy(script.nextToken()), getCMotion(script));
	    player.linkWeapon(Script.getWeapon(script));
	    
		return player;
	}
	
	
	
	/**
	 * Return the entity described by the specified String list.
	 * @param script : the string list stocking the Entity
	 * @return the Entity
	 */
	public static Entity getEntity(StringTokenizer script) {
		if (script.hasMoreTokens()) {
			String subject = script.nextToken();
			if (subject.equals(ZOMBIE01))
				return getZombie01(script);
			if (subject.equals(ROCKET01))
				return getRocket01(script);
			if (subject.equals(TURRET01))
				return getTurret01(script);
			if (subject.equals(SKELETON01))
				return getSkeleton01(script);
			if (subject.equals(FACTORY))
				return getFactory(script);
			if (subject.equals(SCRIPTBOX))
				return Entities.getEntity(script.nextToken());
			if (subject.equals(KEY))
				return getKey(script);
			if (subject.equals(DOOR))
				return getDoor(script);
			if (subject.equals(OBJECT))
				return getObject(script);
			if (subject.equals(EXPLOSIBLE))
				return getExplosible(script);
			if (subject.equals(STORED))
				return Entities.getEntity(script.nextToken());
		}
		return null;
	}
	
	/**
	 * Returns a new Zombie01 described by the specified String list.
	 * @param script : the string list stocking the Zombie01 data
	 * @return the new Zombie01
	 */
	public static Zombie01 getZombie01(StringTokenizer script) {
		
		try {
			
			Node node = Entities.getNode(script.nextToken());
			
			String bId = script.nextToken();
			long time = Long.parseLong(script.nextToken());
			Blood[] bloods = new Blood[3];
			bloods[0] = new Blood(Entities.getNode(bId), time);
			bloods[1] = new Blood(Entities.getNode(bId), time);
			bloods[2] = new Blood(Entities.getNode(bId), time);
			
			int team = Integer.parseInt(script.nextToken());
			float px = Float.parseFloat(script.nextToken());
			float py = Float.parseFloat(script.nextToken());
			float pz = Float.parseFloat(script.nextToken());
			float ax = Float.parseFloat(script.nextToken());
			float ay = Float.parseFloat(script.nextToken());
			float az = Float.parseFloat(script.nextToken());
			float life = Float.parseFloat(script.nextToken());
			float dam = Float.parseFloat(script.nextToken());
			float speed = Float.parseFloat(script.nextToken());
			Shape shape = getCShape(script);
			Mover mover = getCMover(script, shape);
			Motion motion = getCMotion(script);
			
			ZombieAI prototype;
			String aiType = script.nextToken();
			if (aiType.equals(AI_PATH))
				prototype = new ZombieAIpath(null, Entities.getPathGraph(script.nextToken()));
			else
				prototype = new ZombieAIsimple(null);
			
			Zombie01 z = new Zombie01(	team, px, py, pz, ax, ay, az, life, dam, speed, node, bloods, prototype, 
										shape, mover, motion);
			
			while (script.hasMoreTokens()) {
				String comToken = script.nextToken();
				if (comToken.equals(SCRIPTBOX))
					z.storeScriptBox((ScriptBox)Entities.getEntity(script.nextToken()));
			}
			
			return z;
		}
		catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	
	/**
	 * Returns a new Rocket01 described by the specified String list.
	 * @param script : the string list stocking the Rocket01 data
	 * @return the new Rocket01
	 */
	public static Rocket01 getRocket01(StringTokenizer script) {
		
		Node node = Entities.getNode(script.nextToken());
		Node hitNode = Entities.getNode(script.nextToken());
		Explosion expNode = getExplosion(script);
		
		int team = Integer.parseInt(script.nextToken());
		float px = Float.parseFloat(script.nextToken());
		float py = Float.parseFloat(script.nextToken());
		float pz = Float.parseFloat(script.nextToken());
		float ax = Float.parseFloat(script.nextToken());
		float ay = Float.parseFloat(script.nextToken());
		float az = Float.parseFloat(script.nextToken());
		float rx = Float.parseFloat(script.nextToken());
		float ry = Float.parseFloat(script.nextToken());
		float rz = Float.parseFloat(script.nextToken());
		float life = Float.parseFloat(script.nextToken());
		float dam = Float.parseFloat(script.nextToken());
		float speed = Float.parseFloat(script.nextToken());
		Shape shape = getCShape(script);
		Mover mover = getCMover(script, shape);
		Motion motion = getCMotion(script);
		
		Rocket01 r = new Rocket01(	team, px, py, pz, ax, ay, az, rx, ry, rz, life, dam, speed, 
									node, hitNode, expNode, shape, mover, motion);
		
		while (script.hasMoreTokens()) {
			
			String comToken = script.nextToken();
			
			if (comToken.equals(SCRIPTBOX))
				r.storeScriptBox((ScriptBox)Entities.getEntity(script.nextToken()));
		}
		
		return r;
	}
	
	
	
	/**
	 * Returns a new Turret01 described by the specified String list.
	 * @param script : the string list stocking the Turret01 data
	 * @return the new Turret01
	 */
	public static Turret01 getTurret01(StringTokenizer script) {
		
		Node base = Entities.getNode(script.nextToken());
		Node arms = Entities.getNode(script.nextToken());
		Node canon = Entities.getNode(script.nextToken());
		
		String bId = script.nextToken();
		long time = Long.parseLong(script.nextToken());
		Blood[] bloods = new Blood[3];
		bloods[0] = new Blood(Entities.getNode(bId), time);
		bloods[1] = new Blood(Entities.getNode(bId), time);
		bloods[2] = new Blood(Entities.getNode(bId), time);
		
		Explosion expNode = getExplosion(script);
		
		int team = Integer.parseInt(script.nextToken());
		float px = Float.parseFloat(script.nextToken());
		float py = Float.parseFloat(script.nextToken());
		float pz = Float.parseFloat(script.nextToken());
		float ax = Float.parseFloat(script.nextToken());
		float ay = Float.parseFloat(script.nextToken());
		float az = Float.parseFloat(script.nextToken());
		float fy = Float.parseFloat(script.nextToken());
		float fz = Float.parseFloat(script.nextToken());
		float life = Float.parseFloat(script.nextToken());
		float dam = Float.parseFloat(script.nextToken());
		float sp = Float.parseFloat(script.nextToken());
		Shape shape = getCShape(script);
		Mover mover = getCMover(script, shape);
		Motion motion = getCMotion(script);
		
		Turret01 t = new Turret01(	team, px, py, pz, ax, ay, az, fy, fz, life, dam, sp, 
									base, arms, canon, expNode, bloods, shape, mover, motion);
		t.linkWeapon(Script.getWeapon(script));
		
		long shootFrequency = Long.parseLong(script.nextToken());
		int nbKeys = Integer.parseInt(script.nextToken());
		float xr[] = new float[nbKeys];
		float yr[] = new float[nbKeys];
		for (int j=0; j<nbKeys; j++) {
			xr[j] = Float.parseFloat(script.nextToken());
			yr[j] = Float.parseFloat(script.nextToken());
		}
		t.linkAI(new TurretAI(t, xr, yr, shootFrequency));
		
		while (script.hasMoreTokens()) {
			
			String comToken = script.nextToken();
			
			if (comToken.equals(SCRIPTBOX))
				t.storeScriptBox((ScriptBox)Entities.getEntity(script.nextToken()));
		}
		
		return t;
	}
	
	
	
	/**
	 * Returns a new Skeleton01 described by the specified String list.
	 * @param script : the string list stocking the Skeleton01 data
	 * @return the new Skeleton01
	 */
	public static Skeleton01 getSkeleton01(StringTokenizer script) {
		
		try {
			
			Node node = Entities.getNode(script.nextToken());
			
			String bId = script.nextToken();
			long time = Long.parseLong(script.nextToken());
			Blood[] bloods = new Blood[3];
			bloods[0] = new Blood(Entities.getNode(bId), time);
			bloods[1] = new Blood(Entities.getNode(bId), time);
			bloods[2] = new Blood(Entities.getNode(bId), time);
			
			int team = Integer.parseInt(script.nextToken());
			float px = Float.parseFloat(script.nextToken());
			float py = Float.parseFloat(script.nextToken());
			float pz = Float.parseFloat(script.nextToken());
			float ax = Float.parseFloat(script.nextToken());
			float ay = Float.parseFloat(script.nextToken());
			float az = Float.parseFloat(script.nextToken());
			float life = Float.parseFloat(script.nextToken());
			float dam = Float.parseFloat(script.nextToken());
			float speed = Float.parseFloat(script.nextToken());
			Shape shape = getCShape(script);
			Mover mover = getCMover(script, shape);
			Motion motion = getCMotion(script);
			
			Skeleton01 s = new Skeleton01(team, px, py, pz, ax, ay, az, life, dam, speed, node, bloods, 
										shape, mover, motion);
			
			s.linkWeapon(Script.getWeapon(script));
			
			long shootFrequency = Long.parseLong(script.nextToken());
			int nbKeys = Integer.parseInt(script.nextToken());
			JGL_3DVector[] points = new JGL_3DVector[nbKeys];
			for (int j=0; j<nbKeys; j++)
				points[j] = new JGL_3DVector(	Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken()));
			s.linkAI(new SkeletonAI(s, points, shootFrequency, false));
			
			while (script.hasMoreTokens()) {
				String comToken = script.nextToken();
				if (comToken.equals(SCRIPTBOX))
					s.storeScriptBox((ScriptBox)Entities.getEntity(script.nextToken()));
			}
			
			return s;
		}
		catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	
	public static Factory getFactory(StringTokenizer script) {
		
		Node node = getNodeCopy(script.nextToken());
		Node hitNode = getNodeCopy(script.nextToken());
		Explosion expNode = getExplosion(script);
		Explosion genNode = getExplosion(script);
        
        float posX = Float.parseFloat(script.nextToken());
	    float posY = Float.parseFloat(script.nextToken());
	    float posZ = Float.parseFloat(script.nextToken());
	    float life = Float.parseFloat(script.nextToken());
	    float speed = Float.parseFloat(script.nextToken());
	    long productionSpeed = Long.parseLong(script.nextToken());
	    int ennemyNumber = Integer.parseInt(script.nextToken());
	    Entity prototype = getEntity(script);
	    
	    Shape_bsp shape = new Shape_bsp(new JGL_3DVector(posX, posY, posZ), Entities.getBsp(script.nextToken()));
	    Mover mover = getCMover(script, shape);
	    
	    Factory f =  new Factory(	1, posX, posY, posZ, life, speed, productionSpeed, ennemyNumber, 
	    							node, hitNode, expNode, genNode, prototype, shape, mover);
	    
	    while (script.hasMoreTokens()) {
			
			String comToken = script.nextToken();
			if (comToken.equals(SCRIPTBOX))
				f.storeScriptBox((ScriptBox)Entities.getEntity(script.nextToken()));
		}
	    return f;
	}
	
	
	
	public static KeyObject getKey(StringTokenizer script) {
		
		try {
			
			Node node = Entities.getNode(script.nextToken());
	        
	        float posX = Float.parseFloat(script.nextToken());
		    float posY = Float.parseFloat(script.nextToken());
		    float posZ = Float.parseFloat(script.nextToken());
		    float angleX = Float.parseFloat(script.nextToken());
		    float angleY = Float.parseFloat(script.nextToken());
		    float angleZ = Float.parseFloat(script.nextToken());
		    
		    JGL_3DVector min = new JGL_3DVector(Float.parseFloat(script.nextToken()), 
					Float.parseFloat(script.nextToken()), 
					Float.parseFloat(script.nextToken()));
		    JGL_3DVector max = new JGL_3DVector(Float.parseFloat(script.nextToken()), 
					Float.parseFloat(script.nextToken()), 
					Float.parseFloat(script.nextToken()));
		    String id = script.nextToken();
		    
		    KeyObject object = new KeyObject(new Shape_aabb(new JGL_3DVector(posX, posY, posZ), min, max), 
					angleX, angleY, angleZ, id, node, 
					LoadHelper.getTexture(script.nextToken(), true), 
					LoadHelper.getTexture(script.nextToken(), true), 
					LoadHelper.getTexture(script.nextToken(), true));
		    
		    while (script.hasMoreTokens()) {
				String comToken = script.nextToken();
				if (comToken.equals(SCRIPTBOX))
					object.storeScriptBox((ScriptBox)Entities.getEntity(script.nextToken()));
		    }
		    
		    return object;
		}
		catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	public static DoorObject getDoor(StringTokenizer script) {
		
		try {
			
			Node node = Entities.getNode(script.nextToken());
	        
	        float posX = Float.parseFloat(script.nextToken());
		    float posY = Float.parseFloat(script.nextToken());
		    float posZ = Float.parseFloat(script.nextToken());
		    float angleX = Float.parseFloat(script.nextToken());
		    float angleY = Float.parseFloat(script.nextToken());
		    float angleZ = Float.parseFloat(script.nextToken());
		    
		    JGL_3DVector min = new JGL_3DVector(Float.parseFloat(script.nextToken()), 
					Float.parseFloat(script.nextToken()), 
					Float.parseFloat(script.nextToken()));
		    JGL_3DVector max = new JGL_3DVector(Float.parseFloat(script.nextToken()), 
					Float.parseFloat(script.nextToken()), 
					Float.parseFloat(script.nextToken()));
		    Shape_aabb shape = new Shape_aabb(new JGL_3DVector(posX, posY, posZ), min, max);
		    
		    Mover_cycle cmover = null;
			int nbKeys = Integer.parseInt(script.nextToken());
			JGL_3DVector[] keys = new JGL_3DVector[nbKeys];
			for (int k=0; k<nbKeys; k++)
				keys[k] = new JGL_3DVector(	Float.parseFloat(script.nextToken()), 
											Float.parseFloat(script.nextToken()), 
											Float.parseFloat(script.nextToken()));
			float speed = Float.parseFloat(script.nextToken());
			cmover = new Mover_cycle(shape.getPosition(), keys, speed, Mover_cycle.LINEAR);
	        
	        int nbEnts = Integer.parseInt(script.nextToken());
	        Vector ents = new Vector();
	        for (int k=0; k<nbEnts; k++)
	        	ents.add(getEntity(script));
	        
	        DoorObject object = new DoorObject(shape, angleX, angleY, angleZ, cmover, ents, node);
		    
	        while (script.hasMoreTokens()) {
				String comToken = script.nextToken();
				if (comToken.equals(SCRIPTBOX))
					object.storeScriptBox((ScriptBox)Entities.getEntity(script.nextToken()));
		    }
	        
		    return object;
		}
		catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	public static StaticObject getObject(StringTokenizer script) {
		
		Node node = Entities.getNode(script.nextToken());
		
		float posX = Float.parseFloat(script.nextToken());
	    float posY = Float.parseFloat(script.nextToken());
	    float posZ = Float.parseFloat(script.nextToken());
	    float angleX = Float.parseFloat(script.nextToken());
	    float angleY = Float.parseFloat(script.nextToken());
	    float angleZ = Float.parseFloat(script.nextToken());
	    
	    Shape_bsp bsp = new Shape_bsp(new JGL_3DVector(posX, posY, posZ), Entities.getBsp(script.nextToken()));
	    
		return new StaticObject(bsp, angleX, angleY, angleZ, node);
	}
	
	
	
	public static Explosible getExplosible(StringTokenizer script) {
		
		Node node = getNodeCopy(script.nextToken());
		Node hitNode = getNodeCopy(script.nextToken());
		Explosion expNode = getExplosion(script);
		Bsp_tree bsp = Entities.getBsp(script.nextToken());
        
        float posX = Float.parseFloat(script.nextToken());
	    float posY = Float.parseFloat(script.nextToken());
	    float posZ = Float.parseFloat(script.nextToken());
	    float life = Float.parseFloat(script.nextToken());
	    float damage = Float.parseFloat(script.nextToken());
	    float offset = Float.parseFloat(script.nextToken());
	    
	    Explosible e = new Explosible(2, posX, posY, posZ, life, damage, offset, 
	    								node, hitNode, expNode, bsp);
	    return e;
	}
	
	
	
	public static Explosion getExplosion(StringTokenizer script) {
		
		int nbExp = Integer.parseInt(script.nextToken());
		Explode exp[] = new Explode[nbExp];
		for (int i=0; i<nbExp; i++) {
			String type = script.nextToken();
			if (type.equals(EXP2D))
				exp[i] = new Explode2D(	(BillboardNode)Entities.getNode(script.nextToken()), 
										Integer.parseInt(script.nextToken()), 
										Integer.parseInt(script.nextToken()), 
										Long.parseLong(script.nextToken()), 
										Long.parseLong(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()));
			if (type.equals(EXP3D))
				exp[i] = new Explode3D(	Entities.getNode(script.nextToken()), 
										Long.parseLong(script.nextToken()), 
										Long.parseLong(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()));
		}
		return new Explosion(exp, script.nextToken());
	}
	
	
	/**
	 * Returns a new collision shape described by the specified String list.
	 * @param script : the string list stocking the new collision shape data
	 * @return the new collision shape
	 */
	public static Shape getCShape(StringTokenizer script) {
		
		Shape cshape = null;
		String cToken = script.nextToken();
		if (cToken.equals(SPHERE))
			cshape = new Shape_sphere(new JGL_3DVector(), Float.parseFloat(script.nextToken()));
		else if (cToken.equals(AABB))
			cshape = new Shape_aabb(new JGL_3DVector(), 
									new JGL_3DVector(	Float.parseFloat(script.nextToken()), 
														Float.parseFloat(script.nextToken()), 
														Float.parseFloat(script.nextToken())), 
									new JGL_3DVector(	Float.parseFloat(script.nextToken()), 
														Float.parseFloat(script.nextToken()), 
														Float.parseFloat(script.nextToken())));
		else if (cToken.equals(CYLINDER))
			cshape = new Shape_cylinder(new JGL_3DVector(), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()));
		return cshape;
	}
	
	
	/**
	 * Returns a new Mover described by the specified String list.
	 * @param script : the string list stocking the new Mover data
	 * @return the new Mover
	 */
	public static Mover getCMover(StringTokenizer script, Shape shape) {
		
		Mover cmover = null;
		String cToken = script.nextToken();
		if (cToken.equals(NONE))
			cmover = new Mover_none();
		else if (cToken.equals(LINEAR)) {
			JGL_3DVector v = new JGL_3DVector(	Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken()));
			v.normalize();
			cmover = new Mover_linear(v);
		}
		else if (cToken.equals(GRAVITY)) {
			JGL_3DVector v = new JGL_3DVector(	Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken()));
			v.normalize();
			cmover = new Mover_gravity(v);
		}
		else if (cToken.equals(CYCLE)) {
			String interpol = script.nextToken();
			int nbKeys = Integer.parseInt(script.nextToken());
			JGL_3DVector[] keys = new JGL_3DVector[nbKeys];
			for (int i=0; i<nbKeys; i++)
				keys[i] = new JGL_3DVector(	Float.parseFloat(script.nextToken()), 
											Float.parseFloat(script.nextToken()), 
											Float.parseFloat(script.nextToken()));
			if (interpol.equals(LINEAR))
				cmover = new Mover_cycle(shape.getPosition(), keys, 0f, Mover_cycle.LINEAR);
			if (interpol.equals(CUBIC))
				cmover = new Mover_cycle(shape.getPosition(), keys, 0f, Mover_cycle.CUBIC);
		}
		
		return cmover;
	}
	
	
	/**
	 * Returns a new Motion described by the specified String list.
	 * @param script : the string list stocking the new Motion data
	 * @return the new Motion
	 */
	public static Motion getCMotion(StringTokenizer script) {
		
		String cToken = script.nextToken();
		if (cToken.equals(SLIDE))
			return new Motion_slide(Float.parseFloat(script.nextToken()));
		else if (cToken.equals(SLIDE2))
			return new Motion_slide_2(Float.parseFloat(script.nextToken()));
		else if (cToken.equals(BOUNCE))
			return new Motion_bounce();
		else if (cToken.equals(STOP))
			return new Motion_stop();
		else if (cToken.equals(NOCOLLISION))
			return new Motion_NoCollision();
		return null;
	}
	
	
	
	public static void loadSound(StringTokenizer script) {
		String id = script.nextToken();
		Sounds.add(script.nextToken(), id, Integer.parseInt(script.nextToken()), Float.parseFloat(script.nextToken()));
	}
	
	
	
	public static void loadTexture(StringTokenizer script) {
		String id = script.nextToken();
		Entities.addTexture(LoadHelper.getTexture(script.nextToken(), true), id);
	}
	
	
	
	public static void loadBsp(StringTokenizer script) {
		try {
			String id = script.nextToken();
			Data3D data = new Reader_Milkshape(script.nextToken()).getData();
			JGL_3DMesh[] brushes = new JGL_3DMesh[data.subMeshes.size()];
			for (int i=0; i<brushes.length; i++)
				brushes[i] = (JGL_3DMesh)data.subMeshes.get(i);
			Entities.addBsp(new Bsp_tree(brushes), id);
		}
		catch(Exception ex) {
			return;
		}
	}
	
	
	
	public static void loadNode(StringTokenizer script) {
		try {
			
			Node node = null;
			
			String sName = script.nextToken();
			
			if (Entities.getNode(sName)!=null)
				return;
			
			String sToken = script.nextToken();
			if (sToken.endsWith("md2")) {
				node = LoadHelper.getMD2Node(sToken, script.nextToken());
		        
		        while (script.hasMoreTokens()) {
		        	String type = script.nextToken();
		        	if (type.equals(SPEED)) {
		        		KeyframeController kc = (KeyframeController) node.getChild(0).getController(0);
				        kc.setSpeed(Float.parseFloat(script.nextToken()));
				        kc.setRepeatType(Controller.RT_WRAP);
				        kc.setNewAnimationTimes(kc.getMinTime(),kc.getMaxTime());
				        kc.setCurTime(kc.getMinTime());
				        kc.setModelUpdate(false);
		        	}
		        	if (type.equals(LIGHT)) {
						LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
						lightState.setEnabled(true);
						lightState.attach(getLight(script));
						node.setRenderState(lightState);
					}
		        	if (type.equals(ROTATE))
		        		nodeRotate(node, 	Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(TRANSLATE))
		        		nodeTranslate(node, Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(SCALE))
		        		nodeScale(node, Float.parseFloat(script.nextToken()));
		        }
			}
			if (sToken.endsWith("md3")) {
				node = LoadHelper.getMD3Node(sToken, script.nextToken());
		        
		        while (script.hasMoreTokens()) {
		        	String type = script.nextToken();
		        	if (type.equals(SPEED)) {
		        		KeyframeController kc = (KeyframeController) node.getChild(0).getController(0);
				        kc.setSpeed(Float.parseFloat(script.nextToken()));
				        kc.setRepeatType(Controller.RT_WRAP);
				        kc.setNewAnimationTimes(kc.getMinTime(),kc.getMaxTime());
				        kc.setCurTime(kc.getMinTime());
				        kc.setModelUpdate(false);
		        	}
		        	if (type.equals(LIGHT)) {
						LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
						lightState.setEnabled(true);
						lightState.attach(getLight(script));
						node.setRenderState(lightState);
					}
		        	if (type.equals(ROTATE))
		        		nodeRotate(node, 	Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(TRANSLATE))
		        		nodeTranslate(node, Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(SCALE))
		        		nodeScale(node, Float.parseFloat(script.nextToken()));
		        }
			}
			if (sToken.endsWith("ms3d")) {
				node = LoadHelper.getMS3DNode(sToken);
		        
		        while (script.hasMoreTokens()) {
		        	String type = script.nextToken();
		        	if (type.equals(SPEED)) {
		        		JointController kc = (JointController)node.getChild(0).getController(0);
				        kc.setSpeed(Float.parseFloat(script.nextToken()));
				        kc.setRepeatType(Controller.RT_WRAP);
				        kc.setModelUpdate(false);
		        	}
		        	if (type.equals(LIGHT)) {
						LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
						lightState.setEnabled(true);
						lightState.attach(getLight(script));
						node.setRenderState(lightState);
					}
		        	if (type.equals(ROTATE))
		        		nodeRotate(node, 	Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(TRANSLATE))
		        		nodeTranslate(node, Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(SCALE))
		        		nodeScale(node, Float.parseFloat(script.nextToken()));
		        }
			}
			if (sToken.endsWith("tri")) {
				node = LoadHelper.getTriNode(sToken);
		        
		        while (script.hasMoreTokens()) {
		        	String type = script.nextToken();
		        	if (type.equals(LIGHT)) {
						LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
						lightState.setEnabled(true);
						lightState.attach(getLight(script));
						node.setRenderState(lightState);
					}
		        	if (type.equals(ROTATE))
		        		nodeRotate(node, 	Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(TRANSLATE))
		        		nodeTranslate(node, Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(SCALE))
		        		nodeScale(node, Float.parseFloat(script.nextToken()));
		        }
			}
			if (sToken.endsWith("obj")) {
				node = LoadHelper.getObjNode(sToken);
		        
		        while (script.hasMoreTokens()) {
		        	String type = script.nextToken();
		        	if (type.equals(LIGHT)) {
						LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
						lightState.setEnabled(true);
						lightState.attach(getLight(script));
						node.setRenderState(lightState);
					}
		        	if (type.equals(ROTATE))
		        		nodeRotate(node, 	Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(TRANSLATE))
		        		nodeTranslate(node, Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()), 
		        							Float.parseFloat(script.nextToken()));
		        	if (type.equals(SCALE))
		        		nodeScale(node, Float.parseFloat(script.nextToken()));
		        }
			}
			if (sToken.equals(PARTICLES)) {
				BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			    as1.setBlendEnabled(true);
			    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			    as1.setDestinationFunction(BlendState.DestinationFunction.One);
			    as1.setTestEnabled(true);
			    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
			    as1.setEnabled(true);

			    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			    ts.setTexture(LoadHelper.getTexture(script.nextToken(), true));
			    ts.setEnabled(true);
			    
			    ParticleMesh pMesh = ParticleFactory.buildParticles("particles", Integer.parseInt(script.nextToken()));
			    pMesh.setEmissionDirection(new Vector3f(Float.parseFloat(script.nextToken()), 
														Float.parseFloat(script.nextToken()), 
														Float.parseFloat(script.nextToken())));
			    pMesh.setInitialVelocity(Float.parseFloat(script.nextToken()));
			    pMesh.setStartSize(Float.parseFloat(script.nextToken()));
			    pMesh.setEndSize(Float.parseFloat(script.nextToken()));
			    pMesh.setMinimumLifeTime(Float.parseFloat(script.nextToken()));
			    pMesh.setMaximumLifeTime(Float.parseFloat(script.nextToken()));
			    pMesh.setStartColor(new ColorRGBA(	Float.parseFloat(script.nextToken()), 
													Float.parseFloat(script.nextToken()), 
													Float.parseFloat(script.nextToken()), 
													Float.parseFloat(script.nextToken())));
			    pMesh.setEndColor(new ColorRGBA(Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken()), 
												Float.parseFloat(script.nextToken())));
			    pMesh.setMaximumAngle(360f * FastMath.DEG_TO_RAD);
			    pMesh.getParticleController().setControlFlow(false);
			    pMesh.setParticlesInWorldCoords(true);
			    pMesh.warmUp(Integer.parseInt(script.nextToken()));
			    
			    node = new Node();
			    node.setRenderState(ts);
			    node.setRenderState(as1);
		        
			    pMesh.setModelBound(new BoundingSphere());
			    pMesh.updateModelBound();

			    node.attachChild(pMesh);
			}
			if (sToken.equals(EXP_QUAD)) {
				Texture tex = LoadHelper.getTexture(script.nextToken(), true);
				float x = Float.parseFloat(script.nextToken());
				float y = Float.parseFloat(script.nextToken());
				float width = Float.parseFloat(script.nextToken());
				float height = Float.parseFloat(script.nextToken());
				
				int wt = Integer.parseInt(script.nextToken());
				int ht = Integer.parseInt(script.nextToken());
				float xe = 1f / (float)wt;
				float ye = 1f / (float)ht;
				
				node = new BillboardNode("");
				
				float[] tmp = new float[12];
				tmp[0] = x; tmp[1] = y; tmp[2] = 0f;
				tmp[3] = x + width; tmp[4] = y; tmp[5] = 0f;
				tmp[6] = x + width; tmp[7] = y + height; tmp[8] = 0f;
				tmp[9] = x; tmp[10] = y + height; tmp[11] = 0f;
				FloatBuffer vbuf = BufferUtils.createFloatBuffer(tmp);
				
				tmp = new float[8];
				tmp[0] = 0f; tmp[1] = 0f;
				tmp[2] = xe; tmp[3] = 0f;
				tmp[4] = xe; tmp[5] = ye;
				tmp[6] = 0f; tmp[7] = ye;
				FloatBuffer tbuf = BufferUtils.createFloatBuffer(tmp);
				
				int[] tmpInt = new int[4];
				for (int i=0; i<4; i++) tmpInt[i] = i;
				IntBuffer ibuf = BufferUtils.createIntBuffer(tmpInt);
				
				tex.setTranslation(new Vector3f());
				tex.setApply(Texture.ApplyMode.Combine);
				TextureState texState=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
				texState.setTexture(tex, 0);
				
				QuadMesh mesh = new QuadMesh("", vbuf, null, null, new TexCoords(tbuf), ibuf);
				mesh.setRenderState(texState);
				BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		        as1.setBlendEnabled(true);
		        as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		        as1.setDestinationFunction(BlendState.DestinationFunction.OneMinusDestinationAlpha);
		        as1.setTestEnabled(true);
		        as1.setTestFunction(BlendState.TestFunction.GreaterThan);
		        as1.setEnabled(true);
		        mesh.setRenderState(as1);
		        mesh.updateGeometricState( 0.0f, true );
		        mesh.updateRenderState();
		        
		        node.attachChild(mesh);
		        node.updateGeometricState( 0.0f, true );
		        node.updateRenderState();
			}
			
			ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
	        buf.setEnabled( true );
	        buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo );
	        node.setRenderState( buf );
	        
	        node.updateGeometricState( 0.0f, true );
	        node.updateRenderState();
	        
			Entities.addNode(node, sName);
			
		}
		catch(Exception ex) {
			return;
		}
	}
	
	
	
	/**
	 * Returns a copy of the Node identified by the specified ID.
	 * 
	 * @param id : the Node ID
	 * @return a copy of the node, or an empty Node if not found
	 */
	public static Node getNodeCopy(String id) {
		
		try {
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			BinaryExporter.getInstance().save(Entities.getNode(id), BO);
			Node node = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
			node.updateGeometricState( 0.0f, true );
	        node.updateRenderState();
	        return node;
		}
		catch(Exception ex) {
			ex.printStackTrace(System.out);
			return new Node();
		}
	}
	
	
	/**
	 * 
	 * @param script
	 */
	public static void linkNodes(StringTokenizer script) {
		
		String id = script.nextToken();
		Node newNode = new Node();
		while (script.hasMoreTokens())
			newNode.attachChild(Entities.getNode(script.nextToken()));
		Entities.addNode(newNode, id);
	}
	
	
	
	/**
	 * Applies a rotation to the Node with the specified Euler angles in degrees 
	 * and in the YXZ order.
	 * 
	 * @param node : the Node to rotate
	 * @param x : the X axis angle in degrees
	 * @param y : the Y axis angle in degrees
	 * @param z : the Z axis angle in degrees
	 */
	public static void nodeRotate(Node node, float x, float y, float z) {
		JGL_3DMatrix tmpMatrix = new JGL_3DMatrix();
		Matrix3f nodeMatrix = new Matrix3f();
		tmpMatrix.identity();
		tmpMatrix.rotate(x, y, z, JGL.YXZ);
		nodeMatrix.m00 = tmpMatrix.m11;
		nodeMatrix.m01 = tmpMatrix.m12;
		nodeMatrix.m02 = tmpMatrix.m13;
		nodeMatrix.m10 = tmpMatrix.m21;
		nodeMatrix.m11 = tmpMatrix.m22;
		nodeMatrix.m12 = tmpMatrix.m23;
		nodeMatrix.m20 = tmpMatrix.m31;
		nodeMatrix.m21 = tmpMatrix.m32;
		nodeMatrix.m22 = tmpMatrix.m33;
		node.setLocalRotation(nodeMatrix);
	}
	
	
	/**
	 * Applies a translation to the Node on the 3 axis.
	 * 
	 * @param node : the Node to translate
	 * @param x : the X axis translation
	 * @param y : the Y axis translation
	 * @param z : the Z axis translation
	 */
	public static void nodeTranslate(Node node, float x, float y, float z) {
		node.setLocalTranslation(x, y, z);
	}
	
	
	/**
	 * Applies a scale to the Node.
	 * 
	 * @param node : the Node to scale
	 * @param scale : the scale ratio
	 */
	public static void nodeScale(Node node, float scale) {
		node.setLocalScale(scale);
	}
	
	
	
	/**
	 * Returns a Light object contained in the specified String list.
	 * 
	 * @param script : the string list stocking the light
	 * @return the light, or null if wrong data
	 */
	public static Light getLight(StringTokenizer script) {
		String type = script.nextToken();
    	Light light = null;
    	if (type.equals(LIGHT_SPOT)) {
    		light = new SpotLight();
    		((SpotLight)light).setLocation( new Vector3f(	Float.parseFloat(script.nextToken()), 
															Float.parseFloat(script.nextToken()), 
															Float.parseFloat(script.nextToken()) ) );
    		((SpotLight)light).setDirection( new Vector3f(	Float.parseFloat(script.nextToken()), 
															Float.parseFloat(script.nextToken()), 
															Float.parseFloat(script.nextToken()) )
															.normalizeLocal() );
    		((SpotLight)light).setAngle(Float.parseFloat(script.nextToken()));
    		((SpotLight)light).setExponent(Float.parseFloat(script.nextToken()));
    	}
    	if (type.equals(LIGHT_POINT)) {
    		light = new PointLight();
    		((PointLight)light).setLocation( new Vector3f(	Float.parseFloat(script.nextToken()), 
															Float.parseFloat(script.nextToken()), 
															Float.parseFloat(script.nextToken()) ) );
    	}
    	if (type.equals(LIGHT_DIR)) {
    		light = new DirectionalLight();
    		((DirectionalLight)light).setDirection( new Vector3f(	Float.parseFloat(script.nextToken()), 
																	Float.parseFloat(script.nextToken()), 
																	Float.parseFloat(script.nextToken()) )
    																.normalizeLocal() );
    	}
    	
    	light.setSpecular( new ColorRGBA(Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()) ) );
    	light.setDiffuse( new ColorRGBA(Float.parseFloat(script.nextToken()), 
    									Float.parseFloat(script.nextToken()), 
    									Float.parseFloat(script.nextToken()), 
    									Float.parseFloat(script.nextToken()) ) );
    	light.setAmbient( new ColorRGBA(Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()), 
										Float.parseFloat(script.nextToken()) ) );
    	light.setEnabled(true);
    	//light.setAttenuate(true);
    	//light.setShadowCaster(true);
    	return light;
	}
	
	
	
	/**
	 * Returns a new Weapon described by the specified String list.
	 * @param script : the string list stocking the new Weapon data
	 * @return the new Weapon
	 */
	public static Weapon getWeapon(StringTokenizer script) {
		
		try {
			String tex = script.nextToken();
			String name = script.nextToken();
			int maxAmmo = Integer.parseInt(script.nextToken());
			float damage = Float.parseFloat(script.nextToken());
			float speed = Float.parseFloat(script.nextToken());
			float fireX = Float.parseFloat(script.nextToken());
			float fireY = Float.parseFloat(script.nextToken());
			float fireZ = Float.parseFloat(script.nextToken());
			String nodeID = script.nextToken();
			Explosion exp = getExplosion(script);
			String soundID = script.nextToken();
			
			boolean infiniteAmmo = (maxAmmo<0);
			if (infiniteAmmo)
				maxAmmo = 10;
			
			Shoot[] shoots = new Shoot[maxAmmo];
			
			for (int i=0; i<maxAmmo; i++) {
				shoots[i] = new Shoot(	Entities.getNode(nodeID), (Explosion)exp.clone(), 
										new Shape_sphere(new JGL_3DVector(), 0.5f), 
										new Mover_linear(new JGL_3DVector()), 
										1f, damage, speed);
			}
			
			return new Weapon(	null, LoadHelper.getTexture(tex, true), name, 
								shoots, infiniteAmmo, soundID, null, new JGL_3DVector(fireX, fireY, fireZ));
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	
	
	/**
	 * Plays the Kinematic given its ID.
	 * @param id : the Kinematic ID
	 */
	public static void playKinematic(String id) {
		Kinematic kinematic = Entities.getKinematic(id);
		if (kinematic!=null) {
			World.kinematic = kinematic;
			World.mode = World.KINEMATIC;
		}
	}
	
	
	/**
	 * Stops the current Kinematic and sets the in-game display.
	 */
	public static void stopKinematic() {
		World.kinematic.finalScripts();
		World.kinematic = null;
		World.mode = World.INGAME;
	}
	
	
	
	
	private static void setProperty(StringTokenizer script) {
		
		String prop = script.nextToken();
		float arg = Float.parseFloat(script.nextToken());
		
		if (prop.equals(GRAVITY))
			Mover_gravity.GRAVITY = arg;
	}
	
	
	private static void resetProperties() {
		Mover_gravity.GRAVITY = 300f;
	}
	
	
	
}
