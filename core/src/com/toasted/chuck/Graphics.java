package com.toasted.chuck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Graphics {
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	OrthographicCamera cam;
	
	final FileHandle VERTEX = Gdx.files.internal("Vertex.vert");
    final FileHandle FRAGMENT = Gdx.files.internal("Fragment.frag");

    ShaderProgram shader = new ShaderProgram(VERTEX, FRAGMENT);
	public Graphics(){
		cam = new OrthographicCamera();
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		cam.setToOrtho(false, 16 * 28, 9 * 28);
		
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
		
		System.out.println(shader.getLog());
		spriteBatch.setShader(shader);
		System.out.println(shader.getUniforms());
//		for(String s: shader.getUniforms()){
//			System.out.println(s);
//		}
//		System.out.println(shader.getFragmentShaderSource());
//		shader.begin();
		shader.begin();
		
//		shader.setUniform2fv("u_resolution", new float[]{(float) getWidth(),  (float) getHeight()}, 0, 2);
		shader.setUniformf("u_screenResolution", new Vector2(getWidth(), getHeight()));
		
	}
}
