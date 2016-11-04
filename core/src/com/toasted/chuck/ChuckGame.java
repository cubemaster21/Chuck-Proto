package com.toasted.chuck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ChuckGame extends ApplicationAdapter implements InputProcessor{
	Graphics graphics;
	EntityPlayer player;
	ArrayList<Entity> entities = new ArrayList<Entity>();
	Comparator<Entity> zSorter;
	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tmRenderer;
	ArrayList<Rectangle> collisions = new ArrayList<Rectangle>();
	ArrayList<Light> lights = new ArrayList<Light>();
	Light playerLight;
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
				return (int) Math.signum(arg1.position.y - arg0.position.y);
				
			}
			
		};
		tiledMap = new TmxMapLoader().load("testmap.tmx");
		tmRenderer = new OrthogonalTiledMapRenderer(tiledMap, graphics.getBatch());
		playerLight = new Light(8, 8, 1);
		lights.add(playerLight);
		lights.add(new Light( 100, 100, .3f));
		
		MapLayer ml = tiledMap.getLayers().get("collisions");
		for(MapObject o: ml.getObjects()){
			RectangleMapObject rmo = (RectangleMapObject) o;
			Rectangle r = new Rectangle(rmo.getRectangle());
			collisions.add(r);
			
		}
		MapLayer lightLayer = tiledMap.getLayers().get("lights");
		Vector2 t = new Vector2();
		for(MapObject o: lightLayer.getObjects()){
			RectangleMapObject rmo = (RectangleMapObject) o;
			t = new Rectangle(rmo.getRectangle()).getCenter(t);
			lights.add(new Light(t.x, t.y, .3f));
			
		}
		
		
	}

	@Override
	public void render () {
		
		for(Entity e: entities){
			e.update(Gdx.graphics.getDeltaTime(), entities, collisions);
		}
		Collections.sort(entities, zSorter);
		playerLight.position.x = player.position.x + 8;
		playerLight.position.y = player.position.y + 8;
		
		graphics.cam.position.x =  player.position.x + 8;
		graphics.cam.position.y = player.position.y + 8;
		
		graphics.cam.update();
		

		graphics.prepare();
		graphics.passLightsToShader(lights);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tmRenderer.setView(graphics.cam);
		tmRenderer.render();
		graphics.startSprite();
		
		for(Entity e: entities){
			if(e.shouldDrawSelf)
				e.draw(graphics);
		}
		graphics.endSprite();
		System.out.println("FPS:" + Gdx.graphics.getFramesPerSecond());
	}
	
	@Override
	public void dispose () {
	}

	@Override
	public boolean keyDown(int keycode) {
		player.controller.acceptEvent(keycode, true, player, entities, collisions);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		player.controller.acceptEvent(keycode, false, player, entities, collisions);
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
