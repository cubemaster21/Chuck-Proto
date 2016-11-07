package com.toasted.chuck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.toasted.chuck.entities.Entity;
import com.toasted.chuck.entities.EntityBat;
import com.toasted.chuck.entities.EntityBox;
import com.toasted.chuck.entities.EntityPlayer;
import com.toasted.chuck.entities.EntityPressurePad;
import com.toasted.chuck.entities.EntityTorch;

public class Level {
	private EntityPlayer player;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Comparator<Entity> zSorter;
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tmRenderer;
	private ArrayList<Rectangle> collisions = new ArrayList<Rectangle>();
	private ArrayList<Light> lights = new ArrayList<Light>();
	private HashMap<Rectangle, MapObject> collisionLookupTable = new HashMap<Rectangle, MapObject>();
	private ArrayList<SecretRoom> secretRooms = new ArrayList<SecretRoom>();
	
	private Sound doorOpening = Audio.getSound("doorOpen.wav");
	
	public Level(String filename, Graphics graphics){
		tiledMap = new TmxMapLoader().load("testmap.tmx");
		tmRenderer = new OrthogonalTiledMapRenderer(tiledMap, graphics.getBatch());
		zSorter = new Comparator<Entity>(){

			public int compare(Entity arg0, Entity arg1) {
				return (int) Math.signum(arg1.getZSortValue() - arg0.getZSortValue());
				
			}
			
		};
		player = new EntityPlayer();
		spawnEntity(player);
		buildLevel();
		
	}
	private void buildLevel(){
		for(MapLayer ml: tiledMap.getLayers()){
			if(ml.getName().contains("secret")){
//				hiddenLayers.add(ml.getName());
				MapLayer spawner = tiledMap.getLayers().get(ml.getName() + "Spawn");
				SecretRoom room = new SecretRoom();
				room.roomName = ml.getName();
				if(spawner != null){
					for(MapObject o: spawner.getObjects()){
						RectangleMapObject rmo = (RectangleMapObject) o;
						Rectangle r = new Rectangle(rmo.getRectangle());
						room.entityHiders.add(r);					
					}
					System.out.println(room.entityHiders.size() + " hiders added");
				}
				secretRooms.add(room);
				ml.setVisible(false);
			}
		}
		
		MapLayer ml = tiledMap.getLayers().get("collisions");
		for(MapObject o: ml.getObjects()){
			RectangleMapObject rmo = (RectangleMapObject) o;
			Rectangle r = new Rectangle(rmo.getRectangle());
			collisions.add(r);
			collisionLookupTable.put(r, o);
			
		}
		MapLayer lightLayer = tiledMap.getLayers().get("staticLights");
		Vector2 t = new Vector2();
		for(MapObject o: lightLayer.getObjects()){
			RectangleMapObject rmo = (RectangleMapObject) o;
			t = new Rectangle(rmo.getRectangle()).getCenter(t);
			lights.add(new Light(t.x, t.y, Light.VAL_TORCH));
			
		}
		MapLayer objectLayer = tiledMap.getLayers().get("objectSpawn");
		for(MapObject o: objectLayer.getObjects()){
			RectangleMapObject rmo = (RectangleMapObject) o;
			Rectangle r = new Rectangle(rmo.getRectangle());
			t = r.getCenter(t);
			boolean hidden = false;
			for(SecretRoom secretRoom: secretRooms){
				for(Rectangle hider: secretRoom.entityHiders){
					if(hider.overlaps(r)){
						hidden = true;
						buildEntity(o, secretRoom.toSpawnOnReveal);
					}
				}
			}
			if(hidden) continue;
			
			buildEntity(o, null);
			
					
			
		}
	}
	
	
	public void revealSecretRoom(String name){
		
		MapLayer layer = tiledMap.getLayers().get(name);
		if(layer != null){
			layer.setVisible(true);
			doorOpening.play();
			
			
		} else {
			System.err.println("Failed to reveal room: " + name);
		}
		for(SecretRoom sr: secretRooms){
			if(sr.roomName.equals(name)){
				for(Entity e: sr.toSpawnOnReveal){
					spawnEntity(e);
				}
				sr.toSpawnOnReveal.clear();
			}
		}
	}
	private void buildEntity(MapObject o, ArrayList<Entity> listToAdd){
		if(o.getProperties().get("type") == null) {
			System.err.println("Unclassified Entity found");
			return;
		}
		RectangleMapObject rmo = (RectangleMapObject) o;
		Rectangle r = new Rectangle(rmo.getRectangle());
		Vector2 t = new Vector2();
		t = r.getCenter(t);
		Entity newEntity = null;
		String type = (String) o.getProperties().get("type");
		if(type.equals("torch")){
			newEntity = new EntityTorch(t.x - r.getWidth() / 2, t.y - r.getHeight() / 2);
			if(o.getProperties().containsKey("customColor")){
				LightEmitter le  = (LightEmitter)newEntity;
				String[] rgbValues = ((String)o.getProperties().get("customColor")).split(",");
//				System.out.println("Setting CUSTOM RGB");
				le.getLight().setLightColor(Float.parseFloat(rgbValues[0]), Float.parseFloat(rgbValues[1]), Float.parseFloat(rgbValues[2]));
			}
		}
		if(type.equals("box")){
			newEntity = new EntityBox(t.x - r.getWidth() / 2, t.y - r.getHeight() / 2);
		}
		if(type.equals("pressurePad")){
			newEntity = new EntityPressurePad(t.x - r.getWidth() / 2, t.y - r.getHeight() / 2);
		}
		if(type.equals("bat")){
			newEntity = new EntityBat(r.x, r.y);
		}
		if(listToAdd == null){
			spawnEntity(newEntity);
		} else {
			listToAdd.add(newEntity);
		}
	}
	public void spawnEntity(Entity e){
		if(e instanceof LightEmitter){
			lights.add(((LightEmitter) e).getLight());
		}
		entities.add(e);
	}
	public void update(float delta){
		
		for(int i = 0;i < entities.size();i++){
			Entity e = entities.get(i);
			e.update(delta, this);
		}
		Collections.sort(entities, zSorter);
	}
	public void draw(Graphics graphics){
		graphics.cam.position.x =  player.getCenterX();
		graphics.cam.position.y = player.getCenterY();
		
		graphics.cam.update();
		
//		
//		if(testLightFade){
//			graphics.lightValueMultiplier -= Gdx.graphics.getDeltaTime();
//			graphics.lightValueMultiplier = Math.max(0, graphics.lightValueMultiplier);
//		}

		graphics.prepare();
		graphics.passLightsToShader(lights);
		
		
		tmRenderer.setView(graphics.cam);
		tmRenderer.render();
		
		graphics.startSprite();
		
		for(Entity e: entities){
			e.draw(graphics);
		}
		graphics.endSprite();
	}
	public EntityPlayer getPlayer(){
		return player;
	}
	public boolean isCollisionBoxDestructable(Rectangle r){
		MapObject mo = collisionLookupTable.get(r);
		if(mo == null) return false;
		return mo.getProperties().containsKey("destructable");
	}
	public MapProperties getCollisionProperties(Rectangle r){
		MapObject mo = collisionLookupTable.get(r);
		if(mo == null) throw new NullPointerException("This rectangle does not belong to the collision lookup table");
		return mo.getProperties();
	}
	public ArrayList<Rectangle> getCollisions(){
		return collisions;
	}
	public ArrayList<Entity> getEntities(){
		return entities;
	}
}
