package com.toasted.chuck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.toasted.chuck.entities.Entity;
import com.toasted.chuck.entities.EntityBox;
import com.toasted.chuck.entities.EntityPlayer;
import com.toasted.chuck.entities.EntityTorch;

public class ChuckGame extends ApplicationAdapter implements InputProcessor{
	Graphics graphics;
	EntityPlayer player;
	ArrayList<Entity> entities = new ArrayList<Entity>();
	Comparator<Entity> zSorter;
	public static TiledMap tiledMap;
	OrthogonalTiledMapRenderer tmRenderer;
	ArrayList<Rectangle> collisions = new ArrayList<Rectangle>();
	ArrayList<Light> lights = new ArrayList<Light>();
	boolean testLightFade = false;
	public static HashMap<Rectangle, MapObject> collisionLookupTable = new HashMap<Rectangle, MapObject>();
//	public static ArrayList<String> hiddenLayers = new ArrayList<String>();
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		graphics = new Graphics();
		player = new EntityPlayer();
		entities.add(new EntityBox(20, 20));
		entities.add(new EntityBox(100, 100));
		entities.add(player);
		zSorter = new Comparator<Entity>(){

			public int compare(Entity arg0, Entity arg1) {
				return (int) Math.signum(arg1.getY() - arg0.getY());
				
			}
			
		};
		tiledMap = new TmxMapLoader().load("testmap.tmx");
		tmRenderer = new OrthogonalTiledMapRenderer(tiledMap, graphics.getBatch());
		lights.add(player.getLight());
		
		for(MapLayer ml: tiledMap.getLayers()){
			if(ml.getName().contains("secret")){
//				hiddenLayers.add(ml.getName());
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
					
//			System.out.println(o.getProperties().get("type"));
			if(o.getProperties().get("type").equals("torch")){
				EntityTorch torch = new EntityTorch(t.x - r.getWidth() / 2, t.y - r.getHeight() / 2);
				spawnEntity(torch);
			}
		}
		
	}

	@Override
	public void render () {
		
		for(Entity e: entities){
			e.update(Gdx.graphics.getDeltaTime(), entities, collisions);
		}
		Collections.sort(entities, zSorter);
		
		graphics.cam.position.x =  player.getCenterX();
		graphics.cam.position.y = player.getCenterY();
		
		graphics.cam.update();
		
		
		if(testLightFade){
			graphics.lightValueMultiplier -= Gdx.graphics.getDeltaTime();
			graphics.lightValueMultiplier = Math.max(0, graphics.lightValueMultiplier);
		}

		graphics.prepare();
		graphics.passLightsToShader(lights);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tmRenderer.setView(graphics.cam);
		tmRenderer.render();
		
		graphics.startSprite();
		
		for(Entity e: entities){
			e.draw(graphics);
		}
		graphics.endSprite();
//		System.out.println("FPS:" + Gdx.graphics.getFramesPerSecond());
	}
	public void spawnEntity(Entity e){
		if(e instanceof LightEmitter){
			lights.add(((LightEmitter) e).getLight());
		}
		entities.add(e);
	}
	
	@Override
	public void dispose () {
	}

	@Override
	public boolean keyDown(int keycode) {
		player.getController().acceptEvent(keycode, true, player, entities, collisions);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		player.getController().acceptEvent(keycode, false, player, entities, collisions);
		switch(keycode){
		case Keys.F:
			testLightFade = true;
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
