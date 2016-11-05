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
	
	boolean testLightFade = false;
	Level level;
	
	
	public void create () {
		Gdx.input.setInputProcessor(this);
		graphics = new Graphics();
		level = new Level("testmap.tmx", graphics);
		
	}
	

	@Override
	public void render () {
		level.update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		level.draw(graphics);
//		System.out.println("FPS:" + Gdx.graphics.getFramesPerSecond());
	}
	
	
	@Override
	public void dispose () {
	}

	@Override
	public boolean keyDown(int keycode) {
		level.getPlayer().getController().acceptEvent(keycode, true, level);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		level.getPlayer().getController().acceptEvent(keycode, false, level);
		switch(keycode){
		case Keys.F:
			testLightFade = true;
			break;
		}
		return false;
	}
	public boolean keyTyped(char character) {
		return false;
	}
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	public boolean scrolled(int amount) {
		return false;
	}
}
