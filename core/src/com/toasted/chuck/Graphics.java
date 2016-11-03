package com.toasted.chuck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Graphics {
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	OrthographicCamera cam;
	public Graphics(){
		cam = new OrthographicCamera();
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		cam.setToOrtho(false, 16 * 32, 9 * 32);
		
	}
	public int getWidth(){
		return Gdx.graphics.getWidth();
	}
	public int getHeight(){
		return Gdx.graphics.getHeight();
	}
	public void startShapes(ShapeType shapeType){
		shapeRenderer.begin(shapeType);
	}
	public void endShapes(){
		shapeRenderer.end();
	}
	public void startSprite(){
		spriteBatch.begin();
	}
	public void endSprite(){
		spriteBatch.end();
	}
	public SpriteBatch getBatch(){
		return spriteBatch;
	}
	public ShapeRenderer getShapes(){
		return shapeRenderer;
	}
	public void prepare(){
		shapeRenderer.setProjectionMatrix(cam.combined);
		spriteBatch.setProjectionMatrix(cam.combined);
	}
}
