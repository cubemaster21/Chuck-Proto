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
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class ChuckGame extends ApplicationAdapter implements InputProcessor{
	Graphics graphics;
	EntityPlayer player;
	ArrayList<Entity> entities = new ArrayList<Entity>();
	Comparator<Entity> zSorter;
	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tmRenderer;
	ArrayList<Rectangle> collisions = new ArrayList<Rectangle>();
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
		
		MapLayer ml = tiledMap.getLayers().get("collisions");
		for(MapObject o: ml.getObjects()){
			RectangleMapObject rmo = (RectangleMapObject) o;
			Rectangle r = new Rectangle(rmo.getRectangle());
			collisions.add(r);
			
		}
		
		
		
	}

	@Override
	public void render () {
		
//		player.update(Gdx.graphics.getDeltaTime(), entities);
		for(Entity e: entities){
			e.update(Gdx.graphics.getDeltaTime(), entities, collisions);
		}
		Collections.sort(entities, zSorter);
		
//		graphics.cam.lookAt(player.position.x, player.position.y, 0);
		graphics.cam.position.x =  player.position.x + 8;
		graphics.cam.position.y = player.position.y + 8;
		
		graphics.cam.update();
		

		graphics.prepare();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tmRenderer.setView(graphics.cam);
		tmRenderer.render();
		graphics.startSprite();
//		tmRenderer.getBatch().setShader(graphics.shader);
		
//		player.draw(graphics);
		for(Entity e: entities){
			if(e.shouldDrawSelf)
				e.draw(graphics);
		}
		graphics.endSprite();
//		graphics.endShapes();
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
